package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.table.domain.event.TableGroupingEvent;
import kitchenpos.table.domain.event.TableUngroupEvent;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
@Table(name = "table_group")
public class TableGroup extends AbstractAggregateRoot<TableGroup> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    protected TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    public TableGroup(Long id) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
    }

    public static TableGroup create() {
        return new TableGroup();
    }

    public static TableGroup from(Long id) {
        return new TableGroup(id);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void group(List<Long> orderTableIds) {
        registerEvent(new TableGroupingEvent(this, orderTableIds));
    }

    public void ungroup() {
        registerEvent(new TableUngroupEvent(this));
    }
}
