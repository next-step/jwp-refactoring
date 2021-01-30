package kitchenpos.menu;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {
	@DisplayName("가격이 없거나 0보다 작으면 IllegalArgumentException 발생")
	@Test
	void createWhenWrongPrice() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> new Price(-1));
	}

}
