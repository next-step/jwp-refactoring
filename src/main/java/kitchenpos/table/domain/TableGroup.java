package kitchenpos.table.domain;

import java.time.*;
import java.util.*;

import javax.persistence.Id;
import javax.persistence.*;

import org.springframework.data.annotation.*;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate = LocalDateTime.now();

    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {}

    public static TableGroup create() {
        return new TableGroup();
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

    public void addOrderTable(OrderTable orderTable) {
        orderTables.add(this.id, orderTable);
    }

    public void ungroup() {
        orderTables.ungroup();
    }
}
