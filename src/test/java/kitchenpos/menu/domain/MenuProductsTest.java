package kitchenpos.menu.domain;

import static kitchenpos.utils.DomainFixtureFactory.createMenuProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {
    MenuProduct 양념치킨상품;

    @BeforeEach
    void setUp() {
        양념치킨상품 = createMenuProduct(1L, null, 1L, 2L);
    }

    @DisplayName("초기화 테스트")
    @Test
    void of() {
        MenuProducts menuProducts = MenuProducts.of(Lists.newArrayList(양념치킨상품));
        assertThat(menuProducts.readOnlyMenuProducts()).isEqualTo(Lists.newArrayList(양념치킨상품));
    }

    @DisplayName("null 경우 테스트")
    @Test
    void ofWithNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> MenuProducts.of(null))
                .withMessage("메뉴상품들이 필요 합니다.");
    }
}
