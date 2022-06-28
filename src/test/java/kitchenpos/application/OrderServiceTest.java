package kitchenpos.application;

import static kitchenpos.fixture.MenuFactory.createMenu;
import static kitchenpos.fixture.MenuProductFactory.createMenuProduct;
import static kitchenpos.fixture.OrderFactory.createOrder;
import static kitchenpos.fixture.OrderFactory.createOrderLineItem;
import static kitchenpos.fixture.OrderTableFactory.createOrderTable;
import static kitchenpos.fixture.ProductFactory.createProduct;
import static kitchenpos.fixture.OrderFactory.createOrderLineItemRequest;
import static kitchenpos.fixture.OrderFactory.createOrderRequest;
import static kitchenpos.fixture.OrderFactory.createOrderStatusRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.Product;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private Order 주문;
    private OrderTable 주문테이블;
    private Menu 빅맥버거;

    @BeforeEach
    void setUp() {
        Product 토마토 = createProduct(2L, "토마토", 1000);
        Product 양상추 = createProduct(3L, "양상추", 500);

        빅맥버거 = createMenu(1L, "빅맥버거", 3000, 1L,
                Arrays.asList(createMenuProduct(1L, null, 토마토, 1), createMenuProduct(2L, null, 양상추, 4)));
        주문테이블 = createOrderTable(1L, null, 5, false);

        주문 = createOrder(1L, 주문테이블, OrderStatus.COOKING.name(), null,
                Arrays.asList(createOrderLineItem(1L, null, 빅맥버거.getId(), 1)));
        주문.addOrderLineItems(OrderLineItems.from(Arrays.asList(createOrderLineItem(1L, null, 빅맥버거.getId(), 1))));
    }

    @Test
    void 주문_생성_주문_항목_없음_예외() {
        // given
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));

        // when, then
        OrderRequest 주문항목없는주문 = createOrderRequest(주문테이블.getId(), null);
        assertThatThrownBy(
                () -> orderService.create(주문항목없는주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_생성_없는_메뉴_예외() {
        // given
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));
        given(menuRepository.countByIdIn(Arrays.asList(100L))).willReturn(0);

        // when, then
        OrderRequest 없는메뉴주문 = createOrderRequest(주문테이블.getId(), Arrays.asList(createOrderLineItemRequest(100L, 1)));
        assertThatThrownBy(
                () -> orderService.create(없는메뉴주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_생성_존재하지_않는_주문_테이블_예외() {
        // given
        given(orderTableRepository.findById(2L)).willThrow(IllegalArgumentException.class);

        // when, then
        OrderRequest 없는_테이블_주문 = createOrderRequest(2L, Arrays.asList(createOrderLineItemRequest(빅맥버거.getId(), 1)));
        assertThatThrownBy(
                () -> orderService.create(없는_테이블_주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_생성_빈_테이블_예외() {
        // given
        주문테이블.changeEmpty(true);
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));

        // when, then
        OrderRequest 주문 = createOrderRequest(주문테이블.getId(), Arrays.asList(createOrderLineItemRequest(빅맥버거.getId(), 1)));
        assertThatThrownBy(
                () -> orderService.create(주문)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_생성() {
        // given
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));
        given(menuRepository.countByIdIn(Arrays.asList(빅맥버거.getId()))).willReturn(1);

        given(orderRepository.save(any(Order.class))).willReturn(주문);

        // when
        OrderResponse response = orderService.create(
                createOrderRequest(주문테이블.getId(), Arrays.asList(createOrderLineItemRequest(빅맥버거.getId(), 1))));

        // then
        assertThat(response.getId()).isEqualTo(주문.getId());
    }

    @Test
    void 주문_목록_조회() {
        // given
        given(orderRepository.findAll()).willReturn(Arrays.asList(주문));

        // when, then
        assertThat(toIdList(orderService.list())).containsExactlyElementsOf(Arrays.asList(주문.getId()));
    }

    @Test
    void 주문_상태_변경_존재하지_않는_주문_예외() {
        // given
        given(orderRepository.findById(주문.getId())).willThrow(IllegalArgumentException.class);

        // when, then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(주문.getId(), createOrderStatusRequest(OrderStatus.COMPLETION))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태_변경_계산_완료_상태_예외() {

        // given
        Order 완료주문 = createOrder(2L, 주문테이블, OrderStatus.COMPLETION.name(), null,
                Arrays.asList(createOrderLineItem(1L, null, 빅맥버거.getId(), 1)));
        given(orderRepository.findById(완료주문.getId())).willReturn(Optional.ofNullable(완료주문));

        // when, then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(완료주문.getId(), createOrderStatusRequest(OrderStatus.COMPLETION))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태_변경() {
        // given
        given(orderRepository.findById(주문.getId())).willReturn(Optional.ofNullable(주문));

        // when
        OrderResponse result = orderService.changeOrderStatus(주문.getId(),
                createOrderStatusRequest(OrderStatus.COMPLETION));

        // then
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);

    }

    private List<Long> toIdList(List<OrderResponse> orders) {
        return orders.stream()
                .map(OrderResponse::getId)
                .collect(Collectors.toList());
    }
}
