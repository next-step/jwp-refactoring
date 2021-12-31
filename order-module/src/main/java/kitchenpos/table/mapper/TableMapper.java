package kitchenpos.table.mapper;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.TableResponse;

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
                .map(orderTable -> new TableResponse(orderTable.getId(), orderTable.getTableGroup(), orderTable.getNumberOfGuests(), orderTable.isEmpty()))
                .collect(Collectors.toList());
    }
}
