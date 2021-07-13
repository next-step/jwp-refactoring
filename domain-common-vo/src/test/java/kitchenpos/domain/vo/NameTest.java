package kitchenpos.domain.vo;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class NameTest {

	@DisplayName("이름은 빈문자열일 수 없다.")
	@Test
	void createNameTest() {
		assertAll(
			() -> Assertions.assertThatThrownBy(() -> Name.valueOf("  "), "이름이 공백만 있은 문자열 일 때 이름 생성 실패해야함")
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("이름은 빈문자열일 수 없습니다."),

			() -> Assertions.assertThatThrownBy(() -> Name.valueOf(null), "이름이 null 문자열 일 때 이름 생성 실패해야함")
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("이름은 빈문자열일 수 없습니다.")
		);
	}

}