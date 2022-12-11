package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

public class MenuTest {

    public static Menu 메뉴_생성(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu.Builder()
                .id(id)
                .name(name)
                .price(price)
                .menuGroupId(menuGroupId)
                .menuProducts(menuProducts)
                .build();
    }
}