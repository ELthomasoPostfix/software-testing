# Industrial Guest Lectures

The Software Testing course follows up the sequence of six assignments by a series of industrial guest lectures.
This directory contains any documents related to the presentations that we get access to, and the assignment documents and solution code for any hands-on sessions.

## Session 01 - 28/04/2025



## Session 02 - 30/04/2025

This session is a hands-on lab session about front-end testing with cypress. Prior to the session, we are tasked with installing some software.


### NodeJS

For the sake of reproducibility, we provide a tarball of the most recent NodeJS version at the time of writing.

The following commands can be used to install the provided NodeJS version from tarball.

```sh
# Enter the directory containing the tarball.
cd session02

# Taken from an SO post: https://stackoverflow.com/a/66166866
sudo apt update
sudo apt install xz-utils
sudo tar -xvf node-v22.15.0-linux-x64.tar.xz
sudo cp -r node-v22.15.0-linux-x64/{bin,include,lib,share} /usr/local/
node --version

# Cleanup
sudo rm -rf node-v22.15.0-linux-x64/
```

To then purge NodeJS from your system, we refer to an [SO post](https://stackoverflow.com/a/33875362) without guarantees of it working. Use these commands at your own peril, and only **AFTER** carefully reading each command!

```sh
sudo rm -rf /usr/local/bin/npm
sudo rm -rf /usr/local/bin/node
sudo rm -rf /usr/local/lib/node_modules/
sudo rm -rf /usr/local/include/node/
sudo rm -rf /usr/local/share/man/man1/node.1

# Replace <userName> with your actual username.
# Delete files/folders related with node.
sudo rm -f /home/userName/.node_repl_history
sudo rm -rf /home/userName/.npm
```

### Cypress

Installing cypress is done via npm, so NodeJS must be installed first. We then run `npm install` for cypress. This creates several artefacts in the same directory where the command is invoked: `node_modules/`, `package.json` and `package-lock.json`.

```sh
# "--save-dev" add cypress to "devDependencies" in package.json
npm install cypress --save-dev
```
