package kitchenpos.domain;

import static kitchenpos.common.TestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.exception.AlreadyTableGroupException;
import kitchenpos.exception.EmptyTableException;
import kitchenpos.exception.NegativeNumberException;

@DisplayName("OrderTable 도메인 테스트")
class OrderTableTest {

	@DisplayName("create 메소드 numberOfGuests가 0, empty가 true 인 OrderTable 인스턴스를 얻을 수 있다.")
	@Test
	void create() {
		OrderTable result = OrderTable.create();

		assertAll(
			() -> assertThat(result).isInstanceOf(OrderTable.class),
			() -> assertThat(result.getNumberOfGuests()).isEqualTo(0),
			() -> assertThat(result.isEmpty()).isTrue()
		);
	}

	@DisplayName("changeEmpty 메소드는 empty 상태를 변경할 수 있다.")
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void changeEmpty(boolean isEmpty) {
		OrderTable table = OrderTable.create();

		table.changeEmpty(isEmpty);

		assertThat(table.isEmpty()).isEqualTo(isEmpty);
	}

	@DisplayName("changeEmpty 메소드는 단체테이블이 지정된 상태에서 변경하는 경우 AlreadyTableGroupException이 발생한다.")
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void changeEmptyThrow(boolean isEmpty) {
		assertThatExceptionOfType(AlreadyTableGroupException.class)
			.isThrownBy(() -> {
				OrderTable table = 그룹_지정된_테이블_객체();
				table.changeEmpty(isEmpty);
			});
	}

	@DisplayName("changeNumberOfGuests 메소드는 비어있지 않은 상태에서 0보다 큰 인원을 지정할 수 있다.")
	@ParameterizedTest
	@ValueSource(ints = {1, 10, 100, 1000, 10000})
	void changeNumberOfGuests(int numberOfGuest) {
		OrderTable table = 비어있지않은_테이블_객체();

		table.changeNumberOfGuests(numberOfGuest);

		assertThat(table.getNumberOfGuests()).isEqualTo(numberOfGuest);
	}

	@DisplayName("changeNumberOfGuests 메소드는 빈테이블에 인원을 지정하면 EmptyTableException이 발생한다.")
	@Test
	void changeNumberOfGuestsThrow1() {
		assertThatExceptionOfType(EmptyTableException.class)
			.isThrownBy(() -> {
				OrderTable table = 빈_테이블_객체();
				table.changeNumberOfGuests(5);
			});
	}

	@DisplayName("changeNumberOfGuests 메소드는 음수 인원을 지정하면 NegativeNumberException이 발생한다.")
	@ParameterizedTest
	@ValueSource(ints = {-1, -10, -100})
	void changeNumberOfGuestsThrow2(int numberOfGuest) {
		assertThatExceptionOfType(NegativeNumberException.class)
			.isThrownBy(() -> {
				OrderTable table = 비어있지않은_테이블_객체();
				table.changeNumberOfGuests(numberOfGuest);
			});
	}

	@DisplayName("saveGroupInfo 메소드는 empty 상태가 false가 되고 tableGroup 값이 할당된다.")
	@Test
	void saveGroupInfo() {
		OrderTable table = OrderTable.create();

		table.saveGroupInfo(new TableGroup());

		assertAll(
			() -> assertThat(table.isEmpty()).isFalse(),
			() -> assertThat(table.getTableGroup()).isNotNull()
		);
	}

	@DisplayName("ungroup 메소드는 tableGroup이 null이 된다.")
	@Test
	void ungroup() {
		OrderTable table = 그룹_지정된_테이블_객체();

		table.ungroup();

		assertThat(table.getTableGroup()).isNull();
	}

}