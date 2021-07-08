package kitchenpos.utils.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kitchenpos.domain.Product;

public class ProductObjects {
    private final Product product1;
    private final Product product2;
    private final Product product3;
    private final Product product4;
    private final Product product5;
    private final Product product6;

    public ProductObjects() {
        product1 = new Product();
        product1.setId(1L);
        product1.setPrice(BigDecimal.valueOf(16000.00));
        product1.setName("후라이드");
        product2 = new Product();
        product2.setId(2L);
        product2.setName("양념치킨");
        product2.setPrice(BigDecimal.valueOf(16000.00));
        product3 = new Product();
        product3.setId(3L);
        product3.setName("반반치킨");
        product3.setPrice(BigDecimal.valueOf(16000.00));
        product4 = new Product();
        product4.setId(4L);
        product4.setName("통구이");
        product4.setPrice(BigDecimal.valueOf(16000.00));
        product5 = new Product();
        product5.setId(5L);
        product5.setName("간장치킨");
        product5.setPrice(BigDecimal.valueOf(17000.00));
        product6 = new Product();
        product6.setId(6L);
        product6.setName("순살치킨");
        product6.setPrice(BigDecimal.valueOf(17000.00));
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

    public List<Product> getProducts() {
        return new ArrayList<>(Arrays.asList(product1, product2, product3, product4, product5, product6));
    }
}
