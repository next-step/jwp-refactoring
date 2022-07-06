package kitchenpos.menu;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance.utils.RestAssuredRequest;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.dto.MenuGroupCreateRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class MenuGenerator {
    private static final String MENU_PATH = "/api/menus";
    private static final String MENU_GROUP_PATH = "/api/menu-groups";

    private MenuGenerator() {}

    public static Menu 메뉴_생성(String name, int price, long menuGroupId, MenuProducts menuProducts) {
        return new Menu(name, new Price(new BigDecimal(price)), menuGroupId, menuProducts);
    }

    public static MenuGroup 메뉴_그룹_생성(String name) {
        return new MenuGroup(name);
    }

    public static MenuProducts 메뉴_상품_목록_생성(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public static MenuCreateRequest 메뉴_생성_요청(String name, int price, long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return new MenuCreateRequest(name, new BigDecimal(price), menuGroupId, menuProductRequests);
    }

    public static MenuGroupCreateRequest 메뉴_그룹_생성_요청(String name) {
        return new MenuGroupCreateRequest(name);
    }

    public static MenuProductRequest 메뉴_상품_생성_요청(long productId, Long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public static ExtractableResponse<Response> 메뉴_생성_API_호출(
            String name, int menuPrice, long menuGroupId, List<MenuProductRequest> menuProducts
    ) {
        MenuCreateRequest body = 메뉴_생성_요청(name, menuPrice, menuGroupId, menuProducts);

        return RestAssuredRequest.postRequest(MENU_PATH, Collections.emptyMap(), body);
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_API_호출() {
        return RestAssuredRequest.getRequest(MENU_PATH, Collections.emptyMap());
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_API_호출(String name) {
        return RestAssuredRequest.postRequest(MENU_GROUP_PATH, Collections.emptyMap(), 메뉴_그룹_생성_요청(name));
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_API_호출() {
        return RestAssuredRequest.getRequest(MENU_GROUP_PATH, Collections.emptyMap());
    }

    public static ExtractableResponse<Response> 메뉴_그룹_조회_API_요청(long id) {
        return RestAssuredRequest.getRequest(MENU_GROUP_PATH + "/{id}", Collections.emptyMap(), id);
    }
}
