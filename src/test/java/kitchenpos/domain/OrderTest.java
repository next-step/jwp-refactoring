package kitchenpos.domain;

import static kitchenpos.domain.OrderLineItemTest.*;
import static kitchenpos.domain.OrderStatus.*;
import static kitchenpos.domain.OrderTableTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.IllegalOperationException;

@DisplayName("주문 단위 테스트")
public class OrderTest {
    public static final Order 테이블9주문 = new Order(1L, COOKING, OrderLineItems.of(테이블9주문_1));
    public static final Order 테이블10주문 = new Order(2L, MEAL, OrderLineItems.of(테이블10주문_1, 테이블10주문_2));
    public static final Order 테이블11주문 = new Order(3L, COMPLETION, OrderLineItems.of(테이블11주문_1));

    static {
        테이블9_사용중.addOrder(테이블9주문);
        테이블10_사용중.addOrder(테이블10주문);
        테이블11_사용중.addOrder(테이블11주문);
    }

    public static Order order(Long id, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        return new Order(id, orderStatus, orderLineItems);
    }

    Order 주문;

    @BeforeEach
    void setUp() {
        주문 = new Order(4L, COOKING, OrderLineItems.of(테이블9주문_1));
    }

    @Test
    @DisplayName("주문의 상태를 변경한다")
    void proceedTo() {
        주문.proceedTo(MEAL);
        assertThat(주문.getOrderStatus()).isEqualTo(MEAL);
    }

    @Test
    @DisplayName("완결인 경우 상태 변경 불가")
    void proceedTo_failed() {
        주문.proceedTo(COMPLETION);
        assertThatThrownBy(() -> 주문.proceedTo(MEAL))
            .isInstanceOf(IllegalOperationException.class);
    }

    @Test
    @DisplayName("주문이 InProgess 상태인지 확인")
    void inProgress() {
        assertThat(주문.inProgress()).isTrue();
    }
}
