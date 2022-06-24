package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private LocalDateTime createDate;
    private List<OrderTableResponse> orderTables;

    protected TableGroupResponse() {
    }

    protected TableGroupResponse(Long id, LocalDateTime createDate, List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createDate = createDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), OrderTableResponse.of(tableGroup.getOrderTables()));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }
}
