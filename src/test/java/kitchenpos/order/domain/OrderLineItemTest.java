package kitchenpos.order.domain;

import kitchenpos.exception.ErrorMessage;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("주문 상품 단위 테스트")
class OrderLineItemTest {
    private OrderTable 우아한_주문_테이블_1;
    private Order 첫번째_주문;
    private Product 짬뽕;
    private Product 탕수육;
    private MenuGroup 중식;
    private Menu 중식_세트;

    @BeforeEach
    void setUp() {
        우아한_주문_테이블_1 = new OrderTable(1, false);
        첫번째_주문 = new Order(우아한_주문_테이블_1, OrderStatus.COOKING);
        짬뽕 = new Product("짬뽕", 8000);
        탕수육 = new Product("탕수육", 18000);
        중식 = new MenuGroup("중식");
        중식_세트 = new Menu("중식 세트", 26000, 중식);
        중식_세트.create(Arrays.asList(
                new MenuProduct(중식_세트, 짬뽕, 1L),
                new MenuProduct(중식_세트, 탕수육, 1L)));
    }

    @DisplayName("주문이 존재하지 않으면 주문 상품을 생성할 수 없다.")
    @Test
    void 주문이_존재하지_않으면_주문_상품을_생성할_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new OrderLineItem(null, 중식_세트, 1L))
                .withMessage(ErrorMessage.ORDER_LINE_ITEM_REQUIRED_ORDER.getMessage());
    }

    @DisplayName("메뉴가 존재하지 않으면 주문 상품을 생성할 수 없다.")
    @Test
    void 메뉴가_존재하지_않으면_주문_상품을_생성할_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new OrderLineItem(첫번째_주문, null, 1L))
                .withMessage(ErrorMessage.ORDER_LINE_ITEM_REQUIRED_MENU.getMessage());
    }

    @DisplayName("수량이 음수이면 주문 상품을 생성할 수 없다.")
    @Test
    void 수량이_음수이면_주문_상품을_생성할_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new OrderLineItem(첫번째_주문, 중식_세트, -1))
                .withMessage(ErrorMessage.ORDER_LINE_ITEM_INVALID_QUANTITY.getMessage());
    }
}
