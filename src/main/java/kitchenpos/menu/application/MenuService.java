package kitchenpos.menu.application;

import static kitchenpos.Exception.NotFoundMenuGroupException.NOT_FOUND_MENU_GROUP_EXCEPTION;

import java.util.stream.Collectors;
import kitchenpos.product.application.ProductService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupService menuGroupService,
            final ProductService productService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        validateNotFoundMenuGroup(menuRequest);

        Menu menu = menuRequest.toMenu();
        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();
        List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map((it) -> createMenuProductAddedProduct(it))
                .collect(Collectors.toList());
        menu.addMenuProducts(menuProducts);

        return MenuResponse.from(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return MenuResponse.from(menuRepository.findAll());
    }

    public int countByIdIn(List<Long> ids) {
        return menuRepository.countByIdIn(ids);
    }

    private MenuProduct createMenuProductAddedProduct(MenuProductRequest menuProductRequest) {
        Product product = productService.findProductById(menuProductRequest.getProductId());
        return menuProductRequest.toMenuProduct(product);
    }

    private void validateNotFoundMenuGroup(MenuRequest menuRequest) {
        if (!menuGroupService.existsById(menuRequest.getMenuGroupId())) {
            throw NOT_FOUND_MENU_GROUP_EXCEPTION;
        }
    }
}
