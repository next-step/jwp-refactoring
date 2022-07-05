package kitchenpos.menu.application.fixture;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductDto;

public class MenuProductDtoFixtureFactory {
    private MenuProductDtoFixtureFactory() {
    }

    public static MenuProductDto createMenuProduct(Long productId, long quantity) {
        return MenuProductDto.of(new MenuProduct(productId, quantity));
    }
}
