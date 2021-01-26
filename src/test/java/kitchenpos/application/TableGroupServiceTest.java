package kitchenpos.application;

import static kitchenpos.domain.DomainFactory.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TableGroupServiceTest {
	@Autowired
	private TableGroupService tableGroupService;
	@Autowired
	private TableService tableService;

	@DisplayName("테이블 리스트가 2보다 작으면 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException1() {
		TableGroup tableGroup = createTableGroup(1L);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.create(tableGroup));
	}

	@DisplayName("등록되지 않은 테이블 지정시 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException2() {
		TableGroup tableGroup = createTableGroup(1L, 9L);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.create(tableGroup));
	}

	@DisplayName("주문 테이블 지정시 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException3() {
		tableService.changeEmpty(1L, createOrderTable(false));
		TableGroup tableGroup = createTableGroup(1L, 2L);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.create(tableGroup));
	}

	@DisplayName("단체 지정 등록")
	@Test
	void create() {
		TableGroup resultGroup = 단체_지정();

		assertThat(resultGroup.getId()).isNotNull();
		List<OrderTable> resultOrderTables = resultGroup.getOrderTables();
		assertThat(resultOrderTables).hasSize(2);
		assertThat(resultOrderTables.get(0).getTableGroupId()).isEqualTo(resultGroup.getId());
		assertThat(resultOrderTables.get(1).getTableGroupId()).isEqualTo(resultGroup.getId());
	}

	@DisplayName("단체 테이블 지정시 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException4() {
		TableGroup resultGroup = 단체_지정();

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableGroupService.create(resultGroup));
	}

	@DisplayName("주문 상태가 요리 중 또는 식사 중이면 IllegalArgumentException 발생")
	@Test
	void ungroup_ThrowIllegalArgumentException() {
	}

	@DisplayName("단체 지정 해제")
	@Test
	void ungroup() {
		TableGroup resultGroup = 단체_지정();

		tableGroupService.ungroup(resultGroup.getId());

		List<OrderTable> orderTables = tableService.list();
		assertThat(orderTables.get(0).getTableGroupId()).isNull();
		assertThat(orderTables.get(1).getTableGroupId()).isNull();
	}

	private TableGroup 단체_지정() {
		TableGroup tableGroup = createTableGroup(1L, 2L);

		return tableGroupService.create(tableGroup);
	}
}
