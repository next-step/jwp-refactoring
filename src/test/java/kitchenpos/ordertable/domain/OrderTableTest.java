package kitchenpos.ordertable.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.tablegroup.domain.TableGroup;

class OrderTableTest {

	@Test
	@DisplayName("주문테이블 생성 테스트")
	public void createOrderTableTest() {
		//when
		OrderTable orderTable = new OrderTable();

		//then
		assertThat(orderTable).isNotNull();
	}

	@Test
	@DisplayName("테이블이 그룹화되어있는지 확인")
	public void isGrouped() {
		//given
		OrderTable orderTable = new OrderTable(1L, 1L, 0, false);

		//when
		boolean result = orderTable.isGrouped();

		//then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("테이블이 미사용인지 확인")
	public void isNotUse() {
		//given
		OrderTable orderTable = new OrderTable(0, true);

		//when
		boolean result = orderTable.isNotUse();

		//then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("테이블이 사용중인지 확인")
	public void isUse() {
		//given
		OrderTable orderTable = new OrderTable(0, false);

		//when
		boolean result = orderTable.isUse();

		//then
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("미 사용 테이블 그룹화")
	public void toGroup() {
		//given
		TableGroup tableGroup = new TableGroup(1L);
		OrderTable orderTable = new OrderTable(0, true);

		//when
		orderTable.toGroup(tableGroup.getId());

		//then
		assertThat(orderTable.getTableGroupId()).isNotNull();
		assertThat(orderTable.isUse()).isTrue();
	}

	@Test
	@DisplayName("테이블 그룹해제")
	public void ungroup() {
		//given
		OrderTable orderTable = new OrderTable(1L, 1L, 0, true);

		//when
		orderTable.ungroup();

		//then
		assertThat(orderTable.getTableGroupId()).isEqualTo(-1L);
	}

}
