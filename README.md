# Tic-Tac-Toe

A desktop Tic-Tac-Toe game built with Java Swing. It has a dark theme, a bot opponent, a two-player mode, and a running scoreboard.

## Features

- **Two modes:** play against the bot or a friend (PvP) and switch with one click
- **Bot opponent:** takes a winning move if it has one, blocks yours, then prefers the center and corners
- **Score tracking** across games, with a reset option
- **Game timer** for each round
- **Winning line highlight** when someone wins
- **Optional move sound:** add a `move.wav` file next to the jar

## Getting Started

### Requirements
- Java 11 or newer

### Run the jar
```bash
java -jar TicTacToe.jar
```

### Build from source
```bash
javac TicTacToe.java
java TicTacToe
```

### Package as an installer (optional)
```bash
jpackage --type exe --name TicTacToe --input . --main-jar TicTacToe.jar --main-class TicTacToe
```
Use `--type dmg` on macOS or `--type deb` on Linux.

## How to Play

1. X always goes first. In Bot mode, you play X.
2. Click a square to place your mark.
3. Get three in a row, across, down, or diagonally, to win.
4. Use **New Game** for a fresh board, **Reset Score** to clear the scoreboard, or **Mode** to switch between Bot and PvP.

## Tech Stack

Java, Swing (`javax.swing`), `javax.sound.sampled`
