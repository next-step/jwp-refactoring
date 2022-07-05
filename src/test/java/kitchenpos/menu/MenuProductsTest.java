package kitchenpos.menu;

import kitchenpos.global.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static kitchenpos.menu.MenuProductTest.상품이_등록된_메뉴_상품_생성;
import static kitchenpos.menu.MenuProductTest.상품이_등록안된_메뉴_상품_생성;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("MenuProucts Domaian Teste")
public class MenuProductsTest {
    @Test
    void validMenuTest() {
        // given
        List<MenuProduct> menuProductList = 상품이_등록안된_메뉴_상품_리스트_생성();

        // then
        assertThatThrownBy(() -> {
            new MenuProducts(menuProductList, null, null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void validComparePriceTest() {
        // given
        List<MenuProduct> menuProductList = 상품이_등록된_메뉴_상품_리스트_생성();

        // then
        assertThatThrownBy(() -> {
            new MenuProducts(menuProductList, new Price(new BigDecimal(1000)), null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    public List<MenuProduct> 상품이_등록안된_메뉴_상품_리스트_생성() {
        List<MenuProduct> menuProductList = new ArrayList<>();
        menuProductList.add(상품이_등록안된_메뉴_상품_생성());
        menuProductList.add(상품이_등록된_메뉴_상품_생성());
        return menuProductList;
    }

    public List<MenuProduct> 상품이_등록된_메뉴_상품_리스트_생성() {
        List<MenuProduct> menuProductList = new ArrayList<>();
        menuProductList.add(상품이_등록된_메뉴_상품_생성());
        return menuProductList;
    }
}
