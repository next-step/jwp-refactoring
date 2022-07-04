package kitchenpos.utils.generator;

import static kitchenpos.ui.MenuRestControllerTest.MENU_API_URL_TEMPLATE;
import static kitchenpos.utils.MockMvcUtil.postRequestBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.CreateMenuRequest;
import kitchenpos.dto.menu.MenuProductRequest;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public class MenuFixtureGenerator {

    public static Menu 메뉴_생성(
        final String name,
        final int price,
        final MenuGroup menuGroup,
        final MenuProduct... menuProducts
    ) {
        return new Menu(name, new BigDecimal(price), menuGroup, Arrays.asList(menuProducts));
    }

    public static MenuProduct 메뉴_구성_상품_생성(
        final Product product,
        final int quantity
    ) {
        return new MenuProduct(product, quantity);
    }

    public static CreateMenuRequest 메뉴_생성_요청_생성(
        final String name,
        final int price,
        final MenuGroup savedMenuGroup,
        final MenuProduct... menuProducts
    ) {
        return new CreateMenuRequest(name, new BigDecimal(price), savedMenuGroup.getId(), 메뉴_상품_요청_목록_생성(menuProducts));
    }

    public static List<MenuProductRequest> 메뉴_상품_요청_목록_생성(final MenuProduct... menuProducts) {
        List<MenuProductRequest> menuProductRequests = new ArrayList<>();
        for (MenuProduct menuProduct : menuProducts) {
            MenuProductRequest menuProductRequest = new MenuProductRequest(menuProduct.getProduct().getId(),
                (int) menuProduct.getQuantity());
            menuProductRequests.add(menuProductRequest);
        }
        return menuProductRequests;
    }

    public static MockHttpServletRequestBuilder 메뉴_생성_요청(
        final String name,
        final int price,
        final MenuGroup savedMenuGroup,
        final MenuProduct... menuProducts
    ) throws Exception {
        return postRequestBuilder(MENU_API_URL_TEMPLATE, 메뉴_생성_요청_생성(name, price, savedMenuGroup, menuProducts));
    }

    public static MockHttpServletRequestBuilder 메뉴_생성_요청(final CreateMenuRequest createMenuRequest) throws Exception {
        return postRequestBuilder(MENU_API_URL_TEMPLATE, createMenuRequest);
    }
}
