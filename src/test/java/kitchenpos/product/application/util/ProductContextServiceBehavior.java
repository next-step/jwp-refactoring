package kitchenpos.product.application.util;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.fixture.ProductDtoFixtureFactory;
import kitchenpos.product.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductContextServiceBehavior {

    @Autowired
    private ProductService productService;

    public Product 상품_생성됨(String name, int price) {
        return productService.create(ProductDtoFixtureFactory.createProduct(name, price));
    }
}
