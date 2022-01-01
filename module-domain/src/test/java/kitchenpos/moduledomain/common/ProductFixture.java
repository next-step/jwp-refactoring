package kitchenpos.moduledomain.common;

import kitchenpos.moduledomain.product.Product;

public class ProductFixture {

    public static Product 후라이드() {
        return Product.of(1L, "후라이드", 16000L);
    }

    public static Product 양념치킨() {
        return Product.of(2L, "양념치킨", 16000L);
    }

    public static Product 가격이없는_반반치킨() {
        return Product.of(3L, "반반치킨", 0L);
    }

    public static Product 반반치킨() {
        return Product.of(4L, "반반치킨", 16500L);
    }

    public static Product 콜라() {
        return Product.of(5L, "콜라", 2000L);
    }

    public static Product 떡볶이() {
        return Product.of(6L, "떡볶이", 6000L);
    }

}
