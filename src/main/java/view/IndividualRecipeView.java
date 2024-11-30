package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import data_access.GetRecipeId;
import data_access.UserDAOImpl;
import entity.*;

class IndividualRecipeView extends JFrame implements ActionListener {
    private final JButton nutritionButton;
    private final JButton bookmarkButton;
    private final JButton urlButton;
    private JList<String> ingredientsJLIst;
    // if we want to display the used and missed ingredients separately... private JList<String> usedIngredientsJList;
    private final Recipe recipe;
    private URL imageUrl;
    private ImageIcon imageIcon;
    private User user;
    private UserDAOImpl userDAO;

    public IndividualRecipeView(Recipe recipe, User user) {
        this.recipe = recipe;
        this.imageUrl = null;
        this.user = user;
        this.userDAO = new UserDAOImpl();

        // Add this recipe to the user's recently viewed list
        userDAO.addRecentlyViewedToFile(this.user.getUsername(), this.recipe);

        // Initialize ingredient list
        final DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            listModel.addElement(ingredient.getName() + ", Amount: " + ingredient.getAmount() + " " + ingredient.getUnit());
        }
        ingredientsJLIst = new JList<>(listModel);
        final JScrollPane scrollPane = new JScrollPane(ingredientsJLIst);
        scrollPane.setPreferredSize(ingredientsJLIst.getPreferredScrollableViewportSize());

        // Initialize buttons
        nutritionButton = new JButton("Nutrition");
        bookmarkButton = new JButton("Bookmark");
        urlButton = new JButton("Open Recipe in Browser");

        // Set up JFrame properties
        setTitle(recipe.getName());
        setSize(800, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel with vertical BoxLayout - you need a Layout for each Panel
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        setContentPane(mainPanel);

        // initializing image
        try {
            // Specify the image URL
            if (recipe.getImage() == null) {
                throw new IllegalArgumentException("URL string cannot be null");
            }
            this.imageUrl = new URL(recipe.getImage()); // Replace with your image URL

            // Load the image
            this.imageIcon = new ImageIcon(imageUrl);
            int originalWidth = this.imageIcon.getIconWidth();
            int originalHeight = this.imageIcon.getIconHeight();
            int desiredHeight = 200;
            int scaledWidth = (desiredHeight * originalWidth) / originalHeight;

            // Scale the image
            final Image scaledImage = imageIcon.getImage().getScaledInstance(scaledWidth, desiredHeight, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(scaledImage);

            // Add the image to a JLabel
            final JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);

            // Add the label to the frame
            mainPanel.add(imageLabel, BorderLayout.CENTER);

        } catch (Exception e) {
            // Handle exceptions, e.g., invalid URL or connection error
            final JLabel errorLabel = new JLabel("Failed to load image.");
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            mainPanel.add(errorLabel, BorderLayout.CENTER);
            e.printStackTrace();
        }

        // Add components to main panel
        mainPanel.add(scrollPane);

        // Button panel with horizontal BoxLayout
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(nutritionButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Add spacing
        buttonPanel.add(bookmarkButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(urlButton);

        mainPanel.add(buttonPanel);

        // Add action listeners
        nutritionButton.addActionListener(this);
        bookmarkButton.addActionListener(this);
        urlButton.addActionListener(this);

        // Display the frame
        setVisible(true);
    }

    GetRecipeId getRecipeId = new GetRecipeId();

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == nutritionButton) {
            int recipeId = getRecipeId.getRecipeIdByName(recipe.getName());
            new NutritionView(recipeId);
        }
        else if (event.getSource() == bookmarkButton) {
            // TODO complete bookmark function
            if (!user.getBookmarks().contains(recipe)) {
                // user.addBookmark(recipe);
                userDAO.addBookmarkToFile(user.getUsername(), recipe);
                JOptionPane.showMessageDialog(this, "Recipe added to bookmarks!");
            } else {
                JOptionPane.showMessageDialog(this, "Recipe is already bookmarked.");
            }
        }
        else if (event.getSource() == urlButton) {
            try {
                // Create a URI for the URL
                URI uri = new URI(recipe.getUrl());
                // Use the Desktop class to open the default browser
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(uri);
                } else {
                    JOptionPane.showMessageDialog(this, "Desktop browsing is not supported on your system.");
                }
            } catch (URISyntaxException | IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to open the URL: " + ex.getMessage());
            }
        }
    }
}
