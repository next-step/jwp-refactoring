package common;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product 후라이드() {

        Product product = new Product();
        product.setId(1L);
        product.setName("후라이드");
        product.setPrice(new BigDecimal(16000));

        return product;
    }

    public static Product 양념치킨() {

        Product product = new Product();
        product.setId(2L);
        product.setName("양념치킨");
        product.setPrice(new BigDecimal(16000));

        return product;
    }

    public static Product 가격이없는_반반치킨() {
        Product product = new Product();
        product.setId(3L);
        product.setName("반반치킨");
        product.setPrice(new BigDecimal(-1));

        return product;
    }

    public static Product 반반치킨() {

        Product product = new Product();
        product.setId(4L);
        product.setName("반반치킨");
        product.setPrice(new BigDecimal(16500));

        return product;
    }

}
