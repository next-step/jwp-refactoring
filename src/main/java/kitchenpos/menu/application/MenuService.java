package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.application.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;

    private final MenuGroupService menuGroupService;

    private final ProductService productService;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupService menuGroupService,
                       final ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        validateRequest(request);

        final Menu persistMenu = menuRepository.save(createMenu(request));
        persistMenu.addMenuProducts(getMenuProductsFromRequest(request));

        return MenuResponse.of(persistMenu);
    }

    private void validateRequest(final MenuRequest request) {
        validateMenuGroupId(request.getMenuGroupId());
        validateProductsAndPrice(request);
    }

    private void validateMenuGroupId(final Long id) {
        if (!menuGroupService.existsById(id)) {
            throw new IllegalArgumentException(String.format("메뉴 그룹이 존재하지 않습니다. id: %d", id));
        }
    }

    private void validateProductsAndPrice(final MenuRequest request) {
        final long sum = request.getMenuProducts()
                .stream()
                .map(menuProduct ->
                        productService.getById(menuProduct.getProductId()).getPrice().value()
                                * menuProduct.getQuantity().value())
                .reduce(Long::sum)
                .get();
        if (request.getPrice().value() > sum) {
            throw new IllegalArgumentException("메뉴의 가격은 상품 가격의 총합보다 클 수 없습니다.");
        }
    }

    private Menu createMenu(MenuRequest request) {
        return new Menu(request.getName(), request.getPrice(), request.getMenuGroupId());
    }

    private List<MenuProduct> getMenuProductsFromRequest(final MenuRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(menuProductRequest -> new MenuProduct(
                        menuProductRequest.getProductId(),
                        menuProductRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus
                .stream()
                .map(menu -> MenuResponse.of(menu))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean notExistsById(final Long id) {
        return !menuRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public Menu getById(final Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("메뉴를 찾을 수 없습니다. id: %d", id)));
    }
}
