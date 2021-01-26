package kitchenpos.application;

import kitchenpos.advice.exception.MenuException;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final ProductService productService;
    private final MenuGroupService menuGroupService;

    public MenuService(
            final MenuRepository menuRepository,
            final ProductService productService,
            final MenuGroupService menuGroupService) {
        this.menuRepository = menuRepository;
        this.productService = productService;
        this.menuGroupService = menuGroupService;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
        Menu menu = menuRequest.toMenu(menuGroup);

        validateExistsMenuGroup(menu);

        final List<MenuProduct> menuProducts = getMenuProducts(menuRequest, menu);
        menu.validatePriceSum(menuProducts);

        return saveMenu(menu, menuProducts);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

    public long countByIdIn(List<Long> ids) {
        return menuRepository.countByIdIn(ids);
    }

    public Menu findById(long id) {
        return menuRepository.findById(id).orElseThrow(() -> new MenuException("메뉴가 존재하지 않습니다", id));
    }

    private Menu saveMenu(Menu menu, List<MenuProduct> menuProducts) {
        final Menu savedMenu = menuRepository.save(menu);
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.updateMenu(savedMenu);
            savedMenuProducts.add(menuProduct);
        }
        savedMenu.updateMenuProducts(savedMenuProducts);
        return savedMenu;
    }

    private void validateExistsMenuGroup(Menu menu) {
        menuGroupService.validateExistsMenuGroup(menu.getMenuGroup());
    }

    private List<MenuProduct> getMenuProducts(MenuRequest menuRequest, Menu menu) {
        final List<MenuProduct> menuProducts = new ArrayList<>();

        for (MenuProductRequest request : menuRequest.getMenuProducts()) {
            Product product = productService.findById(request.getProductId());
            MenuProduct menuProduct = new MenuProduct(menu, product, request.getQuantity());
            menuProducts.add(menuProduct);
        }

        return menuProducts;
    }
}
