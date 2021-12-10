package kitchenpos.menu.aplication;

import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.menu.domain.menu.MenuRepository;
import kitchenpos.menu.domain.menugroup.MenuGroup;
import kitchenpos.menu.domain.menuproduct.MenuProductRepository;
import kitchenpos.menu.domain.product.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(MenuRepository menuRepository, MenuProductRepository menuProductRepository, MenuGroupService menuGroupService, ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse saveMenu(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());

        final Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        setMenuProduct(menu, menuRequest.getMenuProductRequests());

        return MenuResponse.of(menuRepository.save(menu));
    }

    private void setMenuProduct(Menu menu, List<MenuProductRequest> menuRequest) {
        menuRequest.forEach(it -> {
            Product product = productService.findById(it.getProductId());
            menu.addMenuProduct(product, it.getQuantity());
        });
    }

    public List<MenuResponse> findMenus() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}
