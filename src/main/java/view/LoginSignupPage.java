package view;

import data_access.UserDAO;
import data_access.UserDAOImpl;
import entity.User;
import data_access.SpoonacularRecipeDAO;
import interface_adapter.RecipeController;
import use_case.SearchRecipe.SearchRecipePresenter;
import use_case.SearchRecipe.SearchRecipeInteractor;

import javax.swing.*;
import java.awt.*;

public class LoginSignupPage extends JFrame {
    private UserDAO userDAO;

    // UI components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;

    public LoginSignupPage() {
        userDAO = new UserDAOImpl(); // Initialize with JSON-backed DAO
        setTitle("Login or Sign Up");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));

        // Username field
        JPanel usernamePanel = new JPanel();
        usernamePanel.add(new JLabel("Username:"));
        usernameField = new JTextField(15);
        usernamePanel.add(usernameField);
        add(usernamePanel);

        // Password field
        JPanel passwordPanel = new JPanel();
        passwordPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(15);
        passwordPanel.add(passwordField);
        add(passwordPanel);

        // Login button
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());

        // Signup button
        signupButton = new JButton("Sign Up");
        signupButton.addActionListener(e -> handleSignup());

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);
        add(buttonPanel);

        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (userDAO.validateUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful!");

            // Navigate to HomePage instead of RecipeView
            openHomePage(userDAO.findUserByUsername(username));
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
        }
    }

    private void openHomePage(User user) {
        new HomePage(user); // Navigate to HomePage
    }

    private void openRecipeView(User user) {
        // Initialize dependencies for RecipeView
        SpoonacularRecipeDAO recipeDAO = new SpoonacularRecipeDAO();
        SearchRecipePresenter presenter = new SearchRecipePresenter();
        SearchRecipeInteractor interactor = new SearchRecipeInteractor(recipeDAO, presenter);
        RecipeController controller = new RecipeController(interactor);

        // Open RecipeView
        new RecipeView(controller, presenter, user);
    }


    private void handleSignup() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Check for empty username or password
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.");
            return;
        }

        if (userDAO.findUserByUsername(username) != null) {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose another.");
        } else {
            boolean success = userDAO.addUser(new User(username, password));
            if (success) {
                JOptionPane.showMessageDialog(this, "Signup successful! You can now log in.");
            } else {
                JOptionPane.showMessageDialog(this, "Signup failed. Please try again.");
            }
        }
    }
}
