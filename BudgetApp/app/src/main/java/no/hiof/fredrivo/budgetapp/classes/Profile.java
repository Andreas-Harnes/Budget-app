package no.hiof.fredrivo.budgetapp.classes;

public class Profile {
    private String uID;
    private int incomePerMonth;
    private int savePerMonth;
    private int expensesPerMonth;
    private String categoryToSave;


    public Profile(String uID, int incomePerMonth, int savePerMonth, int expensesPerMonth, String category){
        this.uID = uID;
        this.incomePerMonth = incomePerMonth;
        this.savePerMonth = savePerMonth;
        this.expensesPerMonth = expensesPerMonth;
        this.categoryToSave = category;
    }

    // Funksjoner for profile objekter

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public int getIncomePerMonth() {
        return incomePerMonth;
    }

    public void setIncomePerMonth(int incomePerMonth) {
        this.incomePerMonth = incomePerMonth;
    }

    public int getSavePerMonth() {
        return savePerMonth;
    }

    public void setSavePerMonth(int savePerMonth) {
        this.savePerMonth = savePerMonth;
    }

    public int getExpensesPerMonth() {
        return expensesPerMonth;
    }

    public void setExpensesPerMonth(int expensesPerMonth) {
        this.expensesPerMonth = expensesPerMonth;
    }

    public String getCategoryToSave() {
        return categoryToSave;
    }

    public void setCategoryToSave(String categoryToSave) {
        this.categoryToSave = categoryToSave;
    }
}
