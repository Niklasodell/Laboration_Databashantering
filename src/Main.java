import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:game.db");

            createTables(conn);

            Player player = loadPlayer(conn);
            if (player == null) {
                player = new Player("Player 1", 100, 10);
                savePlayer(conn, player);
            }

            Monster monster = new Monster("Dragon", 150, 15);

            int playerHealth = player.getHealth();
            int monsterHealth = monster.getHealth();

            while (playerHealth > 0 && monsterHealth > 0) {
                playerHealth -= monster.getDamage();
                monsterHealth -= player.getDamage();
            }

            if (playerHealth <= 0) {
                System.out.println("Du förlorade striden mot " + monster.getName());
            } else {
                System.out.println("Du besegrade " + monster.getName());
            }

            saveFightHistory(conn, player.getName(), monster.getName(), playerHealth <= 0);

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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



