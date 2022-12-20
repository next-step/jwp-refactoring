package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderLineItemTest {
    private MenuGroup 뼈치킨;
    private Menu 뿌링클_세트;
    private Product 뿌링클;
    private Product 치즈볼;
    private OrderTable 주문테이블;
    private Order 주문;

    @BeforeEach
    void setUp() {
        뼈치킨 = new MenuGroup("뼈치킨");
        뿌링클_세트 = new Menu("뼈치킨 세트", BigDecimal.valueOf(26000), 뼈치킨);
        뿌링클 = new Product("뿌링클", BigDecimal.valueOf(18000));
        치즈볼 = new Product("치즈볼", BigDecimal.valueOf(4000));
        주문테이블 = new OrderTable(1, false);
        주문 = new Order(주문테이블, OrderStatus.COOKING);

        뿌링클_세트.create(Arrays.asList(new MenuProduct(뿌링클_세트, 뿌링클, 1L),
                new MenuProduct(뿌링클_세트, 치즈볼, 2L)));
    }

    @DisplayName("등록되지 않은 주문으로 주문 상품을 생성할 수 없다.")
    @Test
    void 등록되지_않은_주문으로_주문_상품_생성() {
        assertThatThrownBy(() -> new OrderLineItem(null, 뿌링클_세트, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 메뉴로 주문 상품을 생성할 수 없다.")
    @Test
    void 등록되지_않은_메뉴로_주문_상품_생성() {
        assertThatThrownBy(() -> new OrderLineItem(주문, null, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("음수 수량으로 주문 상품을 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = { -1, -5, -10, -15 })
    void 음수_수량_주문_상품_생성(long quantity) {
        assertThatThrownBy(() -> new OrderLineItem(주문, 뿌링클_세트, quantity))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
