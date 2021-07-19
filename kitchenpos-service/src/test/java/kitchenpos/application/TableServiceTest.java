package kitchenpos.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 컨트롤러 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
	@Mock
	private OrderService orderService;
	@Mock
	private OrderTableRepository orderTableRepository;

	@InjectMocks
	private TableService tableService;

	@Test
	void orderTableCreateTest() {
		OrderTableRequest orderTableRequest = new OrderTableRequest();
		Mockito.when(orderTableRepository.save(orderTableRequest.toOrderTable())).thenReturn(orderTableRequest.toOrderTable());
		assertThat(tableService.create(orderTableRequest)).isNotNull();
	}

	@Test
	void getOrderTableListTest() {
		Mockito.when(orderTableRepository.findAll()).thenReturn(Lists.list(new OrderTable(1, true), new OrderTable(2, false)));
		assertThat(tableService.list()).hasSize(2);
	}

	@Test
	void changeEmptyTest() {
		OrderTable orderTable = new OrderTable(1L, null, 2, true);

		Mockito.when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
		assertThat(tableService.changeEmpty(1L, false).isEmpty()).isFalse();
	}

	@Test
	void changeNumberOfGuests() {
		OrderTable orderTable = new OrderTable(1L, null, 2, false);

		Mockito.when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));

		tableService.changeNumberOfGuests(1L, 4);
		assertThat(orderTable.getNumberOfGuests()).isEqualTo(4);
	}

	@Test
	@DisplayName("손님의 수 변경 시 손님이 음수면 익셉션 발생")
	void changeNumberOfGuestsFailTest() {
		OrderTable orderTable = new OrderTable(1L, null, 2, false);
		Mockito.when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
		Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, -4))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("손님의 수 변경 시 주문테이블이 빈 테이블이면 익셉션 발생생")
	void changeNumberOfGuestsFailTest2() {
		OrderTable orderTable = new OrderTable(1L, null, 2, true);

		Mockito.when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));

		Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 4))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
