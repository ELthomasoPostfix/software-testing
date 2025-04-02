from typing import Callable, Union
import subprocess
import argparse
import random
import time
import os

import constants as cte


parser = argparse.ArgumentParser(
    description="A basic black-box fuzzer for JPacman.",
)

group = parser.add_argument_group("Quit Conditions",
    "The black-box fuzzer quits execution when one of these conditions is met.")
exclusive_group = group.add_mutually_exclusive_group(required=True)
exclusive_group.add_argument("--MAX", type=int, required=False,
    help="The number of fuzzing iterations to run.")
exclusive_group.add_argument("--TIME", type=int, required=False,
    help="The time duration in seconds (time budget) to run the fuzzer for.")


if __name__ == "__main__":
    # Parse the arguments before doing anything else, to fail fast.
    args = parser.parse_args()

    print("#= Fuzzing config =#")
    for arg in vars(args):
        print(f"{arg}\t{getattr(args, arg)}")
    print("#==================#\n")

    # Ensure proper file handling.
    assert os.path.isfile(cte.PATH_PACMAN_JAR), \
        "Pacman .jar not found, run fuzzing from the dir containing the .jar"

    # The iteration progress tracker.
    # A number of iterations OR a timestamp.
    curr_progress: Union[int,float] = None
    # The max value that decides when iteration should halt.
    # A number of iterations OR a timestamp.
    stop_progress: Union[int,float] = None
    # The function to update the iteration progress tracker.
    update: Callable = None
    # The function that checks if iteration should halt.
    notdone: Callable = None

    if args.MAX is not None:
        curr_progress = 0
        stop_progress = args.MAX
        update  = lambda it: it+1
        notdone = lambda curv, maxv: curv < maxv
    elif args.TIME is not None:
        curr_progress = time.time()
        stop_progress = curr_progress + args.TIME
        update  = lambda it: time.time()
        notdone = lambda curv, maxv: curv < maxv
    else:
        raise RuntimeError("No quit condition: neither MAX nor TIME were defined.")

    print(f"Start fuzzing at: {time.ctime()}")
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
    print(f"Stop fuzzing at:  {time.ctime()}")
