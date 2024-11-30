package use_case.SearchRecipe;

import data_access.RecipeDAO;
import entity.Recipe;

import java.util.List;

public class SearchRecipeUseCase {
    private final RecipeDAO recipeDAO;

    public SearchRecipeUseCase(RecipeDAO recipeDAO) {
        this.recipeDAO = recipeDAO;
    }

    public List<Recipe> searchRecipes(List<String> ingredients) {
        return recipeDAO.getRecipesByIngredients(ingredients);
    }
}
