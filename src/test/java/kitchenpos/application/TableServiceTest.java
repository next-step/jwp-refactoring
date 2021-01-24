package kitchenpos.application;

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
import kitchenpos.domain.TableGroup;

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
		OrderTable 새_주문_테이블 = new OrderTable.Builder().id(-1L).empty(false).build();
		given(orderTableDao.save(새_주문_테이블)).willAnswer(invocation -> {
			새_주문_테이블.setId(-1L);
			return 새_주문_테이블;
		});

		// when
		OrderTable saveOrderTable = tableService.create(새_주문_테이블);

		// then
		assertThat(saveOrderTable).isEqualTo(새_주문_테이블);
	}

	@DisplayName("빈 테이블 여부 변경")
	@Test
	void changeEmpty_happyPath() {
		// given
		OrderTable 새_주문_테이블 = new OrderTable.Builder().id(-1L).empty(false).build();

		given(orderTableDao.save(새_주문_테이블)).willReturn(새_주문_테이블);
		given(orderTableDao.findById(새_주문_테이블.getId())).willReturn(Optional.of(새_주문_테이블));
		given(orderDao.existsByOrderTableIdAndOrderStatusIn(
			새_주문_테이블.getId(),
			Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))
		).willReturn(false);

		// when
		OrderTable saveOrderTable = tableService.changeEmpty(새_주문_테이블.getId(),
			new OrderTable.Builder().empty(true).build());

		// then
		assertThat(saveOrderTable.isEmpty()).isTrue();
	}

	@DisplayName("빈 테이블 여부 변경 : 주문 테이블의 단체가 지정되어 있는 경우")
	@Test
	void changeEmpty_exceptionCase1() {
		// given
		TableGroup 이미_단체_지정 = new TableGroup.Builder().id(-1L).build();
		OrderTable 새_주문_테이블 = new OrderTable.Builder().tableGroup(이미_단체_지정).id(9L).empty(false).build();
		given(orderTableDao.findById(새_주문_테이블.getId())).willReturn(Optional.of(새_주문_테이블));

		// when & then
		assertThatThrownBy(
			() -> tableService.changeEmpty(새_주문_테이블.getId(), new OrderTable.Builder().empty(true).build()))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("빈 테이블 여부 변경 : 주문테이블에 속한 주문의 상태가 모두 완료되지 않은 경우")
	@Test
	void changeEmpty_exceptionCase2() {

		// given
		OrderTable 새_주문_테이블 = new OrderTable.Builder().id(-1L).empty(false).build();
		given(orderTableDao.findById(새_주문_테이블.getId())).willReturn(Optional.of(새_주문_테이블));
		given(orderDao.existsByOrderTableIdAndOrderStatusIn(
			새_주문_테이블.getId(),
			Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))
		).willReturn(true);

		// when & then
		assertThatThrownBy(
			() -> tableService.changeEmpty(새_주문_테이블.getId(), new OrderTable.Builder().empty(true).build()))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("방문한 손님 수 변경 : 0명도 가능함")
	@Test
	void changeNumberOfGuests() {
		// given
		OrderTable 새_주문_테이블 = new OrderTable.Builder().id(-1L).empty(false).build();

		given(orderTableDao.save(새_주문_테이블)).willReturn(새_주문_테이블);
		given(orderTableDao.findById(새_주문_테이블.getId())).willReturn(Optional.of(새_주문_테이블));

		// when
		OrderTable saveOrderTable = tableService.changeNumberOfGuests(새_주문_테이블.getId(),
			new OrderTable.Builder().numberOfGuests(0).build());

		// then
		assertThat(saveOrderTable.getNumberOfGuests()).isEqualTo(0);
	}
}
