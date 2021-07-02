package kitchenpos.application;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@DisplayName("주문테이블그룹 요구사항 테스트")
@SpringBootTest
class TableGroupServiceIntegrationTest {
	@Autowired
	private TableGroupService tableGroupService;

	@DisplayName("2개 이상의 주문테이블만 그룹화 할 수 있다.")
	@Test
	void createTableGroupWithLessTwoOrderTablesTest() {
		// given
		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now());

		// when
		// then
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("2개 미만의 주문테이블은 그룹화 할 수 없습니다.");
	}

	@DisplayName("등록된 주문테이블만 그룹화 할 수 있다.")
	@Test
	void createTableGroupWithUnknownOrderTableTest() {
		// given
		OrderTable orderTable1 = new OrderTable(99L, null, 1, true);
		OrderTable orderTable2 = new OrderTable(100L, null, 1, true);

		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), asList(orderTable1, orderTable2));
		tableGroup.setOrderTables(asList(orderTable1, orderTable2));

		// when
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("등록이 되지 않은 주문테이블은 그룹화 할 수 없습니다.");
	}

	@DisplayName("그룹화할 주문테이블들 모두 빈 테이블이어야 한다.")
	@Test
	void createTableGroupWithNotEmptyOrderTableTest() {
		// given
		OrderTable notEmptyTable = new OrderTable(9L, null, 1, false);
		OrderTable orderTable2 = new OrderTable(1L, null, 0, true);

		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), asList(notEmptyTable, orderTable2));
		tableGroup.setOrderTables(asList(notEmptyTable, orderTable2));

		// when
		assertThatThrownBy(() -> tableGroupService.create(tableGroup))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("비어있지 않거나, 이미 그룹화되어 있는 테이블은 그룹화 할 수 없습니다.");
	}

	@DisplayName("그룹화된 주문테이블들 중 조리상태이거나 식사상태이면 그룹해제를 할 수 없다.")
	@Test
	void createTableGroupTest() {
		// when
		assertThatThrownBy(() -> tableGroupService.ungroup(1L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("조리상태이거나 식사상태인 주문이 있는 주문테이블은 그룹해제를 할 수 없습니다.");
	}

}
