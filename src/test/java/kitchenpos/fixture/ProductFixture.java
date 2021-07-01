package kitchenpos.fixture;

import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

public class ProductFixture {
    public static Product 양념치킨_1000원;
    public static Product 후라이드치킨_2000원;
    public static Product 콜라_100원;

    public static void cleanUp() {
        양념치킨_1000원 = new Product(1L,"양념치킨", new Price(1000));
        후라이드치킨_2000원 = new Product(2L,"후라이드치킨", new Price(2000));
        콜라_100원 = new Product(3L, "콜라", new Price(100));
    }
}
