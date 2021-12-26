package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.exception.MenuGroupNotFoundException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.exception.ProductNotFoundException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new MenuGroupNotFoundException(menuRequest.getMenuGroupId()));

        MenuProducts menuProducts = createMenuProducts(menuRequest.getMenuProducts());
        Menu menu = Menu.create(menuRequest.getName(), menuRequest.getPrice(), menuGroup.getId(), menuProducts);
        return MenuResponse.of(menuRepository.save(menu));
    }

    private MenuProducts createMenuProducts(final List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(menuProductRequest.getProductId()));
            menuProducts.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }
        return new MenuProducts(menuProducts);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.ofList(menus);
    }
}
