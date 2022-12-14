package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    private TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {

        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup of(LocalDateTime createdDate, List<OrderTable> orderTables) {
        validTableListSize(orderTables);
        validEmptyTable(orderTables);
        return new TableGroup(createdDate, orderTables);
    }

    private static void validTableListSize(List<OrderTable> orderTables) {
        if (orderTables.isEmpty() || orderTables.size() < 2) {
            throw new IllegalArgumentException("한 개 이상의 테이블이 있어야 합니다");
        }
    }

    private static void validEmptyTable(List<OrderTable> orderTables) {
        if (notEmpty(orderTables)) {
            throw new IllegalArgumentException("단체 지정 테이블에 비어 있지 않은 테이블이 포함 되어 있습니다");
        }
    }

    private static boolean notEmpty(List<OrderTable> orderTables) {
        return orderTables.stream().anyMatch(it -> !it.isEmpty());
    }

    public TableGroup() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableGroup that = (TableGroup) o;
        return Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTables);
    }
}
