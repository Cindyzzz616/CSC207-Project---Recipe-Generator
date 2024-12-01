package view;

import data_access.SpoonacularRecipeDAO;
import entity.Ingredient;
import entity.Recipe;
import entity.User;
import interface_adapter.RecipeController;
import interface_adapter.SearchRecipePresenter;
import interface_adapter.filter_recipes.FilterRecipesController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import interface_adapter.ShoppingListController;
import use_case.ShoppingListUseCase;
import data_access.SpoonacularAPI;
import use_case.SearchRecipeUseCase;
import use_case.filter_recipes.FilterRecipesInteractor;

public class HomePage extends JFrame {
    private final User user;

    public HomePage(User user) {
        this.user = user;
        setTitle("Welcome to the Recipe Finder");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        final JLabel welcomeLabel = new JLabel("Welcome to the Recipe Finder!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(welcomeLabel, BorderLayout.CENTER);

        final JButton startButton = new JButton("Start Searching Recipes");
        startButton.setFont(new Font("Arial", Font.PLAIN, 16));
        startButton.addActionListener(e -> {
            // Create the presenter
            SearchRecipePresenter presenter = new SearchRecipePresenter();
            // Create the use case interactor
            use_case.SearchRecipeUseCase interactor = new use_case.SearchRecipeUseCase(new SpoonacularRecipeDAO(), presenter);
            // Create the controller
            RecipeController controller = new RecipeController(interactor);
            // Open the RecipeView and pass dependencies
            new RecipeView(controller, presenter, user, new SpoonacularRecipeDAO());
        });

        // Shopping List Button
        final JButton shoppingListButton = new JButton("Shopping List");
        shoppingListButton.setFont(new Font("Arial", Font.PLAIN, 16));

// Action Listener for Shopping List Button
        shoppingListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Launch Shopping List GUI
                SpoonacularAPI api = new SpoonacularAPI();
                ShoppingListUseCase useCase = new ShoppingListUseCase(api);
                ShoppingListController controller = new ShoppingListController(useCase);
                ShoppingListGUI gui = new ShoppingListGUI(controller);
                gui.run();
            }
        });

        final JButton bookmarksButton = new JButton("Bookmarks");
        bookmarksButton.setFont(new Font("Arial", Font.PLAIN, 16));
        bookmarksButton.addActionListener(e -> {
//            // Open BookmarkView in a separate window
//            // User user = new User("Test_username", "Test_password");
//            Ingredient ingredient1 = new Ingredient("Test_ingredient1");
//            Ingredient ingredient2 = new Ingredient("Test_Ingredient2");
//            String imageURL = "https://img.spoonacular.com/recipes/716429-556x370.jpg";
//            Recipe recipe1 = new Recipe("name1", "url1", java.util.List.of(ingredient1, ingredient2), imageURL);
//            Recipe recipe2 = new Recipe("name2", "url2", List.of(ingredient1, ingredient2), imageURL);
//            user.addBookmark(recipe1);
//            user.addBookmark(recipe2);
            new BookmarkView(this.user, null);
        });

        final JButton recentlyViewedButton = new JButton("Recently Viewed");
        recentlyViewedButton.setFont(new Font("Arial", Font.PLAIN, 16));
        recentlyViewedButton.addActionListener(e -> {
            // User user = new User("Test_username", "Test_password");
//            Ingredient ingredient1 = new Ingredient("Test_ingredient1");
//            Ingredient ingredient2 = new Ingredient("Test_Ingredient2");
//            String imageURL = "https://img.spoonacular.com/recipes/716429-556x370.jpg";
//            Recipe recipe1 = new Recipe("name1", "url1", java.util.List.of(ingredient1, ingredient2), imageURL);
//            Recipe recipe2 = new Recipe("name2", "url2", List.of(ingredient1, ingredient2), imageURL);
//            user.addRecentlyViewed(recipe1);
//            user.addRecentlyViewed(recipe2);
            new RecentlyViewedView(this.user, null);
        });

        final JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));
        panel.add(startButton);
        panel.add(bookmarksButton);
        panel.add(recentlyViewedButton);
        panel.add(shoppingListButton);
        add(panel, BorderLayout.SOUTH);
//        add(startButton, BorderLayout.SOUTH);
//        add(bookmarksButton, BorderLayout.SOUTH);
        setVisible(true);
    }
}

