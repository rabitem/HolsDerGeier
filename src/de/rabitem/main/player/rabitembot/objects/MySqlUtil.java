package de.rabitem.main.player.rabitembot.objects;

import de.rabitem.main.player.Player;
import de.rabitem.main.player.instances.RabitemBot;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface MySqlUtil {

    public default <T> T getAttribute(String s, String from, Class<T> clazz, Player owner) {
        return this.getAttribute(s, s, from, clazz, owner);
    }

    public default <T> T getAttribute(String s, String s2, String from, Class<T> clazz, Player owner) {
        return this.getAttribute(s, s2, from, "Name" ,clazz, owner);
    }

    public default void updateAttribute(String s, String newValue, String from, Player owner) {
        String query = "update holsdergeier." + from + " " +
                "set " + s + " = " + newValue + " " +
                "where Name = \"" + owner.getName() + "\";";
        RabitemBot.mySql.update(query);
    }

    public default <T> T getAttribute(String s, String s2, String from, String where, Class<T> clazz, Player owner) {
        T output = null;
        String query = "select " + s + " from holsdergeier." + from + " " +
                "where " + where + " = \"" + owner.getName() + "\";";
        ResultSet resultSet = RabitemBot.mySql.query(query);
        try {
            output = resultSet.getObject(s2, clazz);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return output;
    }

    public default <T> T getAttribute(String s, String s2, String from, String where, Class<T> clazz, T comparable) {
        T output = null;
        String query = "select " + s + " from holsdergeier." + from + " " +
                "where " + where + " = \"" + comparable + "\";";
        ResultSet resultSet = RabitemBot.mySql.query(query);
        try {
            output = resultSet.getObject(s2, clazz);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return output;
    }

    public abstract void pushDatabase();

    public abstract boolean existsInDatabase();
}
