package kitchenpos.acceptance.product;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import kitchenpos.ui.dto.ProductRequest;

public class ProductFixture {

	public static ProductRequest 상품(String name, Long price) {
		return new ProductRequest(name, price);
	}

	public static List<ProductRequest> 상품목록(int count) {
		return LongStream.range(0, count)
			.mapToObj(i -> new ProductRequest("product"+i, 1000L))
			.collect(Collectors.toList());
	}
}
