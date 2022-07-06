package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.event.UngroupTableEventPublisher;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup extends AbstractAggregateRoot<TableGroup> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = new OrderTables(orderTables);
    }

    public TableGroup(LocalDateTime createdDate) {
        this(null, createdDate, null);
    }

    public TableGroup(List<OrderTable> orderTables) {
        this(null, null, orderTables);
    }

    public Long getId() {
        return id;
    }

    public List<Long> getOrderTableIds() {
        return this.orderTables.get().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void ungroup() {
        registerEvent(new UngroupTableEventPublisher(this));
        this.orderTables.ungroup();
    }

    public TableGroupResponse toTableGroupResponse() {
        final List<OrderTableResponse> orderTableResponses = this.orderTables.toOrderTableResponses();
        return new TableGroupResponse(this.id, this.createdDate, orderTableResponses);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
