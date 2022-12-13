package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductsTest {
    @DisplayName("전체 메뉴 상품의 총 가격의 합을 알 수 있다.")
    @Test
    void totalPrice() {
        // given
        Product product1 = new Product(new Name("불고기"), new Price(BigDecimal.valueOf(12_000)));
        Product product2 = new Product(new Name("잡채"), new Price(BigDecimal.valueOf(8_000)));
        MenuProduct menuProduct1 = new MenuProduct(new Quantity(1L), product1);
        MenuProduct menuProduct2 = new MenuProduct(new Quantity(1L), product2);
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(menuProduct1, menuProduct2));

        // when
        Price result = menuProducts.totalMenuPrice();

        // then
        assertThat(result.value()).isEqualTo(BigDecimal.valueOf(2_0000));
    }

    @DisplayName("메뉴를 설정할 수 있다.")
    @Test
    void setMenu() {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, new Name("한식"));
        Menu menu = new Menu(new Name("불고기"), new Price(BigDecimal.valueOf(12_000)), menuGroup.getId());
        Product product = new Product(new Name("불고기"), new Price(BigDecimal.valueOf(12_000)));
        MenuProduct menuProduct = new MenuProduct(new Quantity(1L), product);
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(menuProduct));

        // when
        menuProducts.setMenu(menu);

        // then
        assertThat(menuProducts.get().get(0).getMenu()).isEqualTo(menu);
    }
}
