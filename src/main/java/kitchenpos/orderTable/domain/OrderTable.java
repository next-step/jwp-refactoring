package kitchenpos.orderTable.domain;

import kitchenpos.exception.ErrorMessage;
import kitchenpos.exception.IllegalOrderException;
import kitchenpos.exception.IllegalOrderTableException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.tableGroup.domain.TableGroup;

import javax.persistence.*;
import java.util.List;

@Entity
public class OrderTable {
    public static final int MIN_NUMBER_OF_GUESTS = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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

    public void changeEmpty(boolean empty, List<Order> orders) {
        validateOrdersToChangeEmpty(orders);
        if (isGrouped()) {
            throw new IllegalOrderTableException(ErrorMessage.ERROR_ORDER_TABLE_GROUPED);
        }

        this.empty = empty;
    }

    private void validateOrdersToChangeEmpty(List<Order> orders) {
        if (orders.stream()
                .anyMatch(order -> order.isCooking() || order.isEating())) {
            throw new IllegalOrderException(
                    String.format(ErrorMessage.ERROR_ORDER_INVALID_STATUS, OrderStatus.COOKING + " " + OrderStatus.MEAL)
            );
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalOrderTableException(
                    String.format(ErrorMessage.ERROR_ORDER_TABLE_GUESTS_TOO_SMALL, MIN_NUMBER_OF_GUESTS)
            );
        }
        if (isEmpty()) {
            throw new IllegalOrderTableException("주문테이블은 비어있을 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void detachTableGroup() {
        this.tableGroup = null;
    }

    public void assignTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }
}
