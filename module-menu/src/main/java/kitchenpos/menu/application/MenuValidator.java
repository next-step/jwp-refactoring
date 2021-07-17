package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.exception.MenuPriceExceedException;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validationMenuProductPrices(Price menuPrice, List<MenuProductRequest> menuProductRequests) {
        List<Product> products = productRepository.findAllById(findProductIds(menuProductRequests));
        Price sumPrice = sumMenuProductPrices(menuProductRequests, products);
        if (menuPrice.isExceed(sumPrice)) {
            throw new MenuPriceExceedException();
        }
    }

    private Price sumMenuProductPrices(List<MenuProductRequest> menuProductRequests, List<Product> products) {
        Price sumPrice = Price.wonOf(0);
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = findProductById(products, menuProductRequest.getProductId());
            Quantity quantity = new Quantity(menuProductRequest.getQuantity());
            sumPrice = sumPrice.plus(product.multiplyPrice(quantity));
        }
        return sumPrice;
    }

    private List<Long> findProductIds(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
            .map(MenuProductRequest::getProductId)
            .collect(Collectors.toList());
    }

    private Product findProductById(List<Product> products, Long productId) {
        return products.stream()
            .filter(product -> product.getId().equals(productId))
            .findFirst()
            .orElseThrow(EntityNotFoundException::new);
    }

}
