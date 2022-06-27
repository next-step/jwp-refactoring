package kitchenpos.table.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class OrderTableResponse {
    private Long id;
    private Integer numberOfGuests;
    private Boolean empty;
    private Long tableGroupId;

    public OrderTableResponse() {
    }

    public OrderTableResponse(Long id, Integer numberOfGuests, Boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableResponse(Long id, Integer numberOfGuests, Boolean empty, Long tableGroupId) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(),
                orderTable.getNumberOfGuests().getNumberOfGuests(),
                orderTable.getEmpty().isEmpty());
    }

    public static OrderTableResponse of(OrderTable orderTable, TableGroup tableGroup) {
            return new OrderTableResponse(orderTable.getId(),
                    orderTable.getNumberOfGuests().getNumberOfGuests(),
                    orderTable.getEmpty().isEmpty(),
                    tableGroup.getId());
    }

    public static List<OrderTableResponse> ofList(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
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
