from typing import Callable, Union
import argparse
import time
import os

from random_fuzzer import random_fuzzer
from mutation_fuzzer import mutation_fuzzer
import constants as cte


parser = argparse.ArgumentParser(
    description="A basic black-box fuzzer for JPacman.",
)

#
# Define the black-box fuzzer Quit Condition group.
#
group = parser.add_argument_group("Quit Conditions",
    "The black-box fuzzer quits execution when one of these conditions is met.")
exclusive_group = group.add_mutually_exclusive_group(required=True)
exclusive_group.add_argument("--MAX", type=int, required=False,
    help="The number of fuzzing iterations to run.")
exclusive_group.add_argument("--TIME", type=int, required=False,
    help="The time duration in seconds (time budget) to run the fuzzer for.")

#
# Define the black-box fuzzer type group.
#
group = parser.add_argument_group("Fuzzer Type",
    "The specific black-box fuzzer implementation to use.")
exclusive_group = group.add_mutually_exclusive_group(required=True)
exclusive_group.add_argument("--RND", action="store_true", required=False,
    help="The completely random fuzzer implementation.")
exclusive_group.add_argument("--MUT", action="store_true", required=False,
    help="The mutation fuzzer implementation.")
exclusive_group.add_argument("--NOOP", action="store_true", required=False,
    help="Don't run any fuzzer, just do a dry run of the boilerplate around the fuzzer.")


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
        raise RuntimeError("No quit condition: neither MAX nor TIME were defined; should never reach here.")


    print(f"Start fuzzing at: {time.ctime()}")
    # Explicitly do not run any fuzzer implementation.
    if args.NOOP:
        pass
    # Run the completely random fuzzer.
    elif args.RND:
        random_fuzzer(curr_progress, stop_progress, update, notdone)
    elif args.MUT:
        # Use the sample map as the seed map.
        with open("sample.map") as imap:
            seed_map = [ row.strip() for row in imap.readlines() ]
            seed_act = "SUELDUWRUESLUWDE"
            nr_mutations_min: int = 1
            nr_mutations_max: int = 10
            mutation_fuzzer(
                curr_progress, stop_progress, update, notdone,
                seed_map, seed_act, nr_mutations_min, nr_mutations_max,
            )
    # Should never reach here; add an assertion as a fallthrough.
    else:
        raise RuntimeError("No black-box fuzzer type was specified; should never reach here.")
    print(f"Stop fuzzing at:  {time.ctime()}")
