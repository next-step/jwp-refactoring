package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.fixture.MenuFixtureFactory;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.application.fixture.MenuProductFixtureFactory;
import kitchenpos.application.fixture.OrderFixtureFactory;
import kitchenpos.application.fixture.OrderLineItemFixtureFactory;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.ProductFixtureFactory;
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
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
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


    private MenuGroup 초밥_메뉴그룹;
    private Product 우아한_초밥_1;
    private Product 우아한_초밥_2;
    private MenuProduct A_우아한_초밥_1;
    private MenuProduct A_우아한_초밥_2;
    private Menu A;
    private OrderTable A_주문_테이블;
    private Order A_주문;
    private OrderLineItem A_주문항목;

    @BeforeEach
    void setUp() {
        초밥_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "초밥_메뉴그룹");
        우아한_초밥_1 = ProductFixtureFactory.create(1L, "우아한_초밥_1", BigDecimal.valueOf(10_000));
        우아한_초밥_2 = ProductFixtureFactory.create(2L, "우아한_초밥_2", BigDecimal.valueOf(20_000));
        A = MenuFixtureFactory.create("A", BigDecimal.valueOf(30_000), 초밥_메뉴그룹.getId());

        A_우아한_초밥_1 = MenuProductFixtureFactory.create(1L, A, 우아한_초밥_1.getId(), 1);
        A_우아한_초밥_2 = MenuProductFixtureFactory.create(2L, A, 우아한_초밥_2.getId(), 2);

        A_우아한_초밥_1.mappedByMenu(A);
        A_우아한_초밥_2.mappedByMenu(A);

        A_주문_테이블 = OrderTableFixtureFactory.create(false);

        A_주문항목 = OrderLineItemFixtureFactory.create(A.getId(), 1);
        A_주문 = OrderFixtureFactory.create(1L, A_주문_테이블.getId(), OrderStatus.COOKING, Lists.newArrayList(A_주문항목));
    }

    @DisplayName("주문을 할 수 있다.")
    @Test
    void create01() {
        // given
        OrderRequest orderRequest = OrderRequest.of(A_주문_테이블.getId(),
                OrderStatus.COOKING,
                Lists.newArrayList(OrderLineItemRequest.of(A_주문항목.getMenuId(), A_주문항목.getQuantity())));

        given(menuRepository.countByIdIn(Lists.newArrayList(A.getId()))).willReturn(1L);
        given(orderTableRepository.findById(A_주문_테이블.getId())).willReturn(Optional.ofNullable(A_주문_테이블));
        given(orderRepository.save(any(Order.class))).willReturn(A_주문);

        // when
        OrderResponse response = orderService.create(orderRequest);

        // then
        assertThat(response).isEqualTo(OrderResponse.from(A_주문));
    }

    @DisplayName("주문항목은 1건 이상이어야 한다.")
    @Test
    void create02() {
        // given
        OrderRequest orderRequest = OrderRequest.of(A_주문_테이블.getId(),
                OrderStatus.COOKING,
                Collections.emptyList());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(orderRequest));
    }

    @DisplayName("주문항목에 메뉴가 존재하지 않으면 주문을 등록할 수 없다.")
    @Test
    void create03() {
        // given
        OrderRequest orderRequest = OrderRequest.of(A_주문_테이블.getId(),
                OrderStatus.COOKING,
                Lists.newArrayList(OrderLineItemRequest.of(A_주문항목.getMenuId(), A_주문항목.getQuantity())));

        given(menuRepository.countByIdIn(Lists.newArrayList(A.getId()))).willReturn(0L);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(orderRequest));
    }

    @DisplayName("주문 테이블이 없이는 주문을 등록할 수 없다.")
    @Test
    void create04() {
        // given
        OrderRequest orderRequest = OrderRequest.of(A_주문_테이블.getId(),
                OrderStatus.COOKING,
                Lists.newArrayList(OrderLineItemRequest.of(A_주문항목.getMenuId(), A_주문항목.getQuantity())));

        given(menuRepository.countByIdIn(Lists.newArrayList(A.getId()))).willReturn(1L);
        given(orderTableRepository.findById(A_주문_테이블.getId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(orderRequest));
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // given
        given(orderRepository.findAll()).willReturn(Lists.newArrayList(A_주문));

        // when
        List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders).containsExactly(OrderResponse.from(A_주문));
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void change01() {
        // given
        A_주문.changeOrderStatus(OrderStatus.MEAL);
        OrderRequest orderRequest = OrderRequest.of(A_주문_테이블.getId(),
                OrderStatus.MEAL,
                Lists.newArrayList(OrderLineItemRequest.of(A_주문항목.getMenuId(), A_주문항목.getQuantity())));

        given(orderRepository.findById(A_주문.getId())).willReturn(Optional.of(A_주문));
        given(orderRepository.save(any(Order.class))).willReturn(A_주문);

        // when
        OrderResponse changedOrder = orderService.changeOrderStatus(A_주문.getId(), orderRequest);

        // then
        assertAll(
                () -> assertThat(changedOrder).isEqualTo(OrderResponse.from(A_주문)),
                () -> assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL)
        );
    }

    @DisplayName("주문 상태가 \"COMPLETION\"인 경우 주문 상태를 변경할 수 없다.")
    @Test
    void change02() {
        // given
        A_주문.changeOrderStatus(OrderStatus.COMPLETION);
        OrderRequest orderRequest = OrderRequest.of(A_주문_테이블.getId(),
                OrderStatus.COOKING,
                Lists.newArrayList(OrderLineItemRequest.of(A_주문항목.getMenuId(), A_주문항목.getQuantity())));

        given(orderRepository.findById(A_주문.getId())).willReturn(Optional.of(A_주문));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(A_주문.getId(), orderRequest));
    }

}