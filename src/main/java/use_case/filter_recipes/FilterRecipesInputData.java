package use_case.filter_recipes;

import java.util.List;

/**
 * The input data for the filter recipes use case.
 */
public class FilterRecipesInputData {

    private final List<String> ingredients;
    private final String diet;
    private final String cuisine;

    public FilterRecipesInputData(List<String> ingredients, String diet, String cuisine) {
        this.ingredients = ingredients;
        this.diet = diet;
        this.cuisine = cuisine;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public String getDiet() {
        return diet;
    }

    public String getCuisine() {
        return cuisine;
    }

}

