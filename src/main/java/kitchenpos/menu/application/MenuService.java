package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductDao;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductDto;
import kitchenpos.menugroup.MenuGroupDao;
import kitchenpos.product.ProductDao;
import kitchenpos.product.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public Menu create(final MenuCreateRequest menuCreateRequest) {
        menuCreateRequest.checkPriceValidation();
        checkGroupExist(menuCreateRequest);
        final Menu menu = menuDao.save(menuCreateRequest.toMenu(menuGroupDao.getOne(menuCreateRequest.getMenuGroupId())));
        List<MenuProduct> products = menuCreateRequest.getMenuProducts()
                .stream()
                .map(it -> menuProductDao.save(new MenuProduct(menu, productDao.getOne(it.getProductId()), it.getQuantity())))
                .collect(Collectors.toList());
        menu.addMenuProducts(products);
        return menu;
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }

    private void checkGroupExist(MenuCreateRequest menuCreateRequest) {
        if (!menuGroupDao.existsById(menuCreateRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }
}
