package kitchenpos.order;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {
	@DisplayName("테이블이 없을 경우 IllegalArgumentException 발생")
	@Test
	void createWhenNoOrderTable() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Order.builder().build());
	}
}
