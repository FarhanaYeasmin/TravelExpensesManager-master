package tour.icurious.travelexpensesmanager.dataModel;



public class Member {
    private int id;
    private String name;
    private int tour_id;
    private double totalExpenses;
    private double deposit;

    public Member() {
    }

    public Member(String name) {
        this.name = name;
    }

    public Member(String name, int tour_id) {
        this.name = name;
        this.tour_id = tour_id;
    }

    public Member(String name, int tour_id, double totalExpenses, double deposit) {
        this.name = name;
        this.tour_id = tour_id;
        this.totalExpenses = totalExpenses;
        this.deposit = deposit;
    }

    public Member(int id, String name, int tour_id, double totalExpenses, double deposit) {
        this.id = id;
        this.name = name;
        this.tour_id = tour_id;
        this.totalExpenses = totalExpenses;
        this.deposit = deposit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTour_id() {
        return tour_id;
    }

    public void setTour_id(int tour_id) {
        this.tour_id = tour_id;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }
}
