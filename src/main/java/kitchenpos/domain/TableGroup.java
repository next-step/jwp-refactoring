package kitchenpos.domain;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();


    protected TableGroup() {
    }

    public TableGroup(OrderTables orderTables) {
        this.orderTables = orderTables;
        this.createdDate = LocalDateTime.now();
    }

    public static void of() {
    }

    public static TableGroup from(List<OrderTable> orderTables) {
        return new TableGroup(OrderTables.from(orderTables));
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



    public void ungroup(List<Order> order) {
        order.forEach(Order::validCheckOrderStatusIsCookingAndMeal);
        orderTables.ungroup();
    }

    public List<Long> getOrderTablesId() {
        return getOrderTables().stream().map(OrderTable::getId).collect(Collectors.toList());
    }
}
