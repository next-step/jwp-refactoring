package kitchenpos.domain;

import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
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
                .orElseThrow(IllegalArgumentException::new);

        final List<MenuProductRequest> menuProducts = menuRequest.getMenuProducts();

        Menu menu = menuGroup.createMenu(menuRequest.getName(), menuRequest.getPrice());
        for (final MenuProductRequest menuProductRequest : menuProducts) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            menu.addProduct(product, menuProductRequest.getQuantity());
        }

        return menu;
    }
}
