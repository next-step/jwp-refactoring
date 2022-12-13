package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    private OrderTable(OrderTableBuilder builder) {
        this.id = builder.id;
        this.tableGroup = builder.tableGroup;
        this.numberOfGuests = builder.numberOfGuests;
        this.empty = builder.empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return Objects.isNull(tableGroup) ? null : tableGroup.getId();
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        if (empty) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty, List<Order> orders) {
        boolean isBeforeComplete = orders.stream().anyMatch(order -> order.getOrderStatus()
                .equals(OrderStatus.COOKING) || order.getOrderStatus().equals(OrderStatus.MEAL));
        if (isBeforeComplete) {
            throw new IllegalArgumentException();
        }
        this.empty = empty;
    }

    public static OrderTableBuilder builder() {
        return new OrderTableBuilder();
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public static class OrderTableBuilder {
        private Long id;
        private TableGroup tableGroup;
        private int numberOfGuests;
        private boolean empty;

        public OrderTableBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderTableBuilder tableGroup(TableGroup tableGroup) {
            this.tableGroup = tableGroup;
            return this;
        }

        public OrderTableBuilder numberOfGuests(int numberOfGuests) {
            this.numberOfGuests = numberOfGuests;
            return this;
        }

        public OrderTableBuilder empty(boolean empty) {
            this.empty = empty;
            return this;
        }

        public OrderTable build() {
            return new OrderTable(this);
        }
    }
}
