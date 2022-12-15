package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    private OrderTable 주문테이블;
    private Product 뿌링클;
    private Product 후라이드;
    private Product 콜라;
    private Order 주문;
    private Menu 메뉴1;
    private Menu 메뉴2;
    private OrderLineItem 주문_메뉴1;
    private OrderLineItem 주문_메뉴2;
    private OrderLineItemRequest 뿌링클세트주문요청;

    @BeforeEach
    void setUp() {
        뿌링클 = Product.of(1L, "뿌링클", BigDecimal.valueOf(20000));
        후라이드 = Product.of(2L, "후라이드", BigDecimal.valueOf(15000));
        콜라 = Product.of(3L, "콜라", BigDecimal.valueOf(2000));
        MenuProduct 뿌링클_상품 = MenuProduct.of(뿌링클, 1);
        MenuProduct 후라이드_상품 = MenuProduct.of(후라이드, 1);
        MenuProduct 콜라_상품 = MenuProduct.of(콜라, 1);
        MenuGroup 뼈치킨 = MenuGroup.of(1L, "뼈치킨");
        메뉴1 = Menu.of(1L, "메뉴1", BigDecimal.valueOf(22000), 뼈치킨.getId(), Arrays.asList(뿌링클_상품, 콜라_상품));
        메뉴2 = Menu.of(2L, "메뉴2", BigDecimal.valueOf(17000), 뼈치킨.getId(), Arrays.asList(후라이드_상품, 콜라_상품));
        주문테이블 = OrderTable.of(1L, null, 5, false);
        OrderMenu 메뉴1_주문메뉴 = OrderMenu.of(메뉴1);
        OrderMenu 메뉴2_주문메뉴 = OrderMenu.of(메뉴2);
        주문_메뉴1 = OrderLineItem.of(메뉴1_주문메뉴, 1L);
        주문_메뉴2 = OrderLineItem.of(메뉴2_주문메뉴, 1L);
        ReflectionTestUtils.setField(주문_메뉴1, "seq", 1L);
        ReflectionTestUtils.setField(주문_메뉴2, "seq", 2L);
        List<OrderLineItem> 주문_메뉴_목록 = Arrays.asList(주문_메뉴1, 주문_메뉴2);
        주문 = Order.of(1L, 주문테이블.getId(), OrderLineItems.of(주문_메뉴_목록));
        뿌링클세트주문요청 = OrderLineItemRequest.of(주문_메뉴1);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void 주문_생성() {
        // when
        OrderRequest orderRequest = new OrderRequest(주문테이블.getId(), OrderStatus.COOKING, Collections.singletonList(뿌링클세트주문요청));
        Order 주문 = Order.of(1L, 주문테이블.getId(), OrderLineItems.of(Collections.singletonList(뿌링클세트주문요청.toOrderLineItem(OrderMenu.of(메뉴1)))));
        when(menuRepository.findById(메뉴1.getId())).thenReturn(Optional.of(메뉴1));
        when(orderTableRepository.findById(orderRequest.getOrderTableId())).thenReturn(Optional.of(주문테이블));
        when(orderRepository.save(any(Order.class))).thenReturn(주문);

        // when
        OrderResponse orderResponse = orderService.create(orderRequest);

        // then
        assertAll(
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(orderResponse.getId()).isEqualTo(주문.getId())
        );
    }

    @DisplayName("등록되지 않은 메뉴로 주문을 생성할 수 없다.")
    @Test
    void 등록되지않은_메뉴_주문_생성() {
        OrderRequest request = new OrderRequest(주문테이블.getId(), OrderStatus.COOKING,
                OrderLineItemRequest.list(Arrays.asList(주문_메뉴1, 주문_메뉴2)));

        when(menuRepository.findById(메뉴1.getId())).thenReturn(Optional.of(메뉴1));
        when(menuRepository.findById(메뉴2.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("등록되지 않은 테이블은 주문을 생성할 수 없다.")
    @Test
    void 등록되지않은_테이블_주문_생성() {
        when(menuRepository.findById(메뉴1.getId())).thenReturn(Optional.of(메뉴1));
        when(menuRepository.findById(메뉴2.getId())).thenReturn(Optional.of(메뉴2));
        when(orderTableRepository.findById(주문테이블.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> orderService.create(new OrderRequest(주문테이블.getId(), OrderStatus.COOKING,
                        OrderLineItemRequest.list(Arrays.asList(주문_메뉴1, 주문_메뉴2))))
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("빈 테이블은 주문을 생성할 수 없다.")
    @Test
    void 빈_테이블_주문_생성() {
        // when
        주문테이블.changeEmpty(true);
        OrderRequest request = new OrderRequest(주문테이블.getId(), OrderStatus.COOKING,
                OrderLineItemRequest.list(Arrays.asList(주문_메뉴1, 주문_메뉴2)));

        when(menuRepository.findById(메뉴1.getId())).thenReturn(Optional.of(메뉴1));
        when(menuRepository.findById(메뉴2.getId())).thenReturn(Optional.of(메뉴2));
        when(orderTableRepository.findById(주문테이블.getId())).thenReturn(Optional.of(주문테이블));

        assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void 주문_목록_조회() {
        when(orderRepository.findAll()).thenReturn(Collections.singletonList(주문));

        List<OrderResponse> orders = orderService.list();

        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders.stream().map(OrderResponse::getId).collect(toList()))
                        .containsExactly(주문.getId())
        );
    }

    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void 주문_상태_변경() {
        when(orderRepository.findById(주문.getId())).thenReturn(Optional.of(주문));

        orderService.changeOrderStatus(주문.getId(), OrderStatus.COMPLETION);

        assertEquals(OrderStatus.COMPLETION, 주문.getOrderStatus());
    }

    @DisplayName("기등록된 주문이 아니면 주문의 상태를 변경할 수 없다.")
    @Test
    void 등록되지않은_주문_상태_변경() {
        when(orderRepository.findById(주문.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), OrderStatus.COOKING))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("완료 상태 주문은 상태를 변경할 수 없다.")
    @Test
    void 완료_주문_상태_변경() {
        주문.changeOrderStatus(OrderStatus.COMPLETION);
        when(orderRepository.findById(주문.getId())).thenReturn(Optional.of(주문));

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), OrderStatus.COMPLETION))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
