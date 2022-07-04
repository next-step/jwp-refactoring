package kitchenpos.menu.application;

import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.InvalidPriceException;
import kitchenpos.menu.dto.MenuProductRequestDto;
import kitchenpos.menu.dto.MenuRequestDto;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class MenuValidator {

    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(MenuRequestDto request) {
        checkValidPrice(request.getPrice(), request.getMenuProducts());
    }

    private void checkValidPrice(BigDecimal requestPrice, List<MenuProductRequestDto> menuProducts) {
        Price price = new Price(requestPrice);
        BigDecimal sum = menuProducts.stream()
                .map(it -> getAmount(it))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        if (price.greaterThan(sum)) {
            throw new InvalidPriceException(price);
        }
    }

    private BigDecimal getAmount(MenuProductRequestDto request) {
        Product product = productRepository.findById(request.getProductId()).orElseThrow(EntityNotFoundException::new);
        return product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
    }


}
