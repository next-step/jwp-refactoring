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
        Menu menu = menuRequest.toMenu(getMenuGroup(menuRequest));
        MenuProducts menuProducts = getMenuProducts(menuRequest.getMenuProducts());

        menuProducts.validateOverPrice(menu.getPrice());

        menuProducts.initMenu(menu);
        Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.of(savedMenu);
    }

    private MenuGroup getMenuGroup(MenuRequest menuRequest) {
        return menuGroupRepository.findByIdElseThrow(menuRequest.getMenuGroupId());
    }

    private MenuProducts getMenuProducts(List<MenuProductRequest> menuProductRequests) {
        return MenuProducts.of(menuProductRequests.stream()
                .map(this::getMenuProduct)
                .collect(Collectors.toList()));
    }

    private MenuProduct getMenuProduct(MenuProductRequest menuProductRequest) {
        final Product product = productRepository.findByIdElseThrow(menuProductRequest.getProductId());
        return menuProductRequest.toMenuProduct(product);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}
