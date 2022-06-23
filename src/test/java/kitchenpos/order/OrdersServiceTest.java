package kitchenpos.order;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.OrdersRepository;
import kitchenpos.order.dto.*;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrdersServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderService orderService;

    private Product product1;
    private Product product2;
    private Orders orders;
    private OrderTable 비어있지_않은_주문_테이블;
    private OrderTable 비어있는_주문_테이블;
    private MenuGroup menuGroup;
    private Menu menu;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup(1L, "menuGroup");
        비어있지_않은_주문_테이블 = new OrderTable(1L, new TableGroup(), 5, false);
        비어있는_주문_테이블 = new OrderTable(2L, new TableGroup(), 10, true);
        product1 = new Product(1L, "product1", BigDecimal.valueOf(100));
        product2 = new Product(2L, "product2", BigDecimal.valueOf(500));
        menu = new Menu(1L, "menu1", BigDecimal.valueOf(1000), menuGroup);
        menu.add(product1, 1);
        menu.add(product2, 2);
        orders = new Orders(비어있지_않은_주문_테이블, OrderStatus.COOKING, LocalDateTime.now());
    }

    @Test
    @DisplayName("주문을 생성할 수 있다.")
    void create() {
        //given
        Orders savedOrder = new Orders(비어있지_않은_주문_테이블, OrderStatus.COOKING, LocalDateTime.now());
        savedOrder.add(menu, 1);
        given(menuRepository.countByIdIn(any())).willReturn(1L);
        given(menuRepository.findById(any())).willReturn(Optional.of(menu));
        given(orderTableRepository.findByIdAndEmptyIsFalse(any())).willReturn(Optional.of(비어있지_않은_주문_테이블));
        given(ordersRepository.save(any())).willReturn(savedOrder);

        //when
        OrderResponse response = orderService.create(
                new OrdersRequest(비어있지_않은_주문_테이블.getId(), Arrays.asList(new OrderLineItemRequest(menu.getId(), 1))));

        //then
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(response.getOrderLineItems().stream().map(OrderLineItemResponse::getQuantity).collect(Collectors.toList())).containsExactlyInAnyOrder(1L);
    }

    @Test
    @DisplayName("주문 항목의 요청 갯수와 실제 저장된 주문 항목을 조회했을 때 갯수가 다르면 주문에 실패한다.")
    void create_fail_2() {
        //given
        given(menuRepository.countByIdIn(any())).willReturn(2L);

        //then
        assertThatThrownBy(() -> orderService.create(new OrdersRequest(비어있지_않은_주문_테이블.getId(),
                Arrays.asList(new OrderLineItemRequest(menu.getId(), 1))))).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 조회 결과가 없으면 주문에 실패한다.")
    void create_fail_3() {
        //given
        given(menuRepository.countByIdIn(any())).willReturn(1L);
        given(orderTableRepository.findByIdAndEmptyIsFalse(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> orderService.create(new OrdersRequest(비어있지_않은_주문_테이블.getId(),
                Arrays.asList(new OrderLineItemRequest(menu.getId(), 1))))).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 비어있으면 주문에 실패한다.")
    void create_fail_4() {
        //given
        given(menuRepository.countByIdIn(any())).willReturn(1L);
        given(orderTableRepository.findByIdAndEmptyIsFalse(any())).willReturn(Optional.of(비어있는_주문_테이블));

        //then
        assertThatThrownBy(() -> orderService.create(new OrdersRequest(비어있는_주문_테이블.getId(),
                Arrays.asList(new OrderLineItemRequest(menu.getId(), 1))))).isExactlyInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 주문을 조회할 수 있다.")
    void list() {
        //given
        given(ordersRepository.findAll()).willReturn(Arrays.asList(orders));

        //then
        assertThat(orderService.list().stream().map(OrderResponse::getOrderStatus)
                .collect(Collectors.toList())).containsExactly(OrderStatus.COOKING);
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        //given
        given(ordersRepository.findById(any())).willReturn(Optional.of(orders));

        //when
        OrderResponse savedOrder = orderService.changeOrderStatus(0L, new OrderStatusRequest(OrderStatus.COMPLETION));

        //then
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    @DisplayName("조회되지 않는 주문은 상태를 변경할 수 없다.")
    void changeOrderStatus_failed_2() {
        //given
        given(ordersRepository.findById(any())).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(0L,
                new OrderStatusRequest(OrderStatus.COMPLETION))).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("계산 완료된 주문은 상태를 변경할 수 없다.")
    void changeOrderStatus_failed_3() {
        //given
        given(ordersRepository.findById(any())).willReturn(
                Optional.of(new Orders(비어있지_않은_주문_테이블, OrderStatus.COMPLETION, LocalDateTime.now())));

        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(0L,
                new OrderStatusRequest(OrderStatus.COMPLETION))).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
