package kitchenpos.product;

import java.math.BigDecimal;

import kitchenpos.product.dto.ProductCreateRequest;

public class ProductFixture {
	public static ProductCreateRequest 강정치킨() {
		return new ProductCreateRequest("강정치킨", BigDecimal.valueOf(17000L));
	}

	public static ProductCreateRequest 양념치킨() {
		return new ProductCreateRequest("양념치킨", BigDecimal.valueOf(18000L));
	}

	public static ProductCreateRequest 이름없는상품() {
		return new ProductCreateRequest(null, BigDecimal.valueOf(17000L));
	}

	public static ProductCreateRequest 음수가격상품() {
		return new ProductCreateRequest("돈을오히려드려요", BigDecimal.valueOf(-17000L));
	}
}
