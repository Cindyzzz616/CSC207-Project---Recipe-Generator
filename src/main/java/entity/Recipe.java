package entity;

import java.util.List;

/**
 * This class returns a recipe from the API.
 */
public class Recipe {
    private String name;
    private String url;
    private List<String> ingredients;

    public Recipe(String name, String url, List<String> ingredients) {
        this.name = name;
        this.url = url;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getIngredients() {
        return ingredients;
    }
}
