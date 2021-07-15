package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@DisplayName("주문 테이블 기능 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

	private OrderTable orderTable;
	private TableGroup tableGroup;

	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderTableDao orderTableDao;

	@InjectMocks
	private TableService tableService;

	@BeforeEach
	void setup() {
		tableGroup = new TableGroup();
		tableGroup.setId(1L);
		orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setNumberOfGuests(4);

	}

	@DisplayName("주문 테이블을 생성할 수 있다.")
	@Test
	public void create() {
		//given
		given(orderTableDao.save(orderTable)).willReturn(orderTable);

		//when
		OrderTable createdOrderTable = tableService.create(orderTable);

		//then
		assertThat(createdOrderTable.getId()).isEqualTo(1L);
	}

	@DisplayName("주문 테이블 목록을 조회할 수 있다.")
	@Test
	public void list() {
		//given
		given(orderTableDao.findAll()).willReturn(Arrays.asList(orderTable));

		//when
		List<OrderTable> orderTables = tableService.list();

		//then
		assertThat(orderTables).containsExactly(orderTable);
	}

	@DisplayName("주문 테이블을 빈 테이블로 설정 또는 해지할 수 있다.")
	@Test
	public void changeEmpty() {
		//given
		OrderTable changedOrderTable = new OrderTable();
		changedOrderTable.setEmpty(true);

		given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
		given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any()))
			.willReturn(false);
		given(orderTableDao.save(orderTable)).willReturn(changedOrderTable);

		//when
		OrderTable resultOrderTable = tableService.changeEmpty(orderTable.getId(), changedOrderTable);

		//then
		assertThat(resultOrderTable.isEmpty()).isTrue();
	}

	@DisplayName("주문 테이블은 주문이 조리,식사 상태인 경우, 빈테이블로 설정할 수 없다.")
	@Test
	public void notChangeEmpty() {
		given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
		given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any()))
			.willReturn(true);

		assertThrows(IllegalArgumentException.class, () -> {
			tableService.changeEmpty(orderTable.getId(), orderTable);
		});
	}

	@DisplayName("주문 테이블의 손님 숫자를 변경할 수 있다.")
	@Test
	public void changeNumberOfGuests() {
		orderTable.setEmpty(false);
		given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
		given(orderTableDao.save(orderTable)).willReturn(orderTable);

		OrderTable resultOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);

		assertThat(resultOrderTable.getNumberOfGuests()).isEqualTo(4);
	}

	@DisplayName("주문 테이블의 손님 숫자는 음수가 될 수 없다.")
	@Test
	public void InvalideChangeNumberOfGuests() {
		orderTable.setNumberOfGuests(-1);

		assertThrows(IllegalArgumentException.class, ()-> {
			tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
		});
	}
}
