package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProductRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public MenuResponse create(final Menu menu) {
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
        List<MenuProductResponse> collect = savedMenu.getMenuProducts().stream().map(MenuProductResponse::of)
            .collect(Collectors.toList());
        return MenuResponse.of(savedMenu, collect);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));
        }

        return menus.stream().map(menu -> {
            List<MenuProductResponse> collect = menu.getMenuProducts().stream().map(MenuProductResponse::of)
                .collect(Collectors.toList());
            return MenuResponse.of(menu, collect);
        }).collect(Collectors.toList());
    }
}
