package no.hiof.fredrivo.budgetapp.classes;

import java.util.ArrayList;
import java.util.Arrays;

public class Categories {
    private String category;
    private static ArrayList<String> userCategories = new ArrayList<>(
            Arrays.asList("Food", "Fixed expenses", "Transportation", "Activity"));

    public Categories(String category) {
        this.category = category;
        userCategories.add(category);
    }

    public static ArrayList<String> getUserCategories() { return userCategories; }
}
