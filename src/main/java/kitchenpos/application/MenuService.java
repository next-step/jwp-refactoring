package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuGroupDao menuGroupDao;

    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(
            final MenuGroupDao menuGroupDao,
            final MenuRepository menuRepository, final ProductRepository productRepository,
            final MenuProductRepository menuProductRepository) {
        this.menuGroupDao = menuGroupDao;
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public Menu create(final Menu menu) {
        final BigDecimal price = menu.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (menu.getMenuGroup() != null && !menuGroupDao.existsById(menu.getMenuGroup().getId())) {
//        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
//            final Product product = productDao.findById(menuProduct.getProductId())
//            final Product product = productDao.findById(menuProduct.getProduct().getId())
            //
            if(menuProduct.getProduct() == null){
                throw new IllegalArgumentException();
            }
            //
            final Product product = productRepository.findById(menuProduct.getProduct().getId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuRepository.save(menu);

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenu(savedMenu);
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }
        savedMenu.setMenuProducts(new MenuProducts(savedMenuProducts));

        return savedMenu;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(new MenuProducts(menuProductRepository.findAllByMenuId(menu.getId())));
        }

        return menus;
    }
}
