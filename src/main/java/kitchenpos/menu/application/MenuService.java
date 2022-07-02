package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.Products;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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

        Menu menu = Menu.of(request, MenuProducts.of(getMenuProducts(request)));
        Menu persistMenu = menuRepository.save(menu);
        return MenuResponse.of(persistMenu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    public List<Product> findProductsByIdIn(List<Long> productIds) {
        return productRepository.findByIdIn(productIds);
    }

    public boolean existsMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.existsById(menuGroupId);
    }

    private void validate(MenuRequest request) {
        validateMenuGroupExistsById(request.getMenuGroupId());
        validateProductIds(request);
    }

    private void validateProductIds(MenuRequest request) {
        List<Long> productIds = getProductIds(request.getMenuProducts());
        Products products = new Products(findProductsByIdIn(productIds));

        if (!products.isSameSize(productIds)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuGroupExistsById(Long menuGroupId) {
        if (!existsMenuGroupById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private List<Long> getProductIds(List<MenuProductRequest> requests) {
        return requests
                .stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }

    private List<MenuProduct> getMenuProducts(MenuRequest request) {
        List<Long> productIds = getProductIds(request.getMenuProducts());
        List<Product> products = findProductsByIdIn(productIds);

        return request.getMenuProducts().stream()
                .map(p -> new MenuProduct(getProduct(products, p.getProductId()), getQuantity(request.getMenuProducts(), p.getProductId())))
                .collect(Collectors.toList());
    }

    private Product getProduct(List<Product> products, Long productId) {
        return products.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private long getQuantity(List<MenuProductRequest> requests, Long productId) {
        return requests.stream()
                .filter(r -> r.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .getQuantity();
    }
}
