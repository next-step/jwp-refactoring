package kitchenpos.acceptance.product;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import kitchenpos.domain.Money;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductRequest;

public class ProductFixture {

	public static ProductRequest 상품2(String name, Long price) {
		return new ProductRequest(name, price);
	}

	@Deprecated
	public static Product 상품(String name, Money price) {
		return new Product(name, price);
	}

	@Deprecated
	public static Product 상품(String name, int price) {
		return new Product(name, Money.valueOf(price));
	}

	@Deprecated
	public static List<Product> 상품목록(int count) {
		return LongStream.range(0, count)
			.mapToObj(i -> new Product(i, "product"+i, 1000))
			.collect(Collectors.toList());
	}

	public static List<ProductRequest> 상품목록2(int count) {
		return LongStream.range(0, count)
			.mapToObj(i -> new ProductRequest("product"+i, 1000L))
			.collect(Collectors.toList());
	}
}
