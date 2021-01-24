package kitchenpos.application;

import static kitchenpos.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@DisplayName("주문 테이블 BO 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

	@Mock
	private OrderDao orderDao;
	@Mock
	private OrderTableDao orderTableDao;

	@InjectMocks
	private TableService tableService;

	@DisplayName("주문 테이블 생성")
	@Test
	void create() {
		// given
		OrderTable 주문_테이블1 = newOrderTable(null, 1L, 4, false);
		given(orderTableDao.save(주문_테이블1)).willAnswer(invocation -> {
			주문_테이블1.setId(1L);
			return 주문_테이블1;
		});

		// when
		OrderTable saveOrderTable = tableService.create(주문_테이블1);

		// then
		assertThat(saveOrderTable).isEqualTo(주문_테이블1);
	}

	@DisplayName("빈 테이블 여부 변경")
	@Test
	void changeEmpty_happyPath() {
		// given
		OrderTable 주문_테이블1 = newOrderTable(null, null, 4, false);

		given(orderTableDao.save(주문_테이블1)).willReturn(주문_테이블1);
		given(orderTableDao.findById(주문_테이블1.getId())).willReturn(Optional.of(주문_테이블1));
		given(orderDao.existsByOrderTableIdAndOrderStatusIn(
			주문_테이블1.getId(),
			Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
		).willReturn(false);

		// when
		OrderTable saveOrderTable = tableService.changeEmpty(주문_테이블1.getId(), newOrderTable(true));

		// then
		assertThat(saveOrderTable.isEmpty()).isTrue();
	}

	@DisplayName("빈 테이블 여부 변경 : 주문 테이블의 단체가 지정되어 있는 경우")
	@Test
	void changeEmpty_exceptionCase1() {
		// given
		OrderTable 주문_테이블1 = newOrderTable(null, 1L, 4, false);
		given(orderTableDao.findById(주문_테이블1.getId())).willReturn(Optional.of(주문_테이블1));

		// when & then
		assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블1.getId(), newOrderTable(true)))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("빈 테이블 여부 변경 : 주문테이블에 속한 주문의 상태가 모두 완료되지 않은 경우")
	@Test
	void changeEmpty_exceptionCase2() {
		// given
		OrderTable 주문_테이블1 = newOrderTable(null, null, 4, false);
		given(orderTableDao.findById(주문_테이블1.getId())).willReturn(Optional.of(주문_테이블1));
		given(orderDao.existsByOrderTableIdAndOrderStatusIn(
			주문_테이블1.getId(),
			Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
		).willReturn(true);

		// when & then
		OrderTable tempOrderTable = new OrderTable();
		tempOrderTable.setEmpty(true);
		assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블1.getId(), tempOrderTable))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("방문한 손님 수 변경 : 0명도 가능함")
	@Test
	void changeNumberOfGuests() {
		// given
		OrderTable 주문_테이블1 = newOrderTable(null, null, 4, false);

		given(orderTableDao.save(주문_테이블1)).willReturn(주문_테이블1);
		given(orderTableDao.findById(주문_테이블1.getId())).willReturn(Optional.of(주문_테이블1));

		// when
		OrderTable saveOrderTable = tableService.changeNumberOfGuests(주문_테이블1.getId(), newOrderTable(0));

		// then
		assertThat(saveOrderTable.getNumberOfGuests()).isEqualTo(0);
	}
}
