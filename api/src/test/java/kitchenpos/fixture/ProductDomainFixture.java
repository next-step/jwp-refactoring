package kitchenpos.fixture;

import kitchenpos.menu.domain.product.Product;
import kitchenpos.menu.dto.ProductRequest;

import java.math.BigDecimal;

public class ProductDomainFixture {
    public static Product 후라이드 = product("후라이드", BigDecimal.valueOf(15000));
    public static ProductRequest 후라이드_요청 = ProductRequest.of(후라이드.getName(), 후라이드.getProductPrice().getPrice());


    public static Product 사이다 = product("사이다", BigDecimal.valueOf(1000));
    public static ProductRequest 사이다_요청 = ProductRequest.of(사이다.getName(), 사이다.getProductPrice().getPrice());


    public static Product 양념소스 = product("양념 소스", BigDecimal.valueOf(500));
    public static ProductRequest 양념소스_요청 = ProductRequest.of(양념소스.getName(), 양념소스.getProductPrice().getPrice());

    public static Product product(String name, BigDecimal price) {
        return new Product(name, price);
    }

}
