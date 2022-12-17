package kitchenpos.dto;

import static java.util.stream.Collectors.*;

import java.util.List;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private String createdDate;
    private List<OrderTableResponse> orderTables;

    public static TableGroupResponse of(TableGroup tableGroup, List<OrderTable> orderTables) {
        TableGroupResponse response = new TableGroupResponse();
        response.id = tableGroup.getId();
        response.createdDate = String.valueOf(tableGroup.getCreatedDate());
        response.orderTables = orderTables.stream().map(OrderTableResponse::of).collect(toList());
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
