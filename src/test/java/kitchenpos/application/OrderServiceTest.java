package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.fixture.MenuFixtureFactory;
import kitchenpos.fixture.MenuGroupFixtureFactory;
import kitchenpos.fixture.MenuProductFixtureFactory;
import kitchenpos.fixture.OrderFixtureFactory;
import kitchenpos.fixture.OrderLineItemFixtureFactory;
import kitchenpos.fixture.OrderTableFixtureFactory;
import kitchenpos.fixture.ProductFixtureFactory;
import kitchenpos.application.order.OrderService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.exception.NotEqualsMenuAndOrderLineItemMenuException;
import kitchenpos.exception.NotExistOrderLineItemsException;
import kitchenpos.exception.NotFoundOrderTableException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Component.class))
class OrderServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
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

        초밥_메뉴그룹 = menuGroupRepository.save(초밥_메뉴그룹);
        우아한_초밥_1 = productRepository.save(우아한_초밥_1);
        우아한_초밥_2 = productRepository.save(우아한_초밥_2);
        A = menuRepository.save(A);

        A_주문_테이블 = OrderTableFixtureFactory.createWithGuest(false, 2);
        A_주문항목 = OrderLineItemFixtureFactory.create(A.getId(), 1);

        A_주문_테이블 = orderTableRepository.save(A_주문_테이블);

        A_주문 = OrderFixtureFactory.create(A_주문_테이블.getId(), OrderStatus.COOKING, Lists.newArrayList(A_주문항목));
        A_주문 = orderRepository.save(A_주문);
    }

    @DisplayName("주문을 할 수 있다.")
    @Test
    void create01() {
        // given
        OrderRequest orderRequest = OrderRequest.of(A_주문_테이블.getId(),
                OrderStatus.COOKING,
                Lists.newArrayList(OrderLineItemRequest.of(A_주문항목.getMenuId(), A_주문항목.findQuantity())));

        // when
        OrderResponse response = orderService.create(orderRequest);

        // then
        Order findOrder = orderRepository.findById(response.getId()).get();
        assertThat(response).isEqualTo(OrderResponse.from(findOrder));
    }

    @DisplayName("주문항목은 1건 이상이어야 한다.")
    @Test
    void create02() {
        // given
        OrderRequest orderRequest = OrderRequest.of(A_주문_테이블.getId(),
                OrderStatus.COOKING,
                Collections.emptyList());

        // when & then
        assertThatExceptionOfType(NotExistOrderLineItemsException.class)
                .isThrownBy(() -> orderService.create(orderRequest));
    }

    @DisplayName("주문항목에 메뉴가 존재하지 않으면 주문을 등록할 수 없다.")
    @Test
    void create03() {
        // given
        OrderRequest orderRequest = OrderRequest.of(A_주문_테이블.getId(),
                OrderStatus.COOKING,
                Lists.newArrayList(OrderLineItemRequest.of(0L, A_주문항목.findQuantity())));

        // when & then
        assertThatExceptionOfType(NotEqualsMenuAndOrderLineItemMenuException.class)
                .isThrownBy(() -> orderService.create(orderRequest));
    }

    @DisplayName("주문 테이블이 없이는 주문을 등록할 수 없다.")
    @Test
    void create04() {
        // given
        OrderRequest orderRequest = OrderRequest.of(0L,
                OrderStatus.COOKING,
                Lists.newArrayList(OrderLineItemRequest.of(A_주문항목.getMenuId(), A_주문항목.findQuantity())));

        // when & then
        assertThatExceptionOfType(NotFoundOrderTableException.class)
                .isThrownBy(() -> orderService.create(orderRequest));
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // given
        OrderRequest orderRequest = OrderRequest.of(A_주문_테이블.getId(),
                OrderStatus.COOKING,
                Lists.newArrayList(OrderLineItemRequest.of(A_주문항목.getMenuId(), A_주문항목.findQuantity())));

        OrderResponse response = orderService.create(orderRequest);

        // when
        List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders).contains(response);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void change01() {
        // given
        A_주문.changeOrderStatus(OrderStatus.MEAL);
        OrderRequest orderRequest = OrderRequest.of(A_주문_테이블.getId(),
                OrderStatus.MEAL,
                Lists.newArrayList(OrderLineItemRequest.of(A_주문항목.getMenuId(), A_주문항목.findQuantity())));

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
                Lists.newArrayList(OrderLineItemRequest.of(A_주문항목.getMenuId(), A_주문항목.findQuantity())));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(A_주문.getId(), orderRequest));
    }

}