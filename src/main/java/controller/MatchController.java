package controller;
import dao.CardDao;
import models.Card;
import models.Match;
import view.MatchView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class MatchController {

    public MatchController() {
        setup();
    }

    public void playGame() {
        listCards();
        System.out.println("Type a list of cards id to play: ");
        Scanner sc = new Scanner(System.in);
        String listOfCards = sc.nextLine();
        ArrayList<String> ids = new ArrayList<>(Arrays.asList(listOfCards.split(" ")));
        System.out.println("Enter the number of attempts you want to have: ");
        int attempts = sc.nextInt();
        this.match = new Match(ids, attempts);
        this.view = new MatchView(match);
        loop();
    }

    public void listCards() {
        ArrayList<Card> cards;
        try {
            cards = new CardDao().getAll();
        } catch (Exception e) {
            System.out.println("Couldn't get cards");
            return;
        }

        cards.forEach(card -> {
            view.displayId(card);
            view.displayQuestionSide(card);
            view.displayAnswerSide(card);
            System.out.println();
        });
    }

    public void addNewCard() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Type the question of the new card: ");
        String newCardQuestion = sc.nextLine();
        System.out.println("Type the answer of the new card: ");
        String newCardAnswer = sc.nextLine();
        Card card = new Card();
        card.setQuestion(newCardQuestion);
        card.setAnswer(newCardAnswer);

        try {
            CardDao dao = new CardDao();
            dao.insert(card);
        }
        catch (Exception e){
            System.out.println("Couldn't save card");
        }

    }

    public void deleteCard() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Type the ID of the card to delete: ");
        try {
            int deleteCardId = Integer.parseInt(sc.nextLine());
            CardDao dao = new CardDao();
            dao.delete(deleteCardId);
        }
        catch (Exception e){
            System.out.println("Couldn't delete card");
        }
    }

    public void menu() {
        int option = view.displayMenuOptions();
        switch (option) {
            case 0:
                return;
            case 1:
                playGame();
                menu();
                break;
            case 2:
                listCards();
                menu();
                break;
            case 3:
                addNewCard();
                menu();
                break;
            case 4:
                deleteCard();
                menu();
                break;
        }
    }

    public void setup() {
        ArrayList<String> ids = new ArrayList<>(Collections.singletonList("1"));
        int attempts = 1;
        this.match = new Match(ids, attempts);
        this.view = new MatchView(match);
    }

    public void loop() {
        while(!match.isWinner() && !match.isLoser()) {
            view.displayRemainingAttempts();
            view.displayQuestionBoard();
            view.displayAnswerBoard();
            Card questionCard = view.selectQuestion();
            Card answerCard = view.selectAnswar();

            if(questionCard.equals(answerCard)) {
                view.displayRightAnswerScreen();
                match.removeCard(String.valueOf(questionCard.getId()));
            } else {
                view.displayWrongAnswerScreen();
            }
            match.endTurn();
            view.waitScreen();
            view.clearScreen();
        }
        if(match.isWinner()) {
            view.displayWinnerScreen();
        }
        if(match.isLoser()) {
            view.displayLoserScreen();
        }
    }

    private Match match;
    private MatchView view;
}
