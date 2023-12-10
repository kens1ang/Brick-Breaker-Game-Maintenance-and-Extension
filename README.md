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
9. Apply the changes.

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

### Game Ball:
1. **Initialization** (`Model` package, `GameModel` class, `setballpos` method)
- Generate random coordinates for the ball increase the chance that the ball may spawn below the paddle as the level goes up causes instant decrement of heart.
- The ball is initialized at a fixed position at the start of each level, strategically placed between the lowest block and the paddle.
2. **Randomized Movement** (`Model` package, `GameModel` class, `randomballspawn` method)
- Initially, the game ball always falls to the bottom right direction.
- The ball's movement direction is randomized at the beginning of each level, with both horizontal and vertical directions being set randomly. (improve gaming experience)
  
### Paddle:
1. **Replace thread with Timeline** (`Model` package, `GameModel` class, `move` method)
- There is delay on paddle movement and sometimes it moves beyond the scenewidth when handled using separate thread. 
- This is because when using `Thread`, code inside thread runs on separate thread from the UI thread which might run into synchronization issues.
- `Timeline` ensures that keyframes and animations are executed on the UI thread. (smoother paddle movement)
2. **accurate collisions** (`Model` package, `GameModel` class, `handlePaddleCollision` method)
- take ball radius into consideration when collide with paddle.

### Game Ball collision with walls
1. **reset collide flag** (`Model` package, `GameModel` class, `handleWallCollisions` method)
- call `resetCollisionFlags` every time the ball hits the wall to prevent the ball moves out of the screen.
2. **accurate collisions** (`Model` package, `GameModel` class, `handleWallCollisions` method)
- take ball radius into consideration when collide with walls.

### Save Game
1. **Change File location**
- Change to correct save path from D drive to C drive.

### Game Ball collision with blocks

## Additional Fatures:

### Pause and Resume Game 
1. (`Controller` package, `GameController` class, `handle`, `pauseresumeGame` methods)
- User press space bar to pause or resume the game. (imporve gaming experience)
2. (`Controller` package, `GameEngine` class, `startUpdateloop`, `startPhysicsLoop`, `StartTimeLoop` methods)
- Add boolean `isRunning` and `isPaused` in `GameEngine` class to check the game status.  
- If the game is running pause the game, stop methods onUpdate, onPhyscisUpdate, and onTime (`isPaused` = true) else resume the game (`isPaused` = false).

### New Block Type (Bomb block)
1. **PenaltyBlock Class** (`GameElements` package, `PenaltyBlock` class)
- Handle falling bomb object/motion when game ball hit the bomb block
2. **Bomb Object Hit Paddle** (`Model` package, `GameModel` class)
- Logic to calculate the bomb object hit the paddle
- call method `playBombHitSound` method from `SoundManager` class when bomb hit paddle
3. **Visual Effects Bomb Hit Paddle** (`View` package, `GameView` class)
- The paddle becomes invisible for 2 seconds
- Screen shakes as a visual effect to stimulate the impact of a bomb colliding with the paddle. 

### Sound Effects
1. (`brickGame` package, `SoundManager` class)
- The sound effects on different action events for example, Collect bonus, Blocks hit, Game Over etc, enchance gaming experience.
- Background music contributes to overall atmosphere of the game. 
2. (`Model` package, `GameModel` class)
- playsound methods are consistently called within all necessary methods within the `GameModel` class for example: `handleBombPaddleCollisionModel` method etc.




