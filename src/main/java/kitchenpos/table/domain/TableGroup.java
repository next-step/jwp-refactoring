package kitchenpos.table.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {
        createdDate = LocalDateTime.now();
    }

    public void addList(List<OrderTable> orderTableList) {
        validateListSize(orderTableList);
        validateOrderTable(orderTableList);

        orderTableList.stream().forEach(orderTable -> orderTable.group(this));

        orderTables.addList(orderTableList);
    }

    private void validateListSize(List<OrderTable> orderTableList) {
        if (orderTableList.isEmpty() || orderTableList.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTable(List<OrderTable> orderTableList) {
        for (final OrderTable orderTable : orderTableList) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException();
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
