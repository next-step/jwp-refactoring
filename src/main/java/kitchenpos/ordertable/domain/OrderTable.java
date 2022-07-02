package kitchenpos.ordertable.domain;

import kitchenpos.exception.ErrorMessage;
import kitchenpos.exception.IllegalOrderException;
import kitchenpos.exception.IllegalOrderTableException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.*;
import java.util.List;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public static final int MIN_NUMBER_OF_GUESTS = 0;

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

    public void changeEmpty(boolean empty, List<Order> orders) {
        validateOrderStatusToChangeEmpty(orders);
        validateOrderTableNotGrouped();
        this.empty = empty;
    }

    private void validateOrderStatusToChangeEmpty(List<Order> orders) {
        if (orders.stream()
                .anyMatch(order -> order.isCooking() || order.isEating())) {
            throw new IllegalOrderException(
                    String.format(ErrorMessage.ERROR_ORDER_INVALID_STATUS, OrderStatus.COOKING + " " + OrderStatus.MEAL)
            );
        }
    }

    private void validateOrderTableNotGrouped() {
        if (isGrouped()) {
            throw new IllegalOrderTableException(ErrorMessage.ERROR_ORDER_TABLE_GROUPED);
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        validateOrderTableNotEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalOrderTableException(
                    String.format(ErrorMessage.ERROR_ORDER_TABLE_GUESTS_TOO_SMALL, MIN_NUMBER_OF_GUESTS)
            );
        }
    }

    private void validateOrderTableNotEmpty() {
        if (isEmpty()) {
            throw new IllegalOrderTableException(ErrorMessage.ERROR_ORDER_TABLE_EMPTY);
        }
    }

    public void assignTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void removeTableGroup() {
        this.tableGroup = null;
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

    public boolean isGrouped() {
        return tableGroup != null;
    }
}
