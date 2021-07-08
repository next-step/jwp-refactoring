package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Products;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Transactional
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

    public Menu create(final Menu menu) {
        final BigDecimal price = menu.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

     /*   if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }*/

        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuRepository.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    @Transactional
    public Menu create2(final MenuRequest menuRequest) {
        MenuProducts menuProducts = new MenuProducts(menuRequest.getMenuProducts());
        Products products = new Products(productRepository.findAllById(menuProducts.toMenuProductIds()));
        Long sum = products.calculateSumPrice(menuRequest.getMenuProducts());

        return menuRepository.save(new Menu(menuRequest.getName(), BigDecimal.valueOf(menuRequest.getPrice()),
                findMenuGroup(menuRequest), sum, menuRequest.getMenuProducts()));
    }

    private MenuGroup findMenuGroup(MenuRequest menuRequest) {
        return menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElseThrow(() -> new IllegalArgumentException());
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
