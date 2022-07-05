package kitchenpos.menu.application.fixture;

import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductDto;

public class MenuProductDtoFixtureFactory {
    private MenuProductDtoFixtureFactory() {
    }

    public static MenuProductDto createMenuProduct(Long productId, long quantity) {
        return MenuProductDto.of(new MenuProduct(productId,quantity));
    }
}
