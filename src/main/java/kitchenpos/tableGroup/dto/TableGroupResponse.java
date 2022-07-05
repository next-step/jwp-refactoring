package kitchenpos.tableGroup.dto;

import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tableGroup.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {
    private Long id;
    private List<OrderTableResponse> orderTableResponses;

    public static TableGroupResponse from(TableGroup tableGroup) {
        TableGroupResponse response = new TableGroupResponse();

        response.id = tableGroup.getId();
        response.orderTableResponses = tableGroup.getOrderTables()
                .getValue()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());

        return response;
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTableResponses;
    }
}
