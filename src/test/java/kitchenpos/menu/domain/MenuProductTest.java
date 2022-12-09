package kitchenpos.menu.domain;

import kitchenpos.menu.exception.MenuProductExceptionCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("메뉴 상품 클래스 테스트")
class MenuProductTest {

    private MenuGroup 양식;
    private Menu 양식_세트;
    private Product 스테이크;

    @BeforeEach
    void setUp() {
        양식 = new MenuGroup("양식");
        양식_세트 = new Menu("양식 세트", new BigDecimal(43000), 양식);
        스테이크 = new Product("스테이크", new BigDecimal(25000));
    }

    @Test
    void 동등성_테스트() {
        assertEquals(new MenuProduct(양식_세트, 스테이크, 1L), new MenuProduct(양식_세트, 스테이크, 1L));
    }

    @Test
    void 메뉴가_없는_메뉴_상품을_등록할_수_없음() {
        assertThatThrownBy(() -> {
            new MenuProduct(null, 스테이크, 1);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuProductExceptionCode.REQUIRED_MENU.getMessage());
    }

    @Test
    void 상품이_없는_메뉴_상품을_등록할_수_없음() {
        assertThatThrownBy(() -> {
            new MenuProduct(양식_세트, null, 1);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuProductExceptionCode.REQUIRED_PRODUCT.getMessage());
    }

    @ParameterizedTest
    @ValueSource(longs = { -1, -10, -50, -100 })
    void 수량이_음수인_메뉴_상품을_등록할_수_없음(long quantity) {
        assertThatThrownBy(() -> {
            new MenuProduct(양식_세트, 스테이크, quantity);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuProductExceptionCode.INVALID_QUANTITY.getMessage());
    }

    @Test
    void 메뉴_상품의_총_금액을_계산함() {
        MenuProduct menuProduct = new MenuProduct(양식_세트, 스테이크, 2);
        assertEquals((스테이크.getPrice().multiply(new BigDecimal(2))), menuProduct.calculateAmount());
    }
}
