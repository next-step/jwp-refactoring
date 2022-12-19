package kitchenpos.tablegroup.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTableResponses;

    public static TableGroupResponse of(TableGroup tableGroup, OrderTables orderTables) {
        return new TableGroupResponse(tableGroup.getId()
                , tableGroup.getCreatedDate()
                , OrderTableResponse.getOrderTableResponses(orderTables)
        );
    }

    public TableGroupResponse(Long id, LocalDateTime createdDate, List<OrderTableResponse> orderTableResponses) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTableResponses = orderTableResponses;
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
