package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.application.fixture.MenuFixtureFactory;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.application.fixture.MenuProductFixtureFactory;
import kitchenpos.application.fixture.OrderFixtureFactory;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.OrderTable;

class OrderLineItemTest {

    private MenuGroup 고기_메뉴그룹;
    private Product 돼지고기;
    private Product 공기밥;
    private Menu 불고기;
    private MenuProduct 불고기_돼지고기;
    private MenuProduct 불고기_공기밥;
    private OrderTable 주문_개인테이블;
    private Order 불고기_주문;

    @BeforeEach
    void setUp() {
        고기_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "고기 메뉴그룹");
        돼지고기 = ProductFixtureFactory.create(1L, "돼지고기", 9_000);
        공기밥 = ProductFixtureFactory.create(2L, "공기밥", 1_000);
        불고기 = MenuFixtureFactory.create(1L, "불고기", 10_000, 고기_메뉴그룹);

        불고기_돼지고기 = MenuProductFixtureFactory.create(1L, 불고기, 돼지고기, 1L);
        불고기_공기밥 = MenuProductFixtureFactory.create(2L, 불고기, 공기밥, 1L);
        불고기.addMenuProducts(Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        주문_개인테이블 = OrderTableFixtureFactory.create(1L, false);
        불고기_주문 = OrderFixtureFactory.create(1L, 주문_개인테이블.getId(), OrderStatus.COOKING);
    }

    @DisplayName("OrderLineItem 는 Menu, Orders, Quantity 로 생성된다.")
    @Test
    void creat1() {
        // when
        OrderLineItem 불고기_주문항목 = OrderLineItem.of(불고기, 1L);
        불고기_주문.addOrderLineItems(Arrays.asList(불고기_주문항목));

        // then
        assertAll(
            () -> assertEquals(불고기_주문항목.getOrders().getId(), 불고기_주문.getId()),
            () -> assertEquals(불고기_주문항목.getMenu().getId(), 불고기.getId()),
            () -> assertEquals(불고기_주문항목.getQuantity(), Quantity.from(1L))
        );
    }

    @DisplayName("OrderLineItem 생성 시, Menu 가 존재하지 않으면 예외가 발생한다.")
    @Test
    void creat2() {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> OrderLineItem.of(null, 1L))
                                            .withMessageContaining("Menu 가 존재하지 않습니다.");
    }

    @DisplayName("OrderLineItem 생성 시, Order 가 존재하지 않으면 예외가 발생한다.")
    @Test
    void creat3() {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> OrderLineItem.of(1L, null, 불고기, 1L))
                                            .withMessageContaining("Order 가 존재하지 않습니다.");
    }
}