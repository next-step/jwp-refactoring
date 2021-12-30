package kitchenpos.dto.tablegroup;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.OrderTables;
import kitchenpos.tablegroup.TableGroup;
import kitchenpos.dto.table.OrderTableResponse;


public class TableGroupResponse {

    private Long id;

    private LocalDateTime createdDate;

    private List<OrderTableResponse> orderTables;

    public TableGroupResponse() {
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate,
        List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse from(TableGroup tableGroup, OrderTables orderTables) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
            orderTables.getValues()
                .stream()
                .map(OrderTableResponse::from)
                .collect(toList()));
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
