package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import kitchenpos.domain.product.Product;

class MenuProductsTest {

    @DisplayName("MenuProducts 는 MenuProduct 리스트로 생성한다.")
    @Test
    void create1() {
        // given
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(MenuProduct.of(Product.from(1L), 1));
        menuProducts.add(MenuProduct.of(Product.from(2L), 2));

        // when & then
        assertThatNoException().isThrownBy(() -> MenuProducts.from(menuProducts));
    }

    @DisplayName("MenuProducts 생성 시, MenuProduct 리스트가 null 이면 예외가 발생한다.")
    @ParameterizedTest
    @NullSource
    void create2(List<MenuProduct> menuProducts) {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> MenuProducts.from(menuProducts))
                                            .withMessageContaining("MenuProducts 이 유효하지 않습니다.");
    }
}