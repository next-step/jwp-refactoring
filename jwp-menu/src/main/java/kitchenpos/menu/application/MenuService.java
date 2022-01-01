package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.EmptyMenuException;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;
    private final MenuValidator menuValidator;

    public MenuService(final MenuGroupService menuGroupService, final MenuRepository menuRepository
            , final ProductService productService, final MenuValidator menuValidator) {
        this.menuGroupService = menuGroupService;
        this.menuRepository = menuRepository;
        this.productService = productService;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        final MenuGroup menuGroup = menuGroupService.findMenuGroupById(request.getMenuGroupId());
        final Menu menu = request.toMenu(menuGroup);

        final List<MenuProduct> menuProductList = getMenuProductList(request.getMenuProducts());
        menu.addMenuProducts(menuProductList);
        menuValidator.isOverPrice(menu);
        return MenuResponse.from(menuRepository.save(menu));
    }

    private List<MenuProduct> getMenuProductList(final List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream().map(this::getMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct getMenuProduct(final MenuProductRequest request) {
        final Product product = productService.getById(request.getProductId());
        return MenuProduct.of(product.getId(), request.getQuantity());
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    public Menu getMenuById(final Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(EmptyMenuException::new);
    }
}
