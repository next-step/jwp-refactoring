package kitchenpos.utils.generator;

import static kitchenpos.ui.MenuRestControllerTest.MENU_API_URL_TEMPLATE;
import static kitchenpos.utils.MockMvcUtil.postRequestBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class MenuFixtureGenerator {

    private static String NAME = "맵단 스페샬";
    private static BigDecimal PRICE = BigDecimal.valueOf(33000);
    private static int COUNTER = 0;

    public static Menu generateMenu(
        final MenuGroup menuGroup,
        final Product... products
    ) {
        Menu menu = new Menu();
        menu.setName(NAME + COUNTER);
        menu.setPrice(PRICE);
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(generateMenuProduct(products));
        return menu;
    }

    public static List<MenuProduct> generateMenuProduct(Product... products) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        int lastMenuGroupSeq = 7;
        for (Product product : products) {
            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(product.getId());
            menuProduct.setQuantity(1);
            menuProduct.setSeq((long) lastMenuGroupSeq);
            lastMenuGroupSeq++;

            menuProducts.add(menuProduct);
        }
        return menuProducts;
    }

    public static MockHttpServletRequestBuilder 메뉴_생성_요청(
        final MenuGroup savedMenuGroup,
        final Product... savedProducts
    ) throws Exception {
        return postRequestBuilder(MENU_API_URL_TEMPLATE, generateMenu(savedMenuGroup, savedProducts));
    }
}
