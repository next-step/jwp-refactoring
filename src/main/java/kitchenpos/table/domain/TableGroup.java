package kitchenpos.table.domain;

import kitchenpos.table.util.TableGroupValidator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {
    }

    public TableGroup(OrderTables orderTables){
        this.orderTables = orderTables;
        this.createdDate = LocalDateTime.now();
    }

    public void validateGroup(TableGroupValidator tableGroupValidator) {
        tableGroupValidator.validateGroup(this);
        orderTables.registerTableGroup(this);
    }

    public void validateUngroup(TableGroupValidator tableGroupValidator) {
        tableGroupValidator.validateUngroup(this);
        orderTables.unRegisterTableGroup();
        orderTables = null;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public OrderTables getOrderTables() {
        return orderTables;
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
