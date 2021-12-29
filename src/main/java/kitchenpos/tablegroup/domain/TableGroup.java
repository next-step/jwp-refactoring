package kitchenpos.tablegroup.domain;

import kitchenpos.table.application.TableGroupingEvent;
import kitchenpos.table.application.TableUnGroupingEvent;
import kitchenpos.utils.BaseEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "table_group")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup extends AbstractAggregateRoot<TableGroup> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    protected LocalDateTime createdDate;

    protected TableGroup() {
    }

    public static TableGroup setUp() {
        return new TableGroup();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void unGrouping() {
        registerEvent(new TableUnGroupingEvent(this, this.id));
    }

    public void grouping(List<Long> toOrderTableIds) {
        registerEvent(new TableGroupingEvent(this, this.id, toOrderTableIds));
    }
}
