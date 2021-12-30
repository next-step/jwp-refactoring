package kitchenpos.ordertable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;
import java.util.List;
import java.util.Optional;

import org.mockito.ArgumentMatchers;

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

    public static void 특정_테이블ID_리스트에_해당하는_테이블_조회_모킹(OrderTableRepository orderTableRepository,
        List<OrderTable> orderTables) {
        given(orderTableRepository.findByIdIn(ArgumentMatchers.anyList()))
            .willReturn(orderTables);
    }

    public static void 특정그룹에_해당하는_주문테이블_리스트_조회_모킹(OrderTableRepository orderTableRepository,
        List<OrderTable> orderTables) {
        given(orderTableRepository.findByTableGroupId(any()))
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
