package kitchenpos.order.application;

import static kitchenpos.menu.application.MenuGroupServiceTest.메뉴_그룹_생성;
import static kitchenpos.menu.application.MenuServiceTest.메뉴_상품_생성;
import static kitchenpos.menu.application.MenuServiceTest.메뉴_생성;
import static kitchenpos.product.application.ProductServiceTest.상품_생성;
import static kitchenpos.table.application.TableServiceTest.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
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
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private MenuService menuService;

    @InjectMocks
    private OrderService orderService;

    private Order 주문;
    private OrderTable 주문_테이블;
    private OrderLineItem 주문_목록_추천_치킨;

    private Product 후라이드;
    private MenuProduct 후라이드_원플원;

    private MenuGroup 추천_메뉴;
    private Menu 후라이드_세트_메뉴;

    @BeforeEach
    void init() {
        주문_테이블 = 주문_테이블_생성(1L, 4, false);

        후라이드 = 상품_생성(1L, "후라이드", 16_000L);
        후라이드_원플원 = 메뉴_상품_생성(후라이드.getId(), 1L);

        추천_메뉴 = 메뉴_그룹_생성(1L, "추천메뉴");
        후라이드_세트_메뉴 = 메뉴_생성(1L, "후라이드세트메뉴", 16_000L, 추천_메뉴, Arrays.asList(후라이드_원플원));

        주문_목록_추천_치킨 = 주문_목록_생성(주문, 후라이드_세트_메뉴, 2);
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        // given
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(주문_목록_추천_치킨));
        주문 = 주문_생성(1L, 주문_테이블, orderLineItems);

        given(orderTableDao.findById(any())).willReturn(Optional.of(주문_테이블));
        given(menuService.countByIdIn(anyList())).willReturn(1L);
        given(menuService.findMenuById(any())).willReturn(후라이드_세트_메뉴);
        given(orderDao.save(any(Order.class))).willReturn(주문);

        // when
        OrderResponse savedOrder = orderService.create(new OrderRequest(주문));

        // then
        assertAll(
            () -> assertThat(savedOrder).isNotNull(),
            () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    @Test
    @DisplayName("존재하지 않은 주문 목록으로 주문을 생성할 경우 - 오류")
    void createOrderIfNonExistentMenu() {
        // given
        Menu 존재하지_않는_메뉴 = 메뉴_생성(3L, "존재하지않는메뉴", 3000L, null, Arrays.asList(후라이드_원플원));
        OrderLineItem 존재하지_않는_메뉴의_주문_목록 = 주문_목록_생성(주문, 존재하지_않는_메뉴, 2);
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(존재하지_않는_메뉴의_주문_목록));
        주문 = 주문_생성(1L, 주문_테이블, orderLineItems);

        // when then
        assertThatThrownBy(() -> orderService.create(new OrderRequest(주문)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않은 주문 테이블로 주문을 생성할 경우 - 오류")
    void createOrderIfNonExistentOrderTable() {
        // given
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(주문_목록_추천_치킨));
        주문 = 주문_생성(1L, 주문_테이블, orderLineItems);

        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> orderService.create(new OrderRequest(주문)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    void findAll() {
        // given
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(주문_목록_추천_치킨));
        주문 = 주문_생성(1L, 주문_테이블, orderLineItems);
        given(orderDao.findAll()).willReturn(Arrays.asList(주문));

        // when
        List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("주문의 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(주문_목록_추천_치킨));
        Order 저장된_주문 = 주문_생성(1L, 주문_테이블, orderLineItems);
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(OrderStatus.MEAL.name());

        given(orderDao.findById(저장된_주문.getId())).willReturn(Optional.of(저장된_주문));

        // when
        OrderResponse changedOrder = orderService.changeOrderStatus(저장된_주문.getId(), orderStatusRequest);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(orderStatusRequest.getOrderStatus());
    }

    @Test
    @DisplayName("존재하지 않는 주문의 상태를 변경할 경우 - 오류")
    void changeOrderStatusIfNonExistentOrder() {
        // given
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(주문_목록_추천_치킨));
        Order 없는_주문 = 주문_생성(1L, 주문_테이블, OrderStatus.COOKING, orderLineItems);

        given(orderDao.findById(없는_주문.getId())).willReturn(Optional.empty());

        // when then
        assertThatThrownBy(() -> orderService.changeOrderStatus(없는_주문.getId(), new OrderStatusRequest(OrderStatus.MEAL.name())))
            .isInstanceOf(IllegalArgumentException.class);
    }

    public static Order 주문_생성(Long id, OrderTable orderTable, OrderLineItems orderLineItems) {
        return new Order(id, orderTable, orderLineItems);
    }

    public static Order 주문_생성(Long id, OrderTable orderTable, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        return new Order(id, orderTable, orderStatus, orderLineItems);
    }

    public static OrderLineItem 주문_목록_생성(Order order, Menu menu, int quantity) {
        return new OrderLineItem(order, menu, quantity);
    }
}
