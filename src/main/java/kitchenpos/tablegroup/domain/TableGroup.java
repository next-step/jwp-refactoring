package kitchenpos.tablegroup.domain;

import kitchenpos.tablegroup.event.TableGroupPublisher;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;


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
        TableGroupPublisher tableGroupPublisher = new TableGroupPublisher(id);
        registerEvent(tableGroupPublisher);
    }
}
