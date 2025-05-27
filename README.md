# software-testing

A repo for the testing assignments for the course Software Testing (Master Computer Science 2001WETSWT) at the University of Antwerp in the academic year of 2024-2025. This is a group project, the members are [Niels Van der Planken](https://github.com/N1ELS2000) and [Thomas Gueutal](https://github.com/ELthomasoPostfix).

These projects make use of the JPacman-Framework project, provided to us by the teaching assistants as a zip-file of the source code. The original repo may be found [here](https://github.com/SERG-Delft/jpacman-framework).

## Artifacts

### Assignments & Reports

The assignment ([here](/assignments)) and corresponding report ([here](/reports)) pdfs are included in the repository for easy access.

### Industrial Guest Lectures

The Software Testing course also features industrial guest lectures. See the [sub README](/guest-lectures/README.md) for more details.

### Articles

Several articles are also part of the course material. They are included for completeness. See [this subdirectory](/articles). Beware that there may be supplementary videos attached to some articles, available on the course blackboard page 


## Basic Workflow

The basic workflow for this assignment is to run the tests and generate the site documentation.

```sh
# Basic workflow
mvn clean site

# If xvfb is installed, you can use it to run the test GUIs in headless mode.
xvfb-run mvn clean site
```

## Run JPacman

It is possible to actually play the JPacman game manually.

```sh
# (1) Compile the project and generate the jar
mvn clean package

# (2) Run the entrypoint / main class method
java -cp target/jpacman-3.0.1.jar jpacman.controller.Pacman
```

## Fuzzing

Assignment 06 requires us to write a black box fuzzer in any language.
See the [sub README](/src/fuzz/README.md) for more details.
