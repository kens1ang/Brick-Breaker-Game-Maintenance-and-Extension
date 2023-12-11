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
- Press "Start Game" button to begin playing.
- Press "Load Game" button to load previous saved game.
- Use the `left` and `right` arrow keys to move the paddle.
- Press `space bar` to pause or resume the game.
- Press `s` to save game. 
    
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
1. **Bomb blocks only final level** (`Model` package, `GameModel` class, `initboardmodel` method)
    - Intoduced a new game level where all the blocks are bomb blocks, create a challenging final level. 
    - When the level is 18, all blocks are now bomb blocks, the method `determineBlockType` has beed adjusted to handle this scenario.

### Smooth Label Animation:
1. **Add smooth transition to label** (`View` package, `GameScore` class, `animateLabel` method)
    - The `animateLabel` method create a visual effect by smoothly adjusting the scale and opacity of a label over a specified duration and number of animation steps. 
    - Displaying score changes, game messages such as `Level Up :)`, `Score +1` etc.

### Gold Status Visual Effect:
1. **Gold theme** (`View` package, `GameView` class, `handlegoldblockView` method)
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

## Implemented but Not Working Properly:

### Handle block penetration issue
1. **Considering blocks edges in collision logic**
    - Take blocks' edges into consideration when tuning the collision detection logic.
    - This resolve the penetration when ball is bouncing from walls at a steeper angle and faster speed. However there are still penetration sometimes as stated below.
    - Overall this implemented solution resolve most of the issues, the penetration happens occasionally in specific scenarios. 
2. **Ball direction move vertically up or down**:
    - In this case, sometimes the ball straight penetrate through the blocks even the velocity of ball is slow.
3. **Ball bouncing between multiple blocks with high velocity**
    - In this case, sometimes the blocks beside hit block will be destroyed as well.

## Features Not Implemented:

### Main Menu / Start Page
- Limited development time and focusing on core gameplay elements and refactoring the code
- Design interface might considered outside of the scope of this project
  
### Multiball
- Limited development time for this complex features and this features require lot of development effort as functionality is complex considerating ball physics, collisions and user interaction
- Mulitiple features could introduce new challenges in testing, debugging and refactoring
- Require a lot of adjustment on the provided base code, and this decisions might need to refine existing features

## New Java Classes:

### GameModel
- This class located in `Model` package
- `GameModel` class contains object instances of `GameBall`,`GamePaddle`, and `SoundManager` class and have direct access with these classes.
- This class does not have direct knowledge of `GameView` and `GameController` class.
- The primary class responsible for managing the game's logic, state, and various game elements such as the ball, paddle, blocks, and bonuses.
- Manages the game state, including the level, score, and heart count.
- Handles the initialization of game elements such as the ball, paddle, and blocks.
- Updates the position of the ball, checks for collisions, and manages game logic.
- Manages bonus and penalty blocks, including their interactions with the paddle.
- Handles sound effects and manages the game's audio.
  
### GameBall
- This class located in `Model` package
- This `GameBall` class is part of the model component in the Model-View-Controller (MVC) architecture.
- The `GameBall` class is responsible for encapsulating the properties and behavior of the game ball.
- It stores information about the ball's position (xBall and yBall), its movement direction (goDownBall and goRightBall), and provides methods to update its position.
- `incxBall`, `drexBall`, `incyBall`, `dreyBall`: Methods to update the ball's position based on its movement.
- `setGoDownBall` and `setGoRightBall`: Methods to set the direction of the ball's movement.
- `isGoRightBall` and `isGoDownBall`: Methods to query the direction of the ball's movement.

