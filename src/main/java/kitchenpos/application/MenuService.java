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
        this.validateMenu(menu);

        final Menu savedMenu = menuDao.save(menu);
        this.addPersistMenuProducts(menu, savedMenu);

        return savedMenu;
    }

    /**
     * 저장된 메뉴에 포함 된 메뉴상품들을 저장하고, 해당 메뉴에 추가합니다.
     * @param savedMenu
     */
    private void addPersistMenuProducts(Menu menu, Menu savedMenu) {
        final Long menuId = savedMenu.getId();

        for (final MenuProduct menuProduct : menu.getMenuProducts()) {
            menuProduct.setMenuId(menuId);
            savedMenu.addMenuProducts(menuProductDao.save(menuProduct));
        }
    }

    /**
     * 요청 받은 메뉴를 등록 할 수 있는지 검증합니다.
     * @param menu
     */
    private void validateMenu(Menu menu) {
        this.validatePrice(menu);
        this.existMenuGroup(menu);
        this.compareMenuProductsSum(menu);
    }

    /**
     * 메뉴 상품의 총 가격과 메뉴의 가격을 비교합니다.
     * @param menu
     * @throws IllegalArgumentException
     */
    private void compareMenuProductsSum(Menu menu) {
        if (menu.getPrice().compareTo(this.calculateMenuProductsSum(menu)) > 0) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 메뉴상품의 전체 가격을 구합니다
     * @param menu 
     * @return
     */
    private BigDecimal calculateMenuProductsSum(Menu menu) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menu.getMenuProducts()) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
    }

    /**
     * 요청 받은 메뉴의 그룹이 실제 존재하는지 확인합니다.
     * @param menu
     * @throws IllegalArgumentException
     */
    private void existMenuGroup(Menu menu) {
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 요청 받은 메뉴의 가격을 검증합니다.
     * @param menu
     * @throws IllegalArgumentException
     */
    private void validatePrice(Menu menu) {
        BigDecimal price = menu.getPrice();
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.changeMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
