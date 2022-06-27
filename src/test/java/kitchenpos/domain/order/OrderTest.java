package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.collect.Lists;
import java.math.BigDecimal;
import kitchenpos.fixture.MenuFixtureFactory;
import kitchenpos.fixture.MenuGroupFixtureFactory;
import kitchenpos.fixture.MenuProductFixtureFactory;
import kitchenpos.fixture.OrderFixtureFactory;
import kitchenpos.fixture.OrderTableFixtureFactory;
import kitchenpos.fixture.ProductFixtureFactory;
import kitchenpos.application.order.OrderValidator;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.exception.NotFoundOrderTableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Component.class))
class OrderTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderValidator orderValidator;

    private MenuGroup 메뉴그룹;
    private Menu 메뉴;
    private Product 상품_1;
    private Product 상품_2;
    private MenuProduct 메뉴_상품_1;
    private MenuProduct 메뉴_상품_2;
    private OrderTable 주문테이블;
    private Order 주문;

    @BeforeEach
    void setUp() {
        메뉴그룹 = MenuGroupFixtureFactory.create("메뉴그룹");
        메뉴그룹 = menuGroupRepository.save(메뉴그룹);

        상품_1 = ProductFixtureFactory.create("상품_1", BigDecimal.valueOf(10_000));
        상품_2 = ProductFixtureFactory.create("상품_2", BigDecimal.valueOf(20_000));
        상품_1 = productRepository.save(상품_1);
        상품_2 = productRepository.save(상품_2);

        메뉴 = MenuFixtureFactory.create("메뉴", BigDecimal.valueOf(30_000), 메뉴그룹.getId());
        메뉴_상품_1 = MenuProductFixtureFactory.create(1L, 메뉴, 상품_1.getId(), 1L);
        메뉴_상품_2 = MenuProductFixtureFactory.create(2L, 메뉴, 상품_2.getId(), 2L);

        메뉴_상품_1.mappedByMenu(메뉴);
        메뉴_상품_2.mappedByMenu(메뉴);
        메뉴 = menuRepository.save(메뉴);

        주문테이블 = OrderTableFixtureFactory.createWithGuest(false, 2);
        주문테이블 = orderTableRepository.save(주문테이블);

        주문 = OrderFixtureFactory.create(주문테이블.getId(), OrderStatus.COOKING, Lists.newArrayList());
        주문 = orderRepository.save(주문);
    }

    @DisplayName("Order를 생성할 수 있다.(OrderTable)")
    @Test
    void create01() {
        // given
        Order order = Order.from(주문테이블.getId());

        // when & then
        assertAll(
                () -> assertNotNull(order),
                () -> assertEquals(OrderStatus.COOKING, order.getOrderStatus())
        );
    }

    @DisplayName("Order 생성 시 OrderTable이 없는 경우 예외가 발생한다.")
    @Test
    void create02() {
        // given
        Order order = Order.from(0L);

        // when & then
        assertThrows(NotFoundOrderTableException.class, () -> orderValidator.validate(order));
    }

    @DisplayName("Order 의 주문상태 OrderStatus를 변경할 수 있다.")
    @Test
    void change01() {
        // given
        Order order = Order.from(주문테이블.getId());

        // when
        order.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertEquals(OrderStatus.MEAL, order.getOrderStatus());
    }

    @DisplayName("Order 의 주문상태가 완료상태이면 OrderStatus를 변경할 수 없다.")
    @Test
    void change02() {
        // given
        Order order = Order.from(주문테이블.getId());
        order.changeOrderStatus(OrderStatus.COMPLETION);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL));
    }
}