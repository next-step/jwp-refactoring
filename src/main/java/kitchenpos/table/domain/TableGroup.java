package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;


    public TableGroup() {

    }

    private TableGroup(Long id) {
        this.id = id;
    }

    public static TableGroup of(Long id) {
        return new TableGroup(id);
    }

    public void ungroup(List<Order> orders) {
        orders.forEach(Order::checkOngoingOrderTable);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
