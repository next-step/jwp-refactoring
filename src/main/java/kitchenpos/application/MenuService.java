package kitchenpos.application;

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
import java.util.stream.Collectors;

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
    public Menu create(final MenuRequest request) {
        final MenuGroup menuGroup = getMenuGroup(request);
        final List<MenuProduct> menuProducts = getMenuProducts(request);
        final Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup, menuProducts);

        menu.checkPrice();

        return menuDao.save(menu);
    }

    private List<MenuProduct> getMenuProducts(MenuRequest request) {
        return request.getMenuProducts().stream()
                .map(this::getMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuGroup getMenuGroup(MenuRequest request) {
        return menuGroupDao.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
    }

    private MenuProduct getMenuProduct(MenuProductRequest menuProductRequest) {
        return new MenuProduct(getProduct(menuProductRequest), menuProductRequest.getQuantity());
    }

    private Product getProduct(MenuProductRequest menuProductRequest) {
        return productDao.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
