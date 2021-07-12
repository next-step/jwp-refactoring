package kitchenpos.table.dto;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    public TableGroupResponse() { }

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroupDto dto) {
        return new TableGroupResponse(dto.getId(), dto.getCreatedDate(),
                                      dto.getOrderTables().stream().map(OrderTableResponse::of).collect(toList()));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
