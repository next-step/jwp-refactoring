package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        validate(request);
        Menu save = menuRepository.save(createMenu(request));
        return MenuResponse.of(save);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    public boolean exists(List<Long> menuIds) {
        return menuRepository.existsByIdIn(menuIds);
    }

    private void validate(MenuRequest request) {
        if (request.notValidPrice()) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        List<MenuProductRequest> menuProductsRequest = request.getMenuProducts();
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProduct : menuProductsRequest) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (request.priceIsGreaterThen(sum)) {
            throw new IllegalArgumentException();
        }
    }

    private Menu createMenu(MenuRequest request) {
        Menu menu = new Menu(request.getName(), new BigDecimal(request.getPrice()), request.getMenuGroupId());

        List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(product -> new MenuProduct(menu, productRepository.findById(product.getProductId()).get(), product.getQuantity()))
                .collect(Collectors.toList());
        menu.addProducts(menuProducts);
        return menu;
    }
}
