package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.exception.IllegalOrderTableException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    public static final String ERROR_ORDER_TABLE_NOT_EMPTY = "주문테이블은 비어있어야 합니다.";
    public static final String ERROR_ORDER_TABLE_DUPLICATED = "주문테이블은 중복될 수 없습니다.";
    public static final String ERROR_ORDER_TABLE_GROUPED = "주문테이블은 그룹에 지정되어있을 수 없습니다.";
    public static final String ERROR_ORDER_TABLE_TOO_SMALL = "주문테이블의 개수는 %d 미만일 수 없습니다.";
    public static final int MIN_ORDER_TABLE_NUMBER = 2;

    protected TableGroup() {
    }

    private TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
        assignOrderTables(orderTables);
    }

    private TableGroup(List<OrderTable> orderTables) {
        assignOrderTables(orderTables);
    }

    public static TableGroup of(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }

    public static TableGroup of(Long id, List<OrderTable> orderTables) {
        return new TableGroup(id, orderTables);
    }

    private void assignOrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
        orderTables.forEach(orderTable -> orderTable.assignTableGroup(this));
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        validateOrderTablesDuplicated(orderTables);
        validateOrderTablesNotGrouped(orderTables);
        validateOrderTablesEmpty(orderTables);
    }

    private void validateOrderTablesSize(List<OrderTable> orderTables) {
        if (orderTables == null || orderTables.size() < MIN_ORDER_TABLE_NUMBER) {
            throw new IllegalOrderTableException(
                    String.format(ERROR_ORDER_TABLE_TOO_SMALL, MIN_ORDER_TABLE_NUMBER)
            );
        }
    }

    private void validateOrderTablesDuplicated(List<OrderTable> orderTables) {
        if(orderTables.size() != orderTables.stream().distinct().count()){
            throw new IllegalOrderTableException(ERROR_ORDER_TABLE_DUPLICATED);
        }
    }

    private void validateOrderTablesNotGrouped(List<OrderTable> orderTables) {
        if (orderTables.stream().
                anyMatch(orderTable -> orderTable.isGrouped())) {
            throw new IllegalOrderTableException(ERROR_ORDER_TABLE_GROUPED);
        }
    }

    private void validateOrderTablesEmpty(List<OrderTable> orderTables) {
        if (orderTables.stream().
                anyMatch(orderTable -> !orderTable.isEmpty())) {
            throw new IllegalOrderTableException(ERROR_ORDER_TABLE_NOT_EMPTY);
        }
    }

    public void ungroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.removeTableGroup();
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
