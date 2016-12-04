package tour.icurious.travelexpensesmanager.dataModel;

/**
 * Created by Towhid on 11/2/2016.
 */

public class ExpRowItem {
    private Member members;
    private ExpField expFields;

    public ExpRowItem(Member members, ExpField expFields) {
        this.members = members;
        this.expFields = expFields;
    }

    public Member getMembers() {
        return members;
    }

    public void setMembers(Member members) {
        this.members = members;
    }

    public ExpField getExpFields() {
        return expFields;
    }

    public void setExpFields(ExpField expFields) {
        this.expFields = expFields;
    }
}
