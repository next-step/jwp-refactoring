package kitchenpos.menu.domain;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.dto.MenuRequest;

public class MenuMapper {
    private final MenuRequest menuRequest;

    public MenuMapper(final MenuValidator menuValidator, final MenuRequest menuRequest) {
        this.menuRequest = menuRequest;
        menuValidator.validate(this.menuRequest);
    }

    public Menu toMenu() {
        return new Menu(menuRequest.getName(), new Price(menuRequest.getPrice()), menuRequest.getMenuGroupId());
    }

    public List<MenuProduct> toMenuProducts(final Menu saved) {
        return menuRequest.getMenuProducts().stream()
            .map(request -> new MenuProduct(saved, request.getProductId(), request.getQuantity()))
            .collect(Collectors.toList());
    }
}