### GamePaddle
- This class located in `Model` package
- This `GamePaddle` class is part of the model component in the Model-View-Controller (MVC) architecture.
- The `GamePaddle` class is responsible for encapsulating the properties and behavior of the paddle within the Brick Breaker game.
- It stores information about the paddle's position (xBreak and yBreak), its width (breakWidth), and provides methods to update its position.
- `centerBreakX`: Represents the horizontal center of the paddle.
- `xBreak` and `yBreak`: Represent the current coordinates of the top-left corner of the paddle.
- `breakWidth` and `breakHeight`: Define the dimensions of the paddle.
- `addxBreak` and `minusxBreak`: Methods to increment and decrement the horizontal position of the paddle.
- `getHalfBreakWidth`: Returns half of the paddle's width.
- `setxBreak`, `setCenterBreakX`, `setyBreak`: Methods to set the position of the paddle.

### SoundManager
- This class located in `Model` package
- The `SoundManager` class in the Model package is responsible for managing and playing various sounds
- The `SoundManager` is part of the model component, and it can be accessed by other components, such as the controller or even the view, if necessary.
- The `SoundManager` class encapsulates the functionality related to game sounds.
- It provides methods to play different sounds such as bomb hits, bonus collections, heart collections, level-ups, block hits, and more. It also manages the background music.
- `MediaPlayer` objects for various sounds like bomb hit, bonus collection, heart collection, level-up, block hit, and more.
- `backgroundMusic`: MediaPlayer for the background music.
- `isBackgroundMusicPlaying`: A boolean flag to track whether background music is currently playing.
- `initSounds`: Initializes MediaPlayer objects for each sound by loading sound files.
- `loadSound`: Loads a sound file and returns a MediaPlayer object.
- Methods like `playBombHitSound`, `playCollectBonusSound`, etc.: Play the corresponding sounds.
- `playBackgroundMusic`: Plays the background music in a loop.
- `pauseBackgroundMusic`: Pauses the background music.
- `resumeBackgroundMusic`: Resumes the background music.
- `playSound`: General method to play a sound, stopping it if it's already playing.

### GameController
- The `GameController` class in the `Controller` package
- It handles user input, manages the game state, and interacts with both the model (`GameModel`) and the view (`GameView`).
- model: An instance of the `GameModel` class, representing the game's underlying data and logic.
- view: An instance of the `GameView` class, representing the visual aspects of the game.
- It handles events such as key presses, manages game state transitions, and orchestrates the game engine etc.
- `start`: Initializes and starts the game, setting up the initial game elements, initializing the view, and handling user input.
- `saveGame`, `loadGame`: Methods for saving and loading the game state.
- `nextLevel`: Moves to the next level of the game.
- `restartGame`: Restarts the game with initial settings.
- `onUpdate`, `onPhysicsUpdate`, `onInit`, `onTime`: Methods implementing the `OnAction` interface for game engine callbacks.

### GameView
- The `GameView` class is in the `View` package
- The class interacts with the `GameController` to update the view based on the state of the game model.
- It also handles user interface events, such as button clicks.
- The class uses CSS styling to modify the appearance of specific elements, such as the root during the final level or when a gold block is hit.
- The class uses JavaFX animations, such as screen shaking and transitions, to enhance the user experience.

### PenaltyBlock
- The `PenaltyBlock` class in the `GameElements` package
- The `PenaltyBlock` class is responsible for creating and managing penalty blocks that fall down the game screen. When the ball collides with a paddle, these blocks trigger a penalty event.
- `bomb`: A Rectangle representing the penalty block.
- `x`, `y`: The coordinates of the penalty block.
- `taken`: A boolean indicating whether the penalty block has been taken by the player.
- The constructor takes the row and column parameters to calculate the initial position of the penalty block based on the dimensions of the `GameBlock`. It then calls the draw method to create the Rectangle and set its visual properties.
- `draw`: Initializes the Rectangle (bomb) by setting its width, height, position, and fill (using an image pattern).
- `updatePosition`: Updates the position of the penalty block to simulate its downward movement. This method is typically called in a game loop or during game updates.
- `getBomb`: Getter method for the Rectangle representing the penalty block.
- `getY`, `getX`: Getter methods for the y and x coordinates of the penalty block.
- `setTaken`: Setter method to indicate whether the penalty block has been taken.

## Modified Java Classes:

