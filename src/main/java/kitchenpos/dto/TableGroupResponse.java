package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.TableGroup;

public class TableGroupResponse {
    private Long id;
    private List<OrderTableResponse> orderTables;
    private LocalDateTime createDate;

    public TableGroupResponse(TableGroup saveTableGroup) {
        this(saveTableGroup.getId(), OrderTableResponse.of(saveTableGroup.getOrderTables()),
                saveTableGroup.getCreatedDate());
    }

    public TableGroupResponse(Long id, List<OrderTableResponse> orderTables, LocalDateTime createDate) {
        this.id = id;
        this.orderTables = orderTables;
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }
}
