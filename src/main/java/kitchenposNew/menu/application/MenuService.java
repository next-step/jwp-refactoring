package kitchenposNew.menu.application;

import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.MenuProduct;
import kitchenposNew.menu.domain.Product;
import kitchenposNew.menu.domain.Menu;
import kitchenposNew.menu.domain.MenuGroupRepository;
import kitchenposNew.menu.domain.MenuRepository;
import kitchenposNew.menu.domain.ProductRepository;
import kitchenposNew.menu.dto.MenuRequest;
import kitchenposNew.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductDao menuProductDao;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository, MenuProductDao menuProductDao, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductDao = menuProductDao;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElseThrow(IllegalArgumentException::new);
        final List<MenuProduct> menuProducts = menuRequest.getMenuProducts();
        menuProducts.forEach(menuProduct ->
                productRepository.findById(menuProduct.getProductId()).orElseThrow(IllegalArgumentException::new)
        );

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (menuRequest.price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        Menu menu = menuRequest.toMenu();
        final Menu savedMenu = menuRepository.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuRequest.getMenuProducts()) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return MenuResponse.of(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus.stream()
                .map(menu -> MenuResponse.of(menu))
                .collect(Collectors.toList());
    }
}
