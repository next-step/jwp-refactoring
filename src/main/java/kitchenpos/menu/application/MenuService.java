package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(
            MenuGroupRepository menuGroupRepository, ProductRepository productRepository, MenuRepository menuRepository, MenuProductRepository menuProductRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        List<MenuProduct> menuProducts = request.getMenuProducts()
                .stream().map(this::mapToMenuProduct)
                .collect(Collectors.toList());

        Menu savedMenu = menuRepository.save(request.toEntity(menuProducts));
        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAllWithMenuProducts();
        return MenuResponse.of(menus);
    }

    private MenuProduct mapToMenuProduct(MenuProductRequest request) {
        final Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        return new MenuProduct(product, request.getQuantity());
    }
}
