package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.exceptions.menu.InvalidMenuPriceException;
import kitchenpos.domain.exceptions.menu.MenuGroupEntityNotFoundException;
import kitchenpos.domain.exceptions.menu.ProductEntityNotFoundException;
import kitchenpos.ui.dto.menu.MenuProductRequest;
import kitchenpos.ui.dto.menu.MenuRequest;
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
    public Menu create(final MenuRequest menuRequest) {
        final BigDecimal price = menuRequest.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMenuPriceException("가격은 음수일 수 없습니다.");
        }

        if (!menuGroupDao.existsById(menuRequest.getMenuGroupId())) {
            throw new MenuGroupEntityNotFoundException("존재하지 않은 메뉴 그룹으로 메뉴를 등록할 수 없습니다.");
        }

        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productDao.findById(menuProductRequest.getProductId())
                    .orElseThrow(() -> new ProductEntityNotFoundException("존재하지 않는 상품으로 메뉴를 등록할 수 없습니다."));
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new InvalidMenuPriceException("메뉴의 가격은 구성된 메뉴 상품들의 가격 합보다 비쌀 수 없습니다.");
        }

        final Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId());
        final Menu savedMenu = menuDao.save(menu);

        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final MenuProduct menuProduct = MenuProduct.of(
                    savedMenu.getId(), menuProductRequest.getProductId(), menuProductRequest.getQuantity());
            savedMenu.addMenuProduct(menuProduct);
        }

        return savedMenu;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
