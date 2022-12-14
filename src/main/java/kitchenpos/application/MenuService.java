package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuResponse;
import kitchenpos.exception.KitchenposException;
import kitchenpos.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MenuService {
    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;
    private final MenuGroupDao menuGroupDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuProductDao menuProductDao,
            final ProductDao productDao,
            final MenuGroupDao menuGroupDao
    ) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    public MenuResponse create(final Menu menu) {
        menu.validatePrice();
        existsMenuGroupById(menu.getMenuGroupId());
        menu.validatePriceGreaterThanSum(getSumPriceFromMenuProducts(menu.getMenuProducts()));

        final Menu savedMenu = menuDao.save(menu);
        final List<MenuProduct> savedMenuProducts = saveMenuProducts(savedMenu.getId(), menu.getMenuProducts());

        return MenuResponse.of(savedMenu, savedMenuProducts);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(findAllByMenuId(menu.getId()));
        }

        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    private List<MenuProduct> findAllByMenuId(Long menuId) {
        return menuProductDao.findAllByMenuId(menuId);
    }

    private void existsMenuGroupById(Long menuGroupId){
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new KitchenposException(ErrorCode.NOT_EXISTS_MENU_GROUP);
        }
    }

    private BigDecimal getSumPriceFromMenuProducts(List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;

        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(() -> new KitchenposException(ErrorCode.NOT_FOUND_PRODUCT));
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        return sum;
    }

    private List<MenuProduct> saveMenuProducts(Long menuId, List<MenuProduct> menuProducts){
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for(MenuProduct menuProduct : menuProducts){
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        return savedMenuProducts;
    }
}
