package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.order.exception.OrderLineItemExceptionCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 상품 클래스 테스트")
class OrderLineItemTest {

    private MenuGroup 양식;
    private Menu 양식_세트;
    private Product 스파게티;
    private Product 에이드;
    private OrderTable 주문테이블;
    private Order 주문;

    @BeforeEach
    void setUp() {
        양식 = new MenuGroup("양식");
        양식_세트 = new Menu("양식 세트", new BigDecimal(26000), 양식);
        스파게티 = new Product("스파게티", new BigDecimal(18000));
        에이드 = new Product("에이드", new BigDecimal(4000));
        주문테이블 = new OrderTable(1, false);
        주문 = new Order(주문테이블, OrderStatus.COOKING);

        양식_세트.create(Arrays.asList(new MenuProduct(양식_세트, 스파게티, 1L),
                new MenuProduct(양식_세트, 에이드, 2L)));
    }

    @Test
    void 주문이_존재하지_않으면_주문_상품을_생성할_수_없음() {
        assertThatThrownBy(() -> {
            new OrderLineItem(null, 양식_세트, 1L);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderLineItemExceptionCode.REQUIRED_ORDER.getMessage());
    }

    @Test
    void 메뉴가_존재하지_않으면_주문_상품을_생성할_수_없음() {
        assertThatThrownBy(() -> {
            new OrderLineItem(주문, null, 1L);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderLineItemExceptionCode.REQUIRED_MENU.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, -5, -10, -15 })
    void 수량이_음수이면_주문_상품을_생성할_수_없음(long quantity) {
        assertThatThrownBy(() -> {
            new OrderLineItem(주문, 양식_세트, quantity);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderLineItemExceptionCode.INVALID_QUANTITY.getMessage());
    }
}
