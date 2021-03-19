package view;

import models.Card;
import models.Match;

import java.util.ArrayList;
import java.util.Scanner;

public class MatchView {
    private final Match match;
    private final Scanner sc;

    public MatchView(Match match) {
        this.match = match;
        this.sc = new Scanner(System.in);
    }

    public int displayMenuOptions() {
        System.out.println("Welcome to memorize!");
        System.out.println("(0) Exit");
        System.out.println("(1) Play");
        System.out.println("(2) List cards in database");
        System.out.println("(3) Add new card in database");
        System.out.println("(4) Delete card in database");

        System.out.println("Choose an option: ");
        int option = sc.nextInt();

        while (option < 0 || option > 4) {
            System.out.println("Invalid option. Please try again!");

            System.out.println("Choose an option: ");
            option = sc.nextInt();
        }

        return option;
    }

    public void displayBoard(ArrayList<Card> board, ArrayList<Card> originalBoard, String boardName){
        System.out.print(boardName + " = { ");
        for (Card card : originalBoard) {
            if (!board.contains(card)) {
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
        System.out.println("You won!");
    }

    public void displayLoserScreen() {
        System.out.println("You lost!");
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
        System.out.print("Press {Enter} to continue!");
        sc.nextLine();
    }

    public void displayRemainingAttempts() {
        System.out.println("Remaining number of attempts: " + match.getRemainingAttempts());
    }

    public Card selectQuestion() {
        System.out.print("Enter a number referencing a card on the Question Board or {x} to leave: ");
        String data = sc.nextLine();
        if (data.equals("x")) {
            return new Card();
        }
        else {
            int questionsNumber = Integer.parseInt(data);
            while (!match.getQuestionBoard().contains(match.getOriginalQuestionBoard().get(questionsNumber))) {
                System.out.println("Invalid option. Please try again!");
                System.out.println("Enter a number referencing a card on the Question Board or {x} to leave: ");
                questionsNumber = sc.nextInt();
            }

            Card questionCard = match.getOriginalQuestionBoard().get(questionsNumber);
            this.displayQuestionSide(questionCard);
            return questionCard;
        }
    }

    public Card selectAnswer() {
        System.out.print("Enter a number referencing a card on the Question Board or {x} to leave: ");
        String data = sc.nextLine();
        if (data.equals("x")) {
            sc.close();
            return new Card();
        }
        else {
            int answersNumber = Integer.parseInt(data);
            while (!match.getAnswerBoard().contains(match.getOriginalAnswerBoard().get(answersNumber))) {
                System.out.println("Invalid option. Please try again!");
                System.out.println("Enter a number referencing a card on the Question Board or {x} to leave: ");
                answersNumber = sc.nextInt();
            }

            Card answerCard = match.getOriginalAnswerBoard().get(answersNumber);
            this.displayAnswerSide(answerCard);
            return answerCard;
        }
    }
}
