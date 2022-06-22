package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.google.common.collect.Lists;
import java.math.BigDecimal;
import java.util.List;
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
import kitchenpos.exception.EmptyOrderLineItemsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class OrderLineItemsTest {

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

    @DisplayName("OrderLineItems 는 OrderLineItem 리스트로 생성한다.")
    @Test
    void creat01() {
        // given
        List<OrderLineItem> orderLineItems = Lists.newArrayList();
        orderLineItems.add(OrderLineItem.of(메뉴, 1L));
        // when & then
        assertThatNoException().isThrownBy(() -> OrderLineItems.from(orderLineItems));
    }

    @DisplayName("OrderLineItems 생성 시, OrderLineItem 리스트가 존재하지 않으면 예외가 발생한다.")
    @ParameterizedTest
    @NullSource
    void creat2(List<OrderLineItem> orderLineItems) {
        // when & then
        assertThatExceptionOfType(EmptyOrderLineItemsException.class)
                .isThrownBy(() -> OrderLineItems.from(orderLineItems));
    }
}