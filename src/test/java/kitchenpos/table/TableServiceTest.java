package kitchenpos.table;

import static org.assertj.core.api.Assertions.*;
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

import kitchenpos.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@DisplayName("주문 테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
	@Mock
	private OrderDao orderDao;
	@Mock
	private OrderTableDao orderTableDao;
	@InjectMocks
	private TableService tableService;

	OrderTable 주문테이블;

	@BeforeEach
	void setUp() {
		주문테이블 = 주문테이블생성(1L, null, false, 1);
	}

	@DisplayName("주문 테이블을 생성한다.")
	@Test
	void 주문_테이블_생성() {
		given(orderTableDao.save(주문테이블)).willReturn(주문테이블);

		OrderTable created = tableService.create(주문테이블);

		주문테이블_생성_확인(created, 주문테이블);
	}

	@DisplayName("주문 테이블 리스트 조회한다.")
	@Test
	void 주문_테이블_리스트_조회() {
		given(orderTableDao.findAll()).willReturn(Arrays.asList(주문테이블));

		List<OrderTable> created = tableService.list();

		주문테이블_리스트_조회_확인(created, Arrays.asList(주문테이블));
	}

	@DisplayName("주문 테이블을 비우거나 채운다.")
	@Test
	void 주문_테이블을_비우거나_채운다() {
		주문테이블.setEmpty(true);
		given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));
		given(orderDao.existsByOrderTableIdAndOrderStatusIn(
			주문테이블.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
			.willReturn(false);
		given(orderTableDao.save(주문테이블)).willReturn(주문테이블);

		OrderTable changed = tableService.changeEmpty(주문테이블.getId(), 주문테이블);

		주문테이블_비우거나_채움_확인(changed, 주문테이블);
	}

	@DisplayName("주문 테이블을 비우거나 채울 때 테이블이 존재하지 않으면 할 수 없다")
	@Test
	void 주문_테이블을_비우거나_채울_때_테이블이_존재하지_않으면_할_수_없다() {
		given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.ofNullable(null));

		assertThatThrownBy(() -> {
			tableService.changeEmpty(주문테이블.getId(), 주문테이블);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블을 비우거나 채울 때 주문 테이블이 단체 지정에 속하면 비울 수 없다")
	@Test
	void 주문_테이블을_비우거나_채울_때_주문_테이블이_단체_지정에_속하면_비울_수_없다() {
		주문테이블.setTableGroupId(1L);
		given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));

		assertThatThrownBy(() -> {
			tableService.changeEmpty(주문테이블.getId(), 주문테이블);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블을 비우거나 채울 때 주문 테이블들이 조리중이거나 식사중일 경우 비울 수 없다.")
	@Test
	void 주문_테이블을_비우거나_채울_때_주문_테이블들이_조리중이거나_식사중일_경우_비울_수_없다() {
		given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));
		given(orderDao.existsByOrderTableIdAndOrderStatusIn(
			주문테이블.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
			.willReturn(true);

		assertThatThrownBy(() -> {
			tableService.changeEmpty(주문테이블.getId(), 주문테이블);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블의 손님 수를 변경한다")
	@Test
	void 주문_테이블의_손님_수_변경() {
		주문테이블.setNumberOfGuests(4);
		given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));
		given(orderTableDao.save(주문테이블)).willReturn(주문테이블);

		OrderTable changedNumberOfGuests = tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블);

		주문_테이블의_손님_수_변경_확인(changedNumberOfGuests.getNumberOfGuests(), 4);
	}

	@DisplayName("주문 테이블의 손님 수를 변경한다 - 변경할 손님 수가 0보다 작으면 변경할 수 없다")
	@Test
	void 주문_테이블의_손님_수_변경_변경할_손님_수가_0보다_작으면_변경할_수_없다() {
		주문테이블.setNumberOfGuests(-1);

		assertThatThrownBy(() -> {
			tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블의 손님 수를 변경한다 - 변경할 주문 테이블이 존재하지 않으면 변경할 수 없다.")
	@Test
	void 주문_테이블의_손님_수_변경_변경할_주문_테이블이_존재하지_않으면_변경할_수_없다() {
		주문테이블.setNumberOfGuests(4);
		given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.ofNullable(null));

		assertThatThrownBy(() -> {
			tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블의 손님 수를 변경한다 - 변경할 주문 테이블이 비워져 있다면 변경할 수 없다.")
	@Test
	void 주문_테이블의_손님_수_변경_변경할_주문_테이블이_비워져_있다면_변경할_수_없다() {
		주문테이블.setNumberOfGuests(4);
		주문테이블.setEmpty(true);
		given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));

		assertThatThrownBy(() -> {
			tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	private void 주문_테이블의_손님_수_변경_확인(int changedNumberOfGuests, int expected) {
		assertThat(changedNumberOfGuests).isEqualTo(expected);
	}

	private void 주문테이블_비우거나_채움_확인(OrderTable changed, OrderTable expected) {
		assertThat(changed.isEmpty()).isEqualTo(expected.isEmpty());
	}

	private void 주문테이블_리스트_조회_확인(List<OrderTable> created, List<OrderTable> expected) {
		assertThat(created).containsAll(expected);
	}

	private void 주문테이블_생성_확인(OrderTable created, OrderTable expected) {
		assertThat(created.getTableGroupId()).isNull();
		assertThat(created.getId()).isEqualTo(expected.getId());
		assertThat(created.isEmpty()).isEqualTo(expected.isEmpty());
		assertThat(created.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
	}

	public static OrderTable 주문테이블생성(Long id, Long tableGroupId, boolean isEmpty, int numberOfGuests) {
		OrderTable orderTable = new OrderTable();
		orderTable.setTableGroupId(tableGroupId);
		orderTable.setEmpty(isEmpty);
		orderTable.setId(id);
		orderTable.setNumberOfGuests(numberOfGuests);
		return orderTable;
	}

}
