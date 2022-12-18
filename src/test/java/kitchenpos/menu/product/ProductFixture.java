package kitchenpos.menu.product;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import kitchenpos.menu.ui.dto.ProductRequest;
import kitchenpos.menu.ui.dto.ProductResponse;

public class ProductFixture {

	public static final String 상품명 = "product";
	public static final long 상품가격 = 1000L;

	public static ProductRequest 상품(String name, Long price) {
		return new ProductRequest(name, price);
	}

	public static ProductResponse 상품(Long id) {
		return new ProductResponse(id, 상품명, 상품가격);
	}

	public static List<ProductRequest> 상품목록(int count) {
		return LongStream.range(0, count)
						 .mapToObj(i -> new ProductRequest(상품명 + i, 상품가격))
						 .collect(Collectors.toList());
	}
}
