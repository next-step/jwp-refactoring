package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroupId;
import kitchenpos.table.dto.OrderTableDto;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    private TableGroup(Long id) {
        this.id = id;
    }

    public static TableGroup of(Long id) {
        return new TableGroup(id);
    }

    public static TableGroup of() {
        return TableGroup.of(null);
    }

    public Long getId() {
        return this.id;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public void ungroup(OrderTables orderTables, TableGroupValidator tableGroupValidator) {
        tableGroupValidator.validateForUnGroup(orderTables);
        orderTables.unGroupTable();
    }

    public void groupingTable(List<OrderTableDto> orderTableDtos, OrderTables savedOrderTables, TableGroupValidator tableGroupValidator) {
        tableGroupValidator.checkAllExistOfOrderTables(orderTableDtos, savedOrderTables);
        tableGroupValidator.checkOrderTableSize(savedOrderTables);

        for (int index = 0; index < savedOrderTables.size(); index++) {
            tableGroupValidator.checkHasTableGroup(savedOrderTables.get(index));
            tableGroupValidator.checkNotEmptyTable(savedOrderTables.get(index));
        }

        savedOrderTables.groupingTable(TableGroupId.of(this.getId()));
    }
}
