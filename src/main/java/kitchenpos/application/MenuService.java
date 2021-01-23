package kitchenpos.application;

import kitchenpos.advice.exception.MenuException;
import kitchenpos.advice.exception.ProductException;
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
import java.util.Objects;

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
    public Menu create(final Menu menu) {
        final BigDecimal price = menu.getPrice();
        validateEmptyPrice(price);
        validateExistsMenuGroup(menu);

        final List<MenuProduct> menuProducts = menu.getMenuProducts();
        BigDecimal sum = getSumOfPrices(menuProducts);
        validatePriceSum(price, sum);

        return saveMenu(menu, menuProducts);
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(findMenuProductsByMenuId(menu.getId()));
        }

        return menus;
    }

    private List<MenuProduct> findMenuProductsByMenuId(Long id) {
        return menuProductDao.findAllByMenuId(id);
    }

    private Product findProductByProductId(Long id) {
        return productDao.findById(id)
                .orElseThrow(()->new ProductException("존재하는 상품 id가 없습니다.", id));
    }

    private Menu saveMenu(Menu menu, List<MenuProduct> menuProducts) {
        final Menu savedMenu = menuDao.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);
        return savedMenu;
    }


    private BigDecimal getSumOfPrices(List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = findProductByProductId(menuProduct.getProductId());
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
    }

    private void validateExistsMenuGroup(Menu menu) {
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateEmptyPrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new MenuException("가격이 0보다 작을 수 없습니다", price.longValue());
        }
    }

    private void validatePriceSum(BigDecimal price, BigDecimal sum) {
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

}
