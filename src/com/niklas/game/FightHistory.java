package com.niklas.game;

public class FightHistory {

    private Connection conn;

    public FightHistory(Connection conn) {
        this.conn = conn;
    }

    public void saveFight(String player, String monster, boolean playerWon) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO FightHistory (timestamp, player, monster, playerWon) VALUES (CURRENT_TIMESTAMP, ?, ?, ?)");
            stmt.setString(1, player);
            stmt.setString(2, monster);
            stmt.setBoolean(3, playerWon);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
