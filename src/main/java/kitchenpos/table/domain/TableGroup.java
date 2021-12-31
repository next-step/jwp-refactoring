package kitchenpos.table.domain;

import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.exception.NotCreatedOrderTablesException;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    public TableGroup() {
    }

    private TableGroup(List<OrderTable> tables) {
        this.orderTables = new OrderTables(tables);
    }

    public TableGroup(Long id) {
        this.id = id;
    }

    public TableGroup(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup of(Long id) {
        return new TableGroup(id);
    }

    public static TableGroup create(List<Long> orderTableIds, OrderTables orderTables) {
        isNotCreatedOrderTables(orderTableIds, orderTables);

        TableGroup tableGroup = new TableGroup(orderTables.getOrderTables());
        tableGroup.assignTable();
        return tableGroup;
    }

    private static void isNotCreatedOrderTables(List<Long> orderTableIds, OrderTables orderTables) {
        if (!orderTables.isCreatedOrderTable(orderTableIds.size())) {
            throw new NotCreatedOrderTablesException();
        }
    }

    private void assignTable() {
        this.orderTables.assignTable(this);
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

}
