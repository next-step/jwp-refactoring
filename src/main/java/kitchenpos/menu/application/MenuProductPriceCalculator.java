package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.domain.ProductRepository;

@Component
public class MenuProductPriceCalculator {
    private final ProductRepository productRepository;

    public MenuProductPriceCalculator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductPrice calculateSum(List<MenuProductRequest> menuProducts) {
        List<Product> products = findAllProducts(toProductIds(menuProducts));

        ProductPrice sum = ProductPrice.zero();
        for (final MenuProductRequest menuProduct : menuProducts) {
            ProductPrice price = findMatchProductPrice(products, menuProduct);
            ProductPrice productPrice = price.multiply(menuProduct.getQuantity());
            sum = sum.add(productPrice);
        }
        return sum;
    }

    private ProductPrice findMatchProductPrice(List<Product> products, MenuProductRequest menuProduct) {
        return products.stream()
            .filter(product -> product.hasId(menuProduct.getProductId()))
            .map(Product::getPrice)
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 ID 입니다."));
    }

    private List<Product> findAllProducts(List<Long> productIds) {
        return productRepository.findAllById(productIds);
    }

    private List<Long> toProductIds(List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
            .map(MenuProductRequest::getProductId)
            .collect(Collectors.toList());
    }
}
