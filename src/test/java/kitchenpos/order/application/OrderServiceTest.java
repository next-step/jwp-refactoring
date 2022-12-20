package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 Business Object 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderValidator orderValidator;

    @InjectMocks
    private OrderService orderService;

    private Long 주문_id;
    private OrderTable 주문_테이블;
    private OrderLineItem 주문_아이템_1;
    private OrderLineItem 주문_아이템_2;
    private List<OrderLineItem> 주문_아이템_목록;
    private Order 주문;

    @BeforeEach
    void setUp() {

        Long 떡튀순_id = 1L;
        Long 떡튀순_곱배기_id = 2L;
        Product 떡볶이 = new Product(1L, "떡볶이", new ProductPrice(4500));
        Product 튀김 = new Product(2L, "튀김", new ProductPrice(2500));
        Product 순대 = new Product(3L, "순대", new ProductPrice(4000));

        MenuProduct 떡튀순_상품_떡볶이 = new MenuProduct(1L, null, 떡볶이.getId(), 1);
        MenuProduct 떡튀순_상품_튀김 = new MenuProduct(2L, null, 튀김.getId(), 1);
        MenuProduct 떡튀순_상품_순대 = new MenuProduct(3L, null, 순대.getId(), 1);
        MenuProduct 떡튀순_곱배기_상품_떡볶이 = new MenuProduct(4L, null, 떡볶이.getId(), 2);

        List<MenuProduct> 떡튀순_상품_목록 = Arrays.asList(떡튀순_상품_떡볶이, 떡튀순_상품_튀김, 떡튀순_상품_순대);
        List<MenuProduct> 떡튀순_곱배기_상품_목록 = Arrays.asList(떡튀순_곱배기_상품_떡볶이, 떡튀순_상품_튀김, 떡튀순_상품_순대);

        MenuGroup 세트 = new MenuGroup(1L, "세트");
        Menu 떡튀순 = new Menu(떡튀순_id, "떡튀순", new MenuPrice(10000), 세트.getId(), new MenuProducts(떡튀순_상품_목록));
        Menu 떡튀순_곱배기 = new Menu(떡튀순_곱배기_id, "떡튀순_곱배기", new MenuPrice(10000), 세트.getId(),
                new MenuProducts(떡튀순_곱배기_상품_목록));

        주문_id = 1L;
        주문_테이블 = new OrderTable(1L, null, new NumberOfGuests(0), false);
        주문_아이템_1 = new OrderLineItem(떡튀순.getId(), 2);
        주문_아이템_2 = new OrderLineItem(떡튀순_곱배기.getId(), 1);
        주문_아이템_목록 = Arrays.asList(주문_아이템_1, 주문_아이템_2);
        주문 = new Order(주문_테이블.getId(), OrderStatus.COOKING);
        주문_아이템_목록.forEach(orderLineItem -> 주문.addOrderLineItem(orderLineItem));
    }

    @DisplayName("주문 등록")
    @Test
    void 주문_등록() {
        OrderRequest request = OrderRequest.from(주문);
        when(orderRepository.save(request.toOrder())).thenReturn(request.toOrder());

        OrderResponse 등록된_주문 = orderService.create(request);

        verify(orderValidator).validateBeforeCreateOrder(any(Order.class));
        assertAll(
                () -> assertThat(등록된_주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(등록된_주문.getOrderLineItems().get(0).getMenuId())
                        .isEqualTo(주문_아이템_1.getMenuId()),
                () -> assertThat(등록된_주문.getOrderLineItems().get(0).getQuantity())
                        .isEqualTo(주문_아이템_1.getQuantityValue()),
                () -> assertThat(등록된_주문.getOrderLineItems().get(1).getMenuId())
                        .isEqualTo(주문_아이템_2.getMenuId()),
                () -> assertThat(등록된_주문.getOrderLineItems().get(1).getQuantity())
                        .isEqualTo(주문_아이템_2.getQuantityValue())
        );
    }

    @DisplayName("주문 조회")
    @Test
    void 주문_조회() {
        Order 주문2 = new Order(2L, 주문_테이블.getId(), OrderStatus.COOKING, null, 주문_아이템_목록);
        List<Order> 등록된_주문_목록 = Arrays.asList(주문, 주문2);
        when(orderRepository.findAll()).thenReturn(등록된_주문_목록);

        List<OrderResponse> 조회된_주문_목록 = orderService.list();

        assertThat(조회된_주문_목록).hasSize(등록된_주문_목록.size());
    }

    @DisplayName("주문 상태변경")
    @Test
    void 주문_상태변경() {
        Order 변경할_주문 = new Order(주문_id, 주문_테이블.getId(), OrderStatus.MEAL, null, 주문_아이템_목록);
        when(orderRepository.findById(주문_id)).thenReturn(Optional.of(주문));

        //조리 -> 식사
        OrderResponse 상태_변경된_주문 = orderService.changeOrderStatus(주문_id, OrderRequest.from(변경할_주문));
        assertThat(상태_변경된_주문.getOrderStatus()).isEqualTo(변경할_주문.getOrderStatus().name());

        //식사 -> 조리
        변경할_주문.updateOrderStatus(OrderStatus.COOKING);
        OrderResponse 상태_변경된_주문2 = orderService.changeOrderStatus(주문_id, OrderRequest.from(변경할_주문));
        assertThat(상태_변경된_주문2.getOrderStatus()).isEqualTo(변경할_주문.getOrderStatus().name());

        //조리 -> 계산 완료
        변경할_주문.updateOrderStatus(OrderStatus.COMPLETION);
        OrderResponse 상태_변경된_주문3 = orderService.changeOrderStatus(주문_id, OrderRequest.from(변경할_주문));
        assertThat(상태_변경된_주문3.getOrderStatus()).isEqualTo(변경할_주문.getOrderStatus().name());
    }

    @DisplayName("등록되지 않은 주문의 상태변경 시 예외처리")
    @Test
    void 동록안된_주문_상태변경_예외처리() {
        Long 변경할_주문_id = 3L;
        Order 변경할_주문 = new Order(변경할_주문_id, 주문_테이블.getId(), OrderStatus.MEAL, null, 주문_아이템_목록);

        assertThatThrownBy(() -> orderService.changeOrderStatus(변경할_주문_id, OrderRequest.from(변경할_주문))).isInstanceOf(
                IllegalArgumentException.class);
    }
}