package kitchenpos.domain.tableGroup;

import kitchenpos.domain.common.BaseEntity;
import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.orderTable.OrderTables;
import kitchenpos.dto.tableGroup.TableGroupRequest;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderTables orderTables;

    public TableGroup() {

    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = new OrderTables(orderTables);
    }

    public void addOrderTables(OrderTables orderTables) {
        this.orderTables.addOrderTables(orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return super.getCreatedDate();
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }
}
