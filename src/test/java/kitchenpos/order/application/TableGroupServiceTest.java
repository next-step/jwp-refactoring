package kitchenpos.order.application;

import static kitchenpos.domain.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.BaseServiceTest;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.order.dao.OrderTableRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;

public class TableGroupServiceTest extends BaseServiceTest {
	@Autowired
	private TableGroupService tableGroupService;

	@Autowired
	private OrderTableRepository orderTableRepository;

	@Test
	@DisplayName("단체를 지정할 수 있다.")
	void create() {
		//given
		OrderTableRequest orderTableRequest1 = new OrderTableRequest(테이블_비어있는_0명_1.getId());
		OrderTableRequest orderTableRequest2 = new OrderTableRequest(테이블_비어있는_0명_2.getId());

		TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTableRequest1, orderTableRequest2));

		//when
		TableGroupResponse result = tableGroupService.create(tableGroupRequest);

		//then
		List<Long> tableGroupIds = result.getOrderTables().stream()
			.map(OrderTableResponse::getTableGroupId)
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

		assertThat(result.getId()).isNotNull();
		assertThat(tableGroupIds.size()).isEqualTo(2);
		assertThat(tableGroupIds.stream()
			.distinct().count()).isEqualTo(1);
		assertThat(result.getOrderTables().stream()
			.filter(OrderTableResponse::isEmpty)
			.count()).isEqualTo(0);
	}

	@Test
	@DisplayName("단체 지정할 시, 대상 테이블이 없으면 IllegalArgumentException을 throw 해야한다.")
	void createTableGroupForNullTable() {
		//given
		TableGroupRequest tableGroupRequest = new TableGroupRequest(null);

		//when-then
		assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("단체 지정할 시, 대상 테이블이 1개이면 IllegalArgumentException을 throw 해야한다.")
	void createTableGroupForOneTable() {
		//given
		OrderTableRequest orderTableRequest2 = new OrderTableRequest(테이블_비어있는_0명_2.getId());
		TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTableRequest2));

		//when-then
		assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("단체 지정할 시, 대상 테이블이 등록되어있지 않으면 IllegalArgumentException을 throw 해야한다.")
	void createTableGroupForNotExistTable() {
		//given
		OrderTableRequest notExistTable = new OrderTableRequest(존재하지않는_ID);
		TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(notExistTable));

		//when-then
		assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("단체 지정할 시, 대상 테이블이 빈 테이블이 아니면 IllegalArgumentException을 throw 해야한다.")
	void createTableGroupForNotEmptyTable() {
		//given
		OrderTableRequest orderTableRequestEmpty = new OrderTableRequest(테이블_비어있는_0명_1.getId());
		OrderTableRequest orderTableRequestNotEmpty = new OrderTableRequest(테이블_비어있지않은_2명_9.getId());
		TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTableRequestEmpty, orderTableRequestNotEmpty));

		//when-then
		assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("단체 지정할 시, 이미 그룹에 포함된 테이블이면 IllegalArgumentException을 throw 해야한다.")
	void createTableGroupForAlreadyHaveGroup() {
		//given
		OrderTableRequest orderTableRequest = new OrderTableRequest(테이블_비어있는_0명_1.getId());
		OrderTableRequest orderTableRequestHaveGroup = new OrderTableRequest(테이블_단체1_0명_10.getId());
		TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTableRequest, orderTableRequestHaveGroup));

		//when-then
		assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("단체 지정을 해지할 수 있다.")
	void ungroup() {
		//given
		Long tableGroupId = 테이블단체_1.getId();
		List<OrderTable> orderTablesHavingGroup = orderTableRepository.findAllByTableGroup(테이블단체_1);
		assertThat(orderTablesHavingGroup.size()).isEqualTo(2);

		//when
		tableGroupService.ungroup(tableGroupId);

		//then
		List<OrderTable> orderTablesAfterUngroup = orderTableRepository.findAllByTableGroup(테이블단체_1);
		assertThat(orderTablesAfterUngroup.size()).isEqualTo(0);
	}

	@Test
	@DisplayName("단체 지정을 해지 시, 주문 상태가 조리 또는 식사인 테이블이면 IllegalArgumentException을 throw 해야한다.")
	void ungroupCookingTable() {
		//when-then
		assertThatThrownBy(() -> tableGroupService.ungroup(테이블_단체2_조리중_3명_11.getTableGroupId()))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
