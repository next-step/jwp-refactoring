package kitchenpos.helper;

import kitchenpos.product.dto.ProductRequest;

public class ProductFixtures {
    public static ProductRequest 제육덮밥 = new ProductRequest("제육덮밥", 8000);
    public static ProductRequest 제육덮밥_가격마이너스 = new ProductRequest( "제육덮밥", -1);
    public static ProductRequest 제육덮밥_가격NULL = new ProductRequest( "제육덮밥", null);
}
