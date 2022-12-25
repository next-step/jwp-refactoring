package kitchenpos.table.domain;

import kitchenpos.common.ErrorMessage;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Embedded
    private OrderGuests numberOfGuests = new OrderGuests();

    @Embedded
    private OrderEmpty empty = new OrderEmpty();

    protected OrderTable() {}

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new OrderGuests(numberOfGuests);
        this.empty = new OrderEmpty(empty);
    }

    public void changeEmpty(boolean empty) {
        this.empty = this.empty.changeEmpty(empty);
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = this.numberOfGuests.changeNumberOfGuests(numberOfGuests, empty.isEmpty());
    }

    public void updateTableGroup(TableGroup tableGroup) {
        if(this.tableGroup != tableGroup) {
            this.tableGroup = tableGroup;
            tableGroup.addOrderTable(this);
        }
    }

    public void group() {
        checkForTableGrouping();
        changeEmpty(false);
    }

    private void checkForTableGrouping() {
        if (hasTableGroup()) {
            throw new IllegalArgumentException(ErrorMessage.ORDER_TABLE_ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getMessage());
        }
        if (!isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.ORDER_TABLE_NON_EMPTY_ORDER_TABLE_CANNOT_BE_INCLUDED_IN_TABLE_GROUP.getMessage());
        }
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroup);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public OrderEmpty getEmpty() {
        return empty;
    }
}
