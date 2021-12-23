package kitchenpos.order;

import kitchenpos.AcceptanceTest;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 관련 기능")
class OrderServiceTest extends AcceptanceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuService menuService;

    @Test
    @DisplayName("주문한 메뉴가 존재하지 않으면 예외가 발생한다.")
    void createOrderFailBecauseOfIsNotExistMenu() {
        // given
        final Order order = new Order(Arrays.asList(new OrderLineItem()));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderService.create(order);
        });
    }

    @Test
    @DisplayName("테이블이 존재하지 않으면 예외가 발생한다.")
    void createOrderFailBecauseOfIsNotExistTable() {
        // given
        final MenuGroup savedMenuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        final Product savedProduct = productService.create(new Product("후라이드", BigDecimal.valueOf(9000)));
        final Menu savedMenu = menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(18000), savedMenuGroup.getId(), Arrays.asList(new MenuProduct(savedProduct.getId(), 2L))));
        final Order order = new Order(Arrays.asList(new OrderLineItem(savedMenu.getId(), 2L)));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderService.create(order);
        });
    }

    @Test
    @DisplayName("테이블의 상태가 사용불가라면 예외가 발생한다.")
    void createOrderFailBecauseOfIsEmptyTableStatus() {
        // given
        final MenuGroup savedMenuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        final Product savedProduct = productService.create(new Product("후라이드", BigDecimal.valueOf(9000)));
        final Menu savedMenu = menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(18000), savedMenuGroup.getId(), Arrays.asList(new MenuProduct(savedProduct.getId(), 2L))));
        final OrderTable savedTable = orderTableDao.save(new OrderTable(true));
        final Order order = new Order(savedTable.getId(), Arrays.asList(new OrderLineItem(savedMenu.getId(), 2L)));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderService.create(order);
        });
    }

    @Test
    @DisplayName("주문을 할 수 있다.")
    void createOrder() {
        // given
        final MenuGroup savedMenuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        final Product savedProduct = productService.create(new Product("후라이드", BigDecimal.valueOf(9000)));
        final Menu savedMenu = menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(18000), savedMenuGroup.getId(), Arrays.asList(new MenuProduct(savedProduct.getId(), 2L))));
        final OrderTable savedTable = orderTableDao.save(new OrderTable(false));
        final Order order = new Order(savedTable.getId(), Arrays.asList(new OrderLineItem(savedMenu.getId(), 2L)));

        // when
        final Order savedOrder = orderService.create(order);

        // then
        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(savedTable.getId()),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrder.getOrderLineItems()).extracting("orderId").contains(savedOrder.getId()),
                () -> assertThat(savedOrder.getOrderLineItems()).extracting("menuId").contains(savedMenu.getId())
        );
    }

    @Test
    @DisplayName("주문 목록을 조회할 수 있다.")
    void findOrder() {
        // given
        final MenuGroup savedMenuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        final Product savedProduct = productService.create(new Product("후라이드", BigDecimal.valueOf(9000)));
        final Menu savedMenu = menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(18000), savedMenuGroup.getId(), Arrays.asList(new MenuProduct(savedProduct.getId(), 2L))));
        final OrderTable savedTable = orderTableDao.save(new OrderTable(false));
        orderDao.save(new Order(savedTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(new OrderLineItem(savedMenu.getId(), 2L))));

        // when
        final List<Order> findByOrder = orderService.list();

        // then
        assertThat(findByOrder.size()).isOne();
    }

    @Test
    @DisplayName("주문 상태 변경 시 주문이 존재하지 않으면 예외가 발생한다.")
    void changeOrderStatusFailBecauseOfIsNotExistOrder() {
        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderService.changeOrderStatus(1L, new Order());
        });
    }

    @Test
    @DisplayName("주문 상태 변경 시 주문이 완료 상태면 예외가 발생한다.")
    void changeOrderStatusFailBecauseOfOrderStatusCompletion() {
        // given
        final MenuGroup savedMenuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        final Product savedProduct = productService.create(new Product("후라이드", BigDecimal.valueOf(9000)));
        final Menu savedMenu = menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(18000), savedMenuGroup.getId(), Arrays.asList(new MenuProduct(savedProduct.getId(), 2L))));
        final OrderTable savedTable = orderTableDao.save(new OrderTable(false));
        final Order savedOrder = orderDao.save(new Order(savedTable.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), Arrays.asList(new OrderLineItem(savedMenu.getId(), 2L))));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            orderService.changeOrderStatus(savedOrder.getId(), new Order(OrderStatus.COOKING.name() ));
        });
    }

    @Test
    @DisplayName("주문의 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        // given
        final MenuGroup savedMenuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        final Product savedProduct = productService.create(new Product("후라이드", BigDecimal.valueOf(9000)));
        final Menu savedMenu = menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(18000), savedMenuGroup.getId(), Arrays.asList(new MenuProduct(savedProduct.getId(), 2L))));
        final OrderTable savedTable = orderTableDao.save(new OrderTable(false));
        final Order savedOrder = orderDao.save(new Order(savedTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(new OrderLineItem(savedMenu.getId(), 2L))));

        // when
        final Order changeOrderStatus = orderService.changeOrderStatus(savedOrder.getId(), new Order(OrderStatus.COMPLETION.name()));

        // then
        assertThat(changeOrderStatus.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }
}
