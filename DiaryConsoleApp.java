import java.sql.*;
import java.util.*;
import java.io.*;

public class DiaryConsoleApp {

    static final String DB_URL = "jdbc:mysql://localhost:3306/diarydb";
    static final String DB_USER = "root"; // change if needed
    static final String DB_PASS = "your_password"; // change this

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n📓 My Diary App");
            System.out.println("1. Add Entry");
            System.out.println("2. View Entries");
            System.out.println("3. Edit Entry");
            System.out.println("4. Download Entry");
            System.out.println("5. Exit");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1: addEntry(scanner); break;
                case 2: viewEntries(); break;
                case 3: editEntry(scanner); break;
                case 4: downloadEntry(scanner); break;
                case 5: System.exit(0);
                default: System.out.println("Invalid option!");
            }
        }
    }

    static void addEntry(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            System.out.print("Enter date (YYYY-MM-DD): ");
            String date = scanner.nextLine();
            System.out.println("Write your entry:");
            String text = scanner.nextLine();

            PreparedStatement ps = conn.prepareStatement("INSERT INTO diary_entries VALUES (?, ?)");
            ps.setString(1, date);
            ps.setString(2, text);
            ps.executeUpdate();
            System.out.println("✅ Entry saved!");

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("⚠️ Entry for this date already exists.");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    static void viewEntries() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM diary_entries ORDER BY entry_date DESC");

            while (rs.next()) {
                System.out.println("\n📅 " + rs.getString("entry_date"));
                System.out.println(rs.getString("entry_text"));
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    static void editEntry(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            System.out.print("Enter date to edit (YYYY-MM-DD): ");
            String date = scanner.nextLine();

            System.out.println("Enter new text:");
            String newText = scanner.nextLine();

            PreparedStatement ps = conn.prepareStatement("UPDATE diary_entries SET entry_text = ? WHERE entry_date = ?");
            ps.setString(1, newText);
            ps.setString(2, date);
            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("✏️ Entry updated.");
            else
                System.out.println("⚠️ No entry found for that date.");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    static void downloadEntry(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            System.out.print("Enter date to download (YYYY-MM-DD): ");
            String date = scanner.nextLine();

            PreparedStatement ps = conn.prepareStatement("SELECT entry_text FROM diary_entries WHERE entry_date = ?");
            ps.setString(1, date);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String text = rs.getString("entry_text");
                FileWriter fw = new FileWriter("Diary_" + date + ".txt");
                fw.write("Date: " + date + "\n\n" + text);
                fw.close();
                System.out.println("✅ Saved to Diary_" + date + ".txt");
            } else {
                System.out.println("⚠️ Entry not found.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}
