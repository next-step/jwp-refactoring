package kitchenpos.helper;

import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;

public class ProductFixtures {
    public static ProductRequest 제육덮밥_요청 = new ProductRequest("제육덮밥", 8000);
    public static ProductRequest 제육덮밥_가격마이너스_요청 = new ProductRequest( "제육덮밥", -1);
    public static ProductRequest 제육덮밥_가격NULL_요청 = new ProductRequest( "제육덮밥", null);

    public static Product 후라이드치킨_상품 = new Product(1L,"후라이드치킨", 16000);
    public static Product 양념치킨_상품 = new Product(2L, "후라이드치킨", 16000);
    public static Product 반반치킨_상품 = new Product(3L, "후라이드치킨", 16000);
    public static Product 통구이_상품 = new Product(4L, "후라이드치킨", 16000);
    public static Product 간장치킨_상품 = new Product(5L, "후라이드치킨", 17000);
}
