package kitchenpos.order.generator;

import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableCreateRequest;

import java.util.List;

public class TableGenerator {
    private static final String PATH = "/api/tables";

    public static NumberOfGuests 손님_수_생성(int value) {
        return new NumberOfGuests(value);
    }

    public static OrderTable 주문_테이블_생성(NumberOfGuests numberOfGuests) {
        return new OrderTable(numberOfGuests);
    }

    public static OrderTables 주문_테이블_목록_생성(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public static OrderTableCreateRequest 주문_테이블_생성_요청(int numberOfGuests) {
        return new OrderTableCreateRequest(numberOfGuests);
    }
}
