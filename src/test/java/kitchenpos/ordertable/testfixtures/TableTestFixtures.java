package kitchenpos.ordertable.testfixtures;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;

public class TableTestFixtures {

    public static void 주문테이블_저장_결과_모킹(OrderTableRepository orderTableRepository,
        OrderTable orderTable) {
        given(orderTableRepository.save(any()))
            .willReturn(orderTable);
    }

    public static void 주문테이블_전체_조회_모킹(OrderTableRepository orderTableRepository,
        List<OrderTable> orderTables) {
        given(orderTableRepository.findAll())
            .willReturn(orderTables);
    }

    public static void 특정_주문테이블_조회_모킹(OrderTableRepository orderTableRepository,
        OrderTable orderTable) {
        given(orderTableRepository.findById(any()))
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

    public static OrderTableRequest convertToOrderTableRequest(int numberOfGuests,
        boolean isOrderClose) {
        return new OrderTableRequest(numberOfGuests, isOrderClose);
    }

    public static OrderTableRequest convertToIsOrderCloseChangeRequest(boolean isOrderClose) {
        return new OrderTableRequest(isOrderClose);
    }

    public static OrderTableRequest convertToNumberOfGuestsChangeRequest(int numberOfGuests) {
        return new OrderTableRequest(numberOfGuests);
    }
}
