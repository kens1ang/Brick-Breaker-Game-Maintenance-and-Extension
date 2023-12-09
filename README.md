# Brick Breaker Game

## Compilation Instructions:
This project is developed using Intellij IDEA Community Edition and utilized JavaFX for the user interface

### Prerequisitres:
- IntelliJ IDEA Community Edition 2023.2.4
- JavaFX SDK 21.0.1

### Adding JavaFX Library:
1. Open IntelliJ IDEA.
2. Navigate to the main menu bar on top right or use the shortcut `Alt + \`.
3. Select "Project Struture".
4. In the Project Settings, choose "Libraries".
5. Add a new project library by clicking on the '+' icon.
6. Select "Java" from the list.
7. Navigate to the location on your computer where JavaFX SDK 21.0.1 is installed.
8. Inside the SDK folder, select the "lib" folder.
9. Apple the changes.

### Running the Game:
1. Open the project in IntelliJ IDEA.
2. Make sure the correct version of JavaFX library is added (as explained above).
3. Locate the `Main` class in the `brickGame` package.
4. Run the `Main` class to start the Brick Breaker Game.

### Game Controls:
- Press "Start Game" to begin playing.
- Press "Load Game" to load previous saved game.
- Use the left and right arrow keys to move the paddle.
- Press space bar to pause or resume the game.
- Press s to save game. 
    
**NOTES:**
- There should be no JavaFX errors and the game should be able to run.
- If the erros still exist, kindly remove and add the library again by following the steps above.


## Implemented and Working Properly:
#Game Ball
**Initialization**
The ball is initialized at a fixed position at the start of each level, strategically placed between the lowest block and the paddle
**Randomized Movement**
The ball's movement direction is randomized at the beginning of each level, with both horizontal and vertical directions being set randomly. 
