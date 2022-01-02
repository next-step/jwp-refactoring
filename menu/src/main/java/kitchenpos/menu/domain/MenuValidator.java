package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.exception.DifferentOrderAndMenuPriceException;
import kitchenpos.menu.exception.NotCreatedProductException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validatePrice(MenuRequest menuRequest) {
        List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProductRequests();
        List<Long> productIds = menuProductRequests.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());

        List<Product> products = productRepository.findAllByIdIn(productIds);

        BigDecimal sum = menuProductRequests.stream()
                .map(menuProductRequest -> calculate(products, menuProductRequest.getProductId(), menuProductRequest.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (menuRequest.getPrice().compareTo(sum) > 0) {
            throw new DifferentOrderAndMenuPriceException();
        }
    }

    private BigDecimal calculate(List<Product> products, Long productId, Long quantity) {
        return products.stream()
                .filter(product -> product.getId().equals(productId))
                .map(product -> product.getTotalPrice(quantity))
                .findFirst()
                .orElseThrow(NotCreatedProductException::new);
    }

}
