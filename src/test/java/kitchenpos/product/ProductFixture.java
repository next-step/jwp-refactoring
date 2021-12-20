package kitchenpos.product;

import java.math.BigDecimal;

import kitchenpos.domain.Product;
import kitchenpos.product.dto.ProductCreateRequest;

public class ProductFixture {
	public static Product 후라이드치킨_상품() {
		Product product = new Product();
		product.setId(1L);
		product.setName("후라이드치킨");
		product.setPrice(BigDecimal.valueOf(16000L));
		return product;
	}

	public static Product 강정치킨_상품() {
		Product product = new Product();
		product.setId(2L);
		product.setName("강정치킨");
		product.setPrice(BigDecimal.valueOf(17000L));
		return product;
	}

	public static Product 양념치킨_상품() {
		Product product = new Product();
		product.setId(3L);
		product.setName("양념치킨");
		product.setPrice(BigDecimal.valueOf(18000L));
		return product;
	}

	public static ProductCreateRequest 후라이드치킨_상품_요청() {
		return new ProductCreateRequest(후라이드치킨_상품().getName(), 후라이드치킨_상품().getPrice());
	}

	public static ProductCreateRequest 강정치킨_상품_요청() {
		return new ProductCreateRequest(강정치킨_상품().getName(), 강정치킨_상품().getPrice());
	}

	public static ProductCreateRequest 양념치킨_상품_요청() {
		return new ProductCreateRequest(양념치킨_상품().getName(), 양념치킨_상품().getPrice());
	}

	public static ProductCreateRequest 이름없는_상품_요청() {
		return new ProductCreateRequest(null, BigDecimal.valueOf(17000L));
	}

	public static ProductCreateRequest 음수가격_상품_요청() {
		return new ProductCreateRequest("돈을오히려드려요", BigDecimal.valueOf(-17000L));
	}
}
