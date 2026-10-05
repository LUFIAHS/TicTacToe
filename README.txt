TIC-TAC-TOE
===========
Run:  double-click TicTacToe.jar  (or "Run TicTacToe.bat" on Windows, run.sh on Mac/Linux)
Needs Java 11+ installed.  Optional: put move.wav next to the jar for move sounds.

Make a real installer (no Java needed by the player) - run on the OS you're targeting:
  Windows (.exe, needs WiX Toolset): 
    jpackage --type exe --name TicTacToe --input . --main-jar TicTacToe.jar --main-class TicTacToe --win-shortcut --win-menu
  macOS (.dmg):
    jpackage --type dmg --name TicTacToe --input . --main-jar TicTacToe.jar --main-class TicTacToe
  Linux (.deb):
    jpackage --type deb --name tictactoe --input . --main-jar TicTacToe.jar --main-class TicTacToe
