package kitchenpos.dto;

import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;

public class TableGroupResponse {

    private final Long id;
    private final OrderTables orderTables;
    private final LocalDateTime createDate;

    public TableGroupResponse(Long id, OrderTables orderTables, LocalDateTime createDate) {
        this.id = id;
        this.orderTables = orderTables;
        this.createDate = createDate;
    }

    public static TableGroupResponse from(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getOrderTables(), tableGroup.getCreatedDate());
    }

    public Long getId() {
        return id;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }
}
