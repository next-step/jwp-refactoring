package kitchenpos.order.domain;

import javax.persistence.*;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    protected OrderTable() {

    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public void seatNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public boolean hasTableGroup() {
        return this.tableGroup != null;
    }

    public void cancelTableGroup() {
        this.tableGroup = null;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void allocateTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void enterGuest() {
        this.empty = false;
    }

    public void leaveGuest() {
        this.empty = true;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void updateEmpty(boolean empty) {
        if (empty) {
            this.leaveGuest();
            return;
        }
        this.enterGuest();
    }


}
