package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTableEntity;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTableResponse {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableResponse() {
    }

    public OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }


    public static OrderTableResponse of(OrderTableEntity orderTableEntity) {
        return new OrderTableResponse(orderTableEntity.getId()
                , orderTableEntity.getTableGroupId()
                , orderTableEntity.getNumberOfGuests()
                , orderTableEntity.isEmpty());
    }

    public static List<OrderTableResponse> ofList(List<OrderTableEntity> orderTableEntities) {
        return orderTableEntities.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
