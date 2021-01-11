package de.rabitem.main.player.rabitembot.objects;

import de.rabitem.main.player.Player;
import de.rabitem.main.player.instances.Felix;

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
        Felix.mySql.update(query);
    }

    public default <T> T getAttribute(String s, String s2, String from, String where, Class<T> clazz, String comperator) {
        T output = null;
        String query = "select " + s + " from holsdergeier." + from + " " +
                "where " + where + " = \"" + comperator + "\";";
        ResultSet resultSet = Felix.mySql.query(query);
        try {
            output = resultSet.getObject(s2, clazz);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return output;
    }

    public default <T> T getAttribute(String s, String s2, String from, String where, Class<T> clazz, Player owner) {
        return this.getAttribute(s, s2, from, where, clazz, owner.getName());
    }

    public abstract void pushDatabase();

    public abstract boolean existsInDatabase();
}
