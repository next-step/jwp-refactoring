package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private static final String ERROR_MESSAGE_NOT_EXIST_MENU = "없는 메뉴입니다.";
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(MenuRepository menuRepository,
        MenuGroupService menuGroupService, ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupService.findMenuGroupById(menuRequest.getMenuGroupId());
        final List<MenuProduct> menuProducts = createMenuProducts(menuRequest.getMenuProducts());
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup,
            menuProducts);

        Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    private List<MenuProduct> createMenuProducts(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
            .map(menuProductRequest -> createMenuProduct(menuProductRequest))
            .collect(Collectors.toList());
    }

    private MenuProduct createMenuProduct(MenuProductRequest menuProductRequest) {
        Product product = productService.findProduct(menuProductRequest.getProductId());
        return new MenuProduct(product, menuProductRequest.getQuantity());
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return MenuResponse.fromList(menus);
    }

    public Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
            .orElseThrow(() -> new EntityNotFoundException(ERROR_MESSAGE_NOT_EXIST_MENU));
    }
}
