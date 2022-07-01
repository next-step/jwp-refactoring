package kitchenpos.table.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.table.exception.CannotChangeEmptyState;
import kitchenpos.table.exception.CannotChangeNumberOfGuests;
import kitchenpos.table.exception.CannotMakeTableGroupException;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void includeTo(TableGroup tableGroup) {
        if (!this.empty) {
            throw new CannotMakeTableGroupException("table " + id + " is not empty table");
        }
        if (this.tableGroup != null) {
            throw new CannotMakeTableGroupException(
                    "table " + id + " is already include another table group " + tableGroup.getId());
        }
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(boolean empty){
        if(this.tableGroup != null){
            throw new CannotChangeEmptyState("table " + id + " is included tableGroup");
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests){
        if (numberOfGuests < 0) {
            throw new CannotChangeNumberOfGuests("numberOfGuests can not be negative");
        }
        if (empty) {
            throw new CannotChangeNumberOfGuests("table " + id + " is empty");
        }
        this.numberOfGuests = numberOfGuests;
    }
}
