package tour.icurious.travelexpensesmanager.dataModel;



public class Expense {

    private int id;
    private int tour_id;
    private int member_id;
    private int exp_field_id;

    public Expense() {
    }

    public Expense(int tour_id, int member_id, int exp_field_id) {
        this.tour_id = tour_id;
        this.member_id = member_id;
        this.exp_field_id = exp_field_id;
    }

    public Expense(int id, int tour_id, int member_id, int exp_field_id) {
        this.id = id;
        this.tour_id = tour_id;
        this.member_id = member_id;
        this.exp_field_id = exp_field_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTour_id() {
        return tour_id;
    }

    public void setTour_id(int tour_id) {
        this.tour_id = tour_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public int getExp_field_id() {
        return exp_field_id;
    }

    public void setExp_field_id(int exp_field_id) {
        this.exp_field_id = exp_field_id;
    }
}
