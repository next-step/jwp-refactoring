package kitchenpos.domain;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TableGroupServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderTableRepository orderTableRepository;

	@Mock
	private TableGroupRepository tableGroupRepository;

	private TableGroupService tableGroupService;

	@Mock
	private TableGroup tableGroup;

	@BeforeEach
	void setUp() {
		tableGroupService = new TableGroupService(orderRepository, orderTableRepository, tableGroupRepository);
		assertThat(tableGroupService).isNotNull();
		tableGroup = mock(TableGroup.class);
	}

	@Test
	@DisplayName("테이블 그룹을 저장한다")
	void create() {
		List<OrderTable> orderTables = spy(ArrayList.class);
		OrderTable orderTable = mock(OrderTable.class);
		given(orderTable.getId()).willReturn(1L);
		given(orderTable.isEmpty()).willReturn(true);
		given(orderTable.getTableGroupId()).willReturn(null);
		orderTables.add(orderTable);

		OrderTable orderTable2 = mock(OrderTable.class);
		given(orderTable2.getId()).willReturn(2L);
		given(orderTable2.isEmpty()).willReturn(true);
		given(orderTable2.getTableGroupId()).willReturn(null);
		orderTables.add(orderTable2);

		given(orderTables.get(0).getTableGroupId()).willReturn(null);
		given(orderTables.get(1).getTableGroupId()).willReturn(null);

		given(tableGroup.getOrderTables()).willReturn(orderTables);
		given(orderTableRepository.findAllByIdIn(anyList())).willReturn(orderTables);
		given(tableGroupRepository.save(tableGroup)).willReturn(tableGroup);

		assertThat(tableGroupService.create(tableGroup)).isEqualTo(tableGroup);
	}

	@Test
	@DisplayName("테이블 그룹을 저장 시 테이블은 2개 이상이어야함")
	void whenSaveTableGroupHaveToUpperTwoTables() {
		List<OrderTable> orderTables = new ArrayList<>();
		OrderTable orderTable = mock(OrderTable.class);
		given(orderTable.getId()).willReturn(1L);
		given(orderTable.isEmpty()).willReturn(true);
		orderTables.add(orderTable);

		given(tableGroup.getOrderTables()).willReturn(orderTables);

		given(orderTableRepository.findAllByIdIn(anyList())).willReturn(orderTables);
		assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
	}

	@Test
	@DisplayName("테이블 그룹을 삭제한다")
	void ungroup() {
		OrderTable orderTable = mock(OrderTable.class);
		List<OrderTable> orderTables = new ArrayList<>(Arrays.asList(orderTable));
		given(orderTableRepository.save(orderTable)).willReturn(orderTable);
		given(orderTableRepository.findAllByTableGroupId(any())).willReturn(orderTables);
		given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);
		tableGroupService.ungroup(1L);
		verify(orderTableRepository, times(1)).save(orderTable);
	}

	@Test
	@DisplayName("조리 식사 상태의 테이블은 삭제할 수 없다")
	void givenMealStatusWhenUngroupThenError조리_식사_상태의_테이블은_삭제할_수_없다() {
		List<OrderTable> orderTables = new ArrayList<>();
		OrderTable orderTable = mock(OrderTable.class);
		given(orderTable.getId()).willReturn(1L);
		orderTables.add(orderTable);

		given(orderTableRepository.findAllByTableGroupId(1L)).willReturn(orderTables);
		given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willThrow(new IllegalArgumentException());
		assertThrows(IllegalArgumentException.class, () -> tableGroupService.ungroup(1L));
	}

}
