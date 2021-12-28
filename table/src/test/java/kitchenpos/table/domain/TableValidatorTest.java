package kitchenpos.table.domain;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTableId;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.exception.HasNotCompletionOrderException;
import kitchenpos.table.exception.EmptyOrderTableException;
import kitchenpos.table.exception.HasOtherTableGroupException;
import kitchenpos.table.exception.NegativeOfNumberOfGuestsException;

@ExtendWith(MockitoExtension.class)
public class TableValidatorTest {
    @Mock
    private OrderService orderService;

    @InjectMocks
    private TableValidator tableValidator;

    @DisplayName("단체지정된 주문테이블의 빈테이블 상태변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderTable_existOrderTableInTableGroup() {
        // given
        OrderTable 치킨_주문테이블 = OrderTable.of(0, true);
        OrderTable 치킨2_주문_단체테이블 = OrderTable.of(0, true);

        치킨_주문테이블.groupingTable(TableGroupId.of(1L));
        치킨2_주문_단체테이블.groupingTable(TableGroupId.of(1L));
        
        // when
        // then
        Assertions.assertThatExceptionOfType(HasOtherTableGroupException.class)
                    .isThrownBy(() -> tableValidator.checkHasTableGroup(치킨_주문테이블));
    }

    @DisplayName("주문상태가 계산완료가 아닌 주문테이블의 빈테이블 상태변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderTable_EmptyStatus() {
        // given
        OrderTable 치킨_주문테이블 = OrderTable.of(10, false);

        when(orderService.findByOrderTableId(nullable(Long.class))).thenReturn(Orders.of(OrderTableId.of(치킨_주문테이블.getId()), OrderStatus.MEAL));
        
        // when
        // then
        Assertions.assertThatExceptionOfType(HasNotCompletionOrderException.class)
                    .isThrownBy(() -> tableValidator.checkOrderStatusOfOrderTable(치킨_주문테이블.getId()));
    }

    @DisplayName("방문한 손님수를 0 미만일시 예외가 발생된다.")
    @ValueSource(ints = {-1, -9})
    @ParameterizedTest(name ="[{index}] 방문한 손님수는 [{0}]")
    void exception_updateOrderTable_underZeroCountAboutNumberOfGuest(int numberOfGuests) {

        // when
        // then
        Assertions.assertThatExceptionOfType(NegativeOfNumberOfGuestsException.class)
                    .isThrownBy(() -> tableValidator.checkPositiveOfNumberOfGuests(numberOfGuests));

    }

    @DisplayName("주문테이블이 빈테이블일시 예외가 발생된다.")
    @Test
    void exception_updateOrderTable_atEmptyTable() {
        // given
        OrderTable 치킨_주문테이블 = OrderTable.of(0, true);

        // when
        // then
        Assertions.assertThatExceptionOfType(EmptyOrderTableException.class)
                    .isThrownBy(() -> tableValidator.checkEmptyTable(치킨_주문테이블));
    }
}
