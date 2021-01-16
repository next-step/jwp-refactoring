package kitchenpos.domain;

import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TableTest {
	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderTableDao orderTableDao;

	private TableService tableService;

	@BeforeEach
	void setUp() {
		tableService = new TableService(orderDao, orderTableDao);
		assertThat(tableService).isNotNull();
	}

	@Test
	@DisplayName("주문 테이블을 등록한다")
	void create() {
		OrderTable orderTable = new OrderTable();
		when(orderTableDao.save(orderTable)).thenReturn(orderTable);
		assertThat(tableService.create(orderTable)).isEqualTo(orderTable);
	}

	@Test
	@DisplayName("주문 테이블 목록을 조회한다")
	void list() {
		when(orderTableDao.findAll()).thenReturn(new ArrayList<>(Arrays.asList(new OrderTable(), new OrderTable())));
		assertThat(tableService.list()).isNotNull();
		assertThat(tableService.list()).isNotEmpty();
		assertThat(tableService.list().size()).isEqualTo(2);
	}

	@Test
	@DisplayName("주문 테이블의 손님수를 업데이트할 수 있다")
	void changeNumberOfGuests() {
		OrderTable orderTable = new OrderTable();
		orderTable.setNumberOfGuests(2);
		orderTable.setEmpty(false);

		when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
		when(orderTableDao.save(orderTable)).thenReturn(orderTable);

		assertThat(tableService.changeNumberOfGuests(1L, orderTable)).isEqualTo(orderTable);
	}

	@Test
	@DisplayName("주문 테이블의 empty 여부를 업데이트할 수 있다")
	void changeEmpty() {
		OrderTable orderTable = new OrderTable();
		orderTable.setNumberOfGuests(2);
		orderTable.setEmpty(false);

		when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).thenReturn(false);

		when(orderTableDao.save(orderTable)).thenReturn(orderTable);
		assertThat(tableService.changeEmpty(1L, orderTable)).isEqualTo(orderTable);
	}
}
