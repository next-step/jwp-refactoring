package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    private TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup of(LocalDateTime createdDate, List<OrderTable> orderTables) {
        return new TableGroup(createdDate, orderTables);
    }

    public Long getId() {
        return this.id;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return this.orderTables;
    }
}
