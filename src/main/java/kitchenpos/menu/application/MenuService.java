package kitchenpos.menu.application;

import java.util.ArrayList;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = getMenuGroup(menuRequest);
        List<MenuProduct> menuProducts = getMenuProducts(menuRequest.getMenuProducts());
        Menu menu = MenuRequest.toMenu(menuRequest, menuGroup, menuProducts);

        return menuDao.save(menu);
    }

    private MenuGroup getMenuGroup(MenuRequest menuRequest) {
        if ( menuRequest.getMenuGroupId() == null) {
            throw new IllegalArgumentException();
        }
        return menuGroupDao.findById(menuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<MenuProduct> getMenuProducts(List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = getProduct(menuProductRequest.getProductId());
            menuProducts.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }
        return menuProducts;
    }

    private Product getProduct(Long productId) {
        return productDao.findById(productId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
