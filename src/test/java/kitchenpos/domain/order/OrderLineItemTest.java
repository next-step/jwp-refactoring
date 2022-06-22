package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.google.common.collect.Lists;
import java.math.BigDecimal;
import kitchenpos.application.fixture.MenuFixtureFactory;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.application.fixture.MenuProductFixtureFactory;
import kitchenpos.application.fixture.OrderFixtureFactory;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.ProductFixtureFactory;
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
import kitchenpos.exception.NotEqualsMenuAndOrderLineItemMenuException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Component.class))
class OrderLineItemTest {

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
        주문 = OrderFixtureFactory.create(주문테이블, OrderStatus.COOKING, Lists.newArrayList());

        주문테이블 = orderTableRepository.save(주문테이블);
        주문 = orderRepository.save(주문);
    }

    @DisplayName("OrderLineItem 을 생성할 수 있다. (Menu, Quantity, Order)")
    @Test
    void create01() {
        // given & when
        OrderLineItem 주문항목 = OrderLineItem.of(메뉴.getId(), 1L);

        // then
        assertAll(
                () -> assertNotNull(주문항목),
                () -> assertEquals(1L, 주문항목.findQuantity())
        );
    }

    @DisplayName("OrderLineItem 생성 시 Menu가 null이면 예외가 발생한다.")
    @Test
    void create02() {
        // given
        OrderLineItem orderLineItem = OrderLineItem.of(0L, 1L);
        Order order = OrderFixtureFactory.create(주문테이블, OrderStatus.COOKING, Lists.newArrayList(orderLineItem));

        // when & then
        assertThatExceptionOfType(NotEqualsMenuAndOrderLineItemMenuException.class)
                .isThrownBy(() -> orderValidator.validate(order));
    }

    @DisplayName("OrderLineItem 생성 후 주문 정보에 할당할 수 있다.")
    @Test
    void create03() {
        // given
        OrderLineItem 주문항목 = OrderLineItem.of(메뉴.getId(), 1L);

        // when
        주문.addAllOrderLineItems(Lists.newArrayList(주문항목));

        // then
        assertThat(주문.getOrderLineItems().getReadOnlyValues()).containsExactly(주문항목);
    }



}