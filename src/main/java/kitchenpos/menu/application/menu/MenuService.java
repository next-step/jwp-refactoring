package kitchenpos.menu.application.menu;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.menu.domain.menu.MenuGroup;
import kitchenpos.menu.domain.menu.MenuGroupRepository;
import kitchenpos.menu.domain.menu.MenuProducts;
import kitchenpos.menu.domain.menu.MenuRepository;
import kitchenpos.menu.domain.product.Product;
import kitchenpos.menu.domain.product.ProductRepository;
import kitchenpos.menu.dto.menu.MenuRequest;
import kitchenpos.menu.dto.menu.MenuResponse;

@Transactional
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

    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
            .orElseThrow(IllegalArgumentException::new);
        final List<Product> products = productRepository.findAllById(menuRequest.getProductIds());

        final Menu menu = new Menu(menuGroup.getName(), menuRequest.getPrice(), menuGroup);
        final Menu savedMenu = menuRepository.save(menu);
        savedMenu.addMenuProducts(MenuProducts.create(savedMenu, products, menuRequest.getMenuProductRequests()));

        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> findAll() {
        return MenuResponse.ofList(menuRepository.findAll());
    }

}
