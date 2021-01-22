package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTableTest {

	private static final TableGroup 등록된_단체 = new TableGroup(1L, LocalDateTime.now());
	private OrderTable 단체가지정되지않은_빈테이블_1;
	private OrderTable 단체가지정된_빈테이블_2;
	private OrderTable 단체가지정되지않은_비어있지않은테이블_3;

	@BeforeEach
	void setUp() {
		단체가지정되지않은_빈테이블_1 = new OrderTable(1L, null, 0, true );
		단체가지정된_빈테이블_2 = new OrderTable(2L, 등록된_단체, 0, true );
		단체가지정되지않은_비어있지않은테이블_3 = new OrderTable(3L, null, 0, false );
	}

	@Test
	@DisplayName("테이블의 단체를 변경하면, 빈 테이블 여부가 비어있지 않음으로 변경되어야 한다.")
	void updateTableGroup() {
		//when
		단체가지정되지않은_빈테이블_1.updateTableGroup(등록된_단체);

		//then
		assertThat(단체가지정되지않은_빈테이블_1.getTableGroupId()).isEqualTo(등록된_단체.getId());
		assertThat(단체가지정되지않은_빈테이블_1.isEmpty()).isFalse();
	}

	@Test
	@DisplayName("테이블의 단체를 변경하면, 빈 테이블 여부가 비어있지 않음으로 변경되어야 한다.")
	void updateTableGroupNull() {
		//when-then
		assertThatThrownBy(() -> 단체가지정되지않은_빈테이블_1.updateTableGroup(null))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("테이블의 단체를 변경하면, 빈 테이블 여부가 비어있지 않음으로 변경되어야 한다.")
	void updateEmpty() {
		//when
		단체가지정되지않은_빈테이블_1.updateEmpty(false);

		//then
		assertThat(단체가지정되지않은_빈테이블_1.isEmpty()).isFalse();
	}

	@Test
	@DisplayName("테이블의 공석여부를 변경할 떄, 단체가 지정되어있다면 IllegalArgumentException 을 throw 해야한다.")
	void updateEmptyWithGroupTable() {
		//when-then
		assertThatThrownBy(() -> 단체가지정된_빈테이블_2.updateEmpty(false))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("테이블의 손님 수를 변경하면, 변경을 시도한 값으로 변경되어야 한다.")
	void updateNumberOfGuests() {
		//when
		int origin = 단체가지정되지않은_비어있지않은테이블_3.getNumberOfGuests();
		단체가지정되지않은_비어있지않은테이블_3.updateNumberOfGuests(origin + 2);

		//then
		assertThat(단체가지정되지않은_비어있지않은테이블_3.getNumberOfGuests()).isEqualTo(origin + 2);
	}

	@Test
	@DisplayName("테이블의 손님 수를 변경할 때, 테이블이 비어있다면 IllegalArgumentException 을 throw 해야한다.")
	void updateNumberOfGuestsWithEmptyTable() {
		//when-then
		assertThatThrownBy(() -> 단체가지정되지않은_빈테이블_1.updateNumberOfGuests(5))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
