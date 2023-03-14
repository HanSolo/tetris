## Tetris

[![Github all releases](https://img.shields.io/github/downloads/HanSolo/tetris/total.svg)](https://GitHub.com/HanSolo/tetris/releases/)

Tetris is a simple clone of the game Tetris from 1984 by Alexey Pajitnov written in JavaFX.

The game contains 20 levels where the levels mainly define the speed of the falling blocks.

All graphics are drawn by myself and can be found in the resources folder
in the main directory of the project. These files are in the format of Affinity Designer.

### Settings
- The game will create a properties file in your user folder named tetris.properties

e.g. `/home/YOUR_USERNAME/tetris.properties`
```properties
highscore=0
```

### Build
- Get the source of this project
- Make sure you are on JDK 17
- call ```./gradlew clean build``` on the command line
- execute ````bash build_app_macos.sh```` on the command line to build native executable and installer

### Gameplay
- Space bar to start the game when in start screen otherwise it will rotate the current block
- Move block to the left: Arrow left
- Move block to the right: Arrow right
- Fast drop block: Arrow down
- Rotate block Space bar
- M key will toggle between modes (GLOSSY, GITHUB, STANDARD)


### ScreenShots

![StartScreen](https://i.ibb.co/71byD10/tetris-start-screen.png)

![GlossyMode](https://i.ibb.co/s3jrJPN/tetris-screen-shot.png)

![GithubMode](https://i.ibb.co/CJtKbcs/tetris-github-mode.png)