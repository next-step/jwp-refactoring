package kitchenpos.order.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 테이블 유효성 관련 테스트")
@ExtendWith(MockitoExtension.class)
class OrderStatusValidatorTest {
    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderStatusValidator statusValidator;

    @DisplayName("주문 테이블의 상태가 '조리' 또는 '식사' 상태일 경우 예외 발생")
    @Test
    void validateOrderTableNotCompletion_exception() {
        // given
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> statusValidator.validateOrderTableNotCompletion(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블들의 상태가 '조리' 또는 '식사' 상태일 경우 예외 발생")
    @Test
    void validateOrderTablesNotCompletion_exception() {
        // given
        given(주문_조리_또는_식사_상태인지_확인()).willReturn(true);

        // when & then
        assertThatThrownBy(() -> statusValidator.validateOrderTablesNotCompletion(new OrderTables()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private boolean 주문_조리_또는_식사_상태인지_확인() {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList());
    }
}
