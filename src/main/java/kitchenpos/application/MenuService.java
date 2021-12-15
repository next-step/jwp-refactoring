package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import kitchenpos.utils.StreamUtils;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = findMenuGroup(menuRequest.getMenuGroupId());
        Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        List<MenuProduct> menuProducts = createMenuProducts(menuRequest);

        menu.addMenuProducts(menuProducts);

        return MenuResponse.from(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return StreamUtils.mapToList(menus, MenuResponse::from);
    }

    private List<MenuProduct> createMenuProducts(MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            Product product = findProduct(menuProductRequest.getProductId());
            MenuProduct menuProduct = MenuProduct.of(product, menuProductRequest.getQuantity());

            menuProducts.add(menuProduct);
        }

        return menuProducts;
    }

    private MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                                  .orElseThrow(EntityNotFoundException::new);
    }

    private Product findProduct(Long productId) {
        return productRepository.findById(productId)
                                .orElseThrow(EntityNotFoundException::new);
    }
}
