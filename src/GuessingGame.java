/**
 * A class to represent a number guessing game with an UI
 */

 /*
 * 1. Create a frame in the constructor and add panels with all important components
 *      > create 3 buttons: Help, Guess, New Game with ActionListeners
 * 2. Create an object of the GuessingGame class in the main method
 * 3. Override the actionPerformed method to recognize when a button is clicked and perform a set of tasks specific to that button
 *      > Guess:
 *        1. Get user input
 *        2. Check if user input is integer and convert from String to int (catch exception here)
 *        3. Check user input and set specific enum value for it (CORRECT, TOOHIGH, TOOLOW, OUTOFRANGE)
 *        4. Switch statement performs spectific tasks (e.g updates UI) for each enum value
 *      > New Game: reset all variables and the UI
 *      > Help: Open JOptionPane and display message that explains the game
 */

import javax.swing.*;
import java.util.Random;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GuessingGame extends JFrame implements ActionListener {
    private static final int minValue = 100; //minimum value that can be guessed is 100.
    private static final int maxValue = 999; //maximum value that can be guessed is 999.
    private static final int maxGuesses = 11; //user can only make 11 guesses.
    private static final Random rng = new Random(); //randome number generator
    private static int numberChosen = rng.nextInt(maxValue - 99) + minValue; // generates a random number between 100 and 999
    private static int numGuesses = 1; //number of guesses so far
    private static int numberGuessed;
    private static String prevGuessesMessage = "Guess " + numGuesses;
    private JLabel guessesLeftLabel, resultLabel, enterGuessLabel, rangeLabel, previousGuessesLabel;
    private JTextField guessField;
    private JButton guessButton, helpButton, restartButton;
    private JTextArea previousGuessesArea, resultTextArea;
    private JPanel mainPanel, topPanel, middlePanel, bottomPanel, buttonPanel;
    
    private enum Results{
        CORRECT, TOOLOW, TOOHIGH, OUTOFRANGE
    } //enumerated types to represent the four possible result.
    private Results guessResult; //updates enum value for user input every round
    
    /**
     * Opens guessing game UI
     * @param args command line arguments
     */
    public static void main(String[] args) {
        //create new GuessingGame object by calling constructor
        GuessingGame guessgame = new GuessingGame();
        //set text in previousGuessesArea to prevGuessesMessage
        guessgame.previousGuessesArea.setText(prevGuessesMessage);
    }

    /** 
    * GuessingGame constructor: creates UI
    */
    public GuessingGame()
    {
        //create new frame
        JFrame frame = new JFrame("Guessing Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,450);  //set frame size
        frame.setLocationRelativeTo(null);  //make frame centered
        frame.setVisible(true);             //set frame to visible
        frame.setResizable(false);  //dont allow resizing


        //create main panel for whole UI
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new BorderLayout(10,10));
        

        //create top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(10,10));
        //initalize components
        rangeLabel = new JLabel("Guess a number between " + minValue + " and " + maxValue);
        resultLabel = new JLabel("Your result:");   
        resultTextArea = new JTextArea(1,30);       //resultTextField shows result message for input: higher/lower/won/lost
        resultTextArea.setEditable(false);
        resultTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        guessesLeftLabel = new JLabel("Guesses left: " + (maxGuesses - numGuesses + 1));
        //add components to topPanel
        topPanel.add(rangeLabel, BorderLayout.PAGE_START);
        topPanel.add(resultLabel, BorderLayout.LINE_START);
        topPanel.add(resultTextArea, BorderLayout.CENTER);
        topPanel.add(guessesLeftLabel, BorderLayout.PAGE_END);


        //create middle panel
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout(10,10));
        //initialize components
        previousGuessesLabel = new JLabel("Previous guesses:");
        previousGuessesArea = new JTextArea(11, 10);
        previousGuessesArea.setEditable(false);
        previousGuessesArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        //add components to middlePanel
        middlePanel.add(previousGuessesLabel, BorderLayout.PAGE_START);
        middlePanel.add(previousGuessesArea, BorderLayout.CENTER);


        //create bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout(10,10));
        //initialize components
        enterGuessLabel = new JLabel("Enter your guess:");
        guessField = new JTextField(10);
        guessField.addActionListener(this);
        
        //create button panel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout(10,100));
        //initialize buttons with action listeners
        guessButton = new JButton("Guess");
        guessButton.addActionListener(this);
        helpButton = new JButton("Help");
        helpButton.addActionListener(this);
        restartButton = new JButton("New Game");
        restartButton.addActionListener(this);
        //add buttons to buttonPanel
        buttonPanel.add(guessButton, BorderLayout.CENTER);
        buttonPanel.add(helpButton, BorderLayout.LINE_START);
        buttonPanel.add(restartButton, BorderLayout.LINE_END);
        
        //add bottom panel components and button panel to bottom panel
        bottomPanel.add(enterGuessLabel, BorderLayout.PAGE_START);
        bottomPanel.add(guessField, BorderLayout.LINE_START);
        bottomPanel.add(buttonPanel, BorderLayout.PAGE_END);


        //add the top, middle and bottom panel to the main panel
        mainPanel.add(topPanel, BorderLayout.PAGE_START);
        mainPanel.add(middlePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.PAGE_END);
        
        frame.getContentPane().add(mainPanel,BorderLayout.CENTER);
    }

    /**
    * Override actionPerformed method which gets invoked when an action is performed
    * Performes specific tasks if one of the three buttons is clicked
    * @param ae the ActionEvent that was invoked
    */
    @Override
    public void actionPerformed(ActionEvent ae){
        if (ae.getSource() == guessButton) {
            //check if user has guesses left 
            if (numGuesses <= maxGuesses && guessResult != guessResult.CORRECT) {
                //get user input from guess textfield
                String inputString = guessField.getText();
                //clear input field
                guessField.setText("");
                //check if input is integer
                try{
                    numberGuessed = Integer.parseInt(inputString);
                } catch (Exception e) {
                    resultTextArea.setText("Your input is not an integer. Please try again.");
                    return; //beaks out of method
                }
        

                //check guess to get correct enum
                guessResult = checkGuess(numberGuessed, numberChosen);

                //perform correct action according to 
                switch (guessResult) {
                    case CORRECT:
                        CorrectGuessMessage(numGuesses, numberChosen); //message to inform user of how many attempts it took them to guess the number correctly.
                        //add user guess to prevGuessesMessage String
                        prevGuessesMessage += " = " + numberGuessed + "\n";
                        previousGuessesArea.setText(prevGuessesMessage);
                        break;
                    case TOOLOW:
                        TooLowMessage(); //number guessed is too low
                        numGuesses++;
                        guessesLeftLabel.setText("Guesses left: " + (maxGuesses - numGuesses + 1));

                        //add user guess to prevGuessesMessage String
                        prevGuessesMessage += " = " + numberGuessed + "\n" + ((numGuesses <= maxGuesses) ? ("Guess " + numGuesses) : "");
                        previousGuessesArea.setText(prevGuessesMessage);

                        //if no guesses left, display lost message
                        if (numGuesses > maxGuesses) {
                            GuessesUsedUpMessage(numberChosen);
                        }
                        break;
                    case TOOHIGH:
                        TooHighMessage(); //number guessed is too high
                        numGuesses++;
                        guessesLeftLabel.setText("Guesses left: " + (maxGuesses - numGuesses + 1));

                        //add user guess to prevGuessesMessage String
                        prevGuessesMessage += " = " + numberGuessed + "\n" + ((numGuesses <= maxGuesses) ? ("Guess " + numGuesses) : "");
                        previousGuessesArea.setText(prevGuessesMessage);

                        //if no guesses left, display lost message
                        if (numGuesses > maxGuesses) {
                            GuessesUsedUpMessage(numberChosen);
                        }
                        break;
                    case OUTOFRANGE:
                        InvalidGuessMessage(); //number guessed is out of the range 100-999
                        break;
                }
            }
        }  else if (ae.getSource() == restartButton) {
            //reset all the variables
            numberChosen = rng.nextInt(maxValue - 99) + minValue;
            numGuesses = 1;
            prevGuessesMessage = "Guess " + numGuesses;

            //update UI
            previousGuessesArea.setText(prevGuessesMessage);
            resultTextArea.setText("");
            guessesLeftLabel.setText("Guesses left: " + (maxGuesses - numGuesses + 1));
        } else if (ae.getSource() == helpButton) {
            String helpString = "Welcome to the Guessing Game!\n\nIn this game, it is your task to guess a randomly chosen number between "
                     + minValue + " and " + maxValue + " within " + maxGuesses + " guesses.\nNo worries, although this might seem impossible "
                     + "at first, we offer you hints.\nThe game will tell you if your guess was higher or lower than the correct number "
                     + "and will even display\nyour guessing history. For each guess, enter your chosen number in the text field below\n\"Enter "
                     + "your guess:\" and click the large \"Guess\"-button to confirm.\nNow check the result field for a hint or other messages.\n\n"
                     + "! IMPORTANT ! Please only enter integers as the program wont accept other types of input.\n"
                     + "Note that if you want to start a new game, please click the \"New Game\"-button and all your \nprogress from the previous game will be "
                     + "deleted. \nHave fun!";
            JOptionPane.showConfirmDialog(null, helpString, "Info", JOptionPane.DEFAULT_OPTION);
        }
    }

    /**
     *Checks whether the guess is correct, too low, too high or out out of range
     * @param guess is the users guess
     * @param correctNumber is the number the program chose to be the correct number
     * @return result as ENUM
     */
    private static Results checkGuess(int guess, int numberChosen) {
        if (guess < minValue || guess > maxValue) {
            return Results.OUTOFRANGE; //If guessed out of range 100-999, returns enum OUTOFRANGE.
        } else if (guess == numberChosen) {
            return Results.CORRECT; //If guessed equal to chosen number, returns enum CORRECT.
        } else if (guess < numberChosen) {
            return Results.TOOLOW; //If guessed less than chosen number, returns enum TOOLOW.
        } else{
            return Results.TOOHIGH; //If guessed greater than chosen number, returns enum TOOHIGH.
        }
    }

    /**
     * Method to display a message to the player after they have correctly guessed the number.
     * @param guessCount is the number of guesses the user used to guess the right number
     * @param correctNumber is the number the program chose to be the chosen number
     */
    private void CorrectGuessMessage(int guessCount, int numberChosen) {
        resultTextArea.setText("Congratulations:) You've read my mind in " + guessCount + " guesses.");
    } 

    /**
     * Method to display a message to the player if number guessed is less than chosen number.
     */
    private void TooLowMessage() {
        resultTextArea.setText("Your guess is too low.");
    } 

    /**
     * Method to display a message to the player if number guessed is higher than chosen number.
     */
    private void TooHighMessage() {
        resultTextArea.setText("Your guess is too high.");
    } 

    /**
     * Displays a message to the user telling them they have to enter a number in a specific range.
     */
    private void InvalidGuessMessage() {
        resultTextArea.setText("Your guess is out of range. Please enter a number between " + minValue + " and " + maxValue + ".");
    }

    /**
     * Method to display a message to the player if all guesses are used up.
     * @param correctNumber is the number the program chose to be the correct number
     */
    private void GuessesUsedUpMessage(int numberChosen) {
        resultTextArea.setText( "Hard Luck :( You have used up all your guesses. The number was " + numberChosen + ".");
    } 
 }    
