package kitchenpos.order.domain;

import static kitchenpos.helper.OrderFixtures.주문_만들기;
import static kitchenpos.helper.OrderLineItemFixtures.주문_항목들_만들기;
import static kitchenpos.helper.TableFixtures.테이블_만들기;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import kitchenpos.order.consts.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DisplayName("주문 관련 Validator 기능 테스트")
@DataJpaTest
@Import(OrderValidator.class)
class OrderValidatorTest {

    @Autowired
    private OrderValidator orderValidator;

    @DisplayName("빈테이블인 경우 주문을 등록 할 수 없다")
    @Test
    void validate() {
        //given
        Order order = 주문_만들기(OrderStatus.COOKING, 테이블_만들기(1L, 3, true), 주문_항목들_만들기());

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderValidator.validate(order))
                .withMessageContaining("빈테이블인 경우 주문을 등록 할 수 없습니다.");
    }
}
