package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;

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

    private TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public TableGroup() {
    }

    public static TableGroup of() {
        return new TableGroup(LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void unGroup(final List<OrderTable> orderTables) {
        orderTables.forEach(OrderTable::unGroup);
    }
}
