package kitchenposNew.table.domain;

import kitchenposNew.order.exception.NotChangeToEmptyThatGroupTable;
import kitchenposNew.table.exception.NotChangeNumberOfGuestThatEmptyTable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class OrderTable {
    private static final Long NON_TABLE_GROUP = null;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, int numberOfGuests) {
        this.id = id;
        this.tableGroupId = NON_TABLE_GROUP;
        this.numberOfGuests = numberOfGuests;
        this.empty = false;
    }

    public OrderTable(int numberOfGuests) {
        this.tableGroupId = NON_TABLE_GROUP;
        this.numberOfGuests = numberOfGuests;
        this.empty = false;
    }

    public Long getId() {
        return id;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void changeToEmpty() {
        if(tableGroupId != NON_TABLE_GROUP){
            throw new NotChangeToEmptyThatGroupTable();
        }
        this.empty = true;
    }

    public void changeTableGroupId(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        if (empty) {
            throw new NotChangeNumberOfGuestThatEmptyTable();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroupId, that.tableGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }
}
