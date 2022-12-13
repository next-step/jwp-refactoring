package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    public void addAllOrderTables(List<OrderTable> savedOrderTables) {
        savedOrderTables.forEach(orderTable -> {
            orderTable.changeTableGroup(this);
            orderTable.changeEmpty(false);
        });
        orderTables.addAllOrderTables(savedOrderTables);
    }

    public void validateOrderTableEmptyOrNonNull(List<OrderTable> savedOrderTables) {
        orderTables.validateOrderTableEmptyOrNonNull(savedOrderTables);
    }

    public void validateOrderTablesSize() {
        orderTables.validateOrderTablesSize();
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

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }

    public Void validateOrderStatus(String... orderStatuses) {
        orderTables.validateOrderStatus(Arrays.asList(orderStatuses));
        return null;
    }

    public void unGroup() {
        orderTables.unGroup();
    }
}
