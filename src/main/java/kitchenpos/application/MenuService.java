package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.exception.InvalidPriceException;
import kitchenpos.exception.MenuGroupAlreadyExistsException;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final ProductService productService;
    private final MenuGroupService menuGroupService;

    public MenuService(
            final MenuRepository menuRepository,
            final ProductService productService,
            final MenuGroupService menuGroupService
    ) {
        this.menuRepository = menuRepository;
        this.productService = productService;
        this.menuGroupService = menuGroupService;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {

        Long menuGroupId = menuRequest.getMenuGroupId();

        if (menuGroupService.isExists(menuGroupId)) {
            throw new MenuGroupAlreadyExistsException("menuGroup: " + menuGroupId);
        }

        MenuGroup findMenuGroup = menuGroupService.findById(menuGroupId);

        Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), findMenuGroup);

        List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProductRequests();

        menuProductRequests.stream()
                .map(menuProductRequest ->
                        MenuProduct.of(null,
                                productService.getProduct(menuProductRequest.getProductId()),
                                menuProductRequest.getQuantity()))
                .forEach(menu::addMenuProduct);

        if (!menu.isReasonablePrice()) {
            throw new InvalidPriceException("Total Price is higher then expected MenuProduct Price");
        }

        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
