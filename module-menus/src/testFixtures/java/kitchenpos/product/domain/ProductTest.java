package kitchenpos.product.domain;

import kitchenpos.generic.price.domain.Price;

public class ProductTest {

    public static Product 후라이드 = new Product(1L, "후라이드", Price.valueOf(16000));
    public static Product 양념치킨 = new Product(2L, "양념치킨", Price.valueOf(16000));
    public static Product 반반치킨 = new Product(3L, "반반치킨", Price.valueOf(16000));
    public static Product 통구이 = new Product(4L, "통구이", Price.valueOf(16000));
    public static Product 간장치킨 = new Product(5L, "간장치킨", Price.valueOf(17000));
    public static Product 순살치킨 = new Product(6L, "순살치킨", Price.valueOf(17000));

    public static Product product(Long id, String name, Price price) {
        return new Product(id, name, price);
    }
}
