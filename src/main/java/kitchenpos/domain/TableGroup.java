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

    public TableGroup(List<OrderTable> orderTablesParam) {
        validateOrderTableEmptyOrNonNull(orderTablesParam);
        addAllOrderTables(orderTablesParam);
        validateOrderTablesSize();
        this.createdDate = LocalDateTime.now();
    }

    public void unGroup() {
        validateOrderStatus(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        orderTables.unGroup();
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

    private void addAllOrderTables(List<OrderTable> savedOrderTables) {
        savedOrderTables.forEach(orderTable -> {
            orderTable.changeEmpty(false);
            orderTable.changeTableGroup(this);
        });
        orderTables.addAllOrderTables(savedOrderTables);
    }

    private void validateOrderTableEmptyOrNonNull(List<OrderTable> savedOrderTables) {
        orderTables.validateOrderTableEmptyOrNonNull(savedOrderTables);
    }

    private void validateOrderTablesSize() {
        orderTables.validateOrderTablesSize();
    }

    private void validateOrderStatus(String... orderStatuses) {
        orderTables.validateOrderStatus(Arrays.asList(orderStatuses));
    }
}
