package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.exception.CommonErrorCode;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.tablegroup.domain.event.TableGroupingEvent;
import kitchenpos.tablegroup.domain.event.TableUnGroupingEvent;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
public class TableGroup extends AbstractAggregateRoot<TableGroup> {

    public static final int ORDER_TABLES_MIN_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    private TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public static TableGroup of() {
        return new TableGroup(LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void group(TableGroupValidator tableGroupValidator, List<Long> tableIds) {
        minOrderTableValid(tableIds);
        tableGroupValidator.groupExistValidate(tableIds);
        registerEvent(new TableGroupingEvent(id, tableIds));
    }

    public void ungroup(TableGroupValidator tableGroupValidator) {
        tableGroupValidator.completedOrdersValid(this);
        registerEvent(new TableUnGroupingEvent(id));
    }

    private void minOrderTableValid(List<Long> tableIds) {
        if (tableIds.size() < ORDER_TABLES_MIN_SIZE) {
            throw new InvalidParameterException(CommonErrorCode.ORDER_TABLES_MIN_UNDER_EXCEPTION);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (Objects.isNull(id)) {
            return false;
        }

        TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
