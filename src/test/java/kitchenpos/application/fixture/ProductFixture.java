package kitchenpos.application.fixture;

import java.math.BigDecimal;

import kitchenpos.domain.Product;

public class ProductFixture {

    public static Product 돼지고기 = new Product();
    public static Product 공기밥 = new Product();
    public static Product 양상추 = new Product();
    public static Product 닭가슴살 = new Product();

    static {
        돼지고기.setId(1L);
        돼지고기.setName("돼지고기");
        돼지고기.setPrice(BigDecimal.valueOf(9_000));

        공기밥.setId(2L);
        공기밥.setName("공기밥");
        공기밥.setPrice(BigDecimal.valueOf(1_000));

        양상추.setId(3L);
        양상추.setName("양상추");
        양상추.setPrice(BigDecimal.valueOf(5_000));

        닭가슴살.setId(4L);
        닭가슴살.setName("닭가슴살");
        닭가슴살.setPrice(BigDecimal.valueOf(3_000));
    }

    private ProductFixture() {}
}
