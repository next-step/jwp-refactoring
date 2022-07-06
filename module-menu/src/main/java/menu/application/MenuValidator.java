package menu.application;

import common.domain.Price;
import common.exception.InvalidPriceException;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import menu.dto.MenuProductRequestDto;
import menu.dto.MenuRequestDto;
import org.springframework.stereotype.Service;
import product.domain.Product;
import product.repository.ProductRepository;

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
