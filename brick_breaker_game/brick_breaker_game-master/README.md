# Brick Breaker Game
This report provides a summary on the major refactoring activities, additions and documentations made to the given codes for the Software Maintenance 
# About the Game
This is a simple arcade video game. Player's goal is to destroy a wall with a small ball. The game has very simple commmand: 

SPACE - Start/Pause the game 

A - move the player LEFT

D - move the player RIGHT 

ESC - Enter/Exit the pause menu 

ALT+SHITF+F1 - open console 

The game automatically pause if the frame loses focus.
# Gradle Run

Pre-requisite : Java 8 to Java 15
Note: Gradle is not compatible with Java versions after Java 15.

A build file is added as it automatically downloads and configures the dependencies and other libraries used. Gradle Wrapper allows us to run the build file without installing Gradle. When we invoke "gradlew", it downloads and builds the Gradle version specified. In order to run the application from the command line, the following steps can be followed:

  1. Open command prompt and navigate to the folder where the file exists by copying the path and typing: cd <path>
  2. Run the application using the command "gradlew run".

 > gradlew run


# Major Refactoring Activities
**1. Using Model View Controller architectural pattern**

Classes were seperated into respective packages based on the MVC pattern. The MVC pattern seperates an application into 3 main logical componnents: model, view, and controller.
The Model contains core functionality and data of the application. It manages the system data and associated operation of data.
The View displays the data to the user, but cannot influence what the user will do with the data. It defines and manages how the data is presented to the user.
The Controller acts on both the model and view. Controllers inform the model what to do.
