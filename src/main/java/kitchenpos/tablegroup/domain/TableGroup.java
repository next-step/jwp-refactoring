package kitchenpos.tablegroup.domain;

import kitchenpos.tablegroup.event.TableGroupEventPublisher;
import kitchenpos.tablegroup.event.TableUnGroupEventPublisher;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "table_group")
public class TableGroup extends AbstractAggregateRoot<TableGroup> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    private TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    private TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroup of(LocalDateTime createdDate) {
        return new TableGroup(createdDate);
    }

    public static TableGroup of(Long id, LocalDateTime createdDate) {
        return new TableGroup(id, createdDate);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void createOrderTableIds(List<Long> orderTablesIds) {
        registerEvent(new TableGroupEventPublisher(id, orderTablesIds));
    }

    public void unGroup() {
        registerEvent(new TableUnGroupEventPublisher(id));
    }
}
