package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final ProductRepository productRepository;
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(ProductRepository productRepository,
                       MenuRepository menuRepository,
                       MenuValidator menuValidator) {
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        menuValidator.validate(request);
        List<MenuProduct> menuProducts = toMenuProducts(request);
        Menu menu = request.toMenu(request.getMenuGroupId(), menuProducts);
        return MenuResponse.from(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    private List<MenuProduct> toMenuProducts(MenuRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(it -> MenuProduct.of(findProductById(it.getProductId()).getId(), it.getQuantity()))
                .collect(Collectors.toList());
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND));
    }

}
