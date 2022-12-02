package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MenuService {
    public static final String PRICE_NOT_NULL_EXCEPTION_MESSAGE = "가격은 필수입니다.";
    public static final String MINIMUM_PRICE_EXCEPTION_MESSAGE = "가격이 0원보다 작을 수 없습니다.";
    public static final String MENU_GROUP_NOT_EXIST_EXCEPTION_MESSAGE = "메뉴 그룹이 존재하지 않습니다.";
    public static final String MENU_PRICE_EXCEPTION_MESSAGE = "메뉴의 가격이 메뉴 상품의 합보다 클 수 없다.";
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
    public MenuResponse create(final MenuCreateRequest request) {
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price)) {
            throw new IllegalArgumentException(PRICE_NOT_NULL_EXCEPTION_MESSAGE);
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(MINIMUM_PRICE_EXCEPTION_MESSAGE);
        }

        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException(MENU_GROUP_NOT_EXIST_EXCEPTION_MESSAGE);
        }

        final List<MenuProduct> menuProducts = request.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException(MENU_PRICE_EXCEPTION_MESSAGE);
        }

        final Menu savedMenu = menuDao.save(request.toMenu());

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return new MenuResponse(savedMenu);
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
