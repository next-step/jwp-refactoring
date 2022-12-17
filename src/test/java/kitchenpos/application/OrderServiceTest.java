package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.*;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderLineItemResponse;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.exception.BadRequestException;
import org.assertj.core.api.Assertions;
import org.codehaus.groovy.control.messages.ExceptionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.utils.Message.EMPTY_ORDER_TABLE;
import static kitchenpos.utils.Message.INVALID_CHANGE_ORDER_STATUS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@DisplayName("order 서비스 테스트")
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

    private Menu  honeycomboChicken;
    private OrderTable  orderTable;
    private OrderTable  emptyOrderTable;
    private Order order;
    private Order payedOrder;
    private OrderLineItem orderItems;
    private OrderRequest orderRequest;
    private OrderRequest noOrderItemRequest;
    private OrderRequest noMenuRequest;
    private OrderRequest notContainTableRequest;
    private OrderRequest emptyTableRequest;

    @BeforeEach
    void setUp() {
        MenuGroup premiumMenu = MenuGroup.of(1L, "premiumMenu");
        Product honeycombo = Product.of(1L, "honeycombo", BigDecimal.valueOf(16_000));

        List<MenuProduct> menuProductItem = Arrays.asList(MenuProduct.of(honeycombo, 2));
         honeycomboChicken = Menu.of(1L, " honeycomboChicken", BigDecimal.valueOf(16_000), premiumMenu, menuProductItem);

         orderTable = OrderTable.of(1L, 3, false);
         emptyOrderTable = OrderTable.of(2L, 2, true);

        orderItems = OrderLineItem.of(1L,  honeycomboChicken.getId(), 2);
        List<OrderLineItem> orderItems_목록 = Arrays.asList(orderItems);
        order = Order.of(1L,  orderTable, orderItems_목록);

        payedOrder = Order.of(2L,  orderTable, orderItems_목록);
        payedOrder.changeOrderStatus(OrderStatus.COMPLETION);

        orderRequest = OrderRequest.of( orderTable.getId(), Arrays.asList(OrderLineItemRequest.of(1L, 2)));
        noOrderItemRequest = OrderRequest.of( orderTable.getId(), Collections.emptyList());
        noMenuRequest = OrderRequest.of( orderTable.getId(), Arrays.asList(OrderLineItemRequest.of(2L, 1)));
        notContainTableRequest = OrderRequest.of(null, Arrays.asList(OrderLineItemRequest.of(1L, 2)));
        emptyTableRequest = OrderRequest.of( emptyOrderTable.getId(), Arrays.asList(OrderLineItemRequest.of(1L, 2)));
    }

    @DisplayName("order을 생성한다.")
    @Test
    void create() {
        when(menuRepository.findById(any())).thenReturn(Optional.of( honeycomboChicken));
        when(orderTableRepository.findById(any())).thenReturn(Optional.of( orderTable));
        when(orderRepository.save(any())).thenReturn(order);

        OrderResponse result = orderService.create(orderRequest);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getOrderLineItems()).hasSize(1),
                () -> assertThat(result.getOrderLineItems())
                        .containsExactly(OrderLineItemResponse.from(orderItems))
        );
    }

    @DisplayName("order 항목이 비어있으면 order 생성 시 예외가 발생한다.")
    @Test
    void createException() {
        Assertions.assertThatThrownBy(() -> orderService.create(noOrderItemRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("order 항목이 메뉴에 등록되어 있지 않다면 order 생성 시 예외가 발생한다.")
    @Test
    void createException2() {
        when(menuRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> orderService.create(noMenuRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("order 테이블이 등록되어 있지 않다면 order을 생성 시 예외가 발생한다.")
    @Test
    void createException3() {
        when(menuRepository.findById(any())).thenReturn(Optional.of( honeycomboChicken));
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> orderService.create(notContainTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("order 테이블이 빈 테이블이면 order을 생성 시 예외가 발생한다.")
    @Test
    void createException4() {
        when(menuRepository.findById(any())).thenReturn(Optional.of( honeycomboChicken));
        when(orderTableRepository.findById(any())).thenReturn(Optional.of( emptyOrderTable));

        Assertions.assertThatThrownBy(() -> orderService.create(emptyTableRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(EMPTY_ORDER_TABLE);
    }

    @DisplayName("order 목록을 조회한다.")
    @Test
    void list() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));

        List<OrderResponse> results = orderService.list();

        assertAll(
                () -> assertThat(results).hasSize(1),
                () -> assertThat(results.get(0)).isEqualTo(OrderResponse.from(order)),
                () -> assertThat(results.get(0).getOrderLineItems())
                        .containsExactly(OrderLineItemResponse.from(orderItems))
        );
    }

    @DisplayName("order 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        OrderResponse result = orderService.changeOrderStatus(order.getId(), OrderStatus.MEAL.name());

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name())
        );
    }

    @DisplayName("order이 없으면 order의 상태 변경 시 예외가 발생한다.")
    @Test
    void changeOrderStatusException() {
        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(0L, OrderStatus.MEAL.name()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("order 상태가 계산 완료이면 order의 상태 변경 시 예외가 발생한다.")
    @Test
    void changeOrderStatusException2() {
        when(orderRepository.findById(any())).thenReturn(Optional.of(payedOrder));

        Long orderId = payedOrder.getId();
        Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, OrderStatus.MEAL.name()))
                .isInstanceOf(BadRequestException.class)
                .hasMessageStartingWith(INVALID_CHANGE_ORDER_STATUS);
    }
}
