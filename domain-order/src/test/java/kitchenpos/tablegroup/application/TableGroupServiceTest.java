package kitchenpos.tablegroup.application;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableUngroupValidator;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;

@DisplayName("주문테이블그룹 요구사항 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
	@Mock
	private TableUngroupValidator tableUngroupValidator;

	@Mock
	private OrderTableRepository orderTableRepository;

	@Mock
	private TableGroupRepository tableGroupRepository;

	@InjectMocks
	private TableGroupService tableGroupService;

	@DisplayName("등록된 주문테이블만 그룹화 할 수 있다.")
	@Test
	void createTableGroupWithUnknownOrderTableTest() {
		// given
		TableGroupRequest mockRequest = mock(TableGroupRequest.class);
		when(mockRequest.getOrderTableIds()).thenReturn(asList(1L, 2L));
		when(orderTableRepository.findAllById(anyList())).thenReturn(new ArrayList<>());

		// when
		assertThatThrownBy(() -> tableGroupService.create(mockRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("등록이 되지 않은 주문테이블은 그룹화 할 수 없습니다.");
	}

	@DisplayName("등록된 테이블그룹만 그룹해제를 할 수 있다.")
	@Test
	void ungroupUnknownTableGroupTest() {
		when(tableGroupRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> tableGroupService.ungroup(1L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("등록된 테이블 그룹만 그룹해제 가능합니다.");
	}

	@DisplayName("테이블그룹을 그룹해제할 수 있다.")
	@Test
	void ungroupTableGroupTest() {
		TableGroup tableGroup = mock(TableGroup.class);
		when(tableGroupRepository.findById(anyLong())).thenReturn(Optional.of(tableGroup));

		tableGroupService.ungroup(1L);

		verify(tableGroup).ungroup(anyList(), any(TableUngroupValidator.class));
	}
}
