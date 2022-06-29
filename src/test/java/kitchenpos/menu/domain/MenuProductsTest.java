package kitchenpos.menu.domain;

import static kitchenpos.utils.DomainFixtureFactory.createMenuProduct;
import static kitchenpos.utils.DomainFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {
    private MenuProduct 양념치킨상품;

    @BeforeEach
    void setUp() {
        Product 양념 = createProduct(1L, "양념", BigDecimal.valueOf(20000L));
        양념치킨상품 = createMenuProduct(양념.id(), 2L);
    }

    @DisplayName("초기화 테스트")
    @Test
    void from() {
        MenuProducts menuProducts = MenuProducts.from(Lists.newArrayList(양념치킨상품));
        assertThat(menuProducts.readOnlyMenuProducts()).isEqualTo(Lists.newArrayList(양념치킨상품));
    }

    @DisplayName("null 경우 테스트")
    @Test
    void ofWithNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> MenuProducts.from(null))
                .withMessage("메뉴상품들이 필요 합니다.");
    }
}
