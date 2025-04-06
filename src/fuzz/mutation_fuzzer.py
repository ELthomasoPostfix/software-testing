from typing import Union, Callable, List
import subprocess
import random
import copy

import constants as cte


"""The mutation fuzzer implementation, for Ex. 6 of the assignment."""
def mutation_fuzzer(
        curr_progress: Union[int,float],
        stop_progress: Union[int,float],
        update: Callable,
        notdone: Callable,
        seed_map: List[str],
        seed_act: str,
        mmin: int,
        mmax: int):

    assert 0 < mmin, "The min nr of mutations is invalid."
    assert 0 < mmax and mmin <= mmax, "The max nr of mutations is invalid."

    with open(cte.PATH_PACMAN_ERR_CSV, 'w') as err_file:
        # Define the mutation operations as a list to sample from randomly.
        action_mutators = [ sadd, sdel, srpl ]
        map_mutators = [ cadd, radd, cdel, rdel, mrpl ]

        while notdone(curr_progress, stop_progress):
            # Mutate the action sequence.
            # Randomize the number of mutations every time.
            mutfuzz_actions: str = seed_act
            for _ in range(random.randint(mmin, mmax)):
                mutfuzz_actions = random.choice(action_mutators)(
                    mutfuzz_actions, cte.ACTIONS
                )

            # The actions sequence must end with an exit action,
            # else JPacman would not terminate!
            if mutfuzz_actions[-1] != cte.ACTIONS_EXIT:
                # Count this as a valid fuzzing attempt!
                curr_progress = update(curr_progress)
                continue

            # Use a file context to close the file even in case of exceptions.
            mutfuzz_map: str = ""
            with open(cte.PATH_FUZZ_MAP, 'w') as map_file:
                # Mutate the map.
                mutfuzz_map_lst: List[str] = seed_map
                for _ in range(random.randint(mmin, mmax)):
                    mutfuzz_map_lst = random.choice(map_mutators)(
                        mutfuzz_map_lst, cte.CELLS
                    )
                mutfuzz_map = "\n".join(mutfuzz_map_lst)

                map_file.write(mutfuzz_map)

            # Use `subprocess.run` as it is the most up-to-date approach.
            result: subprocess.CompletedProcess = subprocess.run([
                "java", "-jar", cte.PATH_PACMAN_JAR, cte.PATH_FUZZ_MAP, mutfuzz_actions
            ])
            code: int = result.returncode

            # Pacman works fine for the given input.
            # Explicitly do nothing.
            if code == cte.CODE_OK: pass
            # Pacman successfully parsed and rejected this input.
            # Explicitly do nothing.
            elif code == cte.CODE_REJ: pass
            # Pacman failed with an error.
            # Report these Pacman inputs, they indicate a potential bug.
            elif code == cte.CODE_ERR: pass
            else:
                raise RuntimeError(f"Unknown Pacman exit code: {code}")

            # Dump results to file.
            dumptext = f"{code},{mutfuzz_actions},\"{mutfuzz_map}\""
            dumptext = dumptext.replace('\n', "\\n")
            dumptext += '\n'
            err_file.write(dumptext)

            curr_progress = update(curr_progress)


#
# Mutation operations: https://www.fuzzingbook.org/html/MutationFuzzer.html
#

"""Add one character from the list at a random position."""
def sadd(s: str, chars: List[str]) -> str:
    # Allow length(s) as an index, to add chars at the end of the string.
    pos = random.randint(0, len(s))
    snew = s[:pos] + random.choice(chars) + s[pos:]
    assert len(snew) == len(s) + 1
    return snew

"""Delete one character at a random position if possible."""
def sdel(s: str, *args) -> str:
    # Only delete a character if possible.
    if len(s) == 0: return s
    # Only valid indexes are targets for char deletion.
    pos = random.randint(0, len(s) - 1)
    snew = s[:pos] + s[pos+1:]
    assert len(snew) == len(s) - 1
    return snew

"""Replace one character from the list at a random position if possible."""
def srpl(s: str, chars: List[str]) -> str:
    # Only replace a character if possible.
    if len(s) == 0: return s
    # Only valid indexes are targets for char replacement.
    pos = random.randint(0, len(s) - 1)
    snew = s[:pos] + random.choice(chars) + s[pos+1:]
    assert len(snew) == len(s)
    return snew

"""Add one row of characters from the list at a random position if possible."""
def radd(m: List[str], chars: List[str]):
    mnew = copy.deepcopy(m)
    # Allow length(m) as an index, to add a rows at the end.
    pos = random.randint(0, len(m))

    # If there is no rows, or there is no columns, then set row length to one.
    # Else maintain the current row length.
    rowlen = max(1, len(m[0]) if len(m) > 0 else 0)

    rnew = "".join(random.choices(chars, k=rowlen))
    mnew.insert(pos, rnew)
    assert len(mnew) == len(m) + 1
    return mnew

"""Add one col of characters from the list at a random position if possible."""
def cadd(m: List[str], chars: List[str]):
    # In an empty map, adding a row is the same as adding a column.
    if len(m) == 0: return radd(m, chars)

    # Allow length(m) as an index, to add a rows at the end.
    rowlen = len(m[0])
    pos = random.randint(0, rowlen)
    mnew = [
        row[:pos] + random.choice(chars) + row[pos:]
        for row in m
    ]
    assert len(mnew) == len(m)
    return mnew

"""Delete one row of characters at a random position if possible."""
def rdel(m: List[str], *args) -> str:
    # Only delete a row if possible.
    if len(m) == 0: return m

    # Only valid indexes are targets for char deletion.
    pos = random.randint(0, len(m) - 1)
    mnew = m[:pos] + m[pos+1:]
    return mnew

"""Delete one col of characters at a random position if possible."""
def cdel(m: List[str], *args) -> str:
    # Only delete a column if possible.
    if len(m) == 0 or len(m[0]) == 0: return m

    # Only valid indexes are targets for char deletion.
    pos = random.randint(0, len(m[0]) - 1)
    mnew = [
        rnew
        for row in m
        if len(rnew := row[:pos] + row[pos+1:]) > 0
    ]
    return mnew

"""Replace one cell at a random row and column if possible."""
def mrpl(m: List[str], chars: List[str]) -> str:
    # Only replace a cell if possible.
    if len(m) == 0 or len(m[0]) == 0: return m

    r = random.randint(0, len(m) - 1)
    c = random.randint(0, len(m[0]) - 1)

    mnew = copy.deepcopy(m)
    row = mnew[r]
    mnew[r] = row[:c] + random.choice(chars) + row[c+1:]
    return mnew
