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
import kitchenpos.dto.menu.CreateMenuRequest;
import kitchenpos.dto.menu.MenuProductRequest;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class MenuFixtureGenerator {

    private static String NAME = "맵단 스페샬";
    private static BigDecimal PRICE = BigDecimal.valueOf(33000);
    private static int COUNTER = 0;

    public static Menu 메뉴_생성(
        final MenuGroup menuGroup,
        final Product... products
    ) {
        return new Menu(NAME + COUNTER, PRICE, menuGroup, 메뉴_상품_목록_생성(products));
    }

    public static List<MenuProduct> 메뉴_상품_목록_생성(Product... products) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        int lastMenuGroupSeq = 7;
        for (Product product : products) {
            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProduct(product);
            menuProduct.setQuantity(1);
            menuProduct.setSeq((long) lastMenuGroupSeq);
            lastMenuGroupSeq++;

            menuProducts.add(menuProduct);
        }
        return menuProducts;
    }

    public static CreateMenuRequest 메뉴_생성_요청_생성(
        final MenuGroup savedMenuGroup,
        final Product... savedProducts) {
        return new CreateMenuRequest(NAME, PRICE, savedMenuGroup.getId(), 메뉴_상품_요청_목록_생성(savedProducts));
    }

    public static List<MenuProductRequest> 메뉴_상품_요청_목록_생성(final Product... savedProducts) {
        List<MenuProductRequest> menuProductRequests = new ArrayList<>();
        for (Product savedProduct : savedProducts) {
            MenuProductRequest menuProductRequest = new MenuProductRequest(savedProduct.getId(), 1);
            menuProductRequests.add(menuProductRequest);
        }
        return menuProductRequests;
    }

    public static MockHttpServletRequestBuilder 메뉴_생성_요청(
        final MenuGroup savedMenuGroup,
        final Product... savedProducts
    ) throws Exception {
        return postRequestBuilder(MENU_API_URL_TEMPLATE, 메뉴_생성_요청_생성(savedMenuGroup, savedProducts));
    }
}
