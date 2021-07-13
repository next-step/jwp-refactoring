package kitchenpos.table;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.exception.TableException;

@DisplayName("손님 수 도메인 테스트")
public class NumberOfGuestTest {

	@DisplayName("손님 수 생성 테스트")
	@Test
	void create() {
		NumberOfGuests numberOfGuests = new NumberOfGuests(10);

		손님_수_생성_확인(numberOfGuests, 10);
	}

	@DisplayName("손님 수 생성 시 0 보다 작은 수 입력 시 에러 발생")
	@Test
	void 손님_수_생성_시_0보다_작은_수_입력_시_에러_발생() {
		assertThatThrownBy(() ->
			new NumberOfGuests(-1)
		).isInstanceOf(TableException.class);
	}

	private void 손님_수_생성_확인(NumberOfGuests numberOfGuests, int value) {
		assertThat(numberOfGuests).isNotNull();
		assertThat(numberOfGuests.value()).isEqualTo(value);
	}
}
