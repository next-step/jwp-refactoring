package menu.application;

import menu.domain.Menu;
import menu.domain.MenuProduct;
import menu.domain.MenuProducts;
import menu.dto.MenuProductRequest;
import menu.dto.MenuRequest;
import menu.dto.MenuResponse;
import menu.repository.MenuRepository;
import menu.validator.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product.domain.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static common.ErrorMessage.INVALID_PRODUCT_ID;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuValidator menuValidator

    ) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        MenuProducts menuProducts = MenuProducts.from(findAllMenuProductsByProductId(request.getMenuProductsRequest()));
        menuValidator.validateCreateMenu(request, menuProducts);
        Long menuGroupId = request.getMenuGroupId();
        Menu menu = request.toMenu(menuGroupId, menuProducts);
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

        return menuValidator.findAllByIdIn(productIds);
    }
}
