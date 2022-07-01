package kitchenpos.table.domain;

import static kitchenpos.table.exception.CannotChangeEmptyState.INCLUDE_TO_TABLE_GROUP;
import static kitchenpos.table.exception.CannotChangeNumberOfGuests.NEGATIVE_NUMBER_OF_GUESTS;
import static kitchenpos.table.exception.CannotChangeNumberOfGuests.TABLE_EMPTY;
import static kitchenpos.table.exception.CannotMakeTableGroupException.INCLUDE_ANOTHER_GROUP;
import static kitchenpos.table.exception.CannotMakeTableGroupException.TABLE_NOT_EMPTY;

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
            throw new CannotMakeTableGroupException(TABLE_NOT_EMPTY);
        }
        if (this.tableGroup != null) {
            throw new CannotMakeTableGroupException(INCLUDE_ANOTHER_GROUP);
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
            throw new CannotChangeEmptyState(INCLUDE_TO_TABLE_GROUP);
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests){
        if (numberOfGuests < 0) {
            throw new CannotChangeNumberOfGuests(NEGATIVE_NUMBER_OF_GUESTS);
        }
        if (empty) {
            throw new CannotChangeNumberOfGuests(TABLE_EMPTY);
        }
        this.numberOfGuests = numberOfGuests;
    }
}
