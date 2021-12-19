package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductDao productDao;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository, MenuProductRepository menuProductRepository,
            final ProductDao productDao
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(final MenuRequest menu) {
        final BigDecimal price = menu.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
        Menu addMenu = new Menu(menu.getName(), menu.getPrice()
                , menuGroupRepository.findById(menu.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new)
                );
        final Menu savedMenu = menuRepository.save(addMenu);

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenu(savedMenu);
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
