package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.common.error.ErrorEnum;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderLineItemTest {
    private OrderMenu 등록되지않은_메뉴;
    private Order 주문;

    @BeforeEach
    void setUp() {
        MenuGroup 후라이드_치킨 = new MenuGroup(new Name("후라이드 치킨"));
        Menu 치킨콜라_세트 = new Menu(new Name("치킨콜라 세트"), new Price(BigDecimal.valueOf(18_000L)), 후라이드_치킨);
        Product 콜라 = new Product(new Name("콜라"), new Price(BigDecimal.valueOf(1_800L)));
        OrderTable 주문테이블 = new OrderTable(new NumberOfGuests(1), false);
        주문 = Order.of(주문테이블.getId(), null);

        치킨콜라_세트.create(Arrays.asList(new MenuProduct(null, new Quantity(1L), 치킨콜라_세트, 콜라)));

        등록되지않은_메뉴 = OrderMenu.of(치킨콜라_세트);
    }

    @Test
    void 등록되지_않은_주문으로_주문_상품_생성시_오류() {
        assertThatThrownBy(() -> new OrderLineItem(null, 등록되지않은_메뉴, new Quantity(1L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.ORDER_TABLE_IS_EMPTY.message());
    }

    @Test
    void 등록되지_않은_메뉴로_주문_상품_생성시_예외() {
        assertThatThrownBy(() -> new OrderLineItem(주문, null, new Quantity(1L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.REQUIRED_MENU.message());
    }

}
