package kitchenpos.application;

import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProductRepository;
import kitchenpos.domain.menu.MenuRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final MenuProductRepository menuProductRepository,
        final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final Menu menu) {
        final Price price = menu.getPrice();

        if (!menuGroupRepository.existsById(menu.getMenuGroup().getId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        Price sum = Price.of(0);

        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProduct().getId())
                                                        .orElseThrow(IllegalArgumentException::new);

            sum = sum.add(product.getPrice().multiply(menuProduct.getQuantity()));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuRepository.save(menu);

        
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.changeMenu(savedMenu);
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }
        savedMenu.changeMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.changeMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
