package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;

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
    public Menu create(final MenuRequest menuRequest) {
        final String name = menuRequest.getName();
        final Price price = new Price(menuRequest.getPrice());
        final MenuGroup menuGroup = findMenuGroup(menuRequest);
        final MenuProducts menuProducts = makeMenuProducts(menuRequest);
        final Menu menu = new Menu(name, price, menuGroup, menuProducts);

        return menuDao.save(menu);
    }

    private MenuGroup findMenuGroup(MenuRequest menuRequest) {
        return menuGroupDao.findById(menuRequest.getMenuGroupId())
            .orElseThrow(IllegalArgumentException::new);
    }

    private MenuProducts makeMenuProducts(MenuRequest menuRequest) {
        final MenuProducts menuProducts = new MenuProducts();
        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProductRequests();
        menuProductRequests.forEach(menuProductRequest -> menuProducts.add(
            new MenuProduct(findProduct(menuProductRequest), menuProductRequest.getQuantity())));

        return menuProducts;
    }

    private Product findProduct(MenuProductRequest menuProductRequest) {
        return productDao.findById(menuProductRequest.getProductId())
            .orElseThrow(IllegalArgumentException::new);
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
