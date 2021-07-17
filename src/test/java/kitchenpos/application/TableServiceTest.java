package kitchenpos.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("테이블 컨트롤러 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
	@Mock
	private OrderRepository orderRepository;
	@Mock
	private OrderTableRepository orderTableRepository;

	@InjectMocks
	private TableService tableService;

	@Test
	void orderTableCreateTest() {
		OrderTable orderTable = new OrderTable();
		when(orderTableRepository.save(orderTable)).thenReturn(orderTable);
		assertThat(tableService.create(orderTable)).isNotNull();
	}

	@Test
	void getOrderTableListTest() {
		when(orderTableRepository.findAll()).thenReturn(Lists.list(new OrderTable(), new OrderTable()));
		assertThat(tableService.list()).hasSize(2);
	}

	@Test
	void changeEmptyTest() {
		OrderTable orderTable = new OrderTable(1L, null, 2, false);

		when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
		when(orderRepository.existsByOrderTableIdAndOrderStatusIn(1L, Lists.list(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
		when(orderTableRepository.save(orderTable)).thenReturn(orderTable);
		assertThat(tableService.changeEmpty(1L, orderTable)).isNotNull();
	}

	@Test
	@DisplayName("주문 테이블을 빈 테이블로 변경 시 테이블 그룹이 이미 존재할 시 익셉션 발생")
	void changeEmptyFailTest() {
		OrderTable orderTable = new OrderTable(1L, 1L, 2, false);
		when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));

		assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("주문 테이블을 빈 테이블로 변경 시 완료되지 않은 주문 테이블이 존재ㅘ녕 익셉션 발생")
	void changeEmptyFailTest2() {
		OrderTable orderTable = new OrderTable(1L, null, 2, false);
		when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
		when(orderRepository.existsByOrderTableIdAndOrderStatusIn(1L, Lists.list(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

		assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void changeNumberOfGuests() {
		OrderTable orderTable = new OrderTable(1L, null, 2, false);
		OrderTable orderTable2 = new OrderTable(2L, null, 4, false);

		when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
		when(orderTableRepository.save(orderTable)).thenReturn(orderTable);

		tableService.changeNumberOfGuests(1L, orderTable2);
		assertThat(orderTable.getNumberOfGuests()).isEqualTo(4);
	}

	@Test
	@DisplayName("손님의 수 변경 시 손님이 음수면 익셉션 발생")
	void changeNumberOfGuestsFailTest() {
		OrderTable orderTable = new OrderTable(2L, null, -4, false);

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("손님의 수 변경 시 주문테이블이 빈 테이블이면 익셉션 발생생")
	void changeNumberOfGuestsFailTest2() {
		OrderTable orderTable = new OrderTable(1L, null, 2, true);

		when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));

		assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
