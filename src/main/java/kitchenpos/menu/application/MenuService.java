package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuProductRepository;
import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dao.ProductRepository;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuGroupService menuGroupService;
    private final MenuProductRepository menuProductRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final ProductRepository productRepository,
            final MenuGroupService menuGroupService,
            final MenuProductRepository menuProductRepository
    ) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuGroupService = menuGroupService;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        menuGroupService.existsById(menuRequest.getMenuGroupId());
        validPriceCheck(menuRequest.getPrice(), menuRequest.getMenuProducts());
        Menu savedMenu = menuRepository.save(menuRequest.toMenu());

        MenuProducts menuProducts = createMenuProduct(menuRequest.getMenuProducts());
        menuProducts.saveMenu(savedMenu);
        menuProductRepository.saveAll(menuProducts.getMenuProducts());

        return MenuResponse.of(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    private void validPriceCheck(BigDecimal price, List<MenuProductRequest> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;

        for (final MenuProductRequest menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 금액은 상품의 합 보다 작아야합니다.");
        }
    }

    private MenuProducts createMenuProduct(List<MenuProductRequest> menuProductRequestList) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequestList) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
            menuProducts.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }
        return new MenuProducts(menuProducts);
    }
}
