package models;

import dao.CardDao;
import java.util.ArrayList;
import java.util.Collections;

public class Match {
    public Match(ArrayList<String> ids, int attempts) {
        this.remainingAttempts = attempts;
        ArrayList<Card> allCards;
        try {
            allCards = new CardDao().getAll();
        }
        catch (Exception e){
            System.out.println("Couldn't get cards");
            return;
        }

        ArrayList<Card> selectedCards = new ArrayList<>();

        try {
            ids.forEach(id -> selectedCards.add(allCards.get(Integer.parseInt(id) - 1)));
        }
        catch (Exception e){
            System.out.println("Couldn't get cards");
            return;
        }

        this.questionBoard = new ArrayList<>(selectedCards);
        Collections.shuffle(this.questionBoard);
        this.originalQuestionBoard = new ArrayList<>(this.questionBoard);
        this.answerBoard = new ArrayList<>(selectedCards);
        Collections.shuffle(this.answerBoard);
        this.originalAnswerBoard = new ArrayList<>(this.answerBoard);
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public void removeCard(String id) {
        this.questionBoard.removeIf(card -> card.getId().equals(id));
        this.answerBoard.removeIf(card -> card.getId().equals(id));
    }

    public void endTurn() {
        this.remainingAttempts -= 1;
    }

    public boolean isWinner() {
        int sizeA = this.questionBoard.size();
        int sizeB = this.answerBoard.size();
        return sizeA == 0 && sizeB == 0 && this.remainingAttempts > 0;
    }

    public boolean isLoser() {
        int sizeA = this.questionBoard.size();
        int sizeB = this.answerBoard.size();
        return sizeA > 0 && sizeB > 0 && this.remainingAttempts < 0 || this.remainingAttempts == 0;
    }

    public int getNumberCards() {
        int sizeA = questionBoard.size();
        int sizeB = answerBoard.size();
        if(sizeA == sizeB) {
            return sizeA;
        } else {
            return -1;
        }
    }

    private int remainingAttempts;

    public ArrayList<Card> getQuestionBoard() {
        return questionBoard;
    }

    public ArrayList<Card> getAnswerBoard() {
        return answerBoard;
    }

    public ArrayList<Card> getOriginalQuestionBoard() {
        return originalQuestionBoard;
    }

    public ArrayList<Card> getOriginalAnswerBoard() {
        return originalAnswerBoard;
    }

    private ArrayList<Card> questionBoard;
    private ArrayList<Card> originalQuestionBoard;
    private ArrayList<Card> answerBoard;
    private ArrayList<Card> originalAnswerBoard;
}
