package kitchenpos.order.application;

import static kitchenpos.menu.domain.MenuTestFixture.*;
import static kitchenpos.order.domain.OrderLineItemTestFixture.*;
import static kitchenpos.order.domain.OrderTestFixture.*;
import static kitchenpos.table.domain.OrderTableTestFixture.orderTable;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@DisplayName("주문 비즈니스 로직 테스트")
@Import(AuditingEntityListener.class)
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderValidator orderValidator;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문 등록시 주문 항목은 필수이다.")
    void createOrderByOrderLineItemIsNull() {
        // given
        OrderTable orderTable = orderTable(1L, null, 3, false);
        OrderRequest orderRequest = orderRequest(orderTable.id(), Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("주문 항목은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("주문 등록시 주문 항목은 모두 등록된 메뉴여야 한다.")
    void createOrderByCreatedMenu() {
        // given
        OrderTable orderTable = orderTable(1L, null, 3, false);
        OrderLineItemRequest orderLineItemRequest = orderLineItemRequest(1L, 1L);
        OrderLineItemRequest orderLineItemRequest2 = orderLineItemRequest(2L, 1L);
        OrderRequest orderRequest = orderRequest(orderTable.id(), Arrays.asList(orderLineItemRequest, orderLineItemRequest2));
        given(menuRepository.countAllByIdIn(Arrays.asList(orderLineItemRequest.getMenuId(), orderLineItemRequest2.getMenuId())))
                .willReturn(2L);
        given(menuRepository.findById(orderLineItemRequest.getMenuId())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("메뉴가 존재하지 않습니다. ID : 1");
    }

    @Test
    @DisplayName("주문을 등록한다.")
    void createOrder() {
        // given
        OrderTable orderTable = orderTable(1L, null, 3, false);
        OrderLineItemRequest orderLineItemRequest = orderLineItemRequest(1L, 1L);
        OrderLineItemRequest orderLineItemRequest2 = orderLineItemRequest(2L, 1L);
        OrderRequest orderRequest = orderRequest(orderTable.id(), Arrays.asList(orderLineItemRequest, orderLineItemRequest2));
        OrderLineItem orderLineItem = orderLineItem(1L, 짜장_탕수육_주문_세트, 1L);
        OrderLineItem orderLineItem2 = orderLineItem(2L, 짬뽕2_탕수육_주문_세트, 1L);
        Order order = order(1L, orderTable.id(), Arrays.asList(orderLineItem, orderLineItem2));
        given(menuRepository.countAllByIdIn(Arrays.asList(짜장_탕수육_주문_세트.id(), 짬뽕2_탕수육_주문_세트.id()))). willReturn(2L);
        given(menuRepository.findById(짜장_탕수육_주문_세트.id())).willReturn(Optional.of(짜장_탕수육_세트));
        given(menuRepository.findById(짬뽕2_탕수육_주문_세트.id())).willReturn(Optional.of(짬뽕2_탕수육_세트));
        given(orderRepository.save(any())).willReturn(order);

        // when
        OrderResponse actual = orderService.create(orderRequest);

        // then
        assertAll(
                () -> assertThat(actual).isInstanceOf(OrderResponse.class),
                () -> assertThat(actual.getOrderTableId()).isEqualTo(1L),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(actual.getOrderLineItems().stream()
                        .map(OrderLineItemResponse::getMenuId)
                        .collect(Collectors.toList())).containsExactly(1L, 2L)

        );
    }

    @Test
    @DisplayName("주문 상태를 변경하려면 주문이 등록되어야 한다.")
    void updateOrderStatusByNoneOrdered() {
        // given
        OrderStatusRequest orderStatusRequest = orderStatusRequest(OrderStatus.MEAL.name());
        given(orderRepository.findById(5L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(5L, orderStatusRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("주문이 존재하지 않습니다. ID : 5");
    }

    @Test
    @DisplayName("주문 상태를 변경하려면 주문상태가 완료가 아니어야 한다.")
    void updateOrderStatusByOrderStatusIsNotEqualToCompleted() {
        // given
        OrderTable orderTable = orderTable(1L, null, 3, false);
        OrderStatusRequest orderStatusRequest = orderStatusRequest(OrderStatus.MEAL.name());
        OrderLineItem orderLineItem = orderLineItem(1L, 짜장_탕수육_주문_세트, 1L);
        OrderLineItem orderLineItem2 = orderLineItem(2L, 짬뽕2_탕수육_주문_세트, 1L);
        Order order = order(1L, orderTable.id(), Arrays.asList(orderLineItem, orderLineItem2));
        given(orderRepository.save(any())).willReturn(order);
        given(orderRepository.findById(order.id())).willReturn(Optional.of(order));
        OrderStatusRequest orderStatusRequest2 = orderStatusRequest(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(orderTable.id(), orderStatusRequest2);

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderTable.id(), orderStatusRequest))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("이미 완료된 주문입니다.");
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void updateOrderStatus() {
        // given
        OrderTable orderTable = orderTable(1L, null, 3, false);
        OrderStatusRequest orderStatusRequest = orderStatusRequest(OrderStatus.MEAL.name());
        OrderLineItem orderLineItem = orderLineItem(1L, 짜장_탕수육_주문_세트, 1L);
        OrderLineItem orderLineItem2 = orderLineItem(2L, 짬뽕2_탕수육_주문_세트, 1L);
        Order order = order(1L, orderTable.id(), Arrays.asList(orderLineItem, orderLineItem2));
        given(orderRepository.save(any())).willReturn(order);
        given(orderRepository.findById(order.id())).willReturn(Optional.of(order));

        // when
        OrderResponse actual = orderService.changeOrderStatus(order.id(), orderStatusRequest);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("주문 목록을 조회하면 주문 목록이 반환된다.")
    void test() {
        // given
        OrderTable orderTable = orderTable(1L, null, 3, false);
        OrderTable orderTable2 = orderTable(2L, null, 3, false);
        OrderLineItem orderLineItem = orderLineItem(1L, 짜장_탕수육_주문_세트, 1L);
        OrderLineItem orderLineItem2 = orderLineItem(2L, 짬뽕2_탕수육_주문_세트, 1L);
        Order order = order(1L, orderTable.id(), Collections.singletonList(orderLineItem));
        Order order2 = order(2L, orderTable2.id(), Collections.singletonList(orderLineItem2));
        given(orderRepository.findAll()).willReturn(Arrays.asList(order, order2));

        // when
        List<OrderResponse> actual = orderService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual.stream()
                        .map(OrderResponse::getId))
                        .containsExactly(1L, 2L)
        );
    }
}
