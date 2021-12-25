package kitchenpos.order;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;

import java.util.List;
import java.util.stream.Collectors;

public class OrderFactory {
    public static OrderTableRequest ofOrderTableRequest(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }

    public static TableGroupRequest ofTableGroupRequest(List<OrderTableResponse> orderTableResponses) {
        List<OrderTable> orderTables = orderTableResponses.stream()
                .map(orderTableResponse -> new OrderTable(orderTableResponse.getId(), null, orderTableResponse.getNumberOfGuests(), orderTableResponse.isEmpty()))
                .collect(Collectors.toList());
        return new TableGroupRequest(orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList()));
    }
}
