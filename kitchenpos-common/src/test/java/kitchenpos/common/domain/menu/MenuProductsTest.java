package kitchenpos.common.domain.menu;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class MenuProductsTest {

    @DisplayName("MenuProducts 는 MenuProduct 리스트로 생성한다.")
    @Test
    void create1() {
        // given
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(MenuProduct.of(1L, 1));
        menuProducts.add(MenuProduct.of(2L, 2));

        // when & then
        assertThatNoException().isThrownBy(() -> MenuProducts.from(menuProducts));
    }

    @DisplayName("MenuProducts 생성 시, MenuProduct 리스트가 존재하지 않으면 예외가 발생한다.")
    @ParameterizedTest
    @NullSource
    void create2(List<MenuProduct> menuProducts) {
        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> MenuProducts.from(menuProducts))
                                            .withMessageContaining("MenuProducts 이 존재하지 않습니다.");
    }
}