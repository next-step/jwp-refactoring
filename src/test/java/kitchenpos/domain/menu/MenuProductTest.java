package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import kitchenpos.application.fixture.MenuFixtureFactory;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.exception.CreateMenuProductException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

class MenuProductTest {

    private MenuGroup 메뉴그룹;
    private Menu 메뉴;
    private Product 상품_1;

    @BeforeEach
    void setUp() {
        메뉴그룹 = MenuGroupFixtureFactory.create("메뉴그룹");
        메뉴 = MenuFixtureFactory.create("메뉴", BigDecimal.valueOf(20_000), 메뉴그룹.getId());
        상품_1 = ProductFixtureFactory.create("상품_1", BigDecimal.valueOf(20_000));
    }

    @DisplayName("MenuProduct를 생성할 수 있다. (Menu, Product, Quantity)")
    @Test
    void create01() {
        // when
        MenuProduct menuProduct = MenuProduct.of(상품_1, 1L);
        메뉴.appendMenuProduct(menuProduct);

        // then
        assertAll(
                () -> assertEquals(메뉴.getId(), menuProduct.getMenuId()),
                () -> assertEquals(1L, menuProduct.findQuantity()),
                () -> assertEquals(상품_1.getId(), menuProduct.getProductId())
        );
    }

    @DisplayName("MenuProduct 생성 시 Product가 존재하지 않으면 생성할 수 없다.")
    @ParameterizedTest
    @NullSource
    void create02(Product product) {
        // when & then
        assertThrows(CreateMenuProductException.class, () -> MenuProduct.of(product, 1L));
    }

    @DisplayName("구성하고 있는 상품 정보를 바탕으로 총 금액을 계산할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"1, 20000", "2, 40000", "3, 60000"})
    void calculate01(long quantity, long price) {
        // given
        MenuProduct menuProduct = MenuProduct.of(상품_1, quantity);

        // when
        BigDecimal totalPrice = menuProduct.calculateTotalPrice();

        // then
        assertThat(totalPrice).isEqualTo(BigDecimal.valueOf(price));
    }
}