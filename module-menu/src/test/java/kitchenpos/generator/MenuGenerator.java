package kitchenpos.generator;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;

import java.math.BigDecimal;
import java.util.List;

public class MenuGenerator {

    private MenuGenerator() {}

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
