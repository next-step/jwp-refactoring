package kitchenpos.product.util;

import kitchenpos.product.fixture.ProductFixtureFactory;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductApplicationBehavior {

    @Autowired
    private ProductService productService;

    public Product 상품_생성됨(String name, int price) {
        return productService.create(ProductFixtureFactory.createProduct(name, price));
    }
}
