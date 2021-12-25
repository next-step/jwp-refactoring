package kitchenpos.mapper;

import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableResponse;

import java.util.List;
import java.util.stream.Collectors;

public class TableMapper {

    private TableMapper() {
    }

    public static TableResponse toOrderTable(final OrderTable orderTable) {
        return new TableResponse(orderTable.getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    public static List<TableResponse> toOrderTables(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(orderTable -> new TableResponse(orderTable.getId(), orderTable.getTableGroup().getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty()))
                .collect(Collectors.toList());
    }
}
