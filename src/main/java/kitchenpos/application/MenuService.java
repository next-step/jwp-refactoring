package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuRequest;
import kitchenpos.port.MenuPort;
import kitchenpos.port.MenuGroupPort;
import kitchenpos.port.MenuProductPort;
import kitchenpos.port.ProductPort;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class MenuService {
    private final MenuPort menuDao;
    private final MenuGroupPort menuGroupPort;
    private final MenuProductPort menuProductPort;
    private final ProductPort productPort;

    public MenuService(
            final MenuPort menuDao,
            final MenuGroupPort menuGroupPort,
            final MenuProductPort menuProductPort,
            final ProductPort productPort
    ) {
        this.menuDao = menuDao;
        this.menuGroupPort = menuGroupPort;
        this.menuProductPort = menuProductPort;
        this.productPort = productPort;
    }

    @Transactional
    public Menu create(final MenuRequest request) {
        MenuGroup menuGroup = menuGroupPort.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);

        List<Product> product = productPort.findAllByIdIn(request.getProductId());


        Menu.of(request.getName(), request.getPrice(), menuGroup, );
        final BigDecimal price = menu.getPrice();


        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupPort.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productPort.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuDao.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductPort.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductPort.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
