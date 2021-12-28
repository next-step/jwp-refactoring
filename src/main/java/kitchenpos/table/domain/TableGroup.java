package kitchenpos.table.domain;

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

    private LocalDateTime createdDate = LocalDateTime.now();

    protected TableGroup() {
    }

    public static TableGroup create() {
        return new TableGroup();
    }

    public TableGroup group(TableGroupValidator tableGroupValidator, List<Long> orderTableIds) {
        tableGroupValidator.validateGrouping(orderTableIds);
        registerEvent(TableGroupEvent.of(id, orderTableIds));

        return this;
    }

    public void ungroup(TableGroupValidator tableGroupValidator) {
        tableGroupValidator.validateUngrouping(id);
        registerEvent(TableUngroupEvent.of(id));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
