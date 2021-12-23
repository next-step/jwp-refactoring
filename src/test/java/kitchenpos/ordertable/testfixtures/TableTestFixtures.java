package kitchenpos.ordertable.testfixtures;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.dao.OrderTableDao;
import kitchenpos.ordertable.dto.OrderTableRequest;

public class TableTestFixtures {

    public static void 주문테이블_저장_결과_모킹(OrderTableDao orderTableDao, OrderTable orderTable) {
        given(orderTableDao.save(any()))
            .willReturn(orderTable);
    }

    public static void 주문테이블_전체_조회_모킹(OrderTableDao orderTableDao, List<OrderTable> orderTables) {
        given(orderTableDao.findAll())
            .willReturn(orderTables);
    }

    public static void 특정_주문테이블_조회_모킹(OrderTableDao orderTableDao, OrderTable orderTable) {
        given(orderTableDao.findById(any()))
            .willReturn(Optional.of(orderTable));
    }

    public static void 특정_주문테이블_조회_모킹(TableService tableService, OrderTable orderTable) {
        given(tableService.findOrderTable(orderTable.getId()))
            .willReturn(orderTable);
    }

    public static void 특정_주문테이블_리스트_조회_모킹(TableService tableService, List<OrderTable> orderTables) {
        given(tableService.findOrderTables(anyList()))
            .willReturn(orderTables);
    }

    public static OrderTableRequest convertToOrderTableRequest(OrderTable orderTable) {
        return new OrderTableRequest(orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

}
