package kitchenpos.menu.application;

import kitchenpos.exception.MenuException;
import kitchenpos.exception.ProductException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.domain.MenuGroupRepository;
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
    private static final String NOT_EQUAL_PRODUCT_COUNT_ERROR_MESSAGE = "미등록 상품을 메뉴 상품으로 등록 요청 하였습니다.";

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
        Products products = productService.findProductsByIds(menuRequest.toProductIds());
        checkProductsSize(products, menuRequest.getMenuProducts().size());
        BigDecimal menuTotalAmount = menuRequest.calcProductsPrice(products);
        Menu menu = menuRequest.toMenu();
        menu.validMenuTotalAmount(menuTotalAmount);
        menu.addMenuProducts(menuRequest.toMenuProducts());
        return MenuResponse.of(menuRepository.save(menu));
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

    public void checkProductsSize(Products products, int size) {
        if (products.getProducts().size() != size) {
            throw new ProductException(NOT_EQUAL_PRODUCT_COUNT_ERROR_MESSAGE);
        }
    }
}
