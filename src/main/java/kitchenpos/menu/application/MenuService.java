package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(
        final MenuDao menuDao,
        final MenuGroupDao menuGroupDao,
        final MenuProductDao menuProductDao,
        final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        List<MenuProductRequest> menuProducts = request.getMenuProducts();
        Menu menu = request
            .toMenu(getMenuProducts(menuProducts), getMenuGroup(request.getMenuGroupId()));
        final Menu savedMenu = menuDao.save(menu);
        return MenuResponse.from(savedMenu);
    }

    private MenuGroup getMenuGroup(Long menuGroupId) {
        return menuGroupDao.findById(menuGroupId)
            .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 메뉴 그룹 입니다."));
    }

    private List<MenuProduct> getMenuProducts(List<MenuProductRequest> requests) {
        return requests.stream()
            .map(menuProductRequest -> menuProductRequest
                .toMenuProduct(findProductById(menuProductRequest.getProductId())))
            .collect(Collectors.toList());
    }

    private Product findProductById(Long productId) {
        return productDao.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 상품 입니다."));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();
        return menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}
