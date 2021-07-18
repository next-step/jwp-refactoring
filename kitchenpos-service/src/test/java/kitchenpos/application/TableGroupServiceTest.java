package kitchenpos.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
	@Mock
	private OrderTableRepository orderTableRepository;
	@Mock
	private TableGroupRepository tableGroupRepository;
	@Mock
	private OrderService orderService;

	@InjectMocks
	private TableGroupService tableGroupService;

	private TableGroupRequest tableGroupRequest;
	private OrderTable orderTable;
	private OrderTable orderTable2;

	@BeforeEach
	void setUp() {
		orderTable = new OrderTable(1L, null, 2, true);
		orderTable2 = new OrderTable(2L, null, 3, true);

		tableGroupRequest = new TableGroupRequest(LocalDateTime.of(2021, 1, 1, 1, 1), Arrays.asList(1L, 2L));
	}

	@Test
	void createTableGroupTest() {
		TableGroup tableGroup = new TableGroup(1L, LocalDateTime.of(2021, 1, 1, 1, 1));

		Mockito.when(orderTableRepository.findAllByIdIn(Lists.list(1L, 2L))).thenReturn(Lists.list(orderTable, orderTable2));
		Mockito.when(tableGroupRepository.save(tableGroupRequest.toTableGroup())).thenReturn(tableGroup);

		assertThat(tableGroupService.create(tableGroupRequest)).isNotNull();
	}

	@Test
	@DisplayName("테이블 그룹 생성 시 저장되어있는 주문테이블과 요청 된 주문테이블의 수가 맞지 않으면 익셉션 발생")
	void createTableGroupFailTest() {
		Mockito.when(orderTableRepository.findAllByIdIn(Lists.list(1L, 2L))).thenReturn(Lists.list(orderTable));

		Assertions.assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void ungroupTest() {
		List<OrderTable> orderTables = Arrays.asList(new OrderTable(1L, 1L, 2, false), new OrderTable(2L, 1L, 3, false));
		Mockito.when(orderTableRepository.findAllByTableGroupId(1L)).thenReturn(orderTables);
		tableGroupService.ungroup(1L);

		assertThat(orderTables.get(0).getTableGroupId()).isNull();
	}
}
