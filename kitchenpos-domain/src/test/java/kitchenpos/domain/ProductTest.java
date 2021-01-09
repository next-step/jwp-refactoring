package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import kitchenpos.exception.WrongPriceException;

@DisplayName("Product 도메인 테스트")
class ProductTest {

	@DisplayName("create 메소드로 상품명과 0보다 큰 가격을 전달하면 Product 인스턴스를 얻을 수 있다.")
	@ParameterizedTest
	@MethodSource("paramProduct")
	void product(String name, BigDecimal price) {
		Product result = Product.create(name, price);

		assertThat(result).isInstanceOf(Product.class);
	}

	public static Stream<Arguments> paramProduct() {
		return Stream.of(
			Arguments.of("피자", BigDecimal.valueOf(1)),
			Arguments.of("치킨", BigDecimal.valueOf(10)),
			Arguments.of("탕수육", BigDecimal.valueOf(100)),
			Arguments.of("짜장", BigDecimal.valueOf(1000)),
			Arguments.of("짬뽕", BigDecimal.valueOf(10000))
		);
	}

	@DisplayName("create 메소드에 상품 가격을 기록하지 않거나, 0보다 작으면 등록할 수 없다.")
	@ParameterizedTest
	@NullSource
	@MethodSource("paramProductThrow")
	void productThrow(BigDecimal price) {
		assertThatExceptionOfType(WrongPriceException.class)
			.isThrownBy(() -> {
				Product.create("상품", price);
			});
	}

	public static Stream<Arguments> paramProductThrow() {
		return Stream.of(
			Arguments.of(BigDecimal.valueOf(-1)),
			Arguments.of(BigDecimal.valueOf(-10)),
			Arguments.of(BigDecimal.valueOf(-100)),
			Arguments.of(BigDecimal.valueOf(-1000)),
			Arguments.of(BigDecimal.valueOf(-10000))
		);
	}

}