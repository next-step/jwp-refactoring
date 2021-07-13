package kitchenpos.order.dto;

import java.util.List;
import kitchenpos.order.domain.entity.TableGroup;

public class TableGroupResponse {

    private Long id;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse() {
    }

    public TableGroupResponse(Long id, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(),
            OrderTableResponse.ofList(tableGroup.getOrderTables().getValue()));
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
