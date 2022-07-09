package kitchenpos.order;

import kitchenpos.application.order.OrderService;
import kitchenpos.application.order.OrderValidator;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderLineItemResponse;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.exception.order.OrderStatusException;
import kitchenpos.repository.order.OrderRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderValidator orderValidator;

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
        주문메뉴1 = 메뉴생성(1L, "치킨메뉴", new BigDecimal(15000), 메뉴그룹.getId());
        주문메뉴2 = 메뉴생성(2L, "피자메뉴", new BigDecimal(20000), 메뉴그룹.getId());
        주문테이블 = 주문테이블_생성(1L);
        주문상품1 = 주문_메뉴_생성(1L, 내주문, 주문메뉴1.getId(), 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문, 주문메뉴2.getId(), 2L);
        내주문 = new Order(1L, 주문테이블, new ArrayList<OrderLineItem>());
        OrderLineItemRequest 주문상품1Request = new OrderLineItemRequest(주문메뉴1.getId(), 1L);
        OrderLineItemRequest 주문상품2Request = new OrderLineItemRequest(주문메뉴2.getId(), 1L);
        OrderRequest 내주문Request = new OrderRequest(주문테이블.getId(), Arrays.asList(주문상품1Request, 주문상품2Request));

        given(orderValidator.tableValidIsEmpty(anyLong())).willReturn(주문테이블);
        given(orderRepository.save(any(Order.class))).willReturn(내주문);
        willDoNothing().given(orderValidator).orderLineItemsValidation(anyList());

        //when
        OrderResponse savedOrder = orderService.create(내주문Request);

        //then
        assertAll(() -> {
            assertThat(savedOrder.getOrderLineItems().stream()
                    .map(OrderLineItemResponse::getMenuId)
                    .collect(Collectors.toList()))
                    .containsExactlyInAnyOrderElementsOf(Arrays.asList(주문상품1.getMenuId(), 주문상품2.getMenuId()));
            assertThat(savedOrder.getOrderStatus().name()).isEqualTo(OrderStatus.COOKING.name());
            assertThat(savedOrder.getOrderTableId()).isEqualTo(주문테이블.getId());
        });
    }

    @Test
    @DisplayName("주문리스트를 조회한다 (Happy Path)")
    void list() {
        //given
        메뉴그룹 = new MenuGroup(1L, "메뉴그룹");
        주문테이블 = 주문테이블_생성(1L);
        주문메뉴1 = 메뉴생성(1L, "치킨메뉴", new BigDecimal(15000), 메뉴그룹.getId());
        주문메뉴2 = 메뉴생성(2L, "피자메뉴", new BigDecimal(20000), 메뉴그룹.getId());
        주문상품1 = 주문_메뉴_생성(1L, 내주문, 주문메뉴1.getId(), 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문, 주문메뉴2.getId(), 2L);
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
                    .map(OrderResponse::getOrderTableId)
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
        주문메뉴1 = 메뉴생성(1L, "치킨메뉴", new BigDecimal(15000), 메뉴그룹.getId());
        주문메뉴2 = 메뉴생성(2L, "피자메뉴", new BigDecimal(20000), 메뉴그룹.getId());
        주문상품1 = 주문_메뉴_생성(1L, 내주문, 주문메뉴1.getId(), 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문, 주문메뉴2.getId(), 2L);
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
        주문메뉴1 = 메뉴생성(1L, "치킨메뉴", new BigDecimal(15000), 메뉴그룹.getId());
        주문메뉴2 = 메뉴생성(2L, "피자메뉴", new BigDecimal(20000), 메뉴그룹.getId());
        주문상품1 = 주문_메뉴_생성(1L, 내주문, 주문메뉴1.getId(), 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문, 주문메뉴2.getId(), 2L);
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
        주문메뉴1 = 메뉴생성(1L, "치킨메뉴", new BigDecimal(15000), 메뉴그룹.getId());
        주문메뉴2 = 메뉴생성(2L, "피자메뉴", new BigDecimal(20000), 메뉴그룹.getId());
        주문상품1 = 주문_메뉴_생성(1L, 내주문, 주문메뉴1.getId(), 1L);
        주문상품2 = 주문_메뉴_생성(2L, 내주문, 주문메뉴2.getId(), 2L);
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
