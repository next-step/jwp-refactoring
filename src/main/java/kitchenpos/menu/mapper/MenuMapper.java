package kitchenpos.menu.mapper;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MenuMapper {

    private MenuMapper() {
    }

    public static MenuResponse toMenuResponse(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroup().getId(), getMenuProduct(menu));
    }

    public static List<MenuResponse> toMenuResponses(List<Menu> menus) {
        return menus.stream()
                .map(MenuMapper::toMenuResponse)
                .collect(Collectors.toList());

    }

    private static List<MenuResponse.MenuProduct> getMenuProduct(final Menu menu) {
        return menu.getMenuProducts()
                .stream()
                .map(menuProduct -> new MenuResponse.MenuProduct(menuProduct.getSeq(), menuProduct.getMenu().getId(), menuProduct.getProduct().getId(), menuProduct.getQuantity()))
                .collect(Collectors.toList());
    }
}
