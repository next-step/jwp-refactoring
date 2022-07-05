package kitchenpos.application;

import kitchenpos.order.exception.OrderStatusException;
import kitchenpos.table.exception.OrderTableException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kitchenpos.factory.OrderFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    MenuRepository menuRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderLineItemRepository orderLineItemRepository;

    @Mock
    OrderTableRepository orderTableRepository;

    Order 내주문;

    OrderTable 주문테이블;

    OrderLineItem 주문상품1;
    OrderLineItem 주문상품2;

    MenuGroup 메뉴그룹;

    Menu 주문메뉴1;
    Menu 주문메뉴2;

    @Test
    @DisplayName("주문을 생성한다 (Happy Path)")
    void create() {
        //given
        메뉴그룹 = new MenuGroup(1L, "메뉴그룹");
        주문메뉴1 = 메뉴생성(1L, "치킨메뉴", new BigDecimal(15000), 메뉴그룹);
        주문메뉴2 = 메뉴생성(2L, "피자메뉴", new BigDecimal(20000), 메뉴그룹);
        주문테이블 = 주문테이블_생성(1L);
        주문상품1 = 주문_메뉴_생성(1L, 내주문, 주문메뉴1, 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문, 주문메뉴2, 2L);
        내주문 = new Order(1L, 주문테이블, new ArrayList<OrderLineItem>());
        OrderLineItemRequest 주문상품1Request = new OrderLineItemRequest(주문메뉴1.getId(), 1L);
        OrderLineItemRequest 주문상품2Request = new OrderLineItemRequest(주문메뉴2.getId(), 1L);
        OrderRequest 내주문Request = new OrderRequest(주문테이블.getId(), Arrays.asList(주문상품1Request, 주문상품2Request));

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문테이블));
        given(orderRepository.save(any(Order.class))).willReturn(내주문);
        given(menuRepository.findById(주문메뉴1.getId())).willReturn(Optional.of(주문메뉴1));
        given(menuRepository.findById(주문메뉴2.getId())).willReturn(Optional.of(주문메뉴2));

        //when
        OrderResponse savedOrder = orderService.create(내주문Request);

        //then
        assertAll(() -> {
            assertThat(savedOrder.getOrderLineItems().stream()
                    .map(OrderLineItemResponse::getMenuId)
                    .collect(Collectors.toList()))
                    .containsExactlyInAnyOrderElementsOf(Arrays.asList(주문상품1.getMenu().getId(), 주문상품2.getMenu().getId()));
            assertThat(savedOrder.getOrderStatus().name()).isEqualTo(OrderStatus.COOKING.name());
            assertThat(savedOrder.getOrderTableResponse().getId()).isEqualTo(주문테이블.getId());
        });
    }

    @Test
    @DisplayName("주문 메뉴가 없을 경우 주문 생성 불가")
    void createEmptyOrderLineItems() {
        //given
        메뉴그룹 = new MenuGroup(1L, "메뉴그룹");
        주문메뉴1 = 메뉴생성(1L, "치킨메뉴", new BigDecimal(15000), 메뉴그룹);
        주문메뉴2 = 메뉴생성(2L, "피자메뉴", new BigDecimal(20000), 메뉴그룹);
        주문테이블 = 주문테이블_생성(1L);
        주문상품1 = 주문_메뉴_생성(1L, 내주문, 주문메뉴1, 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문, 주문메뉴2, 2L);
        내주문 = new Order(1L, 주문테이블, Arrays.asList(주문상품1, 주문상품2));
        OrderLineItemRequest 주문상품1Request = new OrderLineItemRequest(주문메뉴1.getId(), 1L);
        OrderLineItemRequest 주문상품2Request = new OrderLineItemRequest(주문메뉴2.getId(), 1L);
        OrderRequest 내주문Request = new OrderRequest(주문테이블.getId(), null);

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문테이블));
        given(orderRepository.save(any(Order.class))).willReturn(내주문);

        //then
        assertThatThrownBy(() -> {
            orderService.create(내주문Request);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문에 포함된 주문 메뉴가 유효하지 않은 메뉴가 있을 경우 주문 생성 불가")
    void createInvalidOrderLineItems() {
        //given
        메뉴그룹 = new MenuGroup(1L, "메뉴그룹");
        주문메뉴1 = 메뉴생성(1L, "치킨메뉴", new BigDecimal(15000), 메뉴그룹);
        주문메뉴2 = 메뉴생성(2L, "피자메뉴", new BigDecimal(20000), 메뉴그룹);
        주문테이블 = 주문테이블_생성(1L);
        주문상품1 = 주문_메뉴_생성(1L, 내주문, 주문메뉴1, 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문, 주문메뉴2, 2L);
        내주문 = new Order(1L, 주문테이블, Arrays.asList(주문상품1, 주문상품2));
        OrderLineItemRequest 주문상품1Request = new OrderLineItemRequest(주문메뉴1.getId(), 1L);
        OrderLineItemRequest 주문상품2Request = new OrderLineItemRequest(주문메뉴2.getId(), 1L);
        OrderRequest 내주문Request = new OrderRequest(주문테이블.getId(), Arrays.asList(주문상품1Request, 주문상품2Request));

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문테이블));
        given(orderRepository.save(any(Order.class))).willReturn(내주문);
        given(menuRepository.findById(주문메뉴1.getId())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> {
            orderService.create(내주문Request);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 유효하지 않을 경우 주문 생성 불가")
    void createInvalidOrderTable() {
        //given
        메뉴그룹 = new MenuGroup(1L, "메뉴그룹");
        주문메뉴1 = 메뉴생성(1L, "치킨메뉴", new BigDecimal(15000), 메뉴그룹);
        주문메뉴2 = 메뉴생성(2L, "피자메뉴", new BigDecimal(20000), 메뉴그룹);
        주문테이블 = 주문테이블_생성(1L);
        주문상품1 = 주문_메뉴_생성(1L, 내주문, 주문메뉴1, 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문, 주문메뉴2, 2L);
        내주문 = new Order(1L, 주문테이블, Arrays.asList(주문상품1, 주문상품2));
        OrderLineItemRequest 주문상품1Request = new OrderLineItemRequest(주문메뉴1.getId(), 1L);
        OrderLineItemRequest 주문상품2Request = new OrderLineItemRequest(주문메뉴2.getId(), 1L);
        OrderRequest 내주문Request = new OrderRequest(주문테이블.getId(), Arrays.asList(주문상품1Request, 주문상품2Request));

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> {
            orderService.create(내주문Request);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문한 주문 테이블이 비어있는 경우 주문 생성 불가")
    void createIsEmptyOrderTable() {
        //given
        메뉴그룹 = new MenuGroup(1L, "메뉴그룹");
        주문메뉴1 = 메뉴생성(1L, "치킨메뉴", new BigDecimal(15000), 메뉴그룹);
        주문메뉴2 = 메뉴생성(2L, "피자메뉴", new BigDecimal(20000), 메뉴그룹);
        주문테이블 = 주문테이블_생성(1L, 2, true);
        주문상품1 = 주문_메뉴_생성(1L, 내주문, 주문메뉴1, 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문, 주문메뉴2, 2L);
        내주문 = new Order(1L, 주문테이블, Arrays.asList(주문상품1, 주문상품2));
        OrderLineItemRequest 주문상품1Request = new OrderLineItemRequest(주문메뉴1.getId(), 1L);
        OrderLineItemRequest 주문상품2Request = new OrderLineItemRequest(주문메뉴2.getId(), 1L);
        OrderRequest 내주문Request = new OrderRequest(주문테이블.getId(), Arrays.asList(주문상품1Request, 주문상품2Request));

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문테이블));

        //then
        assertThatThrownBy(() -> {
            orderService.create(내주문Request);
        }).isInstanceOf(OrderTableException.class)
        .hasMessageContaining(OrderTableException.ORDER_TABLE_IS_EMPTY_MSG);
    }

    @Test
    @DisplayName("주문리스트를 조회한다 (Happy Path)")
    void list() {
        //given
        메뉴그룹 = new MenuGroup(1L, "메뉴그룹");
        주문테이블 = 주문테이블_생성(1L);
        주문메뉴1 = 메뉴생성(1L, "치킨메뉴", new BigDecimal(15000), 메뉴그룹);
        주문메뉴2 = 메뉴생성(2L, "피자메뉴", new BigDecimal(20000), 메뉴그룹);
        주문상품1 = 주문_메뉴_생성(1L, 내주문, 주문메뉴1, 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문, 주문메뉴2, 2L);
        내주문 = new Order(1L, 주문테이블, Arrays.asList(주문상품1, 주문상품2));
        given(orderRepository.findAll()).willReturn(Arrays.asList(내주문));

        //when
        List<OrderResponse> orders = orderService.list();

        //then
        assertAll(() -> {
            assertThat(orders.stream()
                    .map(OrderResponse::getOrderStatus)
                    .collect(Collectors.toList()))
                    .containsExactlyInAnyOrderElementsOf(Arrays.asList(OrderStatus.COOKING));
            assertThat(orders.stream()
                    .map(OrderResponse::getOrderTableResponse)
                    .map(OrderTableResponse::getId)
                    .collect(Collectors.toList()))
                    .containsExactlyInAnyOrderElementsOf(Arrays.asList(주문테이블.getId()));
        });
    }

    @Test
    @DisplayName("주문상태를 변경한다 (Happy Path)")
    void changeOrderStatus() {
        //given
        메뉴그룹 = new MenuGroup(1L, "메뉴그룹");
        주문테이블 = 주문테이블_생성(1L);
        주문메뉴1 = 메뉴생성(1L, "치킨메뉴", new BigDecimal(15000), 메뉴그룹);
        주문메뉴2 = 메뉴생성(2L, "피자메뉴", new BigDecimal(20000), 메뉴그룹);
        주문상품1 = 주문_메뉴_생성(1L, 내주문, 주문메뉴1, 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문, 주문메뉴2, 2L);
        내주문 = new Order(1L, 주문테이블, Arrays.asList(주문상품1, 주문상품2));
        OrderStatus 변경주문상태 = OrderStatus.MEAL;
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(내주문));

        //when
        OrderResponse changedOrder = orderService.changeOrderStatus(내주문.getId(), 변경주문상태);

        //then
        assertThat(changedOrder.getId()).isEqualTo(내주문.getId());
        assertThat(changedOrder.getOrderStatus()).isEqualTo(변경주문상태);
    }

    @Test
    @DisplayName("변경하려는 주문이 유효하지 않을 때 주문상태 변경불가")
    void changeOrderStatusInvalidOrder() {
        //given
        주문테이블 = 주문테이블_생성(1L);
        메뉴그룹 = new MenuGroup(1L, "메뉴그룹");
        주문상품1 = 주문_메뉴_생성(1L, 내주문, 주문메뉴1, 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문, 주문메뉴2, 2L);
        내주문 = new Order(1L, 주문테이블, Arrays.asList(주문상품1, 주문상품2));
        OrderStatus 변경주문상태 = OrderStatus.MEAL;
        given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(내주문.getId(), 변경주문상태);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("변경하려는 주문이 완료 상태일 때 주문상태 변경불가")
    void changeOrderStatusAlreadyCompleted() {
        //given
        주문테이블 = 주문테이블_생성(1L);
        메뉴그룹 = new MenuGroup(1L, "메뉴그룹");
        주문메뉴1 = 메뉴생성(1L, "치킨메뉴", new BigDecimal(15000), 메뉴그룹);
        주문메뉴2 = 메뉴생성(2L, "피자메뉴", new BigDecimal(20000), 메뉴그룹);
        주문상품1 = 주문_메뉴_생성(1L, 내주문, 주문메뉴1, 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문, 주문메뉴2, 2L);
        내주문 = new Order(1L, 주문테이블, Arrays.asList(주문상품1, 주문상품2));
        내주문.setOrderStatus(OrderStatus.COMPLETION);
        OrderStatus 변경주문상태 = OrderStatus.MEAL;
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(내주문));

        //then
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(내주문.getId(), 변경주문상태);
        }).isInstanceOf(OrderStatusException.class)
            .hasMessageContaining(OrderStatusException.COMPLETE_DOES_NOT_CHANGE_MSG);
    }
}
