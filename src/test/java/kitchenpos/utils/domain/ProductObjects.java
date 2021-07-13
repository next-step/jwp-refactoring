package kitchenpos.utils.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;

public class ProductObjects {
    private final Product product1;
    private final Product product2;
    private final Product product3;
    private final Product product4;
    private final Product product5;
    private final Product product6;

    public ProductObjects() {
        product1 = new Product("후라이드", BigDecimal.valueOf(16000.00));
        product2 = new Product("양념치킨", BigDecimal.valueOf(16000.00));
        product3 = new Product("반반치킨", BigDecimal.valueOf(16000.00));
        product4 = new Product("통구이", BigDecimal.valueOf(16000.00));
        product5 = new Product("간장치킨", BigDecimal.valueOf(17000.00));
        product6 = new Product("순살치킨", BigDecimal.valueOf(17000.00));
    }

    public Product getProduct1() {
        return product1;
    }

    public Product getProduct2() {
        return product2;
    }

    public Product getProduct3() {
        return product3;
    }

    public Product getProduct4() {
        return product4;
    }

    public Product getProduct5() {
        return product5;
    }

    public Product getProduct6() {
        return product6;
    }

    public ProductRequest getProductRequest1() {
        return new ProductRequest(this.product1.getProductName().toString(), this.product1.getProductPrice().toBigDecimal());
    }

    public List<Product> getProducts() {
        return new ArrayList<>(Arrays.asList(product1, product2, product3, product4, product5, product6));
    }
}
