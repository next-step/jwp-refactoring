package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.DuplicateMenuException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private MenuGroup menuGroup;
    private Menu menu;

    @BeforeEach
    void setUp() {
        order = new Order(1L, 1L);
        menuGroup = new MenuGroup(1L, "추천메뉴");
        menu = new Menu(1L, "강정치킨+강정치킨", BigDecimal.valueOf(0), menuGroup, Collections.emptyList());
    }

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void createTest() {
        // given
        OrderLineItemDto orderLineItemDto = new OrderLineItemDto(1L, 1L, 2);
        OrderRequest orderRequest = new OrderRequest(1L, Arrays.asList(orderLineItemDto));

        given(menuRepository.countByIdIn(any())).willReturn(1);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(new OrderTable(1L, 1, true)));
        given(orderRepository.save(any())).willReturn(order);

        // when
        OrderResponse createdOrder = orderService.create(orderRequest);

        // then
        assertThat(createdOrder.getId()).isEqualTo(order.getId());
        assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(createdOrder.getOrderTableId()).isEqualTo(order.getOrderTableId());
    }

    @DisplayName("주문 항목이 올바르지 않으면 등록할 수 없다 : 주문 항목은 메뉴에 존재하고 중복되지않는 메뉴이어야 한다.")
    @Test
    void createTest_duplicateMenu() {
        // given
        OrderLineItemDto orderLineItemDto1 = new OrderLineItemDto(1L, 1L, 2);
        OrderLineItemDto orderLineItemDto2 = new OrderLineItemDto(1L, 1L, 2);
        OrderRequest orderRequest = new OrderRequest(1L, Arrays.asList(orderLineItemDto1, orderLineItemDto2));

        given(menuRepository.countByIdIn(any())).willReturn(1);

        // when & then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(DuplicateMenuException.class);
    }

    @DisplayName("주문 테이블이 올바르지 않으면 등록할 수 없다 : 주문 테이블은 등록된 주문 테이블이어야 한다.")
    @Test
    void createTest_unregisteredOrderTable() {
        // given
        OrderRequest orderRequest = new OrderRequest(1L, new ArrayList<>());

        given(menuRepository.countByIdIn(any())).willReturn(order.getOrderLineItems().size());
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("주문의 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatusTest() {
        // given
        Long orderId = 1L;
        OrderRequest orderRequest = new OrderRequest(OrderStatus.MEAL.name());

        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        // when
        OrderResponse changedOrder = orderService.changeOrderStatus(orderId, orderRequest);

        // then
        assertThat(changedOrder.getId()).isEqualTo(order.getId());
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문의 목록을 조회할 수 있다.")
    @Test
    void listTest() {
        // given
        given(orderRepository.findAll()).willReturn(Arrays.asList(order));

        // when
        List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders.size()).isNotZero();
        assertThat(orders.get(0).getId()).isEqualTo(order.getId());
    }
}