### Main
- The `Main` class is in `brickGame` package
- The initial setup where the `Main` class contains all the logic for the game (including `GameModel`, `GameView`, `GameController`, `GameBall`, and `GamePaddle`) suggests a lack of separation of concerns
- After refactoring based on MVC pattern, each component (`GameModel`, `GameView`, and `GameController`) is responsible for its specific role in the application, adhering to the MVC pattern.
- This separation improves code organization, maintainability, and makes it easier to understand and extend the codebase.
- Now `Main` class serves as the entry point for the JavaFX application and initializes the game by creating an instance of the GameController class.

### GameScore
- `Score` class has been transformed into the `GameScore` class within the `View` package.
- This class is responsible for displaying various messages and handling their animations in the game.
1. Method Extraction:
   - Extracted the common logic for setting the position and size of labels into the private method `setposnsizelabel`.
2. Code Simplification:
   - Simplified the conditional expression in the show method to determine the sign of the score. Utilized `lambda` expressions to simplify code blocks with a single method call.
3. Animation Refactoring:
   - Refactored the animation logic into a separate private method `animateLabel` for better readability. Used a `Timeline` to manage the animation frames, making the code more structured.
4. Label and Button Creation:
   - Created private methods `createLabel` and `createButton` to abstract the creation of Label and Button instances with common properties.
5. Reuse of Logic:
   - Reused the common logic for creating labels and buttons in the `showGameOver`, `showWin`, `showGamePaused`, and `removeGamePaused` methods.
6. EventHandler Simplification:
   -  Used `lambda` expressions to simplify the creation of `EventHandler` instances, making the code more concise.

### GameEngine
- This class is located in `Controller` class act as one of the component of controller
1. **Thread Replacement with JavaFX AnimationTimer and Timeline:**
    - Replaced the usage of traditional threads with JavaFX's AnimationTimer and Timeline for better integration with JavaFX applications.
    - Introduced Timeline for the update loop and physics loop to handle periodic actions.
2. **FPS Calculation:**
    - Instead of calculating the sleep time based on FPS, used AnimationTimer and Timeline which inherently operate with a frame rate.
3. **Pause and Resume Functionality:**
    - Added methods for pausing and resuming the game. This was not present in the original code.
4. **Initialization Logic:**
    - Kept the initialization logic (onAction.onInit()) in a separate method for clarity.
5. **Time Tracking:**
    - Replaced the custom time tracking logic with the use of System.currentTimeMillis().
6. **Code Cleanup:**
    - Removed unnecessary code, such as the isStopped flag and the explicit stopping of threads using stop(). JavaFX handles the lifecycle of Timeline and AnimationTimer.
7. **Public Interface:**
    - Maintained the same public interface through the OnAction interface, ensuring compatibility with the existing implementations.
8. **Default FPS Value:**
    - Set a default value of 60 FPS for smoother animations. This can be modified using the setFps method.
9. **Check for Running State:**
    - Added a method isRunning() to check whether the game engine is currently running.

### LoadSave
1. **Exception Handling Improvement:**
    - Improved exception handling by using a `logger` to log specific information about the exceptions that may occur during file reading.
2. **Dynamic Save Path:**
    - In the original code, the save path was hardcoded to Main.savePath. In the refactored code, the path is obtained from `GameController.savePath`.
    - This allows for more flexibility, especially when the save path is changed or defined dynamically.
3. **File Existence Check:**
    - Removed the unnecessary check for file existence `(new File(Main.savePath))` when creating the `ObjectInputStream`.
    - It's not required because an `ObjectInputStream` can handle the absence of a file by throwing an `EOFException`.

### GameBlock
- The class name has been changed from `Block` to `GameBlock` and moved to `GameElements` package
1. **Refactor collision detection logic `checkhittoblock` method**
    - The method now accepts an additional parameter `ballRadius` to improve collision detection accuracy, considering the size of the ball.
    - The new version uses boolean checks to determine whether the ball collides with the top, bottom, left, or right edges of the block.
    - The collision detection conditions are more explicitly stated, making it clear under which circumstances the ball hits the top, bottom, left, or right of the block.
