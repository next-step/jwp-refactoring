package kitchenpos.infra.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.ProductPrice;
import kitchenpos.domain.menu.SafeProduct;
import kitchenpos.domain.menu.exceptions.InvalidMenuPriceException;
import kitchenpos.domain.menu.exceptions.ProductEntityNotFoundException;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductAdapter implements SafeProduct {
    private final ProductRepository productRepository;

    public ProductAdapter(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public BigDecimal getProductPrice(final Long productId) {
        final Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductEntityNotFoundException("존재하지 않는 상품입니다."));

        return product.getPrice();
    }

    @Override
    public void isValidMenuPrice(Menu menu) {
        BigDecimal productTotalPrice = menu.getMenuProducts().stream()
                .map(this::calculateMenuProductPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (menu.isMoreExpensive(productTotalPrice)) {
            throw new InvalidMenuPriceException("메뉴의 가격은 구성된 메뉴 상품들의 가격 합보다 비쌀 수 없습니다.");
        }
    }

    private BigDecimal calculateMenuProductPrice(final MenuProduct menuProduct) {
        BigDecimal productPrice = this.getProductPrice(menuProduct.getProductId());
        return menuProduct.calculateTotalPrice(productPrice);
    }

    public List<ProductPrice> getProductPrices(final List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);

        return products.stream()
                .map(it -> new ProductPrice(it.getId(), it.getPrice()))
                .collect(Collectors.toList());
    }
}
