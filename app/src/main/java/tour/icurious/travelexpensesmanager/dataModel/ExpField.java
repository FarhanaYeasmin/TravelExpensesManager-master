package tour.icurious.travelexpensesmanager.dataModel;

/**
 * Created by Towhid on 10/30/2016.
 */

public class ExpField {

    private int id;
    private String title;
    private double amount;

    public ExpField() {
    }

    public ExpField(String title, double amount) {
        this.title = title;
        this.amount = amount;
    }

    public ExpField(int id, String title, double amount) {
        this.id = id;
        this.title = title;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
