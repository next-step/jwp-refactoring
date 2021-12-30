package kitchenpos.application;

import kitchenpos.common.exceptions.NotFoundEntityException;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
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

    public MenuService(final MenuGroupService menuGroupService, final MenuRepository menuRepository, final ProductService productService) {
        this.menuGroupService = menuGroupService;
        this.menuRepository = menuRepository;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        final MenuGroup menuGroup = menuGroupService.findMenuGroupById(request.getMenuGroupId());
        final Menu menu = request.toMenu(menuGroup);

        final List<MenuProduct> menuProductList = getMenuProductList(request.getMenuProducts());
        menu.addMenuProducts(menuProductList);
        return MenuResponse.from(menuRepository.save(menu));
    }

    private List<MenuProduct> getMenuProductList(final List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream().map(this::getMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct getMenuProduct(final MenuProductRequest request) {
        final Product product = productService.getById(request.getProductId());
        return request.toMenuProduct(product);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    public Menu getMenuById(final Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(NotFoundEntityException::new);
    }
}
