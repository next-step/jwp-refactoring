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
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("주문 관련 비즈니스 기능 테스트")
@ExtendWith(MockitoExtension.class)
public
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    private Product 삼겹살;
    private Product 김치;
    private MenuGroup 한식;
    private Menu 삼겹살세트메뉴1;
    private Menu 삼겹살세트메뉴2;
    private MenuProduct 삼겹살메뉴상품;
    private MenuProduct 김치메뉴상품;
    private OrderTable 주문테이블;
    private Order 주문;
    private OrderLineItem 삼겹살세트메뉴주문1;
    private OrderLineItem 삼겹살세트메뉴주문2;

    @BeforeEach
    void setUp() {
        삼겹살 = new Product(1L, "삼겹살", BigDecimal.valueOf(5_000));
        김치 = new Product(2L, "김치", BigDecimal.valueOf(3_000));
        한식 = new MenuGroup(1L, "한식");
        삼겹살세트메뉴1 = new Menu(1L, "삼겹살세트메뉴1", BigDecimal.valueOf(8_000), 한식);
        삼겹살세트메뉴2 = new Menu(2L, "삼겹살세트메뉴2", BigDecimal.valueOf(8_000), 한식);
        삼겹살메뉴상품 = 메뉴상품_생성(1L, 삼겹살, 1L);
        김치메뉴상품 = 메뉴상품_생성(2L, 김치, 1L);
        주문테이블 = new OrderTable(1L, 0, false);
        주문 = new Order(주문테이블, OrderStatus.COOKING);

        삼겹살세트메뉴주문1 = new OrderLineItem(1L, 주문, orderMenu(삼겹살세트메뉴1.getId(), 삼겹살세트메뉴1.getName(), 삼겹살세트메뉴1.getPrice()), 1);
        삼겹살세트메뉴주문2 = new OrderLineItem(2L, 주문, orderMenu(삼겹살세트메뉴2.getId(), 삼겹살세트메뉴2.getName(), 삼겹살세트메뉴2.getPrice()), 1);

        주문.order(Arrays.asList(삼겹살세트메뉴주문1, 삼겹살세트메뉴주문2));
    }

    @DisplayName("주문 생성 테스트")
    @Test
    void createOrderTest() {
        OrderRequest request = new OrderRequest(주문테이블.getId(), OrderStatus.COOKING,
                OrderLineItemRequest.toResponselist(Arrays.asList(삼겹살세트메뉴주문1, 삼겹살세트메뉴주문2)));

        when(menuRepository.findById(any())).thenReturn(Optional.of(삼겹살세트메뉴1));
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(주문테이블));
        when(orderRepository.save(any(Order.class))).thenReturn(주문);
        when(menuRepository.findAllById(request.findAllMenuIds()))
                .thenReturn(Arrays.asList(삼겹살세트메뉴1, 삼겹살세트메뉴2));

        OrderResponse orderResponse = orderService.create(request);

        assertThat(orderResponse).satisfies(res -> {
            assertEquals(주문.getId(), res.getId());
            assertEquals(주문테이블.getId(), res.getOrderTableId());
            assertEquals(OrderStatus.COOKING, res.getOrderStatus());
            assertEquals(request.getOrderLineItems().size(), res.getOrderLineItems().size());
        });
    }

    @DisplayName("주문 생성 테스트 - 주문 테이블이 등록되지 않은 경우")
    @Test
    void createOrderTest2() {
        when(orderTableRepository.findById(주문테이블.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(
                () -> orderService.create(new OrderRequest(주문테이블.getId(), OrderStatus.COOKING,
                        OrderLineItemRequest.toResponselist(Arrays.asList(삼겹살세트메뉴주문1, 삼겹살세트메뉴주문2))))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 테스트 - 주문 항목 메뉴가 등록되지 않은 경우")
    @Test
    void createOrderTest3() {
        OrderRequest request = new OrderRequest(주문테이블.getId(), OrderStatus.COOKING,
                OrderLineItemRequest.toResponselist(Arrays.asList(삼겹살세트메뉴주문1, 삼겹살세트메뉴주문2)));

        when(orderTableRepository.findById(주문테이블.getId())).thenReturn(Optional.of(주문테이블));
        when(menuRepository.findAllById(request.findAllMenuIds())).thenReturn(Arrays.asList(삼겹살세트메뉴1));

        Assertions.assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 테스트 - 오더 테이블이 Empty인 경우")
    @Test
    void createOrderTest4() {
        // given
        OrderRequest request = new OrderRequest(null, OrderStatus.COOKING,
                OrderLineItemRequest.toResponselist(Arrays.asList(삼겹살세트메뉴주문1, 삼겹살세트메뉴주문2)));

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 수정 테스트")
    @Test
    void updateOrderStatus() {
        when(orderRepository.findById(주문.getId())).thenReturn(Optional.of(주문));
        when(orderRepository.save(주문)).thenReturn(주문);

        orderService.changeOrderStatus(주문.getId(), OrderStatus.COMPLETION);

        assertEquals(OrderStatus.COMPLETION, 주문.getOrderStatus());
    }

    @DisplayName("주문 상태 수정 테스트 - 등록되지 않은 주문의 상태를 수정하는 경우")
    @Test
    void updateOrderStatus2() {
        when(orderRepository.findById(주문.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), OrderStatus.COOKING))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("주문 상태 수정 테스트 - 계산 완료된 상태의 주문을 수정하는 경우")
    @Test
    void updateOrderStatus3() {
        주문.changeOrderStatus(OrderStatus.COMPLETION);
        when(orderRepository.findById(주문.getId())).thenReturn(Optional.of(주문));

        Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), OrderStatus.COMPLETION))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 목록 조회 테스트")
    @Test
    void findAllOrder() {
        // given
        when(orderRepository.findAll()).thenReturn(Arrays.asList(주문));

        // when
        List<OrderResponse> results = orderService.list();

        // then
        assertThat(results).hasSize(1);
    }

    public static OrderMenu orderMenu(Long menuId, String menuName, BigDecimal menuPrice) {
        return new OrderMenu.Builder()
                .menuId(menuId)
                .menuName(menuName)
                .menuPrice(menuPrice)
                .build();
    }

    public static MenuProduct 메뉴상품_생성(Long seq, Product product, long quantity) {
        return new MenuProduct(seq, product, quantity);
    }

}
