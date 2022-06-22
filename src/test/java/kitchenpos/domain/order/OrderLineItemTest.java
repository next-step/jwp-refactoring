package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

import com.google.common.collect.Lists;
import java.math.BigDecimal;
import kitchenpos.application.fixture.MenuFixtureFactory;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.application.fixture.MenuProductFixtureFactory;
import kitchenpos.application.fixture.OrderFixtureFactory;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.CreateOrderLineItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {

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
        메뉴 = MenuFixtureFactory.create("메뉴", BigDecimal.valueOf(30_000), 메뉴그룹.getId());
        상품_1 = ProductFixtureFactory.create("상품_1", BigDecimal.valueOf(10_000));
        상품_2 = ProductFixtureFactory.create("상품_2", BigDecimal.valueOf(20_000));
        메뉴_상품_1 = MenuProductFixtureFactory.create(1L, 메뉴, 상품_1, 1L);
        메뉴_상품_2 = MenuProductFixtureFactory.create(2L, 메뉴, 상품_2, 2L);
        주문테이블 = OrderTableFixtureFactory.createWithGuest(false, 2);
        주문 = OrderFixtureFactory.create(1L, 주문테이블, OrderStatus.COOKING, Lists.newArrayList());
    }

    @DisplayName("OrderLineItem 을 생성할 수 있다. (Menu, Quantity, Order)")
    @Test
    void create01() {
        // given & when
        OrderLineItem 주문항목 = OrderLineItem.of(메뉴, 1L);

        // then
        assertAll(
                () -> assertNotNull(주문항목),
                () -> assertEquals(1L, 주문항목.findQuantity())
        );
    }

    @DisplayName("OrderLineItem 생성 시 Menu가 null이면 예외가 발생한다.")
    @Test
    void create02() {
        // given & when & then
        assertThatExceptionOfType(CreateOrderLineItemException.class)
                .isThrownBy(() -> OrderLineItem.of(null, 1L));
    }

    @DisplayName("OrderLineItem 생성 후 주문 정보에 할당할 수 있다.")
    @Test
    void create03() {
        // given
        OrderLineItem 주문항목 = OrderLineItem.of(메뉴, 1L);

        // when
        주문.addAllOrderLineItems(Lists.newArrayList(주문항목));

        // then
        assertThat(주문.getOrderLineItems().getReadOnlyValues()).containsExactly(주문항목);
    }



}