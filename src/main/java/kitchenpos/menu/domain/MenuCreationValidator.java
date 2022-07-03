package kitchenpos.menu.domain;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.NotExistProductException;
import org.springframework.stereotype.Component;

@Component
public class MenuCreationValidator {
    private final ProductRepository productRepository;

    public MenuCreationValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        BigDecimal menuPrice = menu.getPrice();

        validateMenuPriceNotEmpty(menuPrice);
        validateMenuPriceNotNegative(menuPrice);

        MenuProducts menuProducts = menu.getMenuProducts();
        List<Long> productIds = menuProducts.productIds();
        List<Product> products = findProductsByIds(productIds);

        validateNotOverSumOfProductPrice(menuPrice, menuProducts, products);
    }

    private List<Product> findProductsByIds(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);

        if (productIds.size() != products.size()) {
            throw new NotExistProductException();
        }
        return products;
    }

    private void validateMenuPriceNotEmpty(BigDecimal menuPrice) {
        if (Objects.isNull(menuPrice)) {
            throw new InvalidMenuPriceException();
        }
    }

    private void validateMenuPriceNotNegative(BigDecimal menuPrice) {
        if (menuPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMenuPriceException();
        }
    }

    private void validateNotOverSumOfProductPrice(BigDecimal menuPrice, MenuProducts menuProducts,
                                                  List<Product> products) {
        List<MenuProductAmount> menuProductAmounts = getMenuProductAmounts(menuProducts, products);

        BigDecimal sum = menuProductAmounts.stream()
                .map(MenuProductAmount::getAmount)
                .reduce(BigDecimal.ZERO, (prev, next) -> prev.add(next));

        if (menuPrice.compareTo(sum) > 0) {
            throw new InvalidMenuPriceException();
        }
    }

    private List<MenuProductAmount> getMenuProductAmounts(MenuProducts menuProducts, List<Product> products) {
        return products.stream()
                .map(product -> new MenuProductAmount(
                        menuProducts.findQuantityByProductId(product.getId()), product.getPrice())
                ).collect(toList());
    }
}
