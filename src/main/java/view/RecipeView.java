package view;

import entity.Recipe;
import entity.User;
import interface_adapter.RecipeController;
import use_case.SearchRecipe.SearchRecipePresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class RecipeView extends JFrame {
    private JTextField ingredientInput;
    private JButton searchButton;
    private JComboBox<String> dietComboBox;
    private JComboBox<String> cuisineComboBox;
    private JList<Recipe> recipeList; // Holds Recipe objects
    private final DefaultListModel<Recipe> listModel; // DefaultListModel for JList
    private final RecipeController controller;
    private final SearchRecipePresenter presenter;
    private final User user;

    public RecipeView(RecipeController controller, SearchRecipePresenter presenter, User user) {
        this.controller = controller;
        this.presenter = presenter;
        this.user = user;

        setTitle("Recipe Generator");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Input field and search button
        ingredientInput = new JTextField(20);
        searchButton = new JButton("Find Recipes");
        recipeList = new JList<>();
        listModel = new DefaultListModel<>();
        recipeList.setModel(listModel);

        // Create filter components
        dietComboBox = new JComboBox<>();
        cuisineComboBox = new JComboBox<>();
        populateDropdowns();

        searchButton.addActionListener(e -> handleSearch());
        dietComboBox.addActionListener(e -> applyFilters());
        cuisineComboBox.addActionListener(e -> applyFilters());

        // Set up layout
        JPanel inputPanel = createInputPanel();
        JPanel filterPanel = createFilterPanel();

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(recipeList), BorderLayout.CENTER);
        add(filterPanel, BorderLayout.SOUTH);

        // Add mouse listener for recipe clicks
        recipeList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click to open IndividualRecipeView
                    int index = recipeList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        Recipe selectedRecipe = recipeList.getModel().getElementAt(index);
                        openIndividualRecipeView(selectedRecipe);
                    }
                }
            }
        });

        setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter ingredients (comma-separated):"));
        panel.add(ingredientInput);
        panel.add(searchButton);
        return panel;
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Diet:"));
        panel.add(dietComboBox);
        panel.add(new JLabel("Cuisine:"));
        panel.add(cuisineComboBox);
        return panel;
    }

    private void populateDropdowns() {
        // Populate diet dropdown
        dietComboBox.addItem("Any");
        for (String diet : controller.getAvailableDiets()) {
            dietComboBox.addItem(diet);
        }

        // Populate cuisine dropdown
        cuisineComboBox.addItem("Any");
        for (String cuisine : controller.getAvailableCuisines()) {
            cuisineComboBox.addItem(cuisine);
        }
    }

    private void handleSearch() {
        String ingredientsText = ingredientInput.getText();
        List<String> ingredients = List.of(ingredientsText.split(","));

        // Call the controller to handle the search
        controller.searchRecipes(ingredients);

        // Retrieve and display recipes from the presenter
        listModel.clear();
        List<Recipe> recipes = presenter.getRecipes(); // Get Recipe objects
        for (Recipe recipe : recipes) {
            listModel.addElement(recipe); // Add Recipe to the JList
        }
    }

    private void applyFilters() {
        String ingredientsText = ingredientInput.getText();
        List<String> ingredients = List.of(ingredientsText.split(","));
        String selectedDiet = (String) dietComboBox.getSelectedItem();
        String selectedCuisine = (String) cuisineComboBox.getSelectedItem();

        // Call the controller to apply filters
        List<Recipe> filteredRecipes = controller.filterSearchRecipes(ingredients, selectedDiet, selectedCuisine);

        // Update the recipe list
        listModel.clear();
        for (Recipe recipe : filteredRecipes) {
            listModel.addElement(recipe);
        }
    }

    private void openIndividualRecipeView(Recipe recipe) {
        // Open a new window to show the details of the selected recipe
        new IndividualRecipeView(recipe, user);
    }
}
