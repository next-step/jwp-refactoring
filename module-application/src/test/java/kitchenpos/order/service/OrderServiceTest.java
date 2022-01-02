package kitchenpos.order.service;


import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @DisplayName("주문을 생성한다.")
    @Test
    void 주문_생성() {
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
        final OrderRequest orderRequest = new OrderRequest(1L, null, Collections.singletonList(orderLineItemRequest));
        final List<MenuProduct> menuProducts = Collections.singletonList(MenuProduct.of(1L, Quantity.of(1L), BigDecimal.valueOf(5000)));
        final Menu menu = Menu.of("menu", Price.of(BigDecimal.valueOf(5000)), 1L, MenuProducts.of(menuProducts));
        final Order order = orderRequest.toOrder();

        given(orderTableRepository.findById(orderRequest.getOrderTableId())).willReturn(Optional.of(OrderTable.of(10, false)));
        given(menuRepository.findById(orderLineItemRequest.getMenuId())).willReturn(Optional.of(menu));
        given(orderRepository.save(order)).willReturn(order);

        OrderResponse response = orderService.create(orderRequest);

        assertAll(
                () -> assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(response.getOrderLineItems().size()).isEqualTo(1)
        );
    }

    @DisplayName("주문 테이블을 미존재 예외")
    @Test
    void 주문_테이블_미존재_검증() {
        OrderRequest orderRequest = new OrderRequest(55L, null, Collections.singletonList(new OrderLineItemRequest(1L, 1L)));
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> orderService.create(orderRequest));

        assertThat(thrown).isInstanceOf(NotFoundException.class)
                .hasMessage("해당 주문 테이블을 찾을 수 없습니다.");
    }

    @DisplayName("메뉴 미존재 예외")
    @Test
    void 메뉴_미존재_검증() {
        OrderRequest orderRequest = new OrderRequest(55L, null, Collections.singletonList(new OrderLineItemRequest(1L, 1L)));
        given(orderTableRepository.findById(orderRequest.getOrderTableId())).willReturn(Optional.of(OrderTable.of(10, false)));
        given(menuRepository.findById(anyLong())).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> orderService.create(orderRequest));

        assertThat(thrown).isInstanceOf(NotFoundException.class)
                .hasMessage("해당 메뉴를 찾을 수 없습니다.");
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void 주문_목록_조회() {
        // given
        Order firstOrder = Order.of(1L, Collections.singletonList(OrderLineItem.of(1L, Quantity.of(2L))));
        Order secondOrder = Order.of(1L, Collections.singletonList(OrderLineItem.of(2L, Quantity.of(1L))));
        given(orderRepository.findAll()).willReturn(Arrays.asList(firstOrder, secondOrder));

        // when
        List<OrderResponse> response = orderService.list();

        // then
        assertThat(response.size()).isEqualTo(2);
    }
}
