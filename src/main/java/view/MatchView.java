package view;
import controller.MatchController;
import models.Card;
import models.Match;
import java.util.ArrayList;
import java.util.Scanner;

public class MatchView {
    public MatchView(Match match) {
        this.match = match;
    }

    public int displayMenuOptions() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to memorize!");
        System.out.println("(0) Exit");
        System.out.println("(1) Play");
        System.out.println("(2) List cards in database");
        System.out.println("(3) Add new card in database");
        System.out.println("(4) Delete card in database");
        System.out.print("Choose an option: ");
        int option = sc.nextInt();
        while(option!=0 && option!=1 && option!=2 && option!=3 && option!=4) {
            System.out.print("\nSorry, this option is no longer available. ");
            System.out.print("Enter a number available in menu: ");
            option = sc.nextInt();
        }
        return option;
    }

    public void displayQuestionBoard() {
        ArrayList<Card> board = match.getQuestionBoard();
        ArrayList<Card> originalBoard = match.getOriginalQuestionBoard();
        System.out.print("Questions Board" + " = { ");
        for (Card card : originalBoard) {
            if(!board.contains(card)) {
                System.out.print("@ ");
            } else {
                System.out.print(originalBoard.indexOf(card) + " ");
            }
        }
        System.out.print("}\n");
    }
    public void displayAnswerBoard() {
        ArrayList<Card> board = match.getAnswerBoard();
        ArrayList<Card> originalBoard = match.getOriginalAnswerBoard();
        System.out.print("Answers Board" + " = { ");
        for (Card card : originalBoard) {
            if(!board.contains(card)) {
                System.out.print("@ ");
            } else {
                System.out.print(originalBoard.indexOf(card) + " ");
            }
        }
        System.out.print("}\n");
    }


    public void displayQuestionSide(Card card) {
        System.out.println("Question: " + card.getQuestion());
    }
    public void displayAnswerSide(Card card) {
        System.out.println("Answer: " + card.getAnswer());
    }
    public void displayId(Card card) {
        System.out.println("ID: " + card.getId());
    }

    public void displayWinnerScreen() {
        System.out.println("Winner!");
    }

    public void displayLoserScreen() {
        System.out.println("Loser!");
    }

    public void displayWrongAnswerScreen() {
        System.out.println("Wrong!");
    }

    public void displayRightAnswerScreen() {
        System.out.println("Right!");
    }

    public void clearScreen() {
        System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    public void waitScreen() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter to continue!");
        sc.nextLine();
    }

    public void displayRemainingAttempts() {
        System.out.println("Remaining number of attempts: " + match.getRemainingAttempts());
    }

    public Card selectQuestion() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a number to Questions Board: ");
        int questionsNumber = sc.nextInt();
        while(!match.getQuestionBoard().contains(match.getOriginalQuestionBoard().get(questionsNumber))) {
            System.out.print("\nSorry, this option is no longer available. ");
            System.out.print("Enter a number to Questions Board: ");
            questionsNumber = sc.nextInt();
        }
        Card questionCard = match.getOriginalQuestionBoard().get(questionsNumber);
        this.displayQuestionSide(questionCard);
        return questionCard;
    }

    public Card selectAnswar() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a number to Answers Board: ");
        int answersNumber = sc.nextInt();
        while(!match.getAnswerBoard().contains(match.getOriginalAnswerBoard().get(answersNumber))) {
            System.out.print("\nSorry, this option is no longer available. ");
            System.out.print("Enter a number to Answers Board: ");
            answersNumber = sc.nextInt();
        }
        Card answerCard = match.getOriginalAnswerBoard().get(answersNumber);
        this.displayAnswerSide(answerCard);
        return answerCard;
    }

    private Match match;
}
