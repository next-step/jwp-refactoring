package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.product.domain.Product;
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
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderLineItemRepository orderLineItemRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    Long 주문_id;
    OrderTable 주문_테이블;
    OrderLineItem 주문_아이템_1;
    OrderLineItem 주문_아이템_2;
    List<OrderLineItem> 주문_아이템_목록;
    Order 주문;

    @BeforeEach
    void setUp() {

        Long 떡튀순_id = 1L;
        Long 떡튀순_곱배기_id = 2L;
        Product 떡볶이 = new Product(1L, "떡볶이", BigDecimal.valueOf(4500));
        Product 튀김 = new Product(2L, "튀김", BigDecimal.valueOf(2500));
        Product 순대 = new Product(3L, "순대", BigDecimal.valueOf(4000));

        MenuProduct 떡튀순_상품_떡볶이 = new MenuProduct(1L, null, 떡볶이.getId(), 1);
        MenuProduct 떡튀순_상품_튀김 = new MenuProduct(2L, null, 튀김.getId(), 1);
        MenuProduct 떡튀순_상품_순대 = new MenuProduct(3L, null, 순대.getId(), 1);
        MenuProduct 떡튀순_곱배기_상품_떡볶이 = new MenuProduct(4L, null, 떡볶이.getId(), 2);

        List<MenuProduct> 떡튀순_상품_목록 = Arrays.asList(떡튀순_상품_떡볶이, 떡튀순_상품_튀김, 떡튀순_상품_순대);
        List<MenuProduct> 떡튀순_곱배기_상품_목록 = Arrays.asList(떡튀순_곱배기_상품_떡볶이, 떡튀순_상품_튀김, 떡튀순_상품_순대);

        MenuGroup 세트 = new MenuGroup(1L, "세트");
        Menu 떡튀순 = new Menu(떡튀순_id, "떡튀순", BigDecimal.valueOf(10000), 세트.getId(), 떡튀순_상품_목록);
        Menu 떡튀순_곱배기 = new Menu(떡튀순_곱배기_id, "떡튀순_곱배기", BigDecimal.valueOf(10000), 세트.getId(), 떡튀순_곱배기_상품_목록);

        주문_id = 1L;
        주문_테이블 = new OrderTable(1L, null, 0, false);
        주문_아이템_1 = new OrderLineItem(떡튀순.getId(), 2);
        주문_아이템_2 = new OrderLineItem(떡튀순_곱배기.getId(), 1);
        주문_아이템_목록 = Arrays.asList(주문_아이템_1, 주문_아이템_2);
        주문 = new Order(주문_테이블.getId(), OrderStatus.COOKING.name());
        주문_아이템_목록.forEach(orderLineItem -> 주문.addOrderLineItem(orderLineItem));
    }

    @DisplayName("주문 등록")
    @Test
    void 주문_등록() {
        OrderRequest request = OrderRequest.from(주문);
        when(menuRepository.countByIdIn(메뉴_Id_목록(주문_아이템_목록))).thenReturn(주문_아이템_목록.size());
        when(orderTableRepository.findById(주문_테이블.getId())).thenReturn(Optional.of(주문_테이블));
        when(orderRepository.save(request.toOrder())).thenReturn(request.toOrder());
        when(orderLineItemRepository.save(주문_아이템_1)).thenReturn(주문_아이템_1);
        when(orderLineItemRepository.save(주문_아이템_2)).thenReturn(주문_아이템_2);

        OrderResponse 등록된_주문 = orderService.create(request);

        assertAll(
                () -> assertThat(등록된_주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(등록된_주문.getOrderLineItems().get(0).getMenuId())
                        .isEqualTo(주문_아이템_1.getMenuId()),
                () -> assertThat(등록된_주문.getOrderLineItems().get(0).getQuantity())
                        .isEqualTo(주문_아이템_1.getQuantity()),
                () -> assertThat(등록된_주문.getOrderLineItems().get(1).getMenuId())
                        .isEqualTo(주문_아이템_2.getMenuId()),
                () -> assertThat(등록된_주문.getOrderLineItems().get(1).getQuantity())
                        .isEqualTo(주문_아이템_2.getQuantity())
        );
    }

    @DisplayName("빈 테이블에 대한 주문 등록 요청 시 예외처리")
    @Test
    void 빈테이블_주문_등록_예외처리() {
        OrderTable 빈_주문_테이블 = new OrderTable(1L, null, 0, true);
        Order 빈_테이블_주문 = new Order(빈_주문_테이블.getId(), OrderStatus.COOKING.name());
        주문_아이템_목록.stream().forEach(빈_테이블_주문::addOrderLineItem);
        when(menuRepository.countByIdIn(메뉴_Id_목록(주문_아이템_목록))).thenReturn(주문_아이템_목록.size());
        when(orderTableRepository.findById(빈_주문_테이블.getId())).thenReturn(Optional.of(빈_주문_테이블));

        assertThatThrownBy(() -> orderService.create(OrderRequest.from(빈_테이블_주문))).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성되지 않은 주문 테이블에 대한 주문 등록 요청 시 예외처리")
    @Test
    void 생성안된_주문_테이블_주문_등록_예외처리() {
        when(menuRepository.countByIdIn(메뉴_Id_목록(주문_아이템_목록))).thenReturn(주문_아이템_목록.size());
        when(orderTableRepository.findById(주문_테이블.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(OrderRequest.from(주문))).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴와 수량 정보 없이 주문 등록 요청 시 예외처리")
    @Test
    void 메뉴와_수량_누락_주문_등록_예외처리() {
        List<OrderLineItem> 빈_아이템_목록 = new ArrayList<>();
        Order 주문_아이템_누락된_주문 = new Order(주문_id, 주문_테이블.getId(), OrderStatus.COOKING.name(), null, 빈_아이템_목록);

        assertThatThrownBy(() -> orderService.create(OrderRequest.from(주문_아이템_누락된_주문))).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 조회")
    @Test
    void 주문_조회() {
        Order 주문2 = new Order(2L, 주문_테이블.getId(), OrderStatus.COOKING.name(), null, 주문_아이템_목록);
        List<Order> 등록된_주문_목록 = Arrays.asList(주문, 주문2);
        when(orderRepository.findAll()).thenReturn(등록된_주문_목록);
        when(orderLineItemRepository.findByOrder(주문)).thenReturn(주문_아이템_목록);
        when(orderLineItemRepository.findByOrder(주문2)).thenReturn(주문_아이템_목록);

        List<OrderResponse> 조회된_주문_목록 = orderService.list();

        assertThat(조회된_주문_목록).hasSize(등록된_주문_목록.size());
    }

    @DisplayName("주문 상태변경")
    @Test
    void 주문_상태변경() {
        Order 변경할_주문 = new Order(주문_id, 주문_테이블.getId(), OrderStatus.MEAL.name(), null, 주문_아이템_목록);
        when(orderRepository.findById(주문_id)).thenReturn(Optional.of(주문));
        when(orderLineItemRepository.findByOrder(주문)).thenReturn(주문_아이템_목록);

        //조리 -> 식사
        OrderResponse 상태_변경된_주문 = orderService.changeOrderStatus(주문_id, OrderRequest.from(변경할_주문));
        assertThat(상태_변경된_주문.getOrderStatus()).isEqualTo(변경할_주문.getOrderStatus());

        //식사 -> 조리
        변경할_주문.setOrderStatus(OrderStatus.COOKING.name());
        OrderResponse 상태_변경된_주문2 = orderService.changeOrderStatus(주문_id, OrderRequest.from(변경할_주문));
        assertThat(상태_변경된_주문2.getOrderStatus()).isEqualTo(변경할_주문.getOrderStatus());

        //조리 -> 계산 완료
        변경할_주문.setOrderStatus(OrderStatus.COMPLETION.name());
        OrderResponse 상태_변경된_주문3 = orderService.changeOrderStatus(주문_id, OrderRequest.from(변경할_주문));
        assertThat(상태_변경된_주문3.getOrderStatus()).isEqualTo(변경할_주문.getOrderStatus());
    }

    @DisplayName("등록되지 않은 주문의 상태변경 시 예외처리")
    @Test
    void 동록안된_주문_상태변경_예외처리() {
        Long 변경할_주문_id = 3L;
        Order 변경할_주문 = new Order(변경할_주문_id, 주문_테이블.getId(), OrderStatus.MEAL.name(), null, 주문_아이템_목록);
        when(orderRepository.findById(변경할_주문_id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(변경할_주문_id, OrderRequest.from(변경할_주문))).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("계산 완료 상태인 주문 상태변경 시 예외처리")
    @Test
    void 계산_완료_주문_상태변경_예외처리() {
        Order 계산_완료된_주문 = new Order(2L, 주문_테이블.getId(), OrderStatus.COMPLETION.name(), null, 주문_아이템_목록);
        Order 변경할_주문 = new Order(2L, 주문_테이블.getId(), OrderStatus.MEAL.name(), null, 주문_아이템_목록);
        when(orderRepository.findById(계산_완료된_주문.getId())).thenReturn(Optional.of(계산_완료된_주문));

        assertThatThrownBy(() -> orderService.changeOrderStatus(계산_완료된_주문.getId(), OrderRequest.from(변경할_주문))).isInstanceOf(
                IllegalArgumentException.class);
    }

    private List<Long> 메뉴_Id_목록(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

}