package kitchenpos.menu.service;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.service.MenuGroupServiceJpa;
import kitchenpos.product.service.ProductServiceJpa;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceJpa {

    private final MenuRepository menuRepository;
    private final MenuGroupServiceJpa menuGroupService;
    private final ProductServiceJpa productService;

    public MenuServiceJpa(MenuRepository menuRepository, MenuGroupServiceJpa menuGroupService, ProductServiceJpa productService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        checkProductsEmpty(menuRequest);
        MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
        kitchenpos.menu.domain.Menu menu = new kitchenpos.menu.domain.Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        List<MenuProduct> menuProducts = menuRequest.getMenuProducts()
                .stream()
                .map(request -> new MenuProduct(menu, productService.findById(request.getProductId()), request.getQuantity()))
                .collect(Collectors.toList());
        menu.addProducts(menuProducts);
        return MenuResponse.ofMenu(menuRepository.save(menu));
    }

    private void checkProductsEmpty(MenuRequest menuRequest) {
        if (menuRequest.isEmptyProducts()) {
            throw new IllegalArgumentException("상품이 비어있습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::ofMenu)
                .collect(Collectors.toList());
    }

}
