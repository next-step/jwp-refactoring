package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProductTestFixture;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuTestFixture;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroupTestFixture;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderMenuTestFixture;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTestFixture;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableTestFixture;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderValidator orderValidator;

    @InjectMocks
    private OrderService orderService;

    private Product 하와이안피자;
    private Product 콜라;
    private Product 피클;
    private MenuGroup 피자;
    private Menu 하와이안피자세트;
    private MenuProduct 하와이안피자상품;
    private MenuProduct 콜라상품;
    private MenuProduct 피클상품;
    private OrderTable 주문테이블;
    private Order 주문;
    private OrderLineItemRequest 하와이안피자세트주문;
    private OrderMenu 주문메뉴;

    @BeforeEach
    void setUp() {
        하와이안피자 = ProductTestFixture.create(1L, "하와이안피자", BigDecimal.valueOf(15_000));
        콜라 = ProductTestFixture.create(2L, "콜라", BigDecimal.valueOf(2_000));
        피클 = ProductTestFixture.create(3L, "피클", BigDecimal.valueOf(1_000));

        피자 = MenuGroupTestFixture.create(1L, "피자");

        하와이안피자상품 = MenuProductTestFixture.create(1L, 하와이안피자세트, 하와이안피자, 1L);
        콜라상품 = MenuProductTestFixture.create(2L, 하와이안피자세트, 콜라, 1L);
        피클상품 = MenuProductTestFixture.create(3L, 하와이안피자세트, 피클, 1L);

        하와이안피자세트 = MenuTestFixture.create(1L, "하와이안피자세트", BigDecimal.valueOf(18_000L), 피자,
            Arrays.asList(하와이안피자상품, 콜라상품, 피클상품));
        주문메뉴 = OrderMenuTestFixture.create(하와이안피자세트);

        주문테이블 = OrderTableTestFixture.create(1L,  0, false);
        하와이안피자세트주문 = OrderLineItemRequest.from(하와이안피자세트.getId(), 1);
        주문 = OrderTestFixture.create(주문테이블, Arrays.asList(하와이안피자세트주문.toOrderLineItem(주문메뉴)));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        OrderRequest orderRequest = OrderRequest.of(주문테이블.getId(), OrderStatus.COOKING, Collections.singletonList(하와이안피자세트주문));
        Order order = Order.of(주문테이블.getId(), OrderLineItems.from(Collections.singletonList(하와이안피자세트주문.toOrderLineItem(주문메뉴))));
        when(menuRepository.findById(하와이안피자세트.getId())).thenReturn(Optional.of(하와이안피자세트));
        when(orderRepository.save(order)).thenReturn(order);

        // when
        OrderResponse result = orderService.create(orderRequest);

        // then
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(주문.getId()),
            () -> assertThat(result.getOrderStatus()).isEqualTo(주문.getOrderStatus())
        );
    }

//    @DisplayName("주문 테이블이 등록되지 않으면 예외가 발생한다.")
//    @Test
//    void crateOrderNotExistOrderTableException() {
//        // given
//        OrderRequest orderRequest = OrderRequest.of(주문테이블.getId(), OrderStatus.COOKING, Collections.singletonList(하와이안피자세트주문));
//        when(menuRepository.findById(하와이안피자세트.getId())).thenReturn(Optional.of(하와이안피자세트));
//
//        // when & then
//        assertThatThrownBy(() -> orderService.create(orderRequest))
//            .isInstanceOf(IllegalArgumentException.class);
//    }

    @DisplayName("주문 항목 메뉴가 등록되지 않으면 예외가 발생한다.")
    @Test
    void createOrderNotExistOrderLineItemException() {
        // given
        OrderRequest orderRequest = OrderRequest.of(주문테이블.getId(), OrderStatus.COOKING, Collections.singletonList(하와이안피자세트주문));

        // when & then
        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목 메뉴가 빈 값이면 예외가 발생한다.")
    @Test
    void createOrderEmptyOrderLineItemException() {
        // given
        OrderRequest orderRequest = OrderRequest.of(주문테이블.getId(), OrderStatus.COOKING, Collections.emptyList());
        // when(orderTableRepository.findById(주문테이블.getId())).thenReturn(Optional.of(주문테이블));

        // when & then
        assertThatThrownBy(() -> orderService.create(orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 수정한다.")
    @Test
    void updateOrderStatus() {
        // given
        OrderStatus expectOrderStatus = OrderStatus.MEAL;
        OrderRequest orderRequest = OrderRequest.of(주문테이블.getId(), expectOrderStatus, Collections.singletonList(하와이안피자세트주문));
        when(orderRepository.findById(주문.getId())).thenReturn(Optional.of(주문));

        // when
        OrderResponse result = orderService.changeOrderStatus(주문.getId(), orderRequest);

        // then
        assertThat(result.getOrderStatus()).isEqualTo(expectOrderStatus);
    }

    @DisplayName("등록되지 않은 주문의 상태를 수정하면 예외가 발생한다.")
    @Test
    void updateOrderStatusNotExistException() {
        // given
        OrderRequest orderRequest = OrderRequest.of(주문테이블.getId(), OrderStatus.COOKING, Collections.singletonList(하와이안피자세트주문));
        when(orderRepository.findById(10L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(10L, orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("계산 완료된 상태의 주문을 수정하면 예외가 발생한다.")
    @Test
    void updateOrderStatusCompleteException() {
        // given
        Order order = Order.of(주문테이블.getId(), OrderLineItems.from(Arrays.asList(하와이안피자세트주문.toOrderLineItem(주문메뉴))))
            .changeOrderStatus(OrderStatus.COMPLETION);
        OrderRequest orderRequest = OrderRequest.of(주문테이블.getId(), OrderStatus.MEAL, Collections.singletonList(하와이안피자세트주문));
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 목록을 조회한다.")
    @Test
    void findAllOrder() {
        // given
        when(orderRepository.findAll()).thenReturn(Arrays.asList(주문));

        // when
        List<OrderResponse> result = orderService.list();

        // then
        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result.stream().map(OrderResponse::getId)).containsExactly(주문.getId())
        );
    }
}
