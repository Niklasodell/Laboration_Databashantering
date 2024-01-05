package com.niklas.game;

import java.sql.*;

public class DatabaseHandler {

    private static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS Player (name TEXT PRIMARY KEY, health INT, damage INT)");
        stmt.execute("CREATE TABLE IF NOT EXISTS Monster (name TEXT PRIMARY KEY, health INT, damage INT)");
        stmt.execute("CREATE TABLE IF NOT EXISTS FightHistory (timestamp TIMESTAMP, player TEXT, monster TEXT, playerWon BOOLEAN)");
        stmt.close();
    }

    private static Player loadPlayer(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Player");
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            String name = rs.getString("name");
            int health = rs.getInt("health");
            int damage = rs.getInt("damage");
            return new Player(name, health, damage);
        }
        return null;
    }

    private static void savePlayer(Connection conn, Player player) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT OR REPLACE INTO Player (name, health, damage) VALUES (?, ?, ?)");
        stmt.setString(1, player.getName());
        stmt.setInt(2, player.getHealth());
        stmt.setInt(3, player.getDamage());
        stmt.executeUpdate();
    }

    private static void saveFightHistory(Connection conn, String player, String monster, boolean playerWon) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO FightHistory (timestamp, player, monster, playerWon) VALUES (CURRENT_TIMESTAMP, ?, ?, ?)");
        stmt.setString(1, player);
        stmt.setString(2, monster);
        stmt.setBoolean(3, playerWon);
        stmt.executeUpdate();
    }
}
