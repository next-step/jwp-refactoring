package kitchenpos.table.domain;

import kitchenpos.table.event.TableUnGroupEventPublisher;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "table_group")
public class TableGroup extends AbstractAggregateRoot<TableGroup>  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    private TableGroup(Long id) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
    }

    public static TableGroup generate() {
        return new TableGroup();
    }

    public static TableGroup of(Long id) {
        return new TableGroup(id);
    }

    public void mapIntoTable(List<OrderTable> items) {
        orderTables.addAll(id, items);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getItems();
    }

    public void unGroup() {
        registerEvent(new TableUnGroupEventPublisher(orderTables.getItems()));
    }
}
