package kitchenpos.order.service;


import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.*;
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
import static org.mockito.BDDMockito.given;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @DisplayName("주문을 생성한다.")
    @Test
    void 주문_생성() {
        final Long orderTableId = 1L;
        final Long menuId = 1L;
        final Long menuQuantity = 2L;
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menuId, menuQuantity);
        final OrderRequest orderRequest = new OrderRequest(orderTableId, null, Collections.singletonList(orderLineItemRequest));
        final OrderTable orderTable = new OrderTable(orderTableId, null, 10, false);
        final Menu menu = Menu.of(menuId, "치킨", Price.of(BigDecimal.valueOf(10000)), MenuGroup.of(1L, "튀김류"));
        final Order order = Order.of(orderTable);
        order.addOrderLineItems(Collections.singletonList(OrderLineItem.of(menu, Quantity.of(menuQuantity))));

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));
        given(menuRepository.findById(menuId)).willReturn(Optional.of(menu));
        given(orderRepository.save(Order.of(orderTable))).willReturn(order);

        OrderResponse response = orderService.create(orderRequest);

        assertAll(
                () -> assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(response.getOrderLineItems().size()).isEqualTo(1)
        );
    }

    @DisplayName("주문에 최소 1개 이상의 주문 라인 아이템이 존재해야 한다.")
    @Test
    void 주문에_주문_라인_아이템_미존재_예외() {
        // given
        final Long orderTableId = 1L;
        final OrderRequest orderRequest = new OrderRequest(orderTableId, null, Collections.singletonList(new OrderLineItemRequest()));
        final OrderTable orderTable = new OrderTable(orderTableId, null, 10, false);

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));

        // when
        Throwable thrown = catchThrowable(() -> orderService.create(orderRequest));

        // then
        assertThat(thrown).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("주문 라인 아이템 갯수만큼 메뉴 테이블에 메뉴가 존재해야 한다.")
    @Test
    void 주문_라인_아이템_메뉴_갯수_불일치_예외() {
        // given
        final Long orderTableId = 1L;
        final Long menuId = 1L;
        final Long menuQuantity = 2L;
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menuId, menuQuantity);
        final OrderRequest orderRequest = new OrderRequest(orderTableId, null, Collections.singletonList(orderLineItemRequest));
        final OrderTable orderTable = new OrderTable(orderTableId, null, 10, false);

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.of(orderTable));
        given(menuRepository.findById(menuId)).willReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> orderService.create(orderRequest));

        // then
        assertThat(thrown).isInstanceOf(NotFoundException.class)
                .hasMessage("해당 메뉴를 찾을 수 없습니다.");
    }

    @DisplayName("주문에 해당하는 주문 테이블이 없을 경우 예외가 발생한다.")
    @Test
    void 주문에_해당하는_주문_테이블_미존재_예외() {
        // given
        final Long orderTableId = 1L;
        final Long menuId = 1L;
        final Long menuQuantity = 2L;
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menuId, menuQuantity);
        final OrderRequest orderRequest = new OrderRequest(orderTableId, null, Collections.singletonList(orderLineItemRequest));

        given(orderTableRepository.findById(orderTableId)).willReturn(Optional.empty());

        // when
        Throwable thrown = catchThrowable(() -> orderService.create(orderRequest));

        // then
        assertThat(thrown).isInstanceOf(NotFoundException.class)
                .hasMessage("해당 주문 테이블을 찾을 수 없습니다.");
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void 주문_목록_조회() {
        // given
        Order firstOrder = Order.of(OrderTable.of(10, false));
        Order secondOrder = Order.of(OrderTable.of(5, false));
        given(orderRepository.findAll()).willReturn(Arrays.asList(firstOrder, secondOrder));

        // when
        List<OrderResponse> response = orderService.list();

        // then
        assertThat(response.size()).isEqualTo(2);
    }
}
