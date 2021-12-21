package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        addMenuProducts(menuRequest.getMenuProducts(), menu);
        return MenuResponse.of(menuRepository.save(menu));
    }

    private void addMenuProducts(final List<MenuProductRequest> menuProducts, final Menu menu) {
        for (final MenuProductRequest menuProductRequest : menuProducts) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(menuProductRequest.getProductId()));
            menu.addProduct(product, menuProductRequest.getQuantity());
        }
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.ofList(menus);
    }
}
