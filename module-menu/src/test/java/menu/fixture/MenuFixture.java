package menu.fixture;

import java.math.BigDecimal;
import java.util.List;
import menu.domain.Menu;
import menu.domain.MenuGroup;
import menu.domain.MenuProduct;
import menu.dto.MenuProductRequestDto;
import menu.dto.MenuProductResponseDto;
import menu.dto.MenuRequestDto;
import menu.dto.MenuResponseDto;

public class MenuFixture {

    public static MenuRequestDto 메뉴_요청_데이터_생성(String name, BigDecimal menuPrice, Long menuGroupId, List<MenuProductRequestDto> menuProducts) {
        return new MenuRequestDto(name, menuPrice, menuGroupId, menuProducts);
    }

    public static Menu 메뉴_데이터_생성(Long id, String name, BigDecimal menuPrice, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(id, name, menuPrice, menuGroup, menuProducts);

    }

    public static MenuResponseDto 메뉴_응답_데이터_생성(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductResponseDto> menuproducts) {
        return new MenuResponseDto(id, name, price, menuGroupId, menuproducts);
    }

}
