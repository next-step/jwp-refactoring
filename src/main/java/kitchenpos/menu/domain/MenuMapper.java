package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuMapper {
    public Menu mapFrom(MenuRequest request) {
        return new Menu(
                request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                getMenuProducts(request));
    }

    private MenuProducts getMenuProducts(MenuRequest request) {
        List<MenuProduct> menuProducts = request.getMenuProducts()
                .stream()
                .map(p -> new MenuProduct(
                        p.getProductId(),
                        p.getQuantity()))
                .collect(Collectors.toList());

        return new MenuProducts(menuProducts);
    }
}
