package kitchenpos.order.application;

import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static kitchenpos.order.application.OrderService.ORDER_LINE_ITEMS_EMPTY_EXCEPTION_MESSAGE;
import static kitchenpos.order.application.OrderService.ORDER_LINE_ITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE;
import static kitchenpos.order.domain.fixture.OrderLineItemsFixture.orderLineItemsA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("OrderCrudService")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    public static final long NOT_EXIST_ORDER_TABLE_ID = 100L;
    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @DisplayName("주문을 생성한다. / 주문 항목이 비어있을 수 없다.")
    @Test
    void create_fail_orderLineItems() {

        OrderCreateRequest request = new OrderCreateRequest(1L, new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_LINE_ITEMS_EMPTY_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문을 생성한다. / 주문 항목의 수와 메뉴의 수는 같아야 한다.")
    @Test
    void create_fail_orderLineItemSize() {
        assertThatThrownBy(() -> orderService.create(new OrderCreateRequest(1L, orderLineItemsA())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_LINE_ITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문을 생성한다. / 주문 테이블은 비어있을 수 없다.")
    @Test
    void create_fail_orderTableEmpty() {

        OrderCreateRequest request = new OrderCreateRequest(NOT_EXIST_ORDER_TABLE_ID, orderLineItemsA());

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {

        OrderResponse orderResponse = orderService.create(new OrderCreateRequest(1L, orderLineItemsA()));

        assertAll(
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(orderResponse.getId()).isNotNull(),
                () -> assertThat(orderResponse.getOrderedTime()).isNotNull(),
                () -> assertThat(orderResponse.getOrderTableId()).isNotNull(),
                () -> assertThat(orderResponse.getOrderLineItems()).isNotNull()
        );
    }

    @DisplayName("주문을 조회한다.")
    @Test
    void list() {
        orderService.create(new OrderCreateRequest(1L, orderLineItemsA()));
        assertThat(orderService.list()).hasSize(1);
    }
}
