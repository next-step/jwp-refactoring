package kitchenpos.product.application;


import static kitchenpos.exception.KitchenposExceptionMessage.MENU_PRICE_CANNOT_OVER_THAN_PRODUCT_PRICE;

import java.util.List;
import kitchenpos.common.Price;
import kitchenpos.exception.KitchenposException;
import kitchenpos.exception.KitchenposExceptionMessage;
import kitchenpos.menu.application.ProductValidator;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductValidateImpl implements ProductValidator {

    private final ProductRepository productRepository;

    public ProductValidateImpl(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void checkOverPrice(Price price, List<MenuProductRequest> menuProductRequests) {
        Price totalProductPrice = menuProductRequests.stream()
                                                     .map(this::getProductPrice)
                                                     .reduce(Price.ZERO, Price::add);
        if (price.isBiggerThan(totalProductPrice)) {
            throw new KitchenposException(MENU_PRICE_CANNOT_OVER_THAN_PRODUCT_PRICE);
        }
    }

    private Price getProductPrice(MenuProductRequest menuProductRequest) {
        return findProductById(menuProductRequest.getProductId()).getPrice()
                                                                 .multiply(menuProductRequest.getQuantity());
    }

    private Product findProductById(final Long productId) {
        return productRepository.findById(productId)
                                .orElseThrow(() -> new KitchenposException(KitchenposExceptionMessage.NOT_FOUND_PRODUCT));
    }
}
