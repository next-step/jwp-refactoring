package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.event.TableGroupEvent;
import kitchenpos.tablegroup.event.TableUnGroupEvent;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;

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
    @CreatedDate
    private LocalDateTime createdDate;

    public TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void ungroup() {
        TableUnGroupEvent tableUnGroupEvent = new TableUnGroupEvent(id);
        registerEvent(tableUnGroupEvent);
    }

    public void setOrderTableId(List<OrderTable> orderTables) {
        registerEvent(new TableGroupEvent(id, orderTables));
    }
}
