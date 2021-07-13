package kitchenpos.menu.application;

import kitchenpos.menu.application.exception.NotExistMenusException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.presentation.dto.MenuRequest;
import kitchenpos.menu.presentation.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(MenuRepository menuRepository, MenuGroupService menuGroupService, ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
        List<Product> products = productService.findByIdIn(menuRequest.getProductsIds());
        List<MenuProduct> menuProducts = menuRequest.getMenuProductsBy(products);
        Menu menu = Menu.createWithMapping(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts);
        return MenuResponse.of(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return MenuResponse.ofList(menuRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<Menu> findByIdIn(List<Long> menuIds) {
        List<Menu> menus = menuRepository.findByIdIn(menuIds);
        if (menus.isEmpty()) {
            throw new NotExistMenusException();
        }

        return menus;
    }
}
