from typing import Union, Callable
import subprocess
import random

import constants as cte


"""The completely random fuzzer implementation, for Ex. 1 of the assignment."""
def random_fuzzer(
        curr_progress: Union[int,float],
        stop_progress: Union[int,float],
        update: Callable,
        notdone: Callable):
    with open(cte.PATH_PACMAN_ERR_CSV, 'w') as err_file:
        while notdone(curr_progress, stop_progress):

            # Generate (FUZZ) random actions.
            # This should be a string of valid ACTIONS.
            # Randomize the action sequence length to expose bugs related to:
            # zero, one, many, etc. actions.
            fuzz_actions_len: int = random.randint(0, cte.LEN_RND_ACTIONS)
            fuzz_actions: str = "".join(random.choices(cte.ACTIONS, k=fuzz_actions_len))

            # Use a file context to close the file even in case of exceptions.
            fuzz_map: bytes = b''
            with open(cte.PATH_FUZZ_MAP, 'wb') as map_file:
                # Generate (FUZZ) random map contents.
                # This should be a random byte string, and maybe a valid map.
                fuzz_map_len: int = random.randint(0, cte.LEN_RND_MAP)
                fuzz_map: bytes = random.randbytes(fuzz_map_len)

                map_file.write(fuzz_map)

            # Use `subprocess.run` as it is the most up-to-date approach.
            result: subprocess.CompletedProcess = subprocess.run([
                "java", "-jar", cte.PATH_PACMAN_JAR, cte.PATH_FUZZ_MAP, fuzz_actions
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
            dumptext = f"{code},{fuzz_actions},{fuzz_map}\n"
            err_file.write(dumptext)

            curr_progress = update(curr_progress)