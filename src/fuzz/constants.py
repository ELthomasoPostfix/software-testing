"""The constants used in the Pacman black-box fuzzer.

    The following kinds of constants are implemented.
    - The set of valid actions for the Pacman `.jar`.
    - The expected exit codes for the Pacman `.jar`.
    - The path to the Pacman `.jar`
    - Auxiliary file paths.
    - ...
"""

from typing import List

"""The list of valid Pacman actions. Pacman should ignore all other actions."""
ACTIONS: List[str] = [
    "U", # Up               Pacman.up()
    "D", # Down             Pacman.down()
    "L", # Left             Pacman.left()
    "R", # Right            Pacman.right()
    "S", # Start            Pacman.start()
    "E", # Exit             Pacman.exit()
    "Q", # Quit (Halt)      Pacman.quit()
    "W"  # Wait (Sleep)     Thread.sleep(50)
]

"""Exit code for a normal termination."""
CODE_OK: int = 0

"""Exit code for a crash. The application is not able to handle this input."""
CODE_ERR: int = 1

"""Exit code for a rejection. The application is able to handle this invalid input."""
CODE_REJ: int = 10

"""The path to the provided Pacman .jar file."""
PATH_PACMAN_JAR: str = "jpacman-3.0.1.jar"

"""The path to the map file generated furing fuzzing."""
PATH_FUZZ_MAP: str = "fuzz.map"

"""The path to the CSV file to dump Pacman error messages into."""
PATH_PACMAN_ERR_CSV: str = "pacman-error.csv"

"""The maximum random length of the fuzzed action sequence."""
LEN_RND_ACTIONS: int = 4

"""The maximum random length of the fuzzed map file contents."""
LEN_RND_MAP: int = 100
