package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@DisplayName("이름")
class NameTest {
	@DisplayName("생성")
	@Test
	void of() {
		// given
		String value = "이름";

		// when
		Name name = Name.of(value);

		// then
		assertThat(name.getValue()).isEqualTo(value);
	}

	@DisplayName("생성 실패 - 이름이 없거나 빈 값인 경우")
	@ParameterizedTest
	@NullAndEmptySource
	void ofFailOnNullOrEmptyName(String value) {
		// given

		// when
		ThrowingCallable throwingCallable = () -> Name.of(value);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("동등성 비교")
	@Test
	void equals() {
		// given
		String name = "이름";

		// when
		Name actual = Name.of(name);
		Name expected = Name.of(name);

		// then
		assertThat(actual).isEqualTo(expected);
	}

}
