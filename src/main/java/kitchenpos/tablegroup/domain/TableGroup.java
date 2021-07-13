package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables;

    public TableGroup() {
        this.createdDate = LocalDateTime.now();
        this.orderTables = new OrderTables();
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

    public void addOrderTable(OrderTable orderTable) {
        validateOrderTableIsEmptyOrHasTableGroup(orderTable);
        this.orderTables.add(orderTable);
        orderTable.setTableGroup(this);
    }

    private void validateOrderTableIsEmptyOrHasTableGroup(OrderTable orderTable) {
        if (orderTable.hasTableGroup()) {
            throw new IllegalArgumentException("정산 그룹에 포함된 테이블을 새로운 정산그룹에 포함시킬 수 없습니다.");
        }
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있지 않은 테이블은 정산 그룹에 포함시킬 수 없습니다.");
        }
    }

    public void addOrderTables(OrderTables orderTables) {
        for (OrderTable orderTable : orderTables.getOrderTables()) {
            addOrderTable(orderTable);
        }
    }
}
