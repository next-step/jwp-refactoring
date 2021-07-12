package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;

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

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderTableDao orderTableDao;

	@Mock
	private TableGroupDao tableGroupDao;

	@InjectMocks
	private TableGroupService tableGroupService;


	@Test
	void createTestInHappyCase() {
		// given
		when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Arrays.asList(new OrderTable(null, 5, true), new OrderTable(null, 10, true)));
		when(tableGroupDao.save(any())).thenReturn(new TableGroup(LocalDateTime.now(), Arrays.asList(new OrderTable(), new OrderTable(), new OrderTable())));
		// when
		TableGroup tableGroup = tableGroupService.create(new TableGroup(LocalDateTime.now(), Arrays.asList(new OrderTable(), new OrderTable())));
		// then
		assertThat(tableGroup.getOrderTables().size()).isEqualTo(2);
	}

	@Test
	void ungroupTestInHappyCase() {
		// given
		when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(new OrderTable(), new OrderTable()));
		when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);
		// when
		tableGroupService.ungroup(1L);
		// then
	}
}
