package no.hiof.fredrivo.budgetapp.classes;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Expenses implements Serializable, Comparable<Expenses> {



    private String id;
    private int sum;
    private String date;
    private Date dateTime;
    private String location;
    private String description;
    private String category;

    public Expenses(int sum, String category) {
        this.sum = sum;
        this.category = category;
    }

   public Expenses() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSum() { return sum; }
    public void setSum(int sum) { this.sum = sum; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public Date getDateTime() { return dateTime; }
    public void setDateTime(Date dateTime) { this.dateTime = dateTime; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }



    // Generer test data brukt for debugging
    public static ArrayList<Expenses> TestData(){

       ArrayList<Expenses> expenseList = new ArrayList<>();

       Expenses data1 = new Expenses();
       Expenses data2 = new Expenses();
       Expenses data3 = new Expenses();
       Expenses data4 = new Expenses();

       data1.setSum(100);
       data1.setDate("11/04/2017");
       data1.setLocation("Halden");
       data1.setDescription("Sulten");
       data1.setCategory("Food");

       data2.setSum(200);
        data2.setDate("04/04/2018");
        data2.setLocation("Halden");
        data2.setDescription("Bleh");
        data2.setCategory("Activity");


        data3.setSum(150);
        data3.setDate("03/11/2017");
        data3.setLocation("Halden");
        data3.setDescription("Mat?");
        data3.setCategory("Food");


        data4.setSum(400);
        data4.setDate("17/08/17");
        data4.setLocation("Halden");
        data4.setDescription("namm");
        data4.setCategory("Transportation");

        expenseList.add(data1);
        expenseList.add(data2);
        expenseList.add(data3);
        expenseList.add(data4);

        return expenseList;
    }

    // Sorterer en liste med expenses basert på katergoriene
    public static ArrayList<Expenses> expensesSortedCategory(ArrayList<Expenses> expenseList){
        ArrayList<Expenses> sortedByCategory = new ArrayList<>();
        ArrayList<String> categoryList = Categories.getUserCategories();

        for (String i : categoryList) {
            int totalSum = 0;
            Expenses categoryEx;

            for (Expenses j : expenseList) {

                if (i.equals(j.getCategory())) {
                    totalSum += j.getSum();
                }
            }
            if(totalSum != 0){
                categoryEx = new Expenses(totalSum, i);
                sortedByCategory.add(categoryEx);
            }
        }
        return sortedByCategory;
    }

    @Override
    public int compareTo(Expenses o) {
        if (getDateTime() == null || o.getDateTime() == null)
            return 0;

        return getDateTime().compareTo(o.getDateTime());
    }
}




