package kitchenpos.order.application;


import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.fixture.OrderFixture;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static kitchenpos.order.application.OrderService.ORDER_LINE_ITEMS_EMPTY_EXCEPTION_MESSAGE;
import static kitchenpos.order.application.OrderService.ORDER_LINE_ITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE;
import static kitchenpos.order.domain.fixture.OrderFixture.orderA;
import static kitchenpos.order.domain.fixture.OrderLineItemsFixture.orderLineItemsA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@DisplayName("OrderCrudService")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    public static final long NOT_EXIST_ORDER_TABLE_ID = 100L;
    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderValidator orderValidator;

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

        doThrow(new IllegalArgumentException(ORDER_LINE_ITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE))
                .when(orderValidator).validate(any());

        assertThatThrownBy(() -> orderService.create(new OrderCreateRequest(1L, orderLineItemsA())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ORDER_LINE_ITEMS_SIZE_MENU_SIZE_NOT_EQUAL_EXCEPTION_MESSAGE);
    }

    @DisplayName("주문을 생성한다. / 주문 테이블은 비어있을 수 없다.")
    @Test
    void create_fail_orderTableEmpty() {

        OrderCreateRequest request = new OrderCreateRequest(NOT_EXIST_ORDER_TABLE_ID, orderLineItemsA());

        doThrow(new IllegalArgumentException("메뉴의 가격이 메뉴 상품의 합보다 클 수 없다."))
                .when(orderValidator).validate(any());

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {

        doNothing().when(orderValidator).validate(any());
        given(orderRepository.save(any())).willReturn(orderA(1L));

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

        given(orderRepository.findAll()).willReturn(Collections.singletonList(OrderFixture.orderA(1L)));

        List<OrderResponse> list = orderService.list();
        assertThat(list.size()).isEqualTo(1);
    }
}
