package kitchenpos.menu.application.fixture;

import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.menu.dto.MenuProductDto;

public class MenuDtoFixtureFactory {
    private MenuDtoFixtureFactory() {
    }

    public static MenuDto createMenu(MenuGroup menuGroup, String menuName, int menuPrice,
                                     List<MenuProductDto> menuProductDtos) {
        MenuDto menuDto = getNewInstance(MenuDto.class);
        setField(menuDto, "name", menuName);
        setField(menuDto, "price", BigDecimal.valueOf(menuPrice));
        setField(menuDto, "menuGroup", menuGroup);
        setField(menuDto, "menuProductDtos", menuProductDtos);
        return menuDto;
    }

    private static <T> T getNewInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }

            return (T) constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            throw new RuntimeException("Can't generate no args constructor. Class : " + clazz.getName(), e);
        }
    }
}
