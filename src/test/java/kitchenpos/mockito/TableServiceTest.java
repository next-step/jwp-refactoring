package kitchenpos.mockito;

import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Disabled
public class TableServiceTest {
	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderTableRepository orderTableRepository;

	private TableService tableService;

	@Mock
	private OrderTable orderTable;

	@BeforeEach
	void setUp() {
		tableService = new TableService(orderRepository, orderTableRepository);
		assertThat(tableService).isNotNull();
		orderTable = mock(OrderTable.class);
	}

	@Test
	@DisplayName("주문 테이블을 등록한다")
	void create() {
		given(orderTableRepository.save(orderTable)).willReturn(orderTable);
		assertThat(tableService.create(orderTable)).isEqualTo(orderTable);
	}

	@Test
	@DisplayName("주문 테이블 목록을 조회한다")
	void list() {
		given(orderTableRepository.findAll()).willReturn(new ArrayList<>(Arrays.asList(mock(OrderTable.class), mock(OrderTable.class))));
		assertThat(tableService.list()).isNotNull();
		assertThat(tableService.list()).isNotEmpty();
		assertThat(tableService.list().size()).isEqualTo(2);
	}

	@Test
	@DisplayName("주문 테이블의 손님수를 업데이트할 수 있다")
	void changeNumberOfGuests() {
		given(orderTable.getNumberOfGuests()).willReturn(2);
		given(orderTable.isEmpty()).willReturn(false);

		given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));
		given(orderTableRepository.save(orderTable)).willReturn(orderTable);

		assertThat(tableService.changeNumberOfGuests(1L, orderTable)).isEqualTo(orderTable);
	}

	@Test
	@DisplayName("주문 테이블의 empty 여부를 업데이트할 수 있다")
	void changeEmpty() {
		given(orderTable.getNumberOfGuests()).willReturn(2);
		given(orderTable.isEmpty()).willReturn(false);
//		given(orderTable.getTableGroupId()).willReturn(null);

		given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(orderTable));
		given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(false);

		given(orderTableRepository.save(orderTable)).willReturn(orderTable);
		assertThat(tableService.changeEmpty(1L, orderTable)).isEqualTo(orderTable);
	}
}
