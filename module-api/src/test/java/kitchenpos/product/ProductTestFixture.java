package kitchenpos.product;

import kitchenpos.common.domain.Price;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

public class ProductTestFixture {

    public static final Price 아이스아메리카노_가격 = Price.valueOf(5000);
    public static final Price 에그맥머핀_가격 = Price.valueOf(7000);
    public static final Product 아이스아메리카노 = new Product("아이스아메리카노", 아이스아메리카노_가격);
    public static final Product 에그맥머핀 = new Product("에그맥머핀", 에그맥머핀_가격);

    public static final ProductRequest 아이스아메리카노_요청 = new ProductRequest("아이스아메리카노", 아이스아메리카노_가격.value());
    public static final ProductResponse 아이스아메리카노_응답 = new ProductResponse(1L, "아이스아메리카노", 아이스아메리카노_가격.value());
    public static final ProductResponse 에그맥머핀_응답 = new ProductResponse(2L, "에그맥머핀", 에그맥머핀_가격.value());
}
