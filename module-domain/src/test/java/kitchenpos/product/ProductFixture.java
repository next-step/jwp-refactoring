package kitchenpos.product;

import java.math.BigDecimal;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductCreateRequest;

public class ProductFixture {
	public static Product 후라이드치킨_상품() {
		return Product.of(1L, Name.from("후라이드치킨"), Price.from(BigDecimal.valueOf(16000L)));
	}

	public static Product 강정치킨_상품() {
		return Product.of(2L, Name.from("강정치킨"), Price.from(BigDecimal.valueOf(17000L)));
	}

	public static Product 양념치킨_상품() {
		return Product.of(3L, Name.from("양념치킨"), Price.from(BigDecimal.valueOf(18000L)));
	}

	public static ProductCreateRequest 후라이드치킨_상품_요청() {
		return new ProductCreateRequest("후라이드치킨", BigDecimal.valueOf(16000L));
	}

	public static ProductCreateRequest 강정치킨_상품_요청() {
		return new ProductCreateRequest("강정치킨", BigDecimal.valueOf(17000L));
	}

	public static ProductCreateRequest 양념치킨_상품_요청() {
		return new ProductCreateRequest("양념치킨", BigDecimal.valueOf(18000L));
	}

	public static ProductCreateRequest 이름없는_상품_요청() {
		return new ProductCreateRequest(null, BigDecimal.valueOf(17000L));
	}

	public static ProductCreateRequest 음수가격_상품_요청() {
		return new ProductCreateRequest("돈을오히려드려요", BigDecimal.valueOf(-17000L));
	}
}
