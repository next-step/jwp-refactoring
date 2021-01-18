package kitchenpos.menu.service;

import kitchenpos.infra.Money;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.service.MenuGroupService;
import kitchenpos.product.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
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

    public MenuResponse create(final MenuRequest menuRequest) {
        checkProductsEmpty(menuRequest);
        final MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
        final Menu menu = new Menu(menuRequest.getName(), Money.price(menuRequest.getPrice()), menuGroup);
        final List<MenuProduct> menuProducts = menuRequest.getMenuProducts()
                .stream()
                .map(request -> new MenuProduct(menu, productService.findById(request.getProductId()), request.getQuantity()))
                .collect(Collectors.toList());
        menu.addProducts(menuProducts);
        return MenuResponse.ofMenu(menuRepository.save(menu));
    }

    private void checkProductsEmpty(final MenuRequest menuRequest) {
        if (menuRequest.isEmptyProducts()) {
            throw new IllegalArgumentException("상품이 비어있습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::ofMenu)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Menu findById(long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을수 없습니다."));
    }

}
