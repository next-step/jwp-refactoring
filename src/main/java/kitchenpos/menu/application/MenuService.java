package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;

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
    public MenuResponse create(final MenuRequest menuRequest) {
        Menu saveTargetMenu = new Menu.Builder()
                                .name(menuRequest.getName())
                                .price(menuRequest.getPrice())
                                .menuGroupId(menuRequest.getMenuGroupId())
                                .build();

        Menu savedMenu = menuDao.save(saveTargetMenu);
        Menu finalSavedMenu = addCreateMenuProductTarget(savedMenu, menuRequest.getMenuGroupId());
        return MenuResponse.of(finalSavedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();
        return menus.stream()
            .map(MenuResponse::of)
            .collect(Collectors.toList());
    }

    private Menu addCreateMenuProductTarget(Menu savedMenu, Long menuGroupId) {
        final List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menuGroupId);

        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId()).orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
            savedMenu.addMenuProduct(new MenuProduct(savedMenu, product, menuProduct.getQuantity()));
        }
        savedMenu.validateSumOfPrice(sum);
        return savedMenu;
    }

}
