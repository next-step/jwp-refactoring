package kitchenpos.domain;

import kitchenpos.common.exceptions.MinimumOrderTableNumberException;
import kitchenpos.common.exceptions.NotEmptyOrderTableGroupException;
import kitchenpos.common.exceptions.NotFoundEntityException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {
    private static final int MINIMUM = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
    }

    private TableGroup(final Long id, final OrderTables orderTables, final LocalDateTime createdDate) {
        this.id = id;
        this.orderTables = orderTables;
        this.createdDate = createdDate;
    }

    public static TableGroup from(final List<OrderTable> orderTableIds) {
        return of(null, orderTableIds);
    }

    public static TableGroup of(final Long id, final List<OrderTable> orderTables) {
        validate(orderTables);
        return new TableGroup(id, OrderTables.from(orderTables), LocalDateTime.now());
    }

    private static void validate(final List<OrderTable> orderTables) {
        if (Objects.isNull(orderTables)) {
            throw new NotFoundEntityException();
        }
        if (orderTables.size() < MINIMUM) {
            throw new MinimumOrderTableNumberException();
        }
        if (hasAnyEmptyTable(orderTables)) {
            throw new NotEmptyOrderTableGroupException();
        }
    }

    private static boolean hasAnyEmptyTable(final List<OrderTable> orderTables) {
        return orderTables.stream().anyMatch(OrderTable::isEmpty);
    }

    public void unGroup() {
        orderTables.unGroup();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
