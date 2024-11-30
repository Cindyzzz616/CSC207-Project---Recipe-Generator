package use_case.SearchRecipe;

import data_access.RecipeDAO;
import entity.Recipe;

import java.util.List;

public class SearchRecipeInteractor implements SearchRecipeInputBoundary {
    private final RecipeDAO recipeDAO;
    private final SearchRecipeOutputBoundary outputBoundary;

    public SearchRecipeInteractor(RecipeDAO recipeDAO, SearchRecipeOutputBoundary outputBoundary) {
        this.recipeDAO = recipeDAO;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void searchRecipes(SearchRecipeInputData inputData) {
        // Fetch recipes from DAO
        List<Recipe> recipes = recipeDAO.getRecipesByIngredients(inputData.getIngredients());

        // Wrap recipes in output data
        SearchRecipeOutputData outputData = new SearchRecipeOutputData(recipes);

        // Pass output data to the presenter via output boundary
        outputBoundary.presentRecipes(outputData);
    }
}
