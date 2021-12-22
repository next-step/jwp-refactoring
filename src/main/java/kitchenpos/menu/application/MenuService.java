package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menu.exception.ProductNotFoundException;
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

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        MenuProducts menuProducts = createMenuProducts(menuRequest.getMenuProducts(), menu);
        menu.setMenuProducts(menuProducts);
        return MenuResponse.of(menuRepository.save(menu));
    }

    private MenuProducts createMenuProducts(final List<MenuProductRequest> menuProductRequests, Menu menu) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(menuProductRequest.getProductId()));
            menuProducts.add(new MenuProduct(menu, product, menuProductRequest.getQuantity()));
        }
        return new MenuProducts(menuProducts);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.ofList(menus);
    }
}
