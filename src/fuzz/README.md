# Fuzzing

Assignment 06 *fuzzing* requires us to perform black-box fuzzing on a [provided](/src/fuzz/jpacman-3.0.1.jar) `.jar` archive of Pacman.
As per the assignment pdf, the provided archive should be able to run the Pacman game.

In this subdirectory; we host the provided `.jar` and `sample.map` file provided on the blackboard page of this course, alongside our black box fuzzer implementation.

```sh
# Actually play the Pacman game manually, with moving ghosts and food.
/src/fuzz$ java -jar jpacman-3.0.1.jar

# Given a map file and sequence of moves, run the moves and abort the game.
# See the assignment file or report for more details.
/src/fuzz$ java -jar jpacman-3.0.1.jar sample.map SUUWDE
```

The fuzzer was structured to allow adding new fuzzing loop implementations easily, since the assignment asks for multiple fuzzer types.
The `fuzzer.py` script is the entrypoint that handles argument parsing, to call the desired fuzzer implementation.

```
src/
 └── fuzz/
      ├── manual_fuzzing/  # The manual fuzzing maps, Ex 4.
      ├── constants.py  # Named constants used by the fuzzer
      ├── fuzzer.py     # The entry point for the black-box fuzzer
      ├── jpacman-3.0.1.jar # The Pacman executable .jar
      ├── mutation_fuzzer.py  # The mutation fuzzer loop implementation, Ex 6.
      ├── random_fuzzer.py  # The random fuzzer loop implementation, Ex 1.
      └── sample.map    # The example map provided on blackboard
```

Calling the fuzzer requires specifying exactly one fuzzing loop implementation and one stop condition.
The stop condition can be running a fixed number of fuzzing iterations, or fuzzing until a fixed time budget is exhausted.

Note that you **must** run `fuzzer.py` from its containing directory. This assumption simplified file handling by the fuzzer.

```sh
# You must run the fuzzer from its containing directory.
$ cd src/fuzz/

# When in doubt, "--help".
src/fuzz$ python3 fuzzer.py --help

# Run some fuzzers.
src/fuzz$ python3 fuzzer.py --NOOP --MAX 10    # Dry run, do not run any fuzzer.
src/fuzz$ python3 fuzzer.py --RND --MAX 10     # Run the completely random fuzzer for 10 iterations.
src/fuzz$ python3 fuzzer.py --RND --TIME 10    # Run the completely random fuzzer for 10 seconds.
src/fuzz$ python3 fuzzer.py --MUT --MAX 10     # Run the mutation fuzzer for 10 iterations.
```
