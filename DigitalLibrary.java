import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*; 

public class DigitalLibrary extends JFrame {
    static ArrayList<Book> books = new ArrayList<>();
    static ArrayList<String> users = new ArrayList<>();
    static String currentUser = "";

    public static void main(String[] args) {
        books.add(new Book("B101", "Java Basics", true));
        books.add(new Book("B102", "Python Essentials", true));
        users.add("user1");
        users.add("user2");

        new DigitalLibrary().showLogin();
    }

    void showLogin() {
        JFrame frame = new JFrame("Digital Library - Login");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Welcome to Digital Library");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(loginBtn);

        frame.add(panel, BorderLayout.CENTER);

        loginBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();

            if (user.equalsIgnoreCase("admin") && pass.equals("admin123")) {
                frame.dispose();
                showAdminPanel();
            } else if (users.contains(user) && pass.equals("1234")) {
                currentUser = user;
                frame.dispose();
                showUserPanel();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Credentials. Try again.");
            }
        });

        frame.setVisible(true);
    }

    void showAdminPanel() {
        JFrame frame = new JFrame("Admin Panel - Digital Library");
        frame.setSize(500, 450);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea display = new JTextArea(10, 40);
        display.setEditable(false);

        JButton addBookBtn = new JButton("Add Book");
        JButton removeBookBtn = new JButton("Remove Book");
        JButton viewBooksBtn = new JButton("View Books");

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(addBookBtn);
        panel.add(removeBookBtn);
        panel.add(viewBooksBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(new JScrollPane(display));

        frame.add(panel);
        
        addBookBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Book ID:");
            if (id == null || id.trim().isEmpty()) {
                display.setText("Book ID cannot be empty.");
                return;
            }

            boolean exists = false;
            for (Book b : books) {
                if (b.id.equalsIgnoreCase(id)) {
                    exists = true;
                    break;
                }
            }

            if (exists) {
                display.setText("Book ID already exists! Use a unique ID.");
            } else {
                String title = JOptionPane.showInputDialog("Enter Book Title:");
                if (title != null && !title.trim().isEmpty()) {
                    books.add(new Book(id, title, true));
                    display.setText("Book added successfully!");
                } else {
                    display.setText("Book title cannot be empty.");
                }
            }
        });

        removeBookBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Book ID to Remove:");
            books.removeIf(book -> book.id.equalsIgnoreCase(id));
            display.setText("Book removed if found.");
        });

        viewBooksBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("Books:\n");
            for (Book b : books)
                sb.append(b).append("\n");
            display.setText(sb.toString());
        });

        frame.setVisible(true);
    }

    void showUserPanel() {
        JFrame frame = new JFrame("User Panel - Welcome " + currentUser);
        frame.setSize(500, 450);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("User Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea display = new JTextArea(10, 40);
        display.setEditable(false);

        JButton viewBtn = new JButton("View Available Books");
        JButton issueBtn = new JButton("Issue Book");
        JButton returnBtn = new JButton("Return Book");

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(viewBtn);
        panel.add(issueBtn);
        panel.add(returnBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(new JScrollPane(display));

        frame.add(panel);

        viewBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("Available Books:\n");
            for (Book b : books)
                if (b.available)
                    sb.append(b).append("\n");
            display.setText(sb.toString());
        });

        issueBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Book ID to Issue:");
            for (Book b : books) {
                if (b.id.equalsIgnoreCase(id) && b.available) {
                    b.available = false;
                    display.setText("Book Issued Successfully!");
                    return;
                }
            }
            display.setText("Book not available!");
        });

        returnBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Book ID to Return:");
            for (Book b : books) {
                if (b.id.equalsIgnoreCase(id) && !b.available) {
                    b.available = true;
                    display.setText("Book Returned Successfully!");
                    return;
                }
            }
            display.setText("Book not found or already returned!");
        });

        frame.setVisible(true);
    }
}

class Book {
    String id;
    String title;
    boolean available;

    Book(String id, String title, boolean available) {
        this.id = id;
        this.title = title;
        this.available = available;
    }

    public String toString() {
        return "[" + id + "] " + title + " - " + (available ? "Available" : "Issued");
    }
}
