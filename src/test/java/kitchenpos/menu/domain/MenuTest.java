package kitchenpos.menu.domain;

import static kitchenpos.utils.DomainFixtureFactory.createMenuGroup;
import static kitchenpos.utils.DomainFixtureFactory.createMenuProduct;
import static kitchenpos.utils.DomainFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {
    private MenuProduct 양념치킨상품;
    private MenuGroup 한마리메뉴;

    @BeforeEach
    void setUp() {
        Product 양념 = createProduct(1L, "양념", BigDecimal.valueOf(20000L));
        양념치킨상품 = createMenuProduct(1L, null, 양념, 2L);
        한마리메뉴 = createMenuGroup(1L, "한마리메뉴");
    }

    @DisplayName("초기화 테스트")
    @Test
    void from() {
        Menu menu = Menu.from(1L, "양념치킨", BigDecimal.valueOf(40000L), 한마리메뉴);
        assertThat(menu).isEqualTo(menu);
    }

    @DisplayName("null 경우 테스트")
    @Test
    void ofWithNullMenuGroup() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Menu.from(1L, "양념치킨", BigDecimal.valueOf(40000L), null))
                .withMessage("메뉴그룹이 있어야 합니다.");
    }
}
