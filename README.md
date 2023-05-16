# GuessingGame
A number guessing game using Java Swing.

[![License MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

## Requirements 
* Java 11 or higher.

## Installation
1. Download the repository files from the download section or clone this project by typing the following command in your terminal:

   ```
   git clone https://github.com/CarolinSchulz/GuessingGame.git
   ```
2. Import it into VS Code or another Java IDE.
3. Run the application :)

## Docker :whale:

1. As above, clone the project.
2. Build the Docker image:
   ```
   docker build . -t guessing-game
   ```
   The Dockerfile applies a multistage build. In the first stage, the Java program is compiled and packaged into a jar file. The second stage runs the jar file using in the [jlesage/baseimage-gui](https://hub.docker.com/r/jlesage/baseimage-gui) image, which exposes an X11 GUI application as web applicaton.

3. Start the Docker container and expose port `5800`
   ```
   docker run --rm  -p 5800:5800 guessing-game
   ```

4. In your browser, access the application at `localhost:5800`.

