package kitchenpos.dto.response;

import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableResponse {
    private Long id;
    private Long tableGroupId;
    private Integer numberOfGuests;
    private Boolean empty;

    public OrderTableResponse() {
    }

    public OrderTableResponse(Long id, Long tableGroupId, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse of(OrderTable orderTable) {
        TableGroup tableGroup = Optional.ofNullable(orderTable.getTableGroup()).orElse(new TableGroup());
        return new OrderTableResponse(orderTable.getId(), tableGroup.getId()
                ,orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
