package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;

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
        validateGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty, List<Order> orders) {
        validateTableGroup();
        orders.forEach(Order::validateComplete);
        this.empty = empty;
    }

    private void validateTableGroup() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException();
        }
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public static OrderTableBuilder builder() {
        return new OrderTableBuilder();
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
