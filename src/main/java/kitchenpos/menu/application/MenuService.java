package kitchenpos.menu.application;

import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.exception.ProductNotFoundException;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuGroupResponse;
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
        final Long menuGroupId = menuRequest.getMenuGroupId();
        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProductRequests();
        final MenuGroup menuGroup = getMenuGroup(menuGroupId);
        final List<MenuProduct> menuProducts = getMenuProducts(menuProductRequests);

        final Menu menu = menuRequest.toMenu(menuGroup, menuProducts);
        final Menu persistMenu = menuRepository.save(menu);

        return MenuResponse.of(persistMenu);
    }

    @Transactional(readOnly = true)
    public MenuGroup getMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(MenuGroupNotFoundException::new);
    }

    private List<MenuProduct> getMenuProducts(List<MenuProductRequest> request) {
        return request.stream()
                .map(this::getMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct getMenuProduct(MenuProductRequest request) {
        final long productId = request.getProductId();
        final int quantity = request.getQuantity();

        return MenuProduct.of(getProduct(productId), quantity);
    }

    @Transactional(readOnly = true)
    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }


    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}
