package interface_adapter;

import use_case.SearchRecipe.SearchRecipeInputBoundary;
import use_case.SearchRecipe.SearchRecipeInputData;
import entity.Recipe;

import java.util.List;

public class RecipeController {
    private final SearchRecipeInputBoundary searchInteractor;

    public RecipeController(SearchRecipeInputBoundary searchInteractor) {
        this.searchInteractor = searchInteractor;
    }

    // Handles search functionality
    public void searchRecipes(List<String> ingredients) {
        SearchRecipeInputData inputData = new SearchRecipeInputData(ingredients);
        searchInteractor.searchRecipes(inputData);
    }

    // Existing collaborator functionality (e.g., filtering) remains unchanged
    // Example placeholder for collaborator methods:
    public List<String> getAvailableDiets() {
        // Assume this delegates to the appropriate interactor or DAO
        return List.of("Any", "Vegetarian", "Vegan", "Keto");
    }

    public List<String> getAvailableCuisines() {
        // Assume this delegates to the appropriate interactor or DAO
        return List.of("Any", "Italian", "Chinese", "Indian");
    }

    public List<Recipe> filterSearchRecipes(List<String> ingredients, String diet, String cuisine) {
        // Delegate to collaborator filtering logic
        // This might interact with a collaborator's interactor or DAO
        return List.of(); // Replace with actual collaborator logic
    }
}
