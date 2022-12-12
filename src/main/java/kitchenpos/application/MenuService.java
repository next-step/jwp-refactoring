package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
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

    @Transactional
    public Menu create(final Menu menu) {
        menu.validateIsPriceNull();
        menu.validateIsPriceLessThanZero();
        existsMenuGroupById(menu.getMenuGroupId());
        validateIsPriceGreaterThanSum(menu.getPrice(), menu.getMenuProducts());

        final Menu savedMenu = menuDao.save(menu);

        final List<MenuProduct> savedMenuProducts = saveMenuProducts(savedMenu.getId(), menu.getMenuProducts());
        savedMenu.setMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }

    private void existsMenuGroupById(Long menuGroupId){
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateIsPriceGreaterThanSum(BigDecimal price, List<MenuProduct> menuProducts){
        BigDecimal sum = BigDecimal.ZERO;

        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException(""));
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
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
