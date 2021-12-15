package kitchenpos.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroupResponse {

    private final Long id;
    private final List<OrderTable> orderTables;
    private final LocalDateTime createDate;

    public TableGroupResponse(Long id, List<OrderTable> orderTables, LocalDateTime createDate) {
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

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }
}
