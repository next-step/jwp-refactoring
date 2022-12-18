package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.message.TableGroupMessage;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {

    }

    public TableGroup(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = new OrderTables(orderTables);
        this.createdDate = LocalDateTime.now();
    }

    private void validateOrderTables(List<OrderTable> orderTableItems) {
        if(orderTableItems == null || orderTableItems.size() < 2) {
            throw new IllegalArgumentException(TableGroupMessage.CREATE_ERROR_MORE_THAN_TWO_ORDER_TABLES.message());
        }

        if(isEnrolledOtherTableGroup(orderTableItems)) {
            throw new IllegalArgumentException(TableGroupMessage.CREATE_ERROR_OTHER_TABLE_GROUP_MUST_BE_NOT_ENROLLED.message());
        }
    }

    private boolean isEnrolledOtherTableGroup(List<OrderTable> orderTableItems) {
        return orderTableItems.stream()
                .filter(OrderTable::isEnrolledGroup)
                .anyMatch(table -> !table.isGroupBy(this));
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

    public void group() {
        this.orderTables.group(this);
    }

    public void unGroup() {
        this.orderTables.unGroup();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TableGroup that = (TableGroup) o;

        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (orderTables != null ? orderTables.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TableGroup{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                '}';
    }
}
