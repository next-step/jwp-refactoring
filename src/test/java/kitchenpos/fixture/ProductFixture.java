package kitchenpos.fixture;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import kitchenpos.domain.Money;
import kitchenpos.domain.Product;

public class ProductFixture {

	public static Product 상품(String name, Money price) {
		return new Product(name, price);
	}

	public static Product 상품(String name, int price) {
		return new Product(name, price);
	}

	public static List<Product> 상품목록(int count) {
		return LongStream.range(0, count)
			.mapToObj(i -> new Product(i, "product"+i, 1000))
			.collect(Collectors.toList());
	}

}
