# Penn-Man
CIS 120 final project

biscaro@seas.upenn.edu //for all inquries

This project was done as final project for CIS 120. 

**Rules and Objectives**
-Use arrow keys to move. 
-Collect all problem sets (white dots) or blow up all TA's to win. 
-Collecting coffees will enter scatter mode for 5 seconds. In scatter mode, game movement 
speed is doubled, and collision with TA's causes them to explode. 
-If not in scatter mode, collision with TA's loses a life. 
-There are 3 lives total. 

The project consists of  7 classes and a Direction enumeration. 
The enumeration stores 5 directions (UP, DOWN , LEFT, RIGHT, PARALLEL). 

Audio and Image Controller simply handle loading and initializing the audio and video files used
throughout the game. 

The GameCourt class handles certain game mechanics such as conditions for winning, losing,
drawing the maze, and animating character movement using a Timer and the tick() method. 
**Method for drawing the maze is as follows: 
-Create a 2d integer array denoting x,y positions in the game court. Current maze is 16x16. Place a 0 to
denote a path and a 1 to denote a wall. A 2 denotes a coffee power-up. 
-When drawing, we use a standard tile size of 32x32 for each of the 16x16 tiles. Note that the
dimensions of the game court are 512x512, and 32*16x32*16 = 512x512. The reason for this
implementation is that it is efficient, and it makes it very easy to create mazes, which can 
eventually be auto-generated by simply placing  numbers throughout the 2d array. 

The GameObj class is an abstract class that defines general behaviors for the characters in the 
game. There are currently two types of GameObj's. PennMan and the TA's. 
A majority of GameObj is devoted to defining methods to enable movement throughout the maze while
respecting wall boundaries. This proved to be somewhat challenging given the implementation of
the maze, as we  need to translate between 16x16 integer array coordinates, and pixel coordinates.
This can be seen in the translate() function. 

Procedure for detecting wall collisions is as follows: 
- We first switch on the current direction of travel, as we only need to check for a collision
in that direction. Depending on the direction, we will check the corners of the 32x32 tile closest
to the wall in question to see if they are colliding (i.e if dirTravel is UP, check topLeft and
topRight. If dirTravel is Right, check topRight and bottomRight, etc.). The collision method 
simply returns the direction of the collision and does is not involved in actually preventing the 
collision from happening. This is the restrict() functions job. Upon collision, the restrict function
"snaps" the user to the closest tile, and sets  velocity  of travel in direction of collision 
to zero. How this is handled depends on the type of GameObj, so this is as specific as it gets
in the gameObj class. 
Note: the SnapX function snaps the x-coordinate to the closest tile. it does this by taking the 
current x-coordinates and finds the closest multiple of 32 (tile size). Similar for snapY. 

Procedure for navigating pennMan is as follows: 
This is handled in the checkPath() method of the Man class. Every time a key is pressed we store it
in the lastPressed Direction variable. We then check if there is currently a path in that 
direction, if so, take it and clear lastPressed to be null. Otherwise, we leave lastPressed. That
way we can continuously check in that direction, and if a new key is not pressed before a 
path in that direction becomes available, we will take that path. This allows for lenience in the
timing of the key press from the user when navigating through the maze, and makes it easier to move.

Procedure for navigating TA is  as follows:
This is slightly more complex as it must be done without any user input. 
This is handled by the navigate function in TA. We first check if the TA is in scatter mode. If
so, it is sufficient for the TA's take random paths throughout the maze. They uses the check path
methods from GameObj to do this, but only take any path with probability 0.3. If they are 
not in scatter mode, we  define them as "chasers". The goal is to constantly try and 
minimize our distance from  man. We use the relativeX and relativeY methods to  determine in 
which direction we need to travel in order to get closer to pennMan. If a path becomes available
in the correct direction, we take it, otherwise we continue searching. If a scenario arises where
the TA has no direct path in horizontal OR vertical direction, chaser is switched off until a path
in the relevant direction becomes available. In other word, the TA will wander randomly until a 
path in the correct direction becomes available. 

Enjoy!!!
