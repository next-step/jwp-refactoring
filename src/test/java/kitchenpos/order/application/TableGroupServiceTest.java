package kitchenpos.order.application;

import kitchenpos.common.application.NotFoundException;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.dto.OrderTableRequest_Create;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.order.dto.TableGroupRequest_Create;
import kitchenpos.order.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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
	private OrderTableRepository orderTableRepository;

	@Autowired
	private TableGroupRepository tableGroupRepository;

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
				.map(id -> orderTableRepository.findById(id).orElseThrow(Exception::new))
				.allSatisfy(orderTable -> assertThat(orderTable.getTableGroupId()).isEqualTo(response.getId()));
	}

	@DisplayName("단체 지정하려는 테이블의 ID가 실제로 존재하지 않을 경우 예외 발생.")
	@Test
	void create_ExceptionNotExistTable() {
		final TableGroupRequest_Create request = new TableGroupRequest_Create(Arrays.asList(-1L, -99L));
		assertThatThrownBy(() -> tableGroupService.create(request))
				.isInstanceOf(NotFoundException.class)
				.hasMessageMatching(TableGroupService.CANNOT_FIND_ORDER_TABLE);
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
				.map(id -> orderTableRepository.findById(id).orElseThrow(Exception::new))
				.allSatisfy(orderTable -> assertThat(orderTable.getTableGroupId()).isNull());

		assertThat(tableGroupRepository.findById(response.getId())).isNotPresent();
	}

	@DisplayName("단체지정 해제하려는 테이블의 ID가 실제로 존재하지 않을 경우 예외 발생")
	@Test
	void ungroup_ExceptionNotExistGroup() {
		assertThatThrownBy(() -> tableGroupService.ungroup(-1))
				.isInstanceOf(NotFoundException.class)
				.hasMessageMatching(TableGroupService.MSG_CANNOT_FIND_TABLE_GROUP);
	}
}
