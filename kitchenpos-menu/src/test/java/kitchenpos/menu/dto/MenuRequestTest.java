package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequestTest {

    public static MenuRequest 메뉴_요청_객체_생성(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest.Builder()
                .name(name)
                .price(price)
                .menuGroupId(menuGroupId)
                .menuProductRequests(menuProductRequests)
                .build();
    }
}