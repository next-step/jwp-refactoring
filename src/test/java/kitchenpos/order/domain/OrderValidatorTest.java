package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("OrderValidator 클래스")
@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {


    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private OrderValidator orderValidator;

    @Test
    @DisplayName("`주문`이 속할 `주문 테이블`은 `빈 테이블`상태가 아니어야 한다.")
    void 주문에_속할_주문테이블이_빈테이블_아니면_실패() {
        // given
        OrderTable 빈테이블 = OrderTable.of(0, true);
        OrderLineItem 주문항목 = OrderLineItem.of(null, 1L);
        Order 주문 = Order.of(1L, Collections.singletonList(주문항목));
        given(orderTableRepository.existsById(any())).willReturn(true);
        given(orderTableRepository.existsByIdAndEmpty(any(), any())).willReturn(true);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> orderValidator.registerValidate(주문);

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }

    @Test
    @DisplayName("`주문`이 속할 `주문 테이블`이 등록되어있어야한다.")
    void 주문테이블이_존재하지_않으면_실패() {
        // given
        OrderTable 빈테이블 = OrderTable.of(0, true);
        OrderLineItem 주문항목 = OrderLineItem.of(null, 1L);
        Order 주문 = Order.of(1L, Collections.singletonList(주문항목));
        given(orderTableRepository.existsById(any())).willReturn(false);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> orderValidator.registerValidate(주문);

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }


}