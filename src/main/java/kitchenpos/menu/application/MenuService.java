package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
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
    public MenuResponse create(final MenuRequest menuRequest) {
        final Menu savedMenu = Menu.of(
                menuRequest.getName(),
                menuRequest.getPrice(),
                menuGroupService.findMenuGroupById(menuRequest.getMenuGroupId()));
        final List<MenuProduct> menuProducts = findMenuProducts(menuRequest.getMenuProducts());
        for (MenuProduct menuProduct : menuProducts) {
            savedMenu.addMenuProduct(menuProduct);
        }
        return MenuResponse.from(menuRepository.save(savedMenu));
    }

    public long countByIdIn(List<Long> ids) {
        return menuRepository.countByIdIn(ids);
    }

    public Menu findMenuById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다."));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    private List<MenuProduct> findMenuProducts(List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
                .map(menuProductRequest ->
                        MenuProduct.of(productService
                                .findProductById(menuProductRequest.getProductId()), menuProductRequest.getQuantity()))
                .collect(Collectors.toList());
    }
}
