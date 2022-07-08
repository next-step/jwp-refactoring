package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static kitchenpos.menu.domain.MenuProductTest.상품이_등록된_메뉴_상품_생성;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@DisplayName("MenuProucts Domaian Teste")
public class MenuProductsTest {
    @Test
    void createTest() {
        // given
        List<MenuProduct> menuProductList = 상품이_등록된_메뉴_상품_리스트_생성();
        Menu menu = mock(Menu.class);
        MenuProducts menuProducts = new MenuProducts(menuProductList, menu);

        // then
        assertNotNull(menuProducts.getMenuProducts().get(0).getMenu());
    }

    public List<MenuProduct> 상품이_등록된_메뉴_상품_리스트_생성() {
        List<MenuProduct> menuProductList = new ArrayList<>();
        menuProductList.add(상품이_등록된_메뉴_상품_생성());
        return menuProductList;
    }
}
