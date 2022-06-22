package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.time.LocalDateTime;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 관련 Domain 단위 테스트")
class OrderTest {

    @DisplayName("주문 테이블 상태 일치 여부를 확인한다.")
    @Test
    void checkChangeEmpty() {
        //given
        Order order1 = new Order(null, OrderStatus.MEAL, LocalDateTime.now());
        Order order2 = new Order(null, OrderStatus.COOKING, LocalDateTime.now());
        Order order3 = new Order(null, OrderStatus.COMPLETION, LocalDateTime.now());

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(order1::checkPossibleChangeEmpty);
        assertThatIllegalArgumentException()
                .isThrownBy(order2::checkPossibleChangeEmpty);
        assertThatNoException()
                .isThrownBy(order3::checkPossibleChangeEmpty);
    }

}
