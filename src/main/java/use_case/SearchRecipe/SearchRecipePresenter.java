package use_case.SearchRecipe;

import entity.Recipe;

import java.util.ArrayList;
import java.util.List;

public class SearchRecipePresenter implements SearchRecipeOutputBoundary {
    private List<Recipe> recipes = new ArrayList<>(); // Default to an empty list

    @Override
    public void presentRecipes(SearchRecipeOutputData outputData) {
        // Store the list of Recipe objects directly
        recipes = outputData.getRecipes();
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
