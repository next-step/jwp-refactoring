package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.validator.MenuValidator;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final ProductRepository productRepository,
            final MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        menuValidator.validate(menuRequest);
        MenuProducts menuProducts = MenuProducts.from(findAllMenuProductsByProductId(menuRequest.getMenuProductRequests()));
        Menu menu = menuRepository.save(menuRequest.toMenu(menuProducts));
        return MenuResponse.from(menu);
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    private List<MenuProduct> findAllMenuProductsByProductId(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(menuProductRequest -> {
                    Long productId = menuProductRequest.getProductId();
                    return menuProductRequest.toMenuProduct(findProductById(productId));
                })
                .collect(Collectors.toList());
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.존재하지_않는_상품.getErrorMessage()));
    }
}
