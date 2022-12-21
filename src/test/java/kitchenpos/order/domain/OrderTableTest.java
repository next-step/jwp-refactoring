package kitchenpos.order.domain;

import static kitchenpos.generator.TableGroupGenerator.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("주문 테이블 테스트")
class OrderTableTest {

	@Test
	@DisplayName("주문 테이블 생성")
	void createOrderTableTest() {
		assertThatNoException()
			.isThrownBy(() -> OrderTable.of(
				NumberOfGuests.from(2),
				TableEmpty.from(false)));
	}

	@Test
	@DisplayName("주문 테이블 생성 - 인원수가 null이면 예외 발생")
	void createOrderTableWithNullNumberTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> OrderTable.of(
				null,
				TableEmpty.from(false)))
			.withMessageEndingWith("필수입니다.");
	}

	@Test
	@DisplayName("주문 테이블 생성 - 비어있는지 여부가 null이면 예외 발생")
	void createOrderTableWithNullEmptyTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> OrderTable.of(
				NumberOfGuests.from(2),
				null))
			.withMessageEndingWith("필수입니다.");
	}

	@Test
	@DisplayName("테이블 상태 변경")
	void updateEmptyTest() {
		OrderTable orderTable = OrderTable.of(
			NumberOfGuests.from(2),
			TableEmpty.from(false));

		orderTable.updateEmpty(true);

		assertThat(orderTable.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("테이블 상태 변경 - 테이블 그룹에 속한 테이블은 변경 불가")
	void updateEmptyWithTableGroupTest() {
		OrderTable orderTable = OrderTable.of(
			NumberOfGuests.from(2),
			TableEmpty.from(false));
		ReflectionTestUtils.setField(orderTable, "tableGroup", 다섯명_두명_테이블그룹());

		assertThatThrownBy(() -> orderTable.updateEmpty(true))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("테이블 그룹에 속한 테이블은 상태를 변경할 수 없습니다.");
	}

	@Test
	@DisplayName("인원수 변경")
	void updateNumberOfGuestsTest() {
		OrderTable orderTable = OrderTable.of(
			NumberOfGuests.from(2),
			TableEmpty.from(false));

		orderTable.updateNumberOfGuests(3);

		assertThat(orderTable.getNumberOfGuests()).isEqualTo(3);
	}

	@Test
	@DisplayName("인원수 변경 - 비어있는 테이블은 인원수 변경 불가")
	void updateNumberOfGuestsWithEmptyTableTest() {
		OrderTable orderTable = OrderTable.of(
			NumberOfGuests.from(2),
			TableEmpty.from(true));

		assertThatThrownBy(() -> orderTable.updateNumberOfGuests(3))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("빈 테이블은 인원수를 변경할 수 없습니다.");
	}
}
