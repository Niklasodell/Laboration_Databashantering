package com.niklas.game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/game", "root", "Botlas");

            DatabaseHandler dbHandler = new DatabaseHandler(conn); // Skapar en instans av DatabaseHandler

            dbHandler.createTables(); // Anropar createTables-metoden för att skapa tabeller

            Player playerName = dbHandler.loadPlayer(); // Laddar spelaren från databasen
            if (playerName == null) {
                playerName = new Player("Player 1", 100, 10);
                dbHandler.savePlayer(playerName); // Sparar spelaren i databasen om den inte finns
            }

            Monster monsterName = new Monster("Dragon", 150, 15);

            int playerHealth = playerName.getHealth();
            int monsterHealth = monsterName.getHealth();

            while (playerHealth > 0 && monsterHealth > 0) {
                playerHealth -= monsterName.getDamage();
                monsterHealth -= playerName.getDamage();
            }

            if (playerHealth <= 0) {
                System.out.println("Du förlorade striden mot " + monsterName.getName());
            } else {
                System.out.println("Du besegrade " + monsterName.getName());
            }

            dbHandler.saveFightHistory(playerName.getName(), monsterName.getName(), playerHealth <= 0); // Sparar stridshistorik i databasen

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



