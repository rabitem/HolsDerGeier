package de.rabitem.main.player.rabitembot;

import de.rabitem.main.player.instances.Felix;
import de.rabitem.main.player.rabitembot.objects.RabitemUtil;

import java.sql.*;

public class MySql {

    private Connection con;
    private String host, database, user, password;

    public MySql(String host, String database, String user, String password) {
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
        this.connect();
    }

    public void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database + "?autoReconnect=true",
                    user, password);
            if (RabitemUtil.OUTPUT)
                System.out.println(RabitemUtil.PREFIX + "Succesfully established MySQL connection.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void close() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(String qry) {
        // System.out.println(qry);
        try {
            try (Statement st = con.createStatement()) {
                st.executeUpdate(qry);
            }
        } catch (SQLException e) {
            connect();
            e.printStackTrace();
        }
    }

    public ResultSet query(String qry) {
        if (con == null)
            Felix.mySql.connect();
        ResultSet rs = null;
        try {
            Statement st = con.createStatement();
            rs = st.executeQuery(qry);
            while (rs.next()) {
                return rs;
            }
        } catch (Exception e) {
            // RabitemBot.mySql.connect();
            e.printStackTrace();
        }
        return null;
    }

}

