package kitchenpos.order.dto;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableState;

import java.util.List;
import java.util.stream.Collectors;

public class TableResponse {
    private Long id;
    private int numberOfGuests;
    private TableState tableState;
    private Long tableGroupId;

    public TableResponse(Long id, int numberOfGuests, TableState tableState, Long tableGroupId) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.tableState = tableState;
        this.tableGroupId = tableGroupId;
    }

    public static TableResponse of(OrderTable orderTable) {
        return new TableResponse(orderTable.getId(), orderTable.getNumberOfGuests(), orderTable.getTableState(), orderTable.getTableGroupId());
    }

    public static List<TableResponse> ofList(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(TableResponse::of)
                .collect(Collectors.toList());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean getTableState() {
        return tableState.isEmpty();
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
