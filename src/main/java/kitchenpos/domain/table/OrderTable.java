package kitchenpos.domain.table;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.vo.TableGroupId;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private TableGroupId tableGroupId;
    private int numberOfGuests;

    boolean empty;
 
    protected OrderTable() {
    }

    private OrderTable(TableGroupId tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(TableGroupId tableGroupId, int numberOfGuests) {
        if (numberOfGuests > 0) {
            return new OrderTable(tableGroupId, numberOfGuests, false);
        }

        return new OrderTable(tableGroupId, numberOfGuests, true);
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public Long getId() {
        return this.id;
    }

    public TableGroupId getTableGroupId() {
        return this.tableGroupId;
    }
    public int getNumberOfGuests() {
        return this.numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void unGroupTable() {
        this.tableGroupId = null;
    }

    public void groupingTable(TableGroupId tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean getEmpty() {
        return this.empty;
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean hasTableGroup() {
        return tableGroupId != null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OrderTable)) {
            return false;
        }
        OrderTable orderTable = (OrderTable) o;
        return Objects.equals(id, orderTable.id) && Objects.equals(tableGroupId, orderTable.tableGroupId) && numberOfGuests == orderTable.numberOfGuests && empty == orderTable.empty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }
}
