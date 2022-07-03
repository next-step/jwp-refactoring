package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    MenuRepository menuRepository;
    @Mock
    OrderLineItemRepository orderLineItemRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    OrderService orderService;

    List<OrderLineItem> orderLineItems;

    Order order;

    @BeforeEach
    void setUp() {
        orderLineItems = new ArrayList<>();
        order = new Order();
        order.setId(1L);

    }

    @Test
    @DisplayName("빈 주문 내역 에러 반환")
    public void emptyOrderLinesCreate() {
        order.setOrderLineItems(orderLineItems);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 내역의 개수는 메뉴의 개수와 동일하지 않으면 에러 반환")
    public void orderLineCountNotEqualsMenuCount() {
        orderLineItems.add(new OrderLineItem(1L, null, new Menu(), 1));
        orderLineItems.add(new OrderLineItem(2L, null, new Menu(), 2));

        order.setOrderLineItems(orderLineItems);

        given(menuRepository.countByIdIn(any(List.class))).willReturn(3L);
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 테이블에서 주문 시 에러 반환")
    public void orderTableNotExists() {
        order.setOrderTableId(1L);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블에서 주문 시 에러 반환")
    public void orderEmptyTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(true);

        order.setOrderTableId(1L);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 정상 저장 후 COOKING 초기 상태 확인")
    public void orderSuccessSave() {
        Order order = new Order();

        OrderLineItem orderLineItem = new OrderLineItem(1L, order, new Menu(), 1);
        orderLineItems.add(orderLineItem);

        OrderTable orderTable = new OrderTable(1L, null, 3, false);

        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(orderTable.getId());

        given(menuRepository.countByIdIn(any(List.class))).willReturn((long) orderLineItems.size());
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        given(orderRepository.save(order)).willReturn(order);

        Order savedOrder = orderService.create(order);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    @DisplayName("전체 주문 내역 보기")
    public void listOrders() {
        Order orderCooking = new Order(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(), null);
        Order orderMeal = new Order(2L, 2L, OrderStatus.MEAL, LocalDateTime.now(), null);

        given(orderRepository.findAll()).willReturn(Arrays.asList(orderCooking, orderMeal));

        assertThat(orderService.list()).contains(orderCooking, orderMeal);
    }

    @Test
    @DisplayName("존재하지 않는 주문 상태 변경 시도 시 에러 반환")
    public void changeStatusNotExitsOrder() {
        given(orderRepository.findById(any())).willReturn(Optional.empty());

        order.setOrderStatus(OrderStatus.MEAL);

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("완료단계 주문 건 상태 변경 시도시 에러 반환")
    public void changeStatusInCompletion() {
        order.setOrderStatus(OrderStatus.COMPLETION);

        given(orderRepository.findById(order.getId())).willReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new Order())).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태 변경 정상 처리")
    public void changeStatusSuccess() {
        order.setOrderStatus(OrderStatus.COOKING);

        Order changeOrder = new Order();
        changeOrder.setOrderStatus(OrderStatus.MEAL);

        given(orderRepository.findById(order.getId())).willReturn(Optional.of(order));

        assertThat(orderService.changeOrderStatus(order.getId(), changeOrder).getOrderStatus()).isEqualTo(
                changeOrder.getOrderStatus());
    }
}