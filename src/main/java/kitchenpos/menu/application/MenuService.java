package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {

        existsCheckByGroupId(menuRequest);

        final Menu savedMenu = menuRepository.save(new Menu(menuRequest.getName(), menuRequest.getPrice(), findMenuGroup(menuRequest)));

        List<MenuProduct> menuProducts = menuRequest.getMenuProducts().stream()
                .map(menuProduct -> new MenuProduct(savedMenu, findProduct(menuProduct), menuProduct.getQuantity())).collect(Collectors.toList());

        savedMenu.mappingProducts(new MenuProducts(menuProductRepository.saveAll(menuProducts)));
        savedMenu.validateMenuProductsPrice();

        return MenuResponse.of(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.mappingProducts(new MenuProducts(menuProductRepository.findAllByMenuId(menu.id())));
        }

        return MenuResponse.ofList(menus);
    }

    private void existsCheckByGroupId(MenuRequest menuRequest) {
        if (!menuGroupRepository.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private Product findProduct(MenuProductRequest menuProduct) {
        return productRepository.findById(menuProduct.getProductId()).orElseThrow(IllegalArgumentException::new);
    }

    private MenuGroup findMenuGroup(MenuRequest menuRequest) {
        return menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElseThrow(IllegalArgumentException::new);
    }
}
