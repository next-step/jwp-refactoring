package kitchenpos.application;

import kitchenpos.common.ErrorMessage;
import kitchenpos.domain.*;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kitchenpos.common.ErrorMessage.INVALID_PRODUCT_ID;

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
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        Long menuGroupId = request.getMenuGroupId();
        MenuGroup menuGroup = menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException(String.format(ErrorMessage.NOT_FOUND_MENU_GROUP.getMessage(), menuGroupId)));
        MenuProducts menuProducts = MenuProducts.from(findAllMenuProductsByProductId(request.getMenuProductsRequest()));
        Menu menu = request.toEntity(menuGroup, menuProducts);

        return MenuResponse.from(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    private List<MenuProduct> findAllMenuProductsByProductId(List<MenuProductRequest> menuProductRequests) {
        List<Product> products = findProducts(menuProductRequests);
        validateProducts(products, menuProductRequests);
        Map<Long, Product> productIdToProduct = new HashMap<>();
        for (Product product : products) {
            productIdToProduct.put(product.getId(), product);
        }

        return menuProductRequests.stream()
                .map(menuProductRequest -> menuProductRequest.toMenuProduct(productIdToProduct.get(menuProductRequest.getProductId())))
                .collect(Collectors.toList());
    }

    private void validateProducts(final List<Product> products, final List<MenuProductRequest> menuProductRequests) {
        if (products.size() != menuProductRequests.size()) {
            throw new IllegalArgumentException(INVALID_PRODUCT_ID.getMessage());
        }
    }

    private List<Product> findProducts(final List<MenuProductRequest> menuProductRequests) {
        List<Long> productIds = menuProductRequests.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());

        return productRepository.findAllByIdIn(productIds);
    }
}
