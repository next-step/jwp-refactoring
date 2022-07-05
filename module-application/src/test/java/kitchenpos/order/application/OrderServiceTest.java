package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.application.menu.MenuService;
import kitchenpos.application.order.OrderService;
import kitchenpos.application.table.TableService;
import kitchenpos.common.common.domain.Quantity;
import kitchenpos.common.order.domain.Order;
import kitchenpos.common.order.domain.OrderLineItems;
import kitchenpos.common.order.domain.OrderStatus;
import kitchenpos.common.order.dto.OrderLineItemRequest;
import kitchenpos.common.order.dto.OrderLineItemResponse;
import kitchenpos.common.order.dto.OrderRequest;
import kitchenpos.common.order.dto.OrderResponse;
import kitchenpos.common.order.repository.OrderRepository;
import kitchenpos.fixture.UnitTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MenuService menuService;
    @Mock
    private TableService tableService;

    @InjectMocks
    private OrderService orderService;

    private UnitTestFixture 식당_포스;

    @BeforeEach
    void setUp() {
        식당_포스 = new UnitTestFixture();
    }

    @Test
    void 주문을_등록할_수_있어야_한다() {
        // given
        final List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(new OrderLineItemRequest(식당_포스.조리중_주문_항목1.getMenuId(), 식당_포스.조리중_주문_항목1.getQuantity()));
        orderLineItemRequests.add(new OrderLineItemRequest(식당_포스.조리중_주문_항목2.getMenuId(), 식당_포스.조리중_주문_항목2.getQuantity()));

        final OrderRequest given = new OrderRequest(식당_포스.테이블.getId(), orderLineItemRequests);

        final Order expected = new Order(
                1L,
                식당_포스.테이블.getId(),
                OrderStatus.COOKING,
                LocalDateTime.now(),
                new OrderLineItems(Arrays.asList(식당_포스.조리중_주문_항목1, 식당_포스.조리중_주문_항목2)));
        when(tableService.getById(식당_포스.테이블.getId())).thenReturn(식당_포스.테이블);
        when(menuService.getById(식당_포스.조리중_주문_항목1.getMenuId())).thenReturn(식당_포스.돼지모듬);
        when(menuService.getById(식당_포스.조리중_주문_항목2.getMenuId())).thenReturn(식당_포스.김치찌개정식);
        when(orderRepository.save(any(Order.class))).thenReturn(expected);

        // when
        final OrderResponse actual = orderService.create(given);

        // then
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getOrderLineItems().stream().map(OrderLineItemResponse::getMenuId))
                .containsExactly(식당_포스.조리중_주문_항목1.getMenuId(), 식당_포스.조리중_주문_항목2.getMenuId());
    }

    @Test
    void 주문_등록_시_주문_항목이_비어있으면_에러가_발생해야_한다() {
        // given
        final OrderRequest given = new OrderRequest(식당_포스.테이블.getId(), new ArrayList<>());
        when(tableService.getById(식당_포스.테이블.getId())).thenReturn(식당_포스.테이블);

        // when and then
        assertThatThrownBy(() -> orderService.create(given))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_등록_시_주문_항목에_속한_메뉴가_하나라도_존재하지_않으면_에러가_발생해야_한다() {
        // given
        final Long invalidMenuId = -1L;
        final OrderRequest given = new OrderRequest(
                식당_포스.테이블.getId(),
                Arrays.asList(new OrderLineItemRequest(invalidMenuId, new Quantity(1))));
        when(tableService.getById(식당_포스.테이블.getId())).thenReturn(식당_포스.테이블);
        when(menuService.getById(invalidMenuId)).thenThrow(new IllegalArgumentException());

        // when and then
        assertThatThrownBy(() -> orderService.create(given))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_등록_시_테이블이_존재하지_않으면_에러가_발생해야_한다() {
        // given
        final Long invalidOrderTableId = -1L;
        final OrderRequest given = new OrderRequest(
                invalidOrderTableId,
                Arrays.asList(
                        new OrderLineItemRequest(식당_포스.조리중_주문_항목1.getMenuId(), 식당_포스.조리중_주문_항목1.getQuantity()),
                        new OrderLineItemRequest(식당_포스.조리중_주문_항목2.getMenuId(), 식당_포스.조리중_주문_항목2.getQuantity())));
        when(tableService.getById(invalidOrderTableId)).thenThrow(new IllegalArgumentException());

        // when and then
        assertThatThrownBy(() -> orderService.create(given))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_등록_시_테이블이_비어_있으면_에러가_발생해야_한다() {
        // given
        final OrderRequest given = new OrderRequest(
                식당_포스.빈_테이블1.getId(),
                Arrays.asList(
                        new OrderLineItemRequest(식당_포스.조리중_주문_항목1.getMenuId(), 식당_포스.조리중_주문_항목1.getQuantity()),
                        new OrderLineItemRequest(식당_포스.조리중_주문_항목2.getMenuId(), 식당_포스.조리중_주문_항목2.getQuantity())));
        when(tableService.getById(식당_포스.빈_테이블1.getId())).thenReturn(식당_포스.빈_테이블1);

        // when and then
        assertThatThrownBy(() -> orderService.create(given))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_목록을_조회할_수_있어야_한다() {
        // given
        when(orderRepository.findAll()).thenReturn(Arrays.asList(식당_포스.조리중_주문, 식당_포스.완료된_주문));

        // when
        final List<OrderResponse> actual = orderService.list();

        // then
        assertThat(actual.stream().map(OrderResponse::getId).collect(Collectors.toList()))
                .containsExactly(식당_포스.조리중_주문.getId(), 식당_포스.완료된_주문.getId());
    }

    @Test
    void 주문_상태를_변경할_수_있어야_한다() {
        // given
        final OrderStatus targetStatus = OrderStatus.MEAL;
        when(orderRepository.findById(식당_포스.조리중_주문.getId())).thenReturn(Optional.of(식당_포스.조리중_주문));
        when(orderRepository.save(any())).thenReturn(
                new Order(
                        식당_포스.조리중_주문.getId(),
                        식당_포스.조리중_주문.getOrderTableId(),
                        targetStatus,
                        LocalDateTime.now(),
                        new OrderLineItems(Arrays.asList(식당_포스.조리중_주문_항목1, 식당_포스.조리중_주문_항목2))));

        // when
        orderService.changeOrderStatus(식당_포스.조리중_주문.getId(), new OrderRequest(targetStatus));

        // then
        assertThat(식당_포스.조리중_주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    void 주문_상태_변경_시_완료된_주문이면_에러가_발생해야_한다() {
        // given
        when(orderRepository.findById(식당_포스.완료된_주문.getId())).thenReturn(Optional.of(식당_포스.완료된_주문));

        // when and then
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(식당_포스.완료된_주문.getId(), new OrderRequest(OrderStatus.COMPLETION));
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 주문_테이블들에_속한_주문들_중_완료되지_않은_주문이_있는지_조회할_수_있어야_한다() {
        // given
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(식당_포스.테이블.getId()),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).thenReturn(true);

        // when and then
        assertThat(orderService.existsNotCompletesByOrderTableIdIn(
                Arrays.asList(식당_포스.테이블.getId()))).isTrue();
    }
}
