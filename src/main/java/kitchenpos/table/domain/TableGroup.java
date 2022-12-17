package kitchenpos.table.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderLineItems = new OrderTables();

    protected TableGroup() {}

    public TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public static TableGroup create() {
        return new TableGroup(LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderLineItems() {
        return orderLineItems.value();
    }

}
