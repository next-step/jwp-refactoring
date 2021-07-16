package kitchenpos.menu.application;

import kitchenpos.exception.MenuException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Products;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {

    private static final String NOT_EXISTS_MENU_GROUP_ERROR_MESSAGE = "미등록 메뉴 그룹 입니다.";

    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductService productService;

    public MenuService(MenuRepository menuRepository, MenuProductRepository menuProductRepository, MenuGroupRepository menuGroupRepository, ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productService = productService;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        checkExistsMenuGroup(menuRequest.getMenuGroupId());
        Menu menu = menuRequest.toMenu();
        Products products = productService.findProductsByIds(menuRequest.toProductIds());
        products.checkProductsSize(menuRequest.getMenuProducts().size());
        BigDecimal menuTotalAmount = products.calcProductsPrice(menuRequest.getMenuProducts());
        menu.validMenuTotalAmount(menuTotalAmount);
        final Menu savedMenu = menuRepository.save(menu);
        List<MenuProduct> savedMenuProducts = menuProductRepository.saveAll(menuRequest.toMenuProducts(savedMenu));
        return MenuResponse.of(savedMenu, savedMenuProducts);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream().map(MenuResponse::of).collect(Collectors.toList());
    }

    private void checkExistsMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new MenuException(NOT_EXISTS_MENU_GROUP_ERROR_MESSAGE);
        }
    }

    public long countByMenuId(List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }
}
