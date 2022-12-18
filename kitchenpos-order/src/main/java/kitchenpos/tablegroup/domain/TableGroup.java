package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(value = AuditingEntityListener.class)
public class TableGroup extends AbstractAggregateRoot<TableGroup> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @PreRemove
    void registerUnGroupEvent() {
        this.registerEvent(new TableUnGroupedEvent(this.id));
    }

    protected TableGroup() {

    }

    public TableGroup(List<Long> tableIds) {
        this.registerEvent(new TableGroupedEvent(this, tableIds));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
