package kitchenpos.ordertable.domain;

import kitchenpos.exception.CannotCleanTableException;
import kitchenpos.exception.CannotUpdateException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Orders;

import javax.persistence.*;

import static kitchenpos.common.Message.*;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;

    @Embedded
    private Orders orders = new Orders();

    @Column(name = "empty", nullable = false)
    private boolean empty = true;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(0L, null, numberOfGuests, empty);
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this(0L, tableGroup, numberOfGuests, empty);
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this(id, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(boolean isEmpty) {
        if (tableGroup != null) {
            throw new CannotCleanTableException(ERROR_ORDER_TABLE_CANNOT_BE_CLEANED_WHEN_GROUPED);
        }
        this.empty = isEmpty;
    }

    public void updateNumberOfGuestsTo(int number) {
        if (number < 0) {
            throw new IllegalArgumentException(ERROR_TABLE_GUESTS_NUMBER_CANNOT_BE_SMALLER_THAN_ZERO.showText());
        }
        if (empty) {
            throw new IllegalArgumentException(ERROR_TABLE_GUESTS_NUMBER_CANNOT_BE_CHANGED_WHEN_EMPTY.showText());
        }
        this.numberOfGuests = number;
    }

    public void assignToTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public boolean isAssignedToTableGroup() {
        return tableGroup != null;
    }

    public void unGroup() {
        if (orders.isNotCompleted()) {
            throw new CannotUpdateException(ERROR_TABLE_GROUP_CANNOT_BE_UNGROUPED_WHEN_ORDERS_NOT_COMPLETED);
        }
        this.tableGroup = null;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
