package kitchenpos.table.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables;

    public TableGroup() {
        this(null, new OrderTables());
    }

    public TableGroup(Long id, OrderTables orderTables) {
        this.id = id;
        this.orderTables = orderTables;
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

    public void changeOrderTables(List<OrderTable> orderTables, boolean empty) {
        OrderTables newOrderTables = new OrderTables(orderTables);
        newOrderTables.addTableGroupAndEmpties(empty, this);
        this.orderTables = newOrderTables;
    }

    public void validateEmptyAndTableGroups() {
        orderTables.validateEmptyAndTableGroups();

    }

}
