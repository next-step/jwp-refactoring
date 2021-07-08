package kitchenpos.table.dto;

import kitchenpos.table.domain.TableGroup;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {

    private Long id;
    private List<OrderTableResponse> orderTableResponses;

    private TableGroupResponse(Long id, List<OrderTableResponse> orderTableResponses) {
        this.id = id;
        this.orderTableResponses = orderTableResponses;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(),
                tableGroup.getOrderTables().stream()
                        .map(OrderTableResponse::of)
                        .collect(Collectors.toList()));
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTableResponses;
    }
}
