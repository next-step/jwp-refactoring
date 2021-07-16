package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
public class TableGroup extends AbstractAggregateRoot<TableGroup> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    public TableGroup() {
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void ungroup(TableGroupValidator validator) {
        validator.validateUngrouping(this);
        registerEvent(new TableUngroupedEvent(this));
    }

    public void group(TableGroupValidator validator, List<Long> orderTableIds) {
        validator.validateGrouping(orderTableIds);
        registerEvent(new TableGroupedEvent(this, orderTableIds));
    }
}
