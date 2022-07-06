package kitchenpos.table.validator;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.Empty;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static kitchenpos.common.Messages.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {

    @InjectMocks
    private OrderTableValidator orderTableValidator;

    @Mock
    private OrderRepository orderRepository;

    @Test
    @DisplayName("테이블 상태 변경시 유효성 검사가 성공한다.")
    void validateChangeEmpty() {
        // given
        OrderTable 신규_주문_테이블 = OrderTable.of(1L, null, NumberOfGuests.of(4), Empty.of(true));

        // when
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(false);

        // then
        assertDoesNotThrow(() -> orderTableValidator.validateChangeEmpty(신규_주문_테이블));
    }

    @Test
    @DisplayName("테이블을 변경시 테이블 정보가 조회되지 않은 경우 상태 변경에 실패한다")
    void hasOrderTableGroup() {
        // given
        OrderTable 테이블_그룹_존재 = OrderTable.of(1L, 1L, NumberOfGuests.of(4), Empty.of(true));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTableValidator.validateChangeEmpty(테이블_그룹_존재))
                .withMessage(HAS_ORDER_TABLE_GROUP)
        ;
    }

    @Test
    @DisplayName("주문 테이블 비어있는 정보로 변경시 주문 상태가 요리와 식사 상태인 경우 실패 테스트")
    void orderTableStatusCannotUpdate() {
        // given
        OrderTable 식사중_테이블 = OrderTable.of(1L, null, NumberOfGuests.of(4), Empty.of(true));

        // when
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(true);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTableValidator.validateChangeEmpty(식사중_테이블))
                .withMessage(ORDER_TABLE_STATUS_CANNOT_UPDATE)
        ;
    }

    @Test
    @DisplayName("주문 테이블 손님 수 변경 유효성 검사가 정상적으로 성공 된다.")
    void validateChangeNumberOfGuests() {
        // given
        OrderTable 신규_주문_테이블 = OrderTable.of(1L, null, NumberOfGuests.of(4), Empty.of(false));

        // when & then
        assertDoesNotThrow(() -> orderTableValidator.validateChangeNumberOfGuests(신규_주문_테이블));
    }

    @Test
    @DisplayName("주문 테이블 손님 수 변경 유효성 검사시 테이블이 비어있는 경우 실패 된다.")
    void orderTableCannotEmpty() {
        // given
        OrderTable 비어있는_테이블 = OrderTable.of(1L, null, NumberOfGuests.of(4), Empty.of(true));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTableValidator.validateChangeNumberOfGuests(비어있는_테이블))
                .withMessage(ORDER_TABLE_CANNOT_EMPTY)
        ;
    }
}
