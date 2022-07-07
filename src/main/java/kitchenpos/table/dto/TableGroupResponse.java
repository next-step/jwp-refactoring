package kitchenpos.table.dto;

import kitchenpos.table.domain.TableGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TableGroupResponse {
    private Long id;
    private List<OrderTableResponse> orderTableResponses = new ArrayList<>();

    public TableGroupResponse(Long id, List<OrderTableResponse> orderTableResponses) {
        this.id = id;
        this.orderTableResponses = orderTableResponses;
    }

    public static TableGroupResponse from(TableGroup tableGroup) {
        if (Objects.isNull(tableGroup)) {
            return null;
        }

        return new TableGroupResponse(tableGroup.getId(), OrderTableResponse.ofResponses(tableGroup.getOrderTables().getOrderTables()));
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTableResponses;
    }
}
