package kitchenpos.tablegroup.domain;

import kitchenpos.tablegroup.event.TableGroupEventPublisher;
import kitchenpos.tablegroup.event.TableUnGroupEventPublisher;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup extends AbstractAggregateRoot<TableGroup> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createdDate;

    protected TableGroup() {}

    public TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this(createdDate);
        this.id = id;
    }

    public void setOrderTableIds(List<Long> orderTablesIds) {
        registerEvent(new TableGroupEventPublisher(id, orderTablesIds));
    }

    public void ungroup() {
        registerEvent(new TableUnGroupEventPublisher(id));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
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
