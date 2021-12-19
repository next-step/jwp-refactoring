package kitchenpos.domain;

import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.exception.ProductNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuFactory {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuFactory(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public Menu create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new MenuGroupNotFoundException(menuRequest.getMenuGroupId()));

        Menu menu = menuGroup.createMenu(menuRequest.getName(), menuRequest.getPrice());
        addMenuProducts(menuRequest.getMenuProducts(), menu);

        return menu;
    }

    private void addMenuProducts(final List<MenuProductRequest> menuProducts, final Menu menu) {
        for (final MenuProductRequest menuProductRequest : menuProducts) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(menuProductRequest.getProductId()));
            menu.addProduct(product, menuProductRequest.getQuantity());
        }
    }
}
