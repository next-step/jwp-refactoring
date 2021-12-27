package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.order.domain.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

	@InjectMocks
	private TableService tableService;

	@Mock
	private OrderTableDao orderTableDao;

	@Mock
	private OrderRepository orderRepository;

	@DisplayName("주문 테이블을 생성한다")
	@Test
	void createTest() {
		// given
		OrderTable orderTableRequest = new OrderTable();
		orderTableRequest.setEmpty(true);
		orderTableRequest.setNumberOfGuests(0);
		OrderTable persist = new OrderTable();
		persist.setId(1L);
		persist.setEmpty(true);
		persist.setNumberOfGuests(0);
		given(orderTableDao.save(any())).willReturn(persist);

		// when
		OrderTable result = tableService.create(orderTableRequest);

		// then
		assertThat(result.getId()).isEqualTo(persist.getId());
	}

	@DisplayName("주문 테이블 목록을 조회한다")
	@Test
	void listTest() {
		// given
		OrderTable orderTable1 = new OrderTable();
		orderTable1.setId(1L);
		orderTable1.setEmpty(true);
		orderTable1.setNumberOfGuests(0);
		List<OrderTable> persist = new ArrayList<>();
		persist.add(orderTable1);
		given(orderTableDao.findAll()).willReturn(persist);

		// when
		List<OrderTable> result = tableService.list();

		// then
		assertThat(result.size()).isEqualTo(persist.size());
	}

	@DisplayName("주문 테이블을 빈 테이블로 변경한다")
	@Test
	void changeEmptyTest() {
		// given
		Long requestId = 1L;
		OrderTable request = new OrderTable();
		request.setEmpty(false);
		OrderTable persist = new OrderTable();
		persist.setId(1L);
		persist.setEmpty(false);
		persist.setNumberOfGuests(0);

		given(orderTableDao.findById(requestId)).willReturn(Optional.of(persist));
		given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);
		given(orderTableDao.save(any())).willReturn(persist);

		// when
		OrderTable result = tableService.changeEmpty(requestId, request);

		// then
		assertThat(result.isEmpty()).isFalse();
	}

	@DisplayName("빈 테이블 변경 시, 테이블 그룹에 속해있지 않아야 한다")
	@Test
	void changeEmptyTest2() {
		// given
		Long requestId = 1L;
		OrderTable request = new OrderTable();
		request.setEmpty(false);
		OrderTable persist = new OrderTable();
		persist.setId(1L);
		persist.setTableGroupId(1L);
		persist.setEmpty(false);
		persist.setNumberOfGuests(0);

		given(orderTableDao.findById(requestId)).willReturn(Optional.of(persist));

		// when, then
		assertThatThrownBy(() -> tableService.changeEmpty(requestId, request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("빈 테이블 변경 시, 조리 중이거나 식사 중인 테이블이면 안된다")
	@Test
	void changeEmptyTest3() {
		// given
		Long requestId = 1L;
		OrderTable request = new OrderTable();
		request.setEmpty(false);
		OrderTable persist = new OrderTable();
		persist.setId(1L);
		persist.setEmpty(false);
		persist.setNumberOfGuests(0);

		given(orderTableDao.findById(requestId)).willReturn(Optional.of(persist));
		given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

		// when, then
		assertThatThrownBy(() -> tableService.changeEmpty(requestId, request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블 손님 수 변경 시, 손님수가 0명 이상이어야 한다")
	@Test
	void changeNumberOfGuests() {
		// given
		Long requestId = 1L;
		OrderTable request = new OrderTable();
		request.setNumberOfGuests(-1);

		// when, then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(requestId, request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블 손님 수 변경 시, 빈 테이블 상태가 아니어야 한다")
	@Test
	void changeNumberOfGuests2() {
		// given
		Long requestId = 1L;
		OrderTable request = new OrderTable();
		request.setNumberOfGuests(3);
		OrderTable persist = new OrderTable();
		persist.setId(1L);
		persist.setEmpty(true);

		given(orderTableDao.findById(any())).willReturn(Optional.of(persist));

		// when, then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(requestId, request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블 손님 수를 변경한다")
	@Test
	void changeNumberOfGuests3() {
		// given
		Long requestId = 1L;
		OrderTable request = new OrderTable();
		request.setNumberOfGuests(3);
		OrderTable persist = new OrderTable();
		persist.setId(1L);
		persist.setNumberOfGuests(3);
		persist.setEmpty(false);

		given(orderTableDao.findById(any())).willReturn(Optional.of(persist));
		given(orderTableDao.save(any())).willReturn(persist);

		// when
		OrderTable result = tableService.changeNumberOfGuests(requestId, request);

		// then
		assertThat(result.getNumberOfGuests()).isEqualTo(3);
	}

}
