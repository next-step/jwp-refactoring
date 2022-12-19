package kitchenpos.product.domain;

import kitchenpos.ExceptionMessage;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.menu.dto.MenuProductRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuProductValidator {

    private MenuProductRepository menuProductRepository;
    private ProductRepository productRepository;

    public MenuProductValidator(MenuProductRepository menuProductRepository, ProductRepository productRepository) {
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    public MenuProducts createMenuProducts(Long menuId, Price menuPrice, List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProductList = menuProductRequests.stream()
                .map(menuProductRequest -> getMenuProduct(menuId, menuProductRequest))
                .collect(Collectors.toList());

        final MenuProducts menuProducts = new MenuProducts(menuProductList);

        final Price productTotalSum = menuProducts.getProductPriceSum();
        if (!menuPrice.lessOrEqualThan(productTotalSum)) {
            throw new IllegalArgumentException(ExceptionMessage.MENU_PRICE_LESS_PRODUCT_PRICE_SUM.getMessage());
        }

        menuProductRepository.saveAll(menuProductList);

        return menuProducts;
    }

    private MenuProduct getMenuProduct(Long menuId, MenuProductRequest menuProductRequest) {
        final Long productId = menuProductRequest.getProductId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_PRODUCT.getMessage()));
        final Long quantity = menuProductRequest.getQuantity();
        return new MenuProduct(menuId, product, new Quantity(quantity));
    }

    public MenuProducts getMenuProductByMenuId(Long menuId) {
        return new MenuProducts(menuProductRepository.findAllByMenuId(menuId));
    }
}
