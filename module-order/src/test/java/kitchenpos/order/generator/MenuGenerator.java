package kitchenpos.order.generator;

import kitchenpos.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;

import java.math.BigDecimal;
import java.util.List;

public class MenuGenerator {
    private static final String MENU_PATH = "/api/menus";

    private MenuGenerator() {}

    public static Menu 메뉴_생성(String name, int price, long menuGroupId, MenuProducts menuProducts) {
        return new Menu(name, new Price(new BigDecimal(price)), menuGroupId, menuProducts);
    }

    public static MenuProducts 메뉴_상품_목록_생성(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public static MenuCreateRequest 메뉴_생성_요청(String name, int price, long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return new MenuCreateRequest(name, new BigDecimal(price), menuGroupId, menuProductRequests);
    }

    public static MenuProductRequest 메뉴_상품_생성_요청(long productId, Long quantity) {
        return new MenuProductRequest(productId, quantity);
    }
}
