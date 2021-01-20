package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

/**
 * @author : byungkyu
 * @date : 2021/01/20
 * @description :
 **/
@DisplayName("테이블")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

	@Mock
	OrderDao orderDao;
	@Mock
	OrderTableDao orderTableDao;

	@DisplayName("테이블을 생성할 수 있다.")
	@Test
	void create(){
		// given
		OrderTable orderTable = new OrderTable();
		OrderTable savedOrderTable = mock(OrderTable.class);
		when(savedOrderTable.getId()).thenReturn(1L);
		when(orderTableDao.save(orderTable)).thenReturn(savedOrderTable);
		TableService tableService = new TableService(orderDao, orderTableDao);

		// when
		OrderTable createdOrderTable = tableService.create(orderTable);

		// then
		assertThat(createdOrderTable.getId()).isNotNull();
	}

	@DisplayName("테이블 목록을 조회할 수 있다.")
	@Test
	void list() {
		// given
		OrderTable savedOrderTable = mock(OrderTable.class);
		when(orderTableDao.findAll()).thenReturn(Arrays.asList(savedOrderTable));
		TableService tableService = new TableService(orderDao, orderTableDao);

		// when
		List<OrderTable> orderTables = tableService.list();

		// then
		assertThat(orderTables).contains(savedOrderTable);
	}
}