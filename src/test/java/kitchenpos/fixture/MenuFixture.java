package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuProductRequestDto;
import kitchenpos.dto.MenuProductResponseDto;
import kitchenpos.dto.MenuRequestDto;
import kitchenpos.dto.MenuResponseDto;

import java.math.BigDecimal;
import java.util.List;

public class MenuFixture {

    public static MenuRequestDto 메뉴_요청_데이터_생성(String name, BigDecimal menuPrice, Long menuGroupId, List<MenuProductRequestDto> menuProducts) {
        return new MenuRequestDto(name, menuPrice, menuGroupId, menuProducts);
    }

    public static Menu 메뉴_데이터_생성(Long id, String name, BigDecimal menuPrice, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        Menu menu = new Menu(id, name, menuPrice, menuGroup);
        menu.addMenuProducts(menuProducts);
        return menu;
    }

    public static MenuResponseDto 메뉴_응답_데이터_생성(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductResponseDto> menuproducts) {
        return new MenuResponseDto(id, name, price, menuGroupId, menuproducts);
    }

}
