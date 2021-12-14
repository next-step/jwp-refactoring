package kitchenpos.domain.menu.application;

import kitchenpos.domain.menu.domain.MenuRepository;
import kitchenpos.domain.menu_group.domain.MenuGroupRepository;
import kitchenpos.domain.product.domain.ProductRepository;
import kitchenpos.domain.menu.domain.Menu;
import kitchenpos.domain.menu_group.domain.MenuGroup;
import kitchenpos.domain.menu.domain.MenuProduct;
import kitchenpos.domain.product.domain.Product;
import kitchenpos.domain.menu.dto.MenuProductRequest;
import kitchenpos.domain.menu.dto.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuRequest request) {
        final MenuGroup menuGroup = getMenuGroup(request.getMenuGroupId());
        final List<MenuProduct> menuProducts = getMenuProducts(request);
        final Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup, menuProducts);

        menu.checkPrice();

        return menuRepository.save(menu);
    }

    private List<MenuProduct> getMenuProducts(MenuRequest request) {
        return request.getMenuProducts().stream()
                .map(this::getMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuGroup getMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private MenuProduct getMenuProduct(MenuProductRequest menuProductRequest) {
        return new MenuProduct(getProduct(menuProductRequest), menuProductRequest.getQuantity());
    }

    private Product getProduct(MenuProductRequest menuProductRequest) {
        return productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
