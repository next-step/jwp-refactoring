package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NameTest {

	@Test
	@DisplayName("이름을 null 로 생성하려고 하면 IllegalArgumentException 을 throw 해야한다.")
	void nullPrice() {
		//when-then
		assertThatThrownBy(() -> new Name(null))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("이름을 공백으로 생성하려고 하면 IllegalArgumentException 을 throw 해야한다.")
	void negativePrice() {
		//when-then
		assertThatThrownBy(() -> new Name(" "))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
