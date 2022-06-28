package kitchenpos.menu.application;

import kitchenpos.product.application.ProductService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
    public MenuResponse create(final MenuRequest request) {
        Menu persistMenu = menuRepository.save(toEntity(request));
        return MenuResponse.of(persistMenu);
    }

    private Menu toEntity(final MenuRequest request) {
        List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(menuProduct -> new MenuProduct(
                        productService.findProductById(menuProduct.getId()),
                        menuProduct.getQuantity()))
                .collect(Collectors.toList());
        MenuGroup menuGroup = menuGroupService.findMenuGroupById(request.getMenuGroupId());

        return Menu.createMenu(
                request.getName(),
                request.getPrice(),
                menuGroup,
                menuProducts);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(menu -> MenuResponse.of(menu))
                .collect(Collectors.toList());
    }

    public Menu findMenuById(final Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("메뉴를 찾을 수 없습니다. id: " + id));
    }
}
