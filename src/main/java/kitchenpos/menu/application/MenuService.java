package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupDao;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductDao;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(final MenuDao menuDao, final MenuGroupDao menuGroupDao,
        final ProductDao productDao) {

        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final String name = menuRequest.getName();
        final MenuGroup menuGroup = findMenuGroup(menuRequest);
        final List<MenuProduct> menuProducts = makeMenuProducts(menuRequest);
        final Menu menu = new Menu(name, menuRequest.getPrice(), menuGroup, menuProducts);
        final Menu saved = menuDao.save(menu);

        return MenuResponse.of(saved);
    }

    private MenuGroup findMenuGroup(MenuRequest menuRequest) {
        return menuGroupDao.findById(menuRequest.getMenuGroupId())
            .orElseThrow(IllegalArgumentException::new);
    }

    private List<MenuProduct> makeMenuProducts(MenuRequest menuRequest) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();
        menuProductRequests.forEach(menuProductRequest -> menuProducts.add(
            new MenuProduct(findProduct(menuProductRequest), menuProductRequest.getQuantity())));

        return menuProducts;
    }

    private Product findProduct(MenuProductRequest menuProductRequest) {
        return productDao.findById(menuProductRequest.getProductId())
            .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuDao.findAll()
            .stream()
            .map(MenuResponse::of)
            .collect(Collectors.toList());
    }
}
