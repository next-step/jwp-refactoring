package api.menu;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * packageName : kitchenpos.fixtures
 * fileName : MenuFixtures
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
public class MenuFixtures {
    public static MenuRequest 양념치킨두마리메뉴요청() {
        return MenuRequest.of(
                "양념치킨두마리메뉴",
                new BigDecimal(32000),
                1L,
                asList(MenuProductRequest.of(2L, 2L))
        );
    }

    public static MenuRequest 후라이드두마리메뉴요청() {
        return MenuRequest.of(
                "두마리메뉴",
                new BigDecimal(32000),
                3L,
                asList(MenuProductRequest.of(1L, 2L))
        );
    }

    public static MenuRequest 후라이드두마리메뉴요청(BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return MenuRequest.of(
                "두마리메뉴",
                price,
                menuGroupId,
                menuProductRequests
        );
    }

    public static MenuRequest 후라이드반양념반메뉴요청(BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return MenuRequest.of(
                "후라이드반양념반메뉴",
                price,
                menuGroupId,
                menuProductRequests
        );
    }

    public static MenuRequest 메뉴등록요청(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return MenuRequest.of(
                name,
                price,
                menuGroupId,
                menuProductRequests
        );
    }
}

