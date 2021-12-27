package kitchenpos.ordertable.domain;

import static kitchenpos.order.application.fixture.OrderFixture.요리중_주문_of;
import static kitchenpos.ordertable.application.fixture.OrderTableFixture.단체지정된_주문테이블;
import static kitchenpos.ordertable.application.fixture.OrderTableFixture.한명_주문테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.order.domain.OrderRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("TableValidator 클래스")
@ExtendWith(MockitoExtension.class)
class TableValidatorTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableValidator tableValidator;

    @Test
    @DisplayName("`주문테이블`에 `단체 지정`이 되어 있으면 `빈 테이블` 여부를 변경 할 수 없다.")
    void 단체지정_중_빈테이블_변경_실패() {
        // given
        OrderTable 단체지정된_주문테이블 = 단체지정된_주문테이블();
        given(orderRepository.findAllByOrderTableId(any())).willReturn(
            Collections.singletonList(요리중_주문_of()));

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableValidator.completedOrderValid(
            단체지정된_주문테이블);

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }

    @Test
    @DisplayName("`주문 테이블`에서 `주문 항목`들의 `주문상태`가 `조리`,`식사`인 경우 `빈 테이블` 여부를 변경 할 수 없다.")
    void 결제완료_아닌_주문_빈테이블_변경_실패() {
        // given
        OrderTable 한명_주문테이블 = 한명_주문테이블();
        given(orderRepository.findAllByOrderTableId(any())).willReturn(
            Collections.singletonList(요리중_주문_of()));

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableValidator.completedOrderValid(
            한명_주문테이블);

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }

}
