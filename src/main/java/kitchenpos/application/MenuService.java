package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;

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
