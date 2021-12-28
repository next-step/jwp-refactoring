package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupService menuGroupService,
        final ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());

        final Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        menu.addMenuProducts(makeMenuProducts(menu, menuRequest.getMenuProductRequests()));

        final Menu persistMenu = menuRepository.save(menu);
        return MenuResponse.of(persistMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
            .map(MenuResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Menu findById(final Long menuId) {
        return menuRepository.findById(menuId)
            .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 메뉴 입니다."));
    }

    private List<MenuProduct> makeMenuProducts(final Menu menu,
        final List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productService.findById(menuProductRequest.getProductId());
            menuProducts.add(new MenuProduct(menu, product, menuProductRequest.getQuantity()));
        }

        return menuProducts;
    }
}
