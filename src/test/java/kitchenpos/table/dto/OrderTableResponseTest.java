package kitchenpos.table.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;

public class OrderTableResponseTest {

    public static OrderTableResponse 주문_테이블_응답_객체_생성(OrderTable orderTable) {
        return new OrderTableResponse.Builder()
                .id(orderTable.getId())
                .tableGroupId(orderTable.getTableGroupId())
                .numberOfGuests(orderTable.getNumberOfGuestsValue())
                .empty(orderTable.isEmpty())
                .build();
    }

    public static List<OrderTableResponse> 주문_테이블_응답_객체들_생성(OrderTable... orderTables) {
        return Arrays.stream(orderTables)
                .map(OrderTableResponseTest::주문_테이블_응답_객체_생성)
                .collect(Collectors.toList());
    }
}