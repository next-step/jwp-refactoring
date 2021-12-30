package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;

@DisplayName("이름 도메인 테스트")
public class NameTest {

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		assertThat(Name.of("이름")).isEqualTo(Name.of("이름"));
	}

	@DisplayName("비어있는 문자열은 안된다")
	@Test
	void validateTest() {
		assertThatThrownBy(() -> Name.of(""))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}
}
