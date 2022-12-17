package kitchenpos.menu.domain;

import kitchenpos.exception.MenuProductErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("메뉴 상품 단위 테스트")
public class MenuProductTest {
    private MenuGroup 양식;
    private Menu 양식_세트;
    private Product 스테이크;

    @BeforeEach
    void setUp() {
        양식 = new MenuGroup("양식");
        양식_세트 = new Menu("양식 세트", new BigDecimal(43000), 양식);
        스테이크 = new Product("스테이크", new BigDecimal(25000));
    }

    @DisplayName("메뉴, 상품, 수량이 동일하면 메뉴 상품은 동일하다.")
    @Test
    void 메뉴_상품_수량이_동일하면_메뉴_상품은_동일하다() {
        assertEquals(
                new MenuProduct(양식_세트, 스테이크, 1L),
                new MenuProduct(양식_세트, 스테이크, 1L)
        );
    }

    @DisplayName("메뉴가 없는 메뉴 상품을 등록할 수 없다.")
    @Test
    void 메뉴가_없는_메뉴_상품을_등록할_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new MenuProduct(null, 스테이크, 1))
                .withMessage(MenuProductErrorMessage.REQUIRED_MENU.getMessage());
    }

    @DisplayName("상품이 없는 메뉴 상품을 등록할 수 없다.")
    @Test
    void 상품이_없는_메뉴_상품을_등록할_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new MenuProduct(양식_세트, null, 1))
                .withMessage(MenuProductErrorMessage.REQUIRED_PRODUCT.getMessage());
    }

    @DisplayName("수량이 음수인 메뉴 상품을 등록할 수 없다.")
    @Test
    void 수량이_음수인_메뉴_상품을_등록할_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new MenuProduct(양식_세트, 스테이크, -1))
                .withMessage(MenuProductErrorMessage.INVALID_QUANTITY.getMessage());
    }

    @DisplayName("메뉴 상품의 총 금액을 계산한다.")
    @Test
    void 메뉴_상품의_총_금액을_계산한다() {
        MenuProduct menuProduct = new MenuProduct(양식_세트, 스테이크, 2);

        assertEquals((스테이크.getPrice().multiply(new BigDecimal(2))), menuProduct.calculateAmount());
    }
}
