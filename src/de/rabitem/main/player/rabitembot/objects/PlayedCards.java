package de.rabitem.main.player.rabitembot.objects;

import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.player.instances.RabitemBot;

import java.util.ArrayList;

public class PlayedCards implements MySqlUtil{

    private int id;
    private Bot bot;

    public PlayedCards(Bot bot) {
        this.bot = bot;
        if (!this.existsInDatabase())
            this.pushDatabase();
        this.getCards().forEach(c ->{
                    if (!c.existsInDatabase())
                        c.pushDatabase();
                });
    }

    @Override
    public void pushDatabase() {
        if (this.existsInDatabase()) {
            System.out.println("[ERROR] Couldn't push PlayerCard to Database - already exists in Database!");
            return;
        }
        String query = "insert into holsdergeier.playedcards(Bot) " +
                "values(" + this.bot.getId() + ");";
        RabitemBot.mySql.update(query);
        // this.id = this.getId();
    }

    @Override
    public boolean existsInDatabase() {
        return this.getAttribute("count(*) as count", "count", "playedcards", "Bot",  Integer.class, String.valueOf(this.bot.getId())) == 1;
    }

    public int getId() {
        return this.getAttribute("idPlayedCards", "idPlayedCards", "playedcards", "Bot",  Integer.class, String.valueOf(this.bot.getId()));
    }

    public Bot getBot() {
        return bot;
    }

    public Card getCard(PlayerCard card) {
        return new Card(card.getValue(), this);
    }

    public ArrayList<Card> getCards() {
        ArrayList<Card> playerCards = new ArrayList<>();
        for (int i = 1; i < 16; i++) {
            playerCards.add(new Card(i, this));
        }
        return playerCards;
    }
}
