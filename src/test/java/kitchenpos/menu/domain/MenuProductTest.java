package kitchenpos.menu.domain;

import kitchenpos.common.ErrorCode;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @DisplayName("MenuProduct의 동등성을 테스트 한다.")
    @Test
    void equalsTest() {
        assertEquals(new MenuProduct(양식_세트, 스테이크, 1L), new MenuProduct(양식_세트, 스테이크, 1L));
    }

    @DisplayName("메뉴가 없는 메뉴 상품을 등록할 수 없다.")
    @Test
    void makeExceptionWhenMenuIsNull() {
        assertThatThrownBy(() -> {
            new MenuProduct(null, 스테이크, 1);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.INVALID_FORMAT_MENU.getErrorMessage());
    }

    @DisplayName("상품이 없는 메뉴 상품을 등록할 수 없다.")
    @Test
    void makeExceptionWhenProductIsNull() {
        assertThatThrownBy(() -> {
            new MenuProduct(양식_세트, null, 1);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.INVALID_FORMAT_PRODUCT.getErrorMessage());
    }

    @DisplayName("수량이 음수인 메뉴 상품을 등록할 수 없다.")
    @ParameterizedTest
    @ValueSource(longs = { -1, -10, -50, -100 })
    void makeExceptionWhenQuantityIsNegative(long quantity) {
        assertThatThrownBy(() -> {
            new MenuProduct(양식_세트, 스테이크, quantity);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.INVALID_FORMAT_MENU_QUANTITY.getErrorMessage());
    }
}
