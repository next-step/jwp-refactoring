package kitchenpos.application;

import java.util.ArrayList;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
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

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = getMenuGroup(menuRequest);
        List<MenuProduct> menuProducts = getMenuProducts(menuRequest.getMenuProducts());
        Menu menu = MenuRequest.toMenu(menuRequest, menuGroup, menuProducts);

        return menuDao.save(menu);
    }

    private MenuGroup getMenuGroup(MenuRequest menuRequest) {
        if ( menuRequest.getMenuGroupId() == null || !menuGroupDao.existsById(menuRequest.getMenuGroupId()) ) {
            throw new IllegalArgumentException();
        }
        return menuGroupDao.findById(menuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<MenuProduct> getMenuProducts(List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productDao.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            menuProducts.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }
        return menuProducts;
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
