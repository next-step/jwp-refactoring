package kitchenpos.table.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    public TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public TableGroup(LocalDateTime createdDate, OrderTables orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        this.validateOrderTablesSize();
        return orderTables.getOrderTables();
    }

    public void addOrderTables(final OrderTable orderTable) {
        this.orderTables.add(orderTable);
    }

    /**
     * 그룹화 하려는 테이블이 없거나, 2개 이상인지 확인합니다.
     */
    public void validateOrderTablesSize() {
        this.orderTables.validateOrderTablesSize();
    }

    /**
     * 저장 된 테이블들이 유효한지(비어있지 않은지) 확인합니다.
     * @param savedOrderTables
     */
    public void comparedSavedOrderTables(List<OrderTable> savedOrderTables) {
        this.orderTables.comparedSavedOrderTables(savedOrderTables);
    }
}
