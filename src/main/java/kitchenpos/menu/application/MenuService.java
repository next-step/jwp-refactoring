package kitchenpos.menu.application;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
        final MenuProductRepository menuProductRepository,
        final MenuGroupRepository menuGroupRepository,
        final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final Menu menu) {
        validateExistMenuGroup(menu);

        MenuProducts menuProducts = fillProduct(menu.getMenuProducts());
        menu.validateMenuPrice(menuProducts.getTotalPrice());

        final Menu savedMenu = menuRepository.save(menu);
        savedMenu.setMenuProducts(menuProducts);
        menuProductRepository.saveAll(menuProducts.getValues());

        return MenuResponse.from(savedMenu);
    }

    private void validateExistMenuGroup(Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private MenuProducts fillProduct(MenuProducts menuProducts) {
        Map<Long, Product> productById =
            productRepository.findAllById(menuProducts.extractProductIds())
                .stream()
                .collect(toMap(Product::getId, Function.identity()));

        for (MenuProduct menuProduct : menuProducts.getValues()) {
            menuProduct.fillProduct(productById.get(menuProduct.getProductId()));
        }

        return menuProducts;
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
            .map(MenuResponse::from)
            .collect(toList());
    }
}
