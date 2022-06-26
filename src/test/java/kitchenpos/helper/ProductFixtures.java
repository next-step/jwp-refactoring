package kitchenpos.helper;

import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;

public class ProductFixtures {
    public static ProductRequest 제육덮밥_요청 = 상품_요청_만들기("제육덮밥", 8000);
    public static ProductRequest 제육덮밥_가격마이너스_요청 = 상품_요청_만들기("제육덮밥", -1);
    public static ProductRequest 제육덮밥_가격NULL_요청 = 상품_요청_만들기("제육덮밥", null);

    public static Product 후라이드치킨_상품 = 상품_만들기(1L, "후라이드치킨", 16000);
    public static Product 양념치킨_상품 = 상품_만들기(2L, "후라이드치킨", 16000);
    public static Product 반반치킨_상품 = 상품_만들기(3L, "후라이드치킨", 16000);
    public static Product 통구이_상품 = 상품_만들기(4L, "후라이드치킨", 16000);
    public static Product 간장치킨_상품 = 상품_만들기(5L, "후라이드치킨", 17000);

    public static ProductRequest 상품_요청_만들기(String name, Integer price) {
        return new ProductRequest(name, price);
    }

    public static Product 상품_만들기(Long id, String name, Integer price) {
        return new Product(id, name, new Price(price));
    }

    public static Product 상품_만들기(String name, Integer price) {
        return 상품_만들기(null, name, price);
    }

}
