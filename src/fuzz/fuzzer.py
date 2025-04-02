from typing import Callable, Union
import subprocess
import argparse
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
    with open(cte.PATH_PACMAN_ERR, 'w') as err_file:
        while notdone(curr_progress, stop_progress):

            # TODO: Generate (FUZZ) random actions. This should be a string of valid ACTIONS.
            fuzz_actions: str = "".join(cte.ACTIONS)

            # Use a file context to close the file even in case of exceptions.
            with open(cte.PATH_FUZZ_MAP, 'w') as map_file:
                fuzz_map = "" # TODO: Generate (FUZZ) random map contents. This should be a random byte string?

                map_file.write(fuzz_map)

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
            elif code == cte.CODE_ERR:
                # TODO: Implement dumping the error to disk?
                # FIXME: Does the error message appear in stderr or stdout?
                dumptext = ""
                err_file.write(dumptext)
            else:
                raise RuntimeError(f"Unknown Pacman exit code: {code}")

            curr_progress = update(curr_progress)
    print(f"Stop fuzzing at:  {time.ctime()}")
