package de.rabitem.main.player.rabitembot;

import java.sql.*;
import java.util.Objects;

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
            try (Statement st = (Statement) con.createStatement()) {
                st.executeUpdate(qry);
            }
        } catch (SQLException e) {
            connect();
            e.printStackTrace();
        }
    }

    public ResultSet query(String qry) {
        // System.out.println(qry);
        ResultSet rs = null;
        try {
            Statement st = (Statement) con.createStatement();
            rs = st.executeQuery(qry);
        } catch (Exception e) {
            connect();
            e.printStackTrace();
        }
        // wait for ResultSet, this might take a bit
        while (true) {
            try {
                if (Objects.requireNonNull(rs).next()) break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rs;
    }

}

