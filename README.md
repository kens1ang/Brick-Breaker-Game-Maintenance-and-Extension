# Brick Breaker Game

## Compilation Instructions:
This Java project is developed using Intellij IDEA Community Edition and utilized JavaFX for the user interface

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
- If the errors still exist, kindly remove and add the library again by following the steps above.


## Implemented and Working Properly:

### Dynamic Game Ball:
1. **Initialization** (`Model` package, `GameModel` class, `setballpos` method)
- Generate random coordinates for the ball increase the chance that the ball may spawn below the paddle as the level goes up causes instant decrement of heart.
- Randomise game ball position cause ball spawn inside of the blocks as higher levels the number of blocks increase. 
- The ball is initialized at a fixed position at the start of each level, strategically placed between the lowest block and the paddle.
- `setballpos` method calculate the height of lowest block and set the position of game ball at the center between lowest block and paddle. 
2. **Randomized Movement** (`Model` package, `GameModel` class, `randomballspawn` method)
- Based on base code, the game ball always falls to the bottom right direction.
- The ball's movement direction is randomized at the beginning of each level, with both horizontal and vertical directions being set randomly. (improve gaming experience)

### New Additional Level: 
**Bomb blocks only final level** (`Model` package, `GameModel` class, `initboardmodel` method)
- Intoduced a new game level where all the blocks are bomb blocks, create a challenging final level. 
- When the level is 18, all blocks are now bomb blocks, the method `determineBlockType` has beed adjusted to handle this scenario.

### Smooth Label Animation:
**Add smooth transition to label** (`View` package, `GameScore` class, `animateLabel` method)
- The `animateLabel` method create a visual effect by smoothly adjusting the scale and opacity of a label over a specified duration and number of animation steps. 
- Displaying score changes, game messages such as `Level Up :)`, `Score +1` etc.

### Gold Status Visual Effect:
**Gold theme** (`View` package, `GameView` class, `handlegoldblockView` method)
- When game ball hits a star block, the game responds by invoking methods to handle the visual changes associated with the Gold Status effect.
- `checkhitnotnormalblock` to check if the game ball hit star block, if so invoke `handlegoldblockView` method.
- `handlegoldblockView` method updates the game ball fill with new image representing gold-themed ball and modify root element's style class to apply a gold-themed background.
- `handleremovegoldstatus` method remove gold status effects after gold time pass. 

### Sound Effects & Background Music:
1. **SoundManager class**(`brickGame` package, `SoundManager` class)
- Introduced SoundManager class to load `mp3` or `wav` file from `resources` and playsound methods to invoke each sound effects. 
- Add sound effects on different action events for example, Collect bonus, Blocks hit, Game Over etc, to enchance gaming experience. Background music contributes to overall atmosphere of the game.  
2. **Playsounds method in neccesary methods** (`Model` package, `GameModel` class)
- Playsound methods are consistently called within all necessary methods within the `GameModel` class for example: `soundManager.playBombHitSound` in `handleBombPaddleCollisionModel` method etc.

### Pause & Resume Game: 
1. **Space bar key** (`Controller` package, `GameController` class, `handle`, `pauseresumeGame` methods)
- `handle` method to receive users' input to pause or resume the game. (imporve gaming experience)
- `pauseresumeGame` method invoked every time user press space bar, game status is checked in this method. If game is running set `isPaused` to true else false. 
2. **Game engine status** (`Controller` package, `GameEngine` class, `startUpdateloop`, `startPhysicsLoop`, `StartTimeLoop` methods)
- Boolean `isRunning` and `isPaused` in `GameEngine` class to check the game status.  
- When `isPaused` is true, `onUpdate`, `onPhyscisUpdate`, and `onTime` stop running until `isPaused` set to false.

### New Block Type (Bomb block):
1. **PenaltyBlock Class** (`GameElements` package, `PenaltyBlock` class)
- Handle falling bomb object/motion when game ball hit the bomb block
- Update the position of bomb object.
2. **Bomb Object Hit Paddle** (`Model` package, `GameModel` class)
- Logic to calculate the bomb object hit the paddle
3. **Sound effect when bomb hit paddle** (`brickGame` package, `SoundManager` class)
- Invoke `playBombHitSound` method when bomb collide with paddle
4. **Visual Effects Bomb Hit Paddle** (`View` package, `GameView` class)
- The paddle becomes invisible for 2 seconds
- Screen shakes as a visual effect to stimulate the impact of a bomb colliding with the paddle. 

## Implemented but Not Working Properly

## Features Not Implemented

## New Java Classes

## Modified Java Classes

## Unexpected Problems:

### Unable to Save Game:
- Adjusted the save path from D drive to C drive to ensure that the game is stored in the correct location.

### Game Ball Move Beyond the Walls:
1. **Reset collide flag** (`Model` package, `GameModel` class, `handleWallCollisions` method)
- Call `resetCollisionFlags` every time the ball hits the wall.
2. **Accurate collisions** (`Model` package, `GameModel` class, `handleWallCollisions` method)
- Take ball radius into consideration when collide with walls, paddle and blocks.

### Thread Related Issues 
1. **Shared data in multi-threaded environment**:
- When multiple threads are involved, it's crucial to synchronize access to shared resources. There are bugs and erros such as label `+1` still displaying after block is destroyed, ball penetrating through blocks frequently, concurrent errors etc
- The issues occur due to lack of synchronization in the code, multiple threads access shared data wihtout proper synchronization
2. **Timeline Integration in `GameEngine` class**:
- JavaFX Timeline is designed to work well with the UI thread ensures that the animations itself run on the UI thread, making it easier to handle animations and updates in a graphicacl user interface and eliminating the need for explicit synchronization in many cases.
- However, ball penetration through blocks issue is reduced but not resolved due to the logic of collision.

### Game Ball Penetration Thrrough Blocks
1. **Ball direction move vertically up or down**:
- In this case, the ball straight penetrate through the blocks even the velocity of ball is slow.
2. **


### Paddle Movement Delayed & Move Beyond `sceneWidth`:
**Replace thread with Timeline** (`Model` package, `GameModel` class, `move` method)
- There is delay on paddle movement and sometimes it moves beyond the scenewidth when handled using separate thread. 
- This is because when using `Thread`, code inside thread runs on separate thread from the UI thread which might run into synchronization issues.
- `Timeline` ensures that keyframes and animations are executed on the UI thread. (smoother paddle movement)




