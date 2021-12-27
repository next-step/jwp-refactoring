package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

	@Autowired
	private TableGroupService tableGroupService;

	@Autowired
	private OrderTableService orderTableService;

	@Test
	@DisplayName("테이블 그룹 생성 테스트")
	public void createTableGroupSuccessTest() {
		//given
		OrderTableRequest tableRequest = new OrderTableRequest(1L);
		OrderTableRequest otherTableRequest = new OrderTableRequest(2L);
		ArrayList<OrderTableRequest> orderTableRequests = Lists.newArrayList(tableRequest, otherTableRequest);
		TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableRequests);

		//when
		TableGroup save = tableGroupService.create(tableGroupRequest);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getId()).isEqualTo(3L);
	}

	@Test
	@DisplayName("테이블의 개수가 2개보다 작아서 그룹생성 실패")
	public void createTableGroupFailOrderTableLessThanTwoTest() {
		//given
		TableGroupRequest tableGroupRequest = new TableGroupRequest(Lists.emptyList());

		//when
		//then
		assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("2개 이상의 테이블만 그룹생성이 가능합니다");
	}

	@Test
	@DisplayName("사용중이거나 이미 그룹에 속한 테이블로 그룹 생성 요청해서 실패")
	public void createTableGroupFailTableUseOrAlreadyGroupingTest() {
		//given
		OrderTableRequest tableRequest = new OrderTableRequest(4L);
		OrderTableRequest otherTableRequest = new OrderTableRequest(8L);
		ArrayList<OrderTableRequest> orderTableRequests = Lists.newArrayList(tableRequest, otherTableRequest);
		TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableRequests);

		//when
		//then
		assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("이미 사용중이거나 그룹화된 테이블은 그룹생성 할 수 없습니다");
	}

	@Test
	@DisplayName("그룹 해제 테스트")
	public void ungroupTest() {
		//when
		tableGroupService.ungroup(1L);
		OrderTable orderTable = orderTableService.findById(9L);

		//then
		assertThat(orderTable.getTableGroup()).isNull();
	}

	@Test
	@DisplayName("주문이 계산완료되지 않아서 테이블 그룹 해제 실패")
	public void ungroupFailTest() {
		assertThatThrownBy(() -> tableGroupService.ungroup(2L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("아직 테이블의 주문이 계산완료되지 않았습니다");
	}
}
