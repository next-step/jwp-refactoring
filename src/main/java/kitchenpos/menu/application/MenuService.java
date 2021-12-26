package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.validator.MenuProductValidator;
import kitchenpos.common.vo.Price;
import kitchenpos.common.vo.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.MenuNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final MenuProductValidator menuProductValidator;

    public MenuService(MenuRepository menuRepository, MenuGroupService menuGroupService,
        MenuProductValidator menuProductValidator) {

        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.menuProductValidator = menuProductValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        Price menuPrice = Price.valueOf(menuRequest.getPrice());
        List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();
        menuProductValidator.validateMenuPriceIsLessThanMenuProductsSum(menuPrice,
            menuProductRequests);

        MenuGroup menuGroup = menuGroupService.findMenuGroupById(menuRequest.getMenuGroupId());
        final List<MenuProduct> menuProducts = createMenuProducts(menuRequest.getMenuProducts());
        Menu menu = new Menu(menuRequest.getName(), Price.valueOf(menuRequest.getPrice()),
            menuGroup, menuProducts);

        Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    private List<MenuProduct> createMenuProducts(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
            .map(menuProductRequest -> createMenuProduct(menuProductRequest))
            .collect(Collectors.toList());
    }

    private MenuProduct createMenuProduct(MenuProductRequest menuProductRequest) {
        return new MenuProduct(menuProductRequest.getProductId(),
            new Quantity(menuProductRequest.getQuantity()));
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return MenuResponse.fromList(menus);
    }

    public Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
            .orElseThrow(MenuNotFoundException::new);
    }
}
