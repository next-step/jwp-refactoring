package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

    public MenuResponse create(final MenuRequest request) {
        validateRequest(request);

        final Menu persistMenu = menuRepository.save(
                new Menu(request.getName(), request.getPrice(), request.getMenuGroupId()));
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

    private List<MenuProduct> getMenuProductsFromRequest(final MenuRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(menuProductRequest -> new MenuProduct(
                        menuProductRequest.getProductId(),
                        menuProductRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    private List<MenuProductResponse> getMenuProductResponsesFromMenu(final Menu menu) {
        return menu.getMenuProducts();
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
    public boolean existsById(final Long id) {
        return menuRepository.existsById(id);
    }
}
