package kitchenpos.application.mock;

import static kitchenpos.domain.DomainFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
	private final OrderTable defaultOrderTable = createOrderTable(0, true, null);
	@InjectMocks
	private TableService tableService;
	@Mock
	private OrderDao orderDao;
	@Mock
	private OrderTableDao orderTableDao;

	@DisplayName("테이블 등록")
	@Test
	void create() {
		when(orderTableDao.save(any(OrderTable.class))).thenAnswer(invocation -> {
			OrderTable orderTable = invocation.getArgument(0, OrderTable.class);
			orderTable.setId(1L);
			return orderTable;
		});

		OrderTable resultOrderTable = tableService.create(createOrderTable(0, true, null));

		assertThat(resultOrderTable.getId()).isNotNull();
		assertThat(resultOrderTable.getNumberOfGuests()).isEqualTo(0);
		assertThat(resultOrderTable.isEmpty()).isTrue();
		assertThat(resultOrderTable.getTableGroupId()).isNull();
	}

	@DisplayName("테이블 목록 조회")
	@Test
	void list() {
		when(orderTableDao.findAll()).thenReturn(createOrderTables(1L, 2L, 3L, 4L));

		List<OrderTable> orderTables = tableService.list();

		assertThat(orderTables).hasSize(4);
	}

	@DisplayName("등록되지 않은 테이블의 상태 변경시 IllegalArgumentException 발생")
	@Test
	void changeEmpty_ThrowIllegalArgumentException1() {
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeEmpty(9L, defaultOrderTable));
	}

	@DisplayName("단체 지정 테이블 변경시 IllegalArgumentException 발생")
	@Test
	void changeEmpty_ThrowIllegalArgumentException2() {
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(createOrderTable(1L, 0, true, 1L)));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeEmpty(1L, defaultOrderTable));
	}

	@DisplayName("주문 상태가 요리 중 또는 식사 중이면 IllegalArgumentException 발생")
	@Test
	void changeEmpty_ThrowIllegalArgumentException3() {
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(createOrderTable(1L, 0, true, null)));
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeEmpty(1L, defaultOrderTable));
	}

	@DisplayName("테이블을 상태를 변경 한다.")
	@Test
	void changeEmpty() {
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(createOrderTable(1L, 0, true, null)));
		when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(false);
		when(orderTableDao.save(any(OrderTable.class))).thenAnswer(invocation -> {
			OrderTable orderTable = invocation.getArgument(0, OrderTable.class);
			orderTable.setId(1L);
			return orderTable;
		});

		OrderTable resultTable = tableService.changeEmpty(1L, createOrderTable(false));

		assertThat(resultTable.getId()).isNotNull();
		assertThat(resultTable.isEmpty()).isFalse();
	}

	@DisplayName("방문한 손님 수가 0 보다 작을 경우 IllegalArgumentException 발생")
	@Test
	void changeNumberOfGuests_ThrowIllegalArgumentException1() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeNumberOfGuests(1L, createOrderTable(-1)));
	}

	@DisplayName("등록되지 않은 테이블의 방문한 손님 변경시 IllegalArgumentException 발생")
	@Test
	void changeNumberOfGuests_ThrowIllegalArgumentException2() {
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeNumberOfGuests(9L, defaultOrderTable));
	}

	@DisplayName("빈 테이블 변경시 IllegalArgumentException 발생")
	@Test
	void changeNumberOfGuests_ThrowIllegalArgumentException3() {
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(createOrderTable(1L, 0, true, null)));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeNumberOfGuests(1L, defaultOrderTable));
	}

	@DisplayName("테이블의 방문한 손님 수 변경")
	@Test
	void changeNumberOfGuests() {
		OrderTable orderTable = createOrderTable(1L, 2, false, null);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
		when(orderTableDao.save(any(OrderTable.class))).thenReturn(orderTable);

		OrderTable resultTable = tableService.changeNumberOfGuests(1L, createOrderTable(4));

		assertThat(resultTable.getId()).isNotNull();
		assertThat(resultTable.getNumberOfGuests()).isEqualTo(4);
	}
}
