package kitchenpos.ordertable.domain;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.common.Message.ERROR_ORDERTABLES_SHOULD_HAVE_AT_LEAST_TWO_TABLES;
import static kitchenpos.common.Message.ERROR_TABLES_CANNOT_BE_GROUPED;

@Entity
public class TableGroup {

    private static final int MINIMUM_REQUIRED_NUMBER_OF_TABLES = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {
    }

    public TableGroup(OrderTables orderTables) {
        this(0L, orderTables);
    }

    public TableGroup(Long id, OrderTables orderTables) {
        validateOrderTables(orderTables);
        this.id = id;
        this.orderTables = orderTables;
        this.orderTables.assignTableGroup(this);
    }

    private void validateOrderTables(OrderTables orderTables) {
        if (orderTables == null || orderTables.size() < MINIMUM_REQUIRED_NUMBER_OF_TABLES) {
            throw new IllegalArgumentException(ERROR_ORDERTABLES_SHOULD_HAVE_AT_LEAST_TWO_TABLES.showText());
        }
        if (orderTables.getOrderTables().stream()
                .anyMatch(orderTable -> orderTable.isAssignedToTableGroup() || !orderTable.isEmpty())) {
            throw new IllegalArgumentException(ERROR_TABLES_CANNOT_BE_GROUPED.showText());
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
