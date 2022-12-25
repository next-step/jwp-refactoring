package kitchenpos.table.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.table.event.TableUnGroupEvent;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
public class TableGroup extends AbstractAggregateRoot<TableGroup> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    public TableGroup(Long id) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void ungroup() {
        registerEvent(new TableUnGroupEvent(this.id));
    }
}
