package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@DisplayName("상품 이름")
class ProductNameTest {
	@DisplayName("생성")
	@Test
	void of() {
		// given
		String name = "추천 메뉴";

		// when
		ProductName productName = ProductName.of(name);

		// then
		assertThat(productName.getValue()).isEqualTo(name);
	}

	@DisplayName("생성 실패 - 이름이 없거나 빈 값인 경우")
	@ParameterizedTest
	@NullAndEmptySource
	void ofFailOnNullOrEmptyName(String name) {
		// given

		// when
		ThrowableAssert.ThrowingCallable throwingCallable = () -> ProductName.of(name);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("동등성 비교")
	@Test
	void equals() {
		// given
		String name = "강정치킨";

		// when
		ProductName actual = ProductName.of(name);
		ProductName expected = ProductName.of(name);

		// then
		assertThat(actual).isEqualTo(expected);
	}
}
