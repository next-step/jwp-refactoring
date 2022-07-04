package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuMapper {
    private ProductRepository productRepository;

    public MenuMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

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
