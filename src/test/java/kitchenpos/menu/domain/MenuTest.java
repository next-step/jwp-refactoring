package kitchenpos.menu.domain;

import static kitchenpos.utils.DomainFixtureFactory.createMenuGroup;
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

class MenuTest {
    private MenuGroup 한마리메뉴;

    @BeforeEach
    void setUp() {
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

    @DisplayName("메뉴상품들 추가 테스트 테스트")
    @Test
    void addMenuProducts() {
        Menu menu = Menu.from("양념치킨", BigDecimal.valueOf(40000L), 한마리메뉴);
        Product 양념 = createProduct(1L, "양념", BigDecimal.valueOf(20000L));
        MenuProduct 양념치킨상품 = createMenuProduct(양념, 2L);
        menu.addMenuProducts(MenuProducts.from(Lists.newArrayList(양념치킨상품)));
        assertThat(menu.readOnlyMenuProducts()).containsExactlyElementsOf(Lists.newArrayList(양념치킨상품));
    }

    @DisplayName("메뉴금액이 상품들 총합보다 큰 경우 테스트")
    @Test
    void validateMenuProducts() {
        Menu menu = Menu.from("양념치킨", BigDecimal.valueOf(50000L), 한마리메뉴);
        Product 양념 = createProduct(1L, "양념", BigDecimal.valueOf(20000L));
        MenuProduct 양념치킨상품 = createMenuProduct(양념, 2L);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menu.addMenuProducts(MenuProducts.from(Lists.newArrayList(양념치킨상품))))
                .withMessage("메뉴 가격은 상품의 총 금액을 넘길 수 없습니다.");
    }
}
