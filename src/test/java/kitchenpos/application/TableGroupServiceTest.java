package kitchenpos.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

	@Autowired
	private TableGroupService tableGroupService;

	@Autowired
	private TableService tableService;

	@Autowired
	private OrderTableDao orderTableDao;

	@Autowired
	private TableGroupDao tableGroupDao;

	@Autowired
	private TableTestSupport tableTestSupport;

	private OrderTableResponse orderTable1;
	private OrderTableResponse orderTable2;
	private OrderTableResponse orderTable3;
	private List<Long> orderTableIds;

	@BeforeEach
	void setUp() {
		orderTable1 = tableService.create(new OrderTableRequest_Create(20, true));
		orderTable2 = tableService.create(new OrderTableRequest_Create(10, true));
		orderTable3 = tableService.create(new OrderTableRequest_Create(30, true));

		orderTableIds = Arrays.asList(orderTable1.getId(), orderTable2.getId(), orderTable3.getId());
	}

	@DisplayName("여러 개의 테이블을 묶어 단체 지정한다.")
	@Test
	void create() {
		TableGroupResponse response = tableGroupService.create(new TableGroupRequest_Create(orderTableIds));

		assertThat(response.getId()).isNotNull();
		assertThat(response.getOrderTables())
				.map(OrderTableResponse::getId)
				.containsExactly(orderTable1.getId(), orderTable2.getId(), orderTable3.getId());
		// OrderTable 에 tableGroupId 변경 됐는지 확인
		assertThat(response.getOrderTables())
				.map(OrderTableResponse::getId)
				.map(id -> orderTableDao.findById(id).orElseThrow(Exception::new))
				.allSatisfy(orderTable -> assertThat(orderTable.getTableGroup().getId()).isEqualTo(response.getId()));
	}

	@DisplayName("단체 지정하려는 테이블 수가 적을 경우 예외 발생.")
	@Test
	void create_OrderTablesLow() {
		TableGroupRequest_Create request1 = new TableGroupRequest_Create(Collections.singletonList(orderTable1.getId()));
		TableGroupRequest_Create request2 = new TableGroupRequest_Create(Collections.emptyList());

		assertThatThrownBy(() -> tableGroupService.create(request1))
				.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> tableGroupService.create(request2))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("이미 단체 지정된 테이블을 단체 지정하려고 시도 할 경우 예외 발생.")
	@Test
	void create_AlreadyGroupExist() {
		// given
		final TableGroupRequest_Create request = new TableGroupRequest_Create(orderTableIds);
		tableGroupService.create(request);

		// when then
		assertThatThrownBy(() -> tableGroupService.create(request))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체 지정시 테이블이 비어 있지 않은 경우 예외 발생.")
	@Test
	void create_EmptyFalse() {
		// given
		tableService.changeEmpty(orderTable1.getId(), new OrderTableRequest_ChangeEmpty(false));

		assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest_Create(orderTableIds)))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("단체 지정된 테이블의 단체지정을 해제한다.")
	@Test
	void ungroup() {
		// given
		TableGroupResponse response = tableGroupService.create(new TableGroupRequest_Create(orderTableIds));

		// when
		tableGroupService.ungroup(response.getId());

		// then
		// 각 테이블별로 그룹 ID 해제된 것 확인
		assertThat(response.getOrderTables())
				.map(OrderTableResponse::getId)
				.map(id -> orderTableDao.findById(id).orElseThrow(Exception::new))
				.allSatisfy(orderTable -> assertThat(orderTable.getTableGroup()).isNull());

		// TODO: 2021-01-15 테이블 그룹 삭제되도록 처리
		assertThat(tableGroupDao.findById(response.getId())).isNotPresent();
	}

	@DisplayName("단체 지정 해제가 불가능한 상태일때 단체지정을 요청할경우 예외 발생.")
	@Test
	void ungroup_StatusWrong() {
		// given
		TableGroupResponse response = tableGroupService.create(new TableGroupRequest_Create(orderTableIds));
		tableTestSupport.addOrder(orderTableIds.get(0));

		// when
		assertThatThrownBy(() -> tableGroupService.ungroup(response.getId()))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
