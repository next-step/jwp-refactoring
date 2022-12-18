package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;

public class MenuResponseTest {

    public static MenuResponse 메뉴_응답_객체_생성(Long id, String name, BigDecimal price, MenuGroupResponse menuGroup, List<MenuProductResponse> menuProducts) {
        return new MenuResponse.Builder()
                .id(id)
                .name(name)
                .price(price)
                .menuGroup(menuGroup)
                .menuProducts(menuProducts)
                .build();
    }

    public static MenuResponse 메뉴_응답_객체_생성(Menu menu) {
        return MenuResponse.from(menu);
    }

    public static List<MenuResponse> 메뉴_응답_객체들_생성(Menu... menus) {
        return Arrays.stream(menus)
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}