package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문테이블 도메인 테스트")
public class OrderTableTest {

	@DisplayName("주문테이블 생성")
	@Test
	void create() {
		final int numberOfGuests = 1;
		final boolean empty = true;

		OrderTable orderTable = new OrderTable(numberOfGuests, empty);

		assertThat(orderTable).isNotNull();
		assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
		assertThat(orderTable.isEmpty()).isEqualTo(empty);
	}

	@DisplayName("주문테이블 빈테이블 여부 변경")
	@Test
	void changeEmpty() {
		final int numberOfGuests = 1;
		final boolean empty = true;
		final boolean expected = false;
		OrderTable orderTable = new OrderTable(numberOfGuests, empty);

		orderTable.changeEmpty(expected);

		assertThat(orderTable).isNotNull();
		assertThat(orderTable.isEmpty()).isEqualTo(expected);

	}

	@DisplayName("주문테이블 빈테이블 여부 변경 예외: 단체 지정이 되어 있음")
	@Test
	void changeEmptyThrowExceptionWhenAlreadyTableGrouping() {
		final int numberOfGuests = 1;
		final boolean empty = true;
		final boolean expected = false;
		OrderTable orderTable1 = new OrderTable(numberOfGuests, empty);
		OrderTable orderTable2 = new OrderTable(numberOfGuests, empty);
		TableGroup.createTableGroup(Arrays.asList(orderTable1, orderTable2));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderTable1.changeEmpty(expected));

	}

	@DisplayName("주문테이블 방문한 손님수 변경")
	@Test
	void changeNumberOfGuests() {
		final int numberOfGuests = 1;
		final boolean empty = false;
		final int expected = 5;
		OrderTable orderTable = new OrderTable(numberOfGuests, empty);

		orderTable.changeNumberOfGuests(expected);

		assertThat(orderTable.getNumberOfGuests()).isEqualTo(expected);
	}

	@DisplayName("주문테이블 방문한 손님수 변경 예외: 0보다 작음")
	@Test
	void changeNumberOfGuestsThrowExceptionWhenLessThenZero() {
		final int numberOfGuests = 1;
		final boolean empty = true;
		final int expected = -1;
		OrderTable orderTable = new OrderTable(numberOfGuests, empty);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderTable.changeNumberOfGuests(expected));
	}

	@DisplayName("주문테이블 방문한 손님수 변경 예외: 빈 테이블임")
	@Test
	void changeNumberOfGuestsThrowExceptionWhenEmptyTable() {
		final int numberOfGuests = 1;
		final boolean empty = true;
		final int expected = -1;
		OrderTable orderTable = new OrderTable(numberOfGuests, empty);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderTable.changeNumberOfGuests(expected));
	}

}
