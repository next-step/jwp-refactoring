package kitchenpos.order.application;

import static kitchenpos.TestInstances.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
import org.springframework.test.util.ReflectionTestUtils;

import kitchenpos.TestInstances;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
	@InjectMocks
	private TableService tableService;
	@Mock
	private OrderTableRepository orderTableRepository;

	@BeforeEach
	void setUp() {
		TestInstances.init();
	}

	@DisplayName("테이블 생성")
	@Test
	void create() {
		OrderTableRequest orderTableRequest = new OrderTableRequest(0, true);

		when(orderTableRepository.save(any(OrderTable.class))).thenAnswer(invocation -> {
			OrderTable orderTable = invocation.getArgument(0, OrderTable.class);
			ReflectionTestUtils.setField(orderTable, "id", 1L);
			return orderTable;
		});

		OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);

		assertThat(orderTableResponse.getId()).isNotNull();
	}

	@DisplayName("테이블 조회")
	@Test
	void list() {
		when(orderTableRepository.findAll()).thenReturn(Arrays.asList(
			테이블1,
			테이블2,
			테이블3,
			테이블4
		));

		List<OrderTableResponse> orderTableResponses = tableService.list();

		assertThat(orderTableResponses.size()).isEqualTo(4);
	}

	@DisplayName("테이블 상태 변경")
	@Test
	void changeEmpty() {
		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(테이블1));

		OrderTableResponse orderTableResponse = tableService.changeEmpty(테이블1.getId(), new OrderTableRequest(false));

		assertThat(orderTableResponse.isEmpty()).isEqualTo(false);
	}

	@DisplayName("그룹 테이블 상태 실패")
	@Test
	void changeEmptyWhenGroupTable() {
		테이블1.setTableGroupId(1L);
		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(테이블1));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeEmpty(테이블1.getId(), new OrderTableRequest(false)));
	}

	@DisplayName("테이블 손님 수 변경")
	@Test
	void changeGuestsNumber() {
		테이블1.changeEmpty(false);
		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(테이블1));

		OrderTableResponse orderTableResponse = tableService.changeGuestsNumber(테이블1.getId(), new OrderTableRequest(4));

		assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(4);
	}

	@DisplayName("빈 테이블 손님 수 변경 실패")
	@Test
	void changeGuestsNumberWhenEmptyTable() {
		테이블1.changeEmpty(true);
		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(테이블1));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> tableService.changeGuestsNumber(테이블1.getId(), new OrderTableRequest(4)));
	}
}
