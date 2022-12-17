package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.EmptyOrderLineItemException;
import kitchenpos.order.exception.EmptyOrderTableException;
import kitchenpos.order.exception.OrderStatusChangeException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderValidator orderValidator;

    @InjectMocks
    private OrderService orderService;

    private Menu 후라이드치킨;
    private OrderTable 주문_테이블;
    private OrderTable 비어있는_주문_테이블;
    private Order 주문;
    private Order 계산완료_주문;
    private OrderLineItem 주문항목;
    private OrderRequest 주문요청;
    private OrderRequest 주문항목_없는_주문요청;
    private OrderRequest 메뉴등록_안된_주문요청;
    private OrderRequest 주문테이블_없는_주문요청;
    private OrderRequest 비어있는_주문_테이블_주문요청;

    @BeforeEach
    void setUp() {
        MenuGroup 두마리메뉴 = MenuGroup.of(1L, "두마리메뉴");
        Product 후라이드 = Product.of(1L, "후라이드", BigDecimal.valueOf(16_000));

        List<MenuProduct> 메뉴상품_목록 = Arrays.asList(MenuProduct.of(후라이드.getId(), 2));
        후라이드치킨 = Menu.of(1L, "후라이드치킨", BigDecimal.valueOf(16_000), 두마리메뉴.getId(), 메뉴상품_목록);

        주문_테이블 = OrderTable.of(1L, 3, false);
        비어있는_주문_테이블 = OrderTable.of(2L, 2, true);

        주문항목 = OrderLineItem.of(1L, OrderMenu.of(후라이드치킨.getId(), 후라이드치킨.getName(), 후라이드치킨.getPrice()), 2);
        List<OrderLineItem> 주문항목_목록 = Arrays.asList(주문항목);
        주문 = Order.of(1L, 주문_테이블.getId(), 주문항목_목록);

        계산완료_주문 = Order.of(2L, 주문_테이블.getId(), 주문항목_목록);
        계산완료_주문.changeOrderStatus(OrderStatus.COMPLETION);

        주문요청 = OrderRequest.of(주문_테이블.getId(), Arrays.asList(OrderLineItemRequest.of(1L, 2)));
        주문항목_없는_주문요청 = OrderRequest.of(주문_테이블.getId(), Collections.emptyList());
        메뉴등록_안된_주문요청 = OrderRequest.of(주문_테이블.getId(), Arrays.asList(OrderLineItemRequest.of(2L, 1)));
        주문테이블_없는_주문요청 = OrderRequest.of(null, Arrays.asList(OrderLineItemRequest.of(1L, 2)));
        비어있는_주문_테이블_주문요청 = OrderRequest.of(비어있는_주문_테이블.getId(), Arrays.asList(OrderLineItemRequest.of(1L, 2)));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        when(menuRepository.findById(any())).thenReturn(Optional.of(후라이드치킨));
        when(orderRepository.save(any())).thenReturn(주문);

        OrderResponse result = orderService.create(주문요청);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getOrderLineItems()).hasSize(1),
                () -> assertThat(result.getOrderLineItems())
                        .containsExactly(OrderLineItemResponse.from(주문항목))
        );
    }

    @DisplayName("주문 항목이 비어있으면 주문 생성 시 예외가 발생한다.")
    @Test
    void createException() {
        Assertions.assertThatThrownBy(() -> orderService.create(주문항목_없는_주문요청))
                .isInstanceOf(EmptyOrderLineItemException.class)
                .hasMessageStartingWith(ExceptionMessage.EMPTY_ORDER_LINE_ITEM);
    }

    @DisplayName("주문 항목이 메뉴에 등록되어 있지 않다면 주문 생성 시 예외가 발생한다.")
    @Test
    void createException2() {
        when(menuRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> orderService.create(메뉴등록_안된_주문요청))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ExceptionMessage.MENU_NOT_FOUND);
    }

    @DisplayName("주문 테이블이 등록되어 있지 않다면 주문을 생성 시 예외가 발생한다.")
    @Test
    void createException3() {
        Mockito.doThrow(new EntityNotFoundException(ExceptionMessage.ORDER_TABLE_NOT_FOUND))
                .when(orderValidator).validate(any());

        Assertions.assertThatThrownBy(() -> orderService.create(주문테이블_없는_주문요청))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ExceptionMessage.ORDER_TABLE_NOT_FOUND);
    }

    @DisplayName("주문 테이블이 빈 테이블이면 주문을 생성 시 예외가 발생한다.")
    @Test
    void createException4() {
        doThrow(new EmptyOrderTableException(ExceptionMessage.EMPTY_ORDER_TABLE))
                .when(orderValidator).validate(any());

        Assertions.assertThatThrownBy(() -> orderService.create(비어있는_주문_테이블_주문요청))
                .isInstanceOf(EmptyOrderTableException.class)
                .hasMessageStartingWith(ExceptionMessage.EMPTY_ORDER_TABLE);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(주문));

        List<OrderResponse> results = orderService.list();

        assertAll(
                () -> Assertions.assertThat(results).hasSize(1),
                () -> assertThat(results.get(0)).isEqualTo(OrderResponse.from(주문)),
                () -> assertThat(results.get(0).getOrderLineItems())
                        .containsExactly(OrderLineItemResponse.from(주문항목))
        );
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        when(orderRepository.findById(any())).thenReturn(Optional.of(주문));

        OrderResponse result = orderService.changeOrderStatus(주문.getId(), OrderStatus.MEAL);

        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL)
        );
    }

    @DisplayName("주문이 없으면 주문의 상태 변경 시 예외가 발생한다.")
    @Test
    void changeOrderStatusException() {
        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(0L, OrderStatus.MEAL))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageStartingWith(ExceptionMessage.ORDER_NOT_FOUND);
    }

    @DisplayName("주문 상태가 계산 완료이면 주문의 상태 변경 시 예외가 발생한다.")
    @Test
    void changeOrderStatusException2() {
        when(orderRepository.findById(any())).thenReturn(Optional.of(계산완료_주문));

        Long orderId = 계산완료_주문.getId();
        Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, OrderStatus.MEAL))
                .isInstanceOf(OrderStatusChangeException.class)
                .hasMessageStartingWith(ExceptionMessage.ORDER_STATUS_CHANGE);
    }
}
