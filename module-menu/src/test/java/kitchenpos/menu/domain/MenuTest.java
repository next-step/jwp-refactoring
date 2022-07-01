package kitchenpos.menu.domain;

import static kitchenpos.utils.DomainFixtureFactory.createMenuGroup;
import static kitchenpos.utils.DomainFixtureFactory.createMenuProduct;
import static kitchenpos.utils.DomainFixtureFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {
    private MenuGroup 한마리메뉴;
    private MenuProduct 양념치킨상품;

    @BeforeEach
    void setUp() {
        Product 양념 = createProduct(1L, "양념", BigDecimal.valueOf(20000L));
        한마리메뉴 = createMenuGroup(1L, "한마리메뉴");
        양념치킨상품 = createMenuProduct(양념.id(), 2L);
    }

    @DisplayName("초기화 테스트")
    @Test
    void from() {
        Menu menu = Menu.from("양념치킨", BigDecimal.valueOf(40000L), 한마리메뉴.id(),
                MenuProducts.from(Lists.newArrayList(양념치킨상품)));
        assertThat(menu).isEqualTo(menu);
    }
}
