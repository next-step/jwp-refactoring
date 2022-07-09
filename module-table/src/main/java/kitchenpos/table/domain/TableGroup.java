package kitchenpos.table.domain;

import kitchenpos.table.event.TableUnGroupEventPublisher;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup extends AbstractAggregateRoot<TableGroup> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {
    }

    public TableGroup(long id) {
        this.id = id;
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = new OrderTables(orderTables);
    }

    public TableGroup(long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = new OrderTables(orderTables);
    }

    public TableGroup(long id, List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = new OrderTables(orderTables);
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

    public void changeCreatedDate(LocalDateTime createdDate, OrderTables orderTables, int size) {
        validate(orderTables, size);
        this.createdDate = createdDate;
        this.orderTables = orderTables;
        this.orderTables.add(this);
    }

    public void validate(OrderTables orderTables, int size) {
        if (orderTables.getSize() != size) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables.getElements()) {
            orderTable.validate();
        }
    }

    public void unGroup() {
        registerEvent(new TableUnGroupEventPublisher(orderTables));
    }
}
