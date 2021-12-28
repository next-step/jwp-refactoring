package kitchenpos.order.application;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 유효성 검증 테스트")
class OrderValidatorTest {

    @InjectMocks
    private OrderValidator orderValidator;

    @Mock
    private MenuRepository menuRepository;

    private Long orderTableId;
    private Long menuId;

    @BeforeEach
    public void setUp() {
        // given
        orderTableId = 1L;
        menuId = 1L;
    }

    @DisplayName("주문 생성 유효성 검증 실패 테스트 - 메뉴 수 일치하지 않음")
    @Test
    void validateCreate_failure_invalidMenuSize() {
        // given
        OrderLineItem orderLineItem = OrderLineItem.of(menuId, Quantity.of(1));
        Order order = Order.of(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItems.of(Arrays.asList(orderLineItem)));

        given(menuRepository.findByIdIn(Arrays.asList(menuId))).willReturn(Collections.emptyList());

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderValidator.validateCreate(order));
    }

    @DisplayName("주문 상태 수정 유효성 검증 성공 테스트 - 주문 상태 COOKING")
    @Test
    void validateChangeOrderStatus_success_cooking() {
        // given
        OrderLineItem orderLineItem = OrderLineItem.of(menuId, Quantity.of(1));
        Order order = Order.of(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItems.of(Arrays.asList(orderLineItem)));

        // when & then
        assertThatNoException()
                .isThrownBy(() -> orderValidator.validateChangeOrderStatus(order));
    }

    @DisplayName("주문 상태 수정 유효성 검증 성공 테스트 - 주문 상태 MEAL")
    @Test
    void validateChangeOrderStatus_success_meal() {
        // given
        OrderLineItem orderLineItem = OrderLineItem.of(menuId, Quantity.of(1));
        Order order = Order.of(orderTableId, OrderStatus.MEAL, LocalDateTime.now(), OrderLineItems.of(Arrays.asList(orderLineItem)));

        // when & then
        assertThatNoException()
                .isThrownBy(() -> orderValidator.validateChangeOrderStatus(order));
    }

    @DisplayName("주문 상태 수정 유효성 검증 실패 테스트 - 주문 상태가 COMPLETION인 경우 주문 상태를 수정할 수 없음")
    @Test
    void validateChangeOrderStatus_failure() {
        // given
        OrderLineItem orderLineItem = OrderLineItem.of(menuId, Quantity.of(1));
        Order order = Order.of(orderTableId, OrderStatus.COMPLETION, LocalDateTime.now(), OrderLineItems.of(Arrays.asList(orderLineItem)));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderValidator.validateChangeOrderStatus(order));
    }
}
