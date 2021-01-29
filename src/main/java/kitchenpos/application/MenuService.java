package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProductRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuRequest;
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
    public MenuResponse create(final MenuRequest menuRequest) {
        final BigDecimal price = menuRequest.getPrice();

        if (validatePrice(price)) {
            throw new IllegalArgumentException();
        }

        if (validateMenuGroupId(menuRequest)) {
            throw new IllegalArgumentException();
        }

        BigDecimal sum = BigDecimal.ZERO;
        List<MenuProductRequest> menuProducts = menuRequest.getMenuProducts();
        for (final MenuProductRequest productId : menuProducts) {
            final Product product = productRepository.findById(productId.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(productId.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
            .orElseThrow(IllegalArgumentException::new);
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        final Menu savedMenu = menuRepository.save(menu);

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : menuProducts) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);

            MenuProduct menuProduct1 = new MenuProduct(savedMenu, product, menuProductRequest.getQuantity());
            savedMenuProducts.add(menuProduct1);
        }

        savedMenu.updateMenuProduct(savedMenuProducts);

        List<MenuProductResponse> collect = savedMenu.getMenuProducts().stream().map(
            menuProduct -> MenuProductResponse.of(menuProduct, menuProduct.getMenu().getId(),
                menuProduct.getProduct().getId()))
            .collect(Collectors.toList());

        return MenuResponse.of(savedMenu, collect, savedMenu.getMenuGroup().getId());
    }

    private boolean validateMenuGroupId(MenuRequest menuRequest) {
        return !menuGroupRepository.existsById(menuRequest.getMenuGroupId());
    }

    private boolean validatePrice(BigDecimal price) {
        return Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0;
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream().map(menu -> {
            List<MenuProductResponse> collect = menu.getMenuProducts().stream().map(
                menuProduct -> MenuProductResponse.of(menuProduct, menuProduct.getMenu().getId(),
                    menuProduct.getProduct().getId()))
                .collect(Collectors.toList());
            return MenuResponse.of(menu, collect, menu.getMenuGroup().getId());
        }).collect(Collectors.toList());
    }
}
