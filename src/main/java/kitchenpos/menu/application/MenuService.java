package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuProductRepository;
import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final MenuProductRepository menuProductRepository;
    private final ProductService productService;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupService menuGroupService,
            final MenuProductRepository menuProductRepository,
            final ProductService productService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.menuProductRepository = menuProductRepository;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        menuGroupService.existsById(menuRequest.getMenuGroupId());
        productService.validPriceCheck(menuRequest);
        Menu savedMenu = menuRepository.save(menuRequest.toMenu());

        MenuProducts menuProducts = createMenuProduct(menuRequest.getMenuProducts());
        menuProducts.saveMenu(savedMenu);
        menuProductRepository.saveAll(menuProducts.getMenuProducts());

        return MenuResponse.of(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public void countByIdIn(List<Long> menuIds) {
        long menuCount = menuRepository.countByIdIn(menuIds);

        if (menuIds.size() != menuCount) {
            throw new IllegalArgumentException("존재하지 않는 메뉴입니다.");
        }
    }

    private MenuProducts createMenuProduct(List<MenuProductRequest> menuProductRequestList) {
        List<MenuProduct> menuProducts = menuProductRequestList.stream()
                .map(menuProductRequest -> new MenuProduct(menuProductRequest.getProductId(),
                        menuProductRequest.getQuantity()))
                .collect(Collectors.toList());

        return new MenuProducts(menuProducts);
    }
}
