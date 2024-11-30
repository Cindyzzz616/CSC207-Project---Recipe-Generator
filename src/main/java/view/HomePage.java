package view;

import data_access.SpoonacularRecipeDAO;
import entity.User;
import interface_adapter.RecipeController;
import use_case.SearchRecipe.SearchRecipePresenter;
import interface_adapter.ShoppingListController;
import use_case.SearchRecipe.SearchRecipeInteractor;
import use_case.ShoppingListUseCase;
import data_access.SpoonacularAPI;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {
    private final User user;

    public HomePage(User user) {
        this.user = user;
        setTitle("Welcome to the Recipe Finder");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Welcome Label
        final JLabel welcomeLabel = new JLabel("Welcome to the Recipe Finder!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(welcomeLabel, BorderLayout.CENTER);

        // Start Button for Recipe Search
        final JButton startButton = new JButton("Start Searching Recipes");
        startButton.setFont(new Font("Arial", Font.PLAIN, 16));
        startButton.addActionListener(e -> {
            // Initialize dependencies for RecipeView
            SpoonacularRecipeDAO recipeDAO = new SpoonacularRecipeDAO();
            SearchRecipePresenter presenter = new SearchRecipePresenter();
            SearchRecipeInteractor interactor = new SearchRecipeInteractor(recipeDAO, presenter);
            RecipeController controller = new RecipeController(interactor);

            // Open RecipeView
            new RecipeView(controller, presenter, user);

            // Uncomment to close HomePage after starting RecipeView
            // dispose();
        });

        // Shopping List Button
        final JButton shoppingListButton = new JButton("Shopping List");
        shoppingListButton.setFont(new Font("Arial", Font.PLAIN, 16));
        shoppingListButton.addActionListener(e -> {
            // Launch Shopping List GUI
            SpoonacularAPI api = new SpoonacularAPI();
            ShoppingListUseCase useCase = new ShoppingListUseCase(api);
            ShoppingListController controller = new ShoppingListController(useCase);
            ShoppingListGUI gui = new ShoppingListGUI(controller);
            gui.run();
        });

        // Bookmarks Button
        final JButton bookmarksButton = new JButton("Bookmarks");
        bookmarksButton.setFont(new Font("Arial", Font.PLAIN, 16));
        bookmarksButton.addActionListener(e -> {
            // Open BookmarkView in a separate window
            new BookmarkView(this.user);
        });

        // Recently Viewed Button
        final JButton recentlyViewedButton = new JButton("Recently Viewed");
        recentlyViewedButton.setFont(new Font("Arial", Font.PLAIN, 16));
        recentlyViewedButton.addActionListener(e -> {
            // Open RecentlyViewedView in a separate window
            new RecentlyViewedView(this.user);
        });

        // Panel for buttons
        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));
        panel.add(startButton);
        panel.add(bookmarksButton);
        panel.add(recentlyViewedButton);
        panel.add(shoppingListButton);

        add(panel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
