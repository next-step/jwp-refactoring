package kitchenpos.tablegroup.domain;

import kitchenpos.tablegroup.application.TableGroupEvent;
import kitchenpos.tablegroup.application.TableGroupValidator;
import kitchenpos.tablegroup.application.TableUngroupEvent;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
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
        this(createdDate);
        this.id = id;
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

    public void create(List<Long> orderTableIds, TableGroupValidator tableGroupValidator) {
        tableGroupValidator.validateCreate(orderTableIds);
        registerEvent(new TableGroupEvent(id, orderTableIds));
    }

    public void ungroup(TableGroupValidator tableGroupValidator) {
        tableGroupValidator.validateUngroup(id);
        registerEvent(new TableUngroupEvent(id));
    }
}
