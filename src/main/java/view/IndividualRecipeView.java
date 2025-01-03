package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import data_access.NutritionInformationDAO;
import data_access.RecipeIdDAO;
import data_access.UserDAOImpl;
import entity.*;
import interface_adapter.nutrition_information.NutritionInformationController;
import interface_adapter.nutrition_information.NutritionInformationPresenter;
import use_case.nutrition_information.NutritionInformationInteractor;

public class IndividualRecipeView extends JFrame implements ActionListener {
    private final JButton nutritionButton;
    private final JButton bookmarkButton;
    private final JButton urlButton;
    private JList<String> ingredientsJLIst;
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

        // Add this recipe to the user's recently viewed list upon the page's opening
        userDAO.addRecentlyViewedToFile(this.user.getUsername(), this.recipe);

        // Set up JFrame properties
        setTitle(recipe.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize main panel with vertical BoxLayout
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        setContentPane(mainPanel);

        // Initialize buttons
        nutritionButton = new JButton("Nutrition");
        bookmarkButton = new JButton("Bookmark");
        urlButton = new JButton("Open Recipe in Browser");

        // Initialize image.
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

            // Add the label to the main panel
            mainPanel.add(imageLabel, BorderLayout.CENTER);

        } catch (Exception e) {
            // Handle exceptions, e.g., invalid URL or connection error
            final JLabel errorLabel = new JLabel("Failed to load image.");
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            mainPanel.add(errorLabel, BorderLayout.CENTER);
            e.printStackTrace();
        }

        // Initialize ingredient list
        final DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            listModel.addElement(ingredient.getName() + ", Amount: " + ingredient.getAmount() + " " + ingredient.getUnit());
        }
        ingredientsJLIst = new JList<>(listModel);
        final JScrollPane scrollPane = new JScrollPane(ingredientsJLIst);
        scrollPane.setPreferredSize(ingredientsJLIst.getPreferredScrollableViewportSize());
        mainPanel.add(scrollPane);

        // Initialize button panel with horizontal BoxLayout
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(nutritionButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Add spacing
        buttonPanel.add(bookmarkButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(urlButton);
        mainPanel.add(buttonPanel);

        // Add action listeners to buttons
        nutritionButton.addActionListener(this);
        bookmarkButton.addActionListener(this);
        urlButton.addActionListener(this);

        // Initialize dropdown menu for adding the recipe to folders
        String[] options = user.getFolders().keySet().toArray(new String[0]);
        final JComboBox<String> dropdown = new JComboBox<>(options);
        final JLabel dropdownLabel = new JLabel("Add this recipe to a folder");
        dropdown.add(dropdownLabel);
        dropdown.addActionListener(folderEvent -> {
            final String selectedOption = (String) dropdown.getSelectedItem();
            userDAO.addRecipeToFolderInFile(this.user.getUsername(), selectedOption, this.recipe);
            JOptionPane.showMessageDialog(this, "Recipe added to " + selectedOption + "!");
        });
        mainPanel.add(dropdown);

        // Display the frame
        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == nutritionButton) {
            final RecipeIdDAO getRecipeIdDAO = new RecipeIdDAO();
            final int recipeId = getRecipeIdDAO.getRecipeIdByName(recipe.getName());
            final NutritionInformationPresenter presenter = new NutritionInformationPresenter();
            final NutritionInformationDAO dataFetcher = new NutritionInformationDAO();
            final NutritionInformationInteractor interactor = new NutritionInformationInteractor(dataFetcher, presenter);
            final NutritionInformationController controller = new NutritionInformationController(interactor);
            new NutritionInformationView(recipeId,controller);
        }
        if (event.getSource() == bookmarkButton) {
            if (!user.getBookmarks().contains(recipe)) {
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
