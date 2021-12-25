package kitchenpos.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderTableResponse {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static List<OrderTableResponse> fromList(List<OrderTable> orderTables) {
        return orderTables
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public static OrderTableResponse from(@org.jetbrains.annotations.NotNull OrderTable orderTable) {
        Optional<TableGroup> tableGroup = Optional.ofNullable(orderTable.getTableGroup());
        Long tableGroupId = null;
        if (tableGroup.isPresent()) {
            tableGroupId = orderTable.getTableGroup().getId();
        }
        return new OrderTableResponse(orderTable.getId(), tableGroupId, orderTable.getNumberOfGuests(), orderTable.isEmpty());
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
