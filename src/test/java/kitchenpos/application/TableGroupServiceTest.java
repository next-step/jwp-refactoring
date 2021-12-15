package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@DisplayName("테이블 그룹(단체지정) : 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

	@Mock
	OrderDao orderDao;

	@Mock
	OrderTableDao orderTableDao;

	@Mock
	TableGroupDao tableGroupDao;

	@Mock
	OrderTable orderTable;

	@Mock
	TableGroup tableGroup;

	@InjectMocks
	private TableGroupService tableGroupService;

	@DisplayName("테이블 그룹을 생성할 때 주문 테이블이 비어있는 경우 예외처리 테스트")
	@Test
	void createTableGroupEmptyOrderTables() {
		// when
		when(tableGroup.getOrderTables()).thenReturn(Collections.emptyList());

		// then
		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroup);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블 그룹을 생성할 떄 주문 테이블의 갯수가 1개인 경우 예외처리 테스트")
	@Test
	void createTableGroupSizeOneOrderTableList() {
		// when
		when(tableGroup.getOrderTables()).thenReturn(Collections.singletonList(orderTable));

		// then
		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroup);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블 그룹을 생성할 때 존재하지 않는 주문 테이블일 경우 예외처리 테스트")
	@Test
	void createTableGroupUnknownOrderTable() {
		// given
		given(tableGroup.getOrderTables()).willReturn(Arrays.asList(orderTable, orderTable));

		// when
		when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Collections.emptyList());

		// then
		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroup);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블 그룹을 생성할 때 주문 테이블이 비어있는 상태인 경우 예외처리 테스트")
	@Test
	void createTableGroupEmptyOrderTable() {
		// given
		given(tableGroup.getOrderTables()).willReturn(Arrays.asList(orderTable, orderTable));
		given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(orderTable, orderTable));

		// when
		when((orderTable.isEmpty())).thenReturn(false);

		// then
		assertThatThrownBy(() -> {
			tableGroupService.create(tableGroup);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블 그룹을 생성하는 테스트")
	@Test
	void createTableGroup() {
		// given
		given(tableGroup.getOrderTables()).willReturn(Arrays.asList(orderTable, orderTable));
		given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(orderTable, orderTable));
		given(orderTable.isEmpty()).willReturn(true);
		given(orderTable.getTableGroupId()).willReturn(null);

		// when
		when(tableGroupDao.save(tableGroup)).thenReturn(tableGroup);

		// then
		assertThat(tableGroupService.create(tableGroup)).isEqualTo(tableGroup);
	}

	@DisplayName("테이블 그룹을 해체할 때 주문 테이블의 상태가 완료가 아닌 경우 예외처리 테스트")
	@Test
	void unGroupOrderStatusNotComplement() {
		// when
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(),
			anyList())).thenReturn(true);

		// then
		assertThatThrownBy(() -> {
			tableGroupService.ungroup(tableGroup.getId());
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("테이블 그룹을 해체하는 기능 테스트")
	@Test
	void unGroup() {
		// given
		given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(orderTable, orderTable));
		given(orderTable.getTableGroupId()).willReturn(null);
		given(orderTableDao.save(orderTable)).willReturn(orderTable);

		// when
		tableGroupService.ungroup(tableGroup.getId());

		// then
		assertThat(orderTable.getTableGroupId()).isNull();
	}
}
