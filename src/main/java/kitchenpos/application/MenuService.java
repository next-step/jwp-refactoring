package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
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
            throw new IllegalArgumentException();
        }
    }
}
