package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@DisplayName("메뉴 이름")
public class MenuNameTest {

	@DisplayName("생성")
	@Test
	void of() {
		// given
		String name = "후라이드+후라이드";

		// when
		MenuName menuGroupName = MenuName.of(name);

		// then
		assertThat(menuGroupName.getValue()).isEqualTo(name);
	}

	@DisplayName("생성 실패 - 이름이 없거나 빈 값인 경우")
	@ParameterizedTest
	@NullAndEmptySource
	void ofFailOnNullOrEmptyName(String name) {
		// given

		// when
		ThrowingCallable throwingCallable = () -> MenuName.of(name);

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("동등성 비교")
	@Test
	void equals() {
		// given
		String name = "후라이드+후라이드";

		// when
		MenuName actual = MenuName.of(name);
		MenuName expected = MenuName.of(name);

		// then
		assertThat(actual).isEqualTo(expected);
	}
}
