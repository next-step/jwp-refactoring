package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;

public class OrderTableResponses {

    List<OrderTableResponse> list;

    public OrderTableResponses(List<OrderTableResponse> list) {
        this.list = list;
    }

    public static OrderTableResponses of(List<OrderTable> orderTables) {
        return new OrderTableResponses(orderTables.stream()
            .map(OrderTableResponse::of)
            .collect(Collectors.toList()));
    }


    public List<OrderTableResponse> getList() {
        return list;
    }
}
