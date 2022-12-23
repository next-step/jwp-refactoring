package kitchenpos.table.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {

    private static final String INVALID_LIST_SIZE_EXCEPTION = "주문 테이블 목록의 건수가 유효하지 않습니다.";
    private static final String INVALID_ORDER_TABLE_EXCEPTION = "그룹으로 지정할 테이블이 유효하지 않습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {
        createdDate = LocalDateTime.now();
    }

    public TableGroup(List<OrderTable> orderTableList) {
        createdDate = LocalDateTime.now();
        orderTables = new OrderTables(orderTableList);
    }

    public void group(List<OrderTable> orderTableList) {
        validateListSize(orderTableList);
        validateOrderTable(orderTableList);
        orderTableList.stream().forEach(orderTable -> orderTable.group(this));

        orderTables.addList(orderTableList);
    }

    private void validateListSize(List<OrderTable> orderTableList) {
        if (orderTableList.isEmpty() || orderTableList.size() < 2) {
            throw new IllegalArgumentException(INVALID_LIST_SIZE_EXCEPTION);
        }
    }

    private void validateOrderTable(List<OrderTable> orderTableList) {
        for (final OrderTable orderTable : orderTableList) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException(INVALID_ORDER_TABLE_EXCEPTION);
            }
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }

}
