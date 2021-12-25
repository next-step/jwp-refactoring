package kitchenpos.order.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    
    private int numberOfGuests;
    
    private boolean empty;
    
    protected OrderTable() {
    }
    
    private OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }
    
    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
    
    public Long getTableGroupId() {
        if (tableGroup == null) {
            return null;
        }
        return tableGroup.getId();
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void updateTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
