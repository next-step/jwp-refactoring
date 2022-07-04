package kitchenpos.menu.application.fixture;

import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import kitchenpos.menu.dto.MenuProductDto;

public class MenuProductDtoFixtureFactory {
    private MenuProductDtoFixtureFactory() {
    }

    public static MenuProductDto createMenuProduct(Long productId, long quantity) {
        MenuProductDto menuProductDto = getNewInstance(MenuProductDto.class);
        setField(menuProductDto, "productId", productId);
        setField(menuProductDto, "quantity", quantity);
        return menuProductDto;
    }

    public static MenuProductDto createMenuProduct(Long seq, Long productId, long quantity) {
        MenuProductDto menuProductDto = createMenuProduct(productId, quantity);
        setField(menuProductDto, "seq", seq);
        return menuProductDto;
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
