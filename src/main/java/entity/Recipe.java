package entity;

import java.util.List;

/**
 * The Recipe class. This class will represent a recipe returned from our API.
 */
public class Recipe {
    private String recipeTitle;
    private List<Ingredient> ingredients;
    private int prepTime;
    private int cookTime;
    private String cuisineType;
    // cuisine could be a class?
    private Nutrition nutritionalInfo;

}
