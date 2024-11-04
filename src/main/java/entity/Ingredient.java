package entity;

/**
 * Represents an ingredient.
 */
public class Ingredient {
    private String name;
    private String quantity;
    private String unit;
    private boolean isAvailable;

    public Ingredient(String name, String quantity, String unit, boolean isAvailable) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.isAvailable = isAvailable;
    }

    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of quantity and unit of ingredient.
     */
    @Override
    public String toString() {
        return quantity + " " + unit + " of " + name;
    }
}
