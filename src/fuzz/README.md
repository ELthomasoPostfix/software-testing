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
