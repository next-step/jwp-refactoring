package kitchenpos.domain.table;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.domain.order.Orders;
import kitchenpos.exception.order.HasNotCompletionOrderException;
import kitchenpos.exception.table.EmptyOrderTableException;
import kitchenpos.exception.table.HasOtherTableGroupException;
import kitchenpos.exception.table.NegativeOfNumberOfGuestsException;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="table_group_id")
    private TableGroup tableGroup;
    private int numberOfGuests;

    boolean empty;
 
    protected OrderTable() {
    }

    private OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(TableGroup tableGroup, int numberOfGuests) {
        if (numberOfGuests > 0) {
            return new OrderTable(tableGroup, numberOfGuests, false);
        }

        return new OrderTable(tableGroup, numberOfGuests, true);
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public Long getId() {
        return this.id;
    }

    public TableGroup getTableGroup() {
        return this.tableGroup;
    }
    public int getNumberOfGuests() {
        return this.numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void unGroupTable(final Orders order) {
        checkOrderStatusOfOrderTable(order);

        this.tableGroup.getOrderTables().remove(this);
        this.tableGroup = null;
    }

    private void checkOrderStatusOfOrderTable(final Orders order) {
        if (order != null && !order.isCompletion()) {
            throw new HasNotCompletionOrderException();
        }
    }

    public void groupingTable(TableGroup tableGroup) {
        if (this.tableGroup != null) {
            this.tableGroup.getOrderTables().remove(this);
        }

        this.tableGroup = tableGroup;
        this.tableGroup.getOrderTables().add(this);
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        checkEmptyTable();
        checkPositiveOfNumberOfGuests(numberOfGuests);

        this.numberOfGuests = numberOfGuests;
    }

    public boolean getEmpty() {
        return this.empty;
    }

    public void changeEmpty(boolean empty, Orders order) {
        checkHasTableGroup();
        checkOrderStatusOfOrderTable(order);

        this.empty = empty;
    }

    private void checkHasTableGroup() {
        if (this.tableGroup != null) {
            throw new HasOtherTableGroupException();
        }
    }

    public boolean hasTableGroup() {
        return tableGroup != null;
    }

    private void checkEmptyTable() {
        if (this.empty) {
            throw new EmptyOrderTableException();
        }
    }

    private void checkPositiveOfNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new NegativeOfNumberOfGuestsException();
        }
    }
}
