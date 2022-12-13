package kitchenpos.menu.application;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.validator.MenuValidator;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            MenuRepository menuRepository,
            ProductRepository productRepository,
            MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(MenuRequest request) {
        menuValidator.createMenu(request);
        List<MenuProduct> menuProducts =
                findAllMenuProducts(request.getMenuProducts(), findAllProductByIds(request.getMenuProductIds()));

        final Menu savedMenu = menuRepository.save(request.createMenu(new MenuProducts(menuProducts)));
        return MenuResponse.from(savedMenu);
    }

    private List<MenuProduct> findAllMenuProducts(List<MenuProductRequest> menuProducts, List<Product> products) {
        return menuProducts.stream()
                .map(menuProductRequest -> {
                    Product findProduct = products.stream()
                            .filter(product -> product.getId() == menuProductRequest.getProductId())
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException(ErrorCode.PRODUCT_IS_NOT_EXIST.getMessage()));
                    return menuProductRequest.createMenuProduct(findProduct);
                })
                .collect(Collectors.toList());
    }

    private List<Product> findAllProductByIds(List<Long> ids) {
        List<Product> products = productRepository.findAllById(ids);

        if (products.size() != ids.size()) {
            throw new IllegalArgumentException(ErrorCode.PRODUCT_IS_NOT_EXIST.getMessage());
        }

        return products;
    }

    public List<MenuResponse> findAll() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