2. **Additional Block Types**
    - An extra block type (`BLOCK_BOMB`) has been introduced, expanding the variety of blocks in the game.

### BonusBlock
- In the refactored code, the `Bonus` class has been renamed to `BonusBlock` to follow a more consistent naming convention with other classes in the project. 
- Additionally, the class has been moved to the `GameElements` package to organize related game elements. 
- The structure of the code remains similar to the original, with minor improvements for better readability and maintainability.

## Unexpected Problems:
  
### Unable to Save Game:
- Adjusted the save path from D drive to C drive to ensure that the game is stored in the correct location.

### Load game issue:
- After load the saved game, after all blocks are destroyed the level doesnt proceed to next level.
- This issue resolved by adding a variable `initialblockcount` to calculate the number of blocks remaining from saved game and compare with `destroyedblockcount` to check whether all the blocks are destroyed. 

### Game Ball Move Beyond the Walls:
1. **Reset collide flag** (`Model` package, `GameModel` class, `handleWallCollisions` method)
    - Call `resetCollisionFlags` every time the ball hits the wall.
2. **Accurate collisions** (`Model` package, `GameModel` class, `handleWallCollisions` method)
    - Take ball radius into consideration when collide with walls, paddle and blocks.

### Paddle Movement Delayed & Move Beyond `sceneWidth`:
1. **Replace thread with Timeline** (`Model` package, `GameModel` class, `move` method)
    - There is delay on paddle movement and sometimes it moves beyond the scenewidth when handled using separate thread. 
    - This is because when using `Thread`, code inside thread runs on separate thread from the UI thread which might run into synchronization issues.
    - `Timeline` ensures that keyframes and animations are executed on the UI thread. (smoother paddle movement)

### Thread Related Issues 
1. **Shared data in multi-threaded environment**:
    - When multiple threads are involved, it's crucial to synchronize access to shared resources. There are bugs and erros such as label `+1` still displaying after block is destroyed, ball penetrating through blocks frequently, concurrent errors etc
    - The issues occur due to lack of synchronization in the code, multiple threads access shared data wihtout proper synchronization
2. **Timeline Integration in `GameEngine` class**:
    - JavaFX Timeline is designed to work well with the UI thread ensures animations itself run on the UI thread, making it easier to handle animations and updates in a graphicacl user interface and eliminating the need for explicit synchronization in many cases.
    - However, ball penetration through blocks issue is reduced but not resolved due to the logic of collision.

### Bug after Timeline integration
- `nextLevel` method in `checkdestroyedcount` method, `checkdestroyedcount` method in `onPhysicsUpdate` method.
- After integration `Timeline` instead of `Thread` in `GameEngine` class, the level proceed twice after all blocks destroyed for example, level 1 straight jump to level 3.
- Based on my understanding is `onPhysicsUpdate` method is invoked twice as new `Timeline` is created at the end of level and start of new level.
- Due to the invocation of the `onPhysicsUpdate` method occurring twice, it results in the `nextLevel` method being triggered twice as well.
1. **Add a boolean flag `leveldone`**
    - This boolean is only set to true only `nextLevel` method is invoked and in reset to false in `nextLevel` method.
    - This ensure that the `nextLevel` method can only invoke one time.

### Game Ball Penetration Thrrough Blocks
1. **Ball direction move vertically up or down**:
    - In this case, sometimes the ball straight penetrate through the blocks even the velocity of ball is slow.
2. **Ball bouncing between multiple blocks with high velocity**
    - In this case, sometimes the blocks beside hit block will be destroyed as well.
    - This is because when ball hit a blocks but didnt bounce immediately and bounce when the ball is already inside the blocks and due to the bouncing angle hit the blocks beside, seems like the ball is penetrating.
3. **Considering blocks edges in collision logic**
    - Take blocks' edges into consideration when tuning the collision detection logic. This already implemented in the collision detection logic and reduce the penetration.
4. **Add cooldown time between collision of blocks**
5. **Frame Rate Independent Movement** 
