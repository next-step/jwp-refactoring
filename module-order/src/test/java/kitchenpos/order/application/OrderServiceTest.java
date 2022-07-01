package kitchenpos.order.application;


import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 관련 기능")
public class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderValidator orderValidator;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    private Product 강정치킨;
    private MenuGroup 치킨메뉴;
    private Menu 추천메뉴;
    private OrderTable 빈테이블;
    private OrderTable 테이블;
    private List<OrderLineItem> 주문항목;

    @BeforeEach
    void setUp() {
        강정치킨 = 상품_등록(1L, "강정치킨", 17000);
        치킨메뉴 = 메뉴_그룹_등록(1L, "치킨메뉴");
        추천메뉴 = 메뉴_등록(1L, "추천메뉴", 강정치킨.getPriceIntValue(), 치킨메뉴.getId(),
                Arrays.asList(메뉴_상품_등록(1L, 강정치킨.getId(), 1L)));

        빈테이블 = 테이블_등록(1L, 4, true);
        테이블 = 테이블_등록(1L, 4, false);
        주문항목 = Arrays.asList(주문_항목_등록(1L, 추천메뉴.getId(), 1L));
    }

    @Test
    @DisplayName("주문을 등록할 때 주문 항목이 비어있으면 실패한다.")
    void createWithNoOrder() {
        // given
        OrderRequest order = 주문_등록_요청(빈테이블.getId(), Lists.emptyList());

        // when-then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 등록할 때 메뉴가 등록 되어있지 않으면 실패한다.")
    void createWithInvalidMenuId() {
        // given
        OrderRequest order = 주문_등록_요청(빈테이블.getId(),
                Arrays.asList(주문_항목_등록_요청(추천메뉴.getId(), 1)));

        // when-then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 등록할 때 주문 테이블 정보가 등록 되어있지 않으면 실패한다.")
    void createWithInvalidOrderTable() {
        // given
        OrderRequest order = 주문_등록_요청(빈테이블.getId(),
                Arrays.asList(주문_항목_등록_요청(추천메뉴.getId(), 1)));

        // when-then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 등록할 때 주문 테이블이 비어있으면 실패한다.")
    void createWithEmptyOrderTable() {
        // given
        OrderRequest order = 주문_등록_요청(빈테이블.getId(),
                Arrays.asList(주문_항목_등록_요청(추천메뉴.getId(), 1)));
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(빈테이블));

        // when-then
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 등록한다.")
    void createOrder() {
        // given
        OrderRequest order = 주문_등록_요청(빈테이블.getId(),
                Arrays.asList(주문_항목_등록_요청(추천메뉴.getId(), 1)));

        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(테이블));
        given(orderRepository.save(any())).willReturn(주문_등록(1L, 빈테이블.getId(), 주문항목));

        // when-the
        assertThat(orderService.create(order).getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    @DisplayName("주문을 조회한다.")
    void list() {
        // given
        Order order = 주문_등록(1L, 빈테이블.getId(), 주문항목);
        given(orderRepository.findAll()).willReturn(Arrays.asList(order));

        // when
        List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getOrderLineItems()).hasSize(1);
    }

    @Test
    @DisplayName("주문 상태가 완료이면 상태 변경에 실패한다.")
    void changeOrderStatusOfCompleted() {
        // given
        Order order = 주문_등록(1L, 빈테이블.getId(), 주문항목);
        order.changeOrderStatus(OrderStatus.COMPLETION);
        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        // when-then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), OrderStatusRequest.of(OrderStatus.COMPLETION)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        Order order = 주문_등록(1L, 빈테이블.getId(), 주문항목);
        order.changeOrderStatus(OrderStatus.COOKING);

        Order newOrder = 주문_등록(1L, 빈테이블.getId(), 주문항목);
        newOrder.changeOrderStatus(OrderStatus.MEAL);

        given(orderRepository.findById(any())).willReturn(Optional.of(order));
        given(orderRepository.save(any())).willReturn(newOrder);

        // when-then
        assertThat(orderService.changeOrderStatus(order.getId(), OrderStatusRequest.of(OrderStatus.MEAL)).getOrderStatus())
                .isEqualTo(OrderStatus.MEAL);
    }

    public static Product 상품_등록(Long id, String name, Integer price) {
        return Product.of(name, price);
    }

    public static Menu 메뉴_등록(Long id, String name, Integer price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return Menu.of(name, price, menuGroupId, menuProducts);
    }

    public static MenuGroup 메뉴_그룹_등록(Long id, String name) {
        return MenuGroup.of(name);
    }

    public static MenuProduct 메뉴_상품_등록(Long seq, Long productId, long quantity) {
        return MenuProduct.of(productId, quantity);
    }

    public static OrderTable 테이블_등록(long tableGroupId, int numberOfGuests, boolean empty) {
        return OrderTable.of(tableGroupId, numberOfGuests, empty);
    }

    private OrderLineItem 주문_항목_등록(Long seq, Long menuId, long quantity) {
        return OrderLineItem.of(menuId, quantity);
    }

    private Order 주문_등록(Long id, Long orderTableId, List<OrderLineItem> orderLineItems) {
        return Order.of(orderTableId, orderLineItems);
    }

    private OrderLineItemRequest 주문_항목_등록_요청(Long menuId, Integer quantity) {
        return OrderLineItemRequest.of(menuId, quantity);
    }

    private OrderRequest 주문_등록_요청(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return OrderRequest.of(orderTableId, orderLineItems);
    }
}
