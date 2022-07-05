package kitchenpos.menu.application.fixture;

import static java.util.stream.Collectors.toList;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.menu.dto.MenuProductDto;

public class MenuDtoFixtureFactory {
    private MenuDtoFixtureFactory() {
    }

    public static MenuDto createMenu(MenuGroup menuGroup, String menuName, int menuPrice,
                                     List<MenuProductDto> menuProductDtos) {
        List<MenuProduct> menuProducts = menuProductDtos.stream()
                .map(menuProductDto -> new MenuProduct(menuProductDto.getProductId(),menuProductDto.getQuantity()))
                .collect(toList());
        return MenuDto.of(new Menu(menuName,new BigDecimal(menuPrice),menuGroup, menuProducts));
    }
}
