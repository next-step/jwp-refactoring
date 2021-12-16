package kitchenpos.domain.tablegroup;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.domain.AbstractAggregateRoot;

import kitchenpos.domain.tablegroup.event.TableGroupGroupedEvent;
import kitchenpos.domain.tablegroup.event.TableGroupUngroupedEvent;

@Entity
@Table(name = "table_group")
public class TableGroup extends AbstractAggregateRoot<TableGroup> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    protected TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    private TableGroup(Long id) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
    }

    public static TableGroup from(Long id) {
        return new TableGroup(id);
    }

    public static TableGroup create() {
        return new TableGroup();
    }

    public void group(List<Long> orderTableIds) {
        registerEvent(new TableGroupGroupedEvent(this, orderTableIds));
    }

    public void ungroup() {
        registerEvent(new TableGroupUngroupedEvent(this));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
