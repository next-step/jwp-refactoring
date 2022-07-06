package ktichenpos.table.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import ktichenpos.table.domain.OrderTable;
import ktichenpos.table.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTableResponses;

    private TableGroupResponse() {

    }

    private TableGroupResponse(Long id, LocalDateTime createdDate,
                               List<OrderTableResponse> orderTableResponses) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableResponses = orderTableResponses;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        List<OrderTable> orderTables = tableGroup.getOrderTables();
        return new TableGroupResponse(
                tableGroup.getId()
                , tableGroup.getCreatedDate()
                , orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList()));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTableResponses;
    }
}
