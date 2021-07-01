package kitchenpos.dto.response;

import kitchenpos.domain.table.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupViewResponse {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableViewResponse> orderTables;

    public static TableGroupViewResponse of(TableGroup tableGroup) {
        List<OrderTableViewResponse> tableViewResponses = tableGroup.getOrderTables()
                .stream()
                .map(OrderTableViewResponse::of)
                .collect(Collectors.toList());

        return new TableGroupViewResponse(
                tableGroup.getId(),
                tableGroup.getCreatedDate(),
                tableViewResponses
        );
    }

    public TableGroupViewResponse() {
    }

    public TableGroupViewResponse(Long id, LocalDateTime createdDate, List<OrderTableViewResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableViewResponse> getOrderTables() {
        return orderTables;
    }
}
