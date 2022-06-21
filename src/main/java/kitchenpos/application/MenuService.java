package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = this.findMenuGroup(menuRequest.getMenuGroupId());
        Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        List<MenuProduct> menuProducts = this.createMenuProducts(menuRequest);

        menu.appendAllMenuProducts(menuProducts);
        return MenuResponse.from(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return MenuResponse.fromList(menuRepository.findAll());
    }

    public Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(IllegalArgumentException::new);
    }

    public long countByIdIn(List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<MenuProduct> createMenuProducts(MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            Product product = this.findProduct(menuProductRequest.getProductId());
            menuProducts.add(MenuProduct.of(product, menuProductRequest.getQuantity()));
        }

        return menuProducts;
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
