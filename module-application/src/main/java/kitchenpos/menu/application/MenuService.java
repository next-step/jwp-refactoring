package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.NoSuchMenuGroupException;
import kitchenpos.menu.Menu;
import kitchenpos.menu.MenuGroup;
import kitchenpos.menu.MenuGroupRepository;
import kitchenpos.menu.MenuProduct;
import kitchenpos.menu.MenuProducts;
import kitchenpos.menu.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.Product;
import kitchenpos.product.ProductRepository;

@Service
public class MenuService {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuRepository menuRepository;

    public MenuService(
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository,
            final MenuRepository menuRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
            .orElseThrow(NoSuchMenuGroupException::new);
        List<Product> productList = productRepository.findAllById(menuRequest.getProductIds());
        List<MenuProduct> menuProductList = menuRequest.toMenuProducts(productList);
        Menu menu = menuRepository.save(Menu.create(menuRequest.getName(), menuRequest.getPrice(), menuGroup));
        menu.productsAssginMenu(menuRequest.getPrice(), MenuProducts.of(menuProductList));

        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
            .map(menu -> MenuResponse.of(menu))
            .collect(Collectors.toList());
    }
}
