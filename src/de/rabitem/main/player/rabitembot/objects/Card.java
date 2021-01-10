package de.rabitem.main.player.rabitembot.objects;

import de.rabitem.main.player.instances.Felix;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Card implements MySqlUtil {
    int value;
    PlayedCards fk_PlayedCards;

    public Card(int value, PlayedCards playerCards) {
        this.value = value;
        this.fk_PlayedCards = playerCards;
    }

    @Override
    public boolean existsInDatabase() {
        String query = "select count(*) as count from holsdergeier.card where fk_PlayedCards = " +
                this.fk_PlayedCards.getBot().getId() + " && Value = " + this.value + ";";
        ResultSet resultSet = Felix.mySql.query(query);
        try {
            return resultSet.getInt("count") == 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public void pushDatabase() {
        if (this.existsInDatabase()) {
            System.out.println("[ERROR] Couldn't push Card to Database - already exists in Database!");
            return;
        }
        String query = "insert into holsdergeier.card(Value, fk_PlayedCards)" +
                "values(" + this.value + "," + fk_PlayedCards.getId() + ");";
        Felix.mySql.update(query);
    }

    public void incCardUsed(int pos) {
        String s = pos < 0 ? "Minus" + pos * -1 : String.valueOf(pos);
        String query = "update holsdergeier.card " +
                "set TimesUsedFor" + s + " = TimesUsedFor" + s + " + 1 " +
                "where fk_PlayedCards = " + this.fk_PlayedCards.getId() + " && Value = " + this.value + ";";
        Felix.mySql.update(query);
    }

    public int getValue() {
        return this.value;
    }

    public double getProbability(int pos) {
        String s = pos < 0 ? "Minus" + pos * -1 : String.valueOf(pos);
        String query = "select TimesUsedFor" + s + " from holsdergeier.card where fk_PlayedCards = " + this.fk_PlayedCards.getId() +
                " && Value = " + this.value + ";";
        // System.out.println(query);
        ResultSet resultSet = Felix.mySql.query(query);
        try {
            int i = resultSet.getInt(1);
            return (double) i/(this.fk_PlayedCards.getBot().getTotalRoundsPlayedAgainst()/15.0);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0.0;
    }
}
