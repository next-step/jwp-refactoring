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

import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableRepository;
import kitchenpos.ordertable.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;

@DisplayName("주문 테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
	@Mock
	private TableRepository tableRepository;
	@InjectMocks
	private TableService tableService;

	OrderTable 주문테이블;

	@BeforeEach
	void setUp() {
		주문테이블 = 주문테이블생성(1L, new NumberOfGuests(1));
	}

	@DisplayName("주문 테이블을 생성한다.")
	@Test
	void 주문_테이블_생성() {
		OrderTableRequest 주문테이블_생성_요청 = new OrderTableRequest(1);
		given(tableRepository.save(주문테이블)).willReturn(주문테이블);

		OrderTableResponse created = tableService.create(주문테이블_생성_요청);

		주문테이블_생성_확인(created, 주문테이블);
	}

	@DisplayName("주문 테이블 리스트 조회한다.")
	@Test
	void 주문_테이블_리스트_조회() {
		given(tableRepository.findAll()).willReturn(Arrays.asList(주문테이블));

		List<OrderTableResponse> created = tableService.list();

		주문테이블_리스트_조회_확인(created);
	}

	@DisplayName("주문 테이블을 비우거나 채운다.")
	@Test
	void 주문_테이블을_비우거나_채운다() {
		OrderTableChangeEmptyRequest 주문_테이블_비움_요청 = new OrderTableChangeEmptyRequest(true);
		Order order = new Order(); // todo
		주문테이블.changeEmpty(true, order);
		given(tableRepository.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));
		given(tableRepository.save(주문테이블)).willReturn(주문테이블);

		OrderTable changed = tableService.changeEmpty(주문테이블.getId(), 주문_테이블_비움_요청);

		주문테이블_비우거나_채움_확인(changed, 주문테이블);
	}

	@DisplayName("주문 테이블을 비우거나 채울 때 테이블이 존재하지 않으면 할 수 없다")
	@Test
	void 주문_테이블을_비우거나_채울_때_테이블이_존재하지_않으면_할_수_없다() {
		OrderTableChangeEmptyRequest 주문_테이블_비움_요청 = new OrderTableChangeEmptyRequest(true);
		given(tableRepository.findById(주문테이블.getId())).willReturn(Optional.ofNullable(null));

		assertThatThrownBy(() -> {
			tableService.changeEmpty(주문테이블.getId(), 주문_테이블_비움_요청);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블을 비우거나 채울 때 주문 테이블이 단체 지정에 속하면 비울 수 없다")
	@Test
	void 주문_테이블을_비우거나_채울_때_주문_테이블이_단체_지정에_속하면_비울_수_없다() {
		OrderTableChangeEmptyRequest 주문_테이블_비움_요청 = new OrderTableChangeEmptyRequest(true);
		//주문테이블.setTableGroup(1L); todo : 주문테이블 채우는 방법 고려
		given(tableRepository.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));

		assertThatThrownBy(() -> {
			tableService.changeEmpty(주문테이블.getId(), 주문_테이블_비움_요청);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블을 비우거나 채울 때 주문 테이블들이 조리중이거나 식사중일 경우 비울 수 없다.")
	@Test
	void 주문_테이블을_비우거나_채울_때_주문_테이블들이_조리중이거나_식사중일_경우_비울_수_없다() {
		OrderTableChangeEmptyRequest 주문_테이블_비움_요청 = new OrderTableChangeEmptyRequest(true);
		given(tableRepository.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));
		assertThatThrownBy(() -> {
			tableService.changeEmpty(주문테이블.getId(), 주문_테이블_비움_요청);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블의 손님 수를 변경한다")
	@Test
	void 주문_테이블의_손님_수_변경() {
		OrderTableRequest 주문테이블_생성_요청 = new OrderTableRequest(4);
		주문테이블.changeNumberOfGuests(new NumberOfGuests(4));
		given(tableRepository.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));
		given(tableRepository.save(주문테이블)).willReturn(주문테이블);

		OrderTable changedNumberOfGuests = tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블_생성_요청);

		주문_테이블의_손님_수_변경_확인(changedNumberOfGuests.getNumberOfGuests().value(), 4);
	}

	@DisplayName("주문 테이블의 손님 수를 변경한다 - 변경할 손님 수가 0보다 작으면 변경할 수 없다")
	@Test
	void 주문_테이블의_손님_수_변경_변경할_손님_수가_0보다_작으면_변경할_수_없다() {
		OrderTableRequest 주문테이블_생성_요청 = new OrderTableRequest(-1);
		주문테이블.changeNumberOfGuests(new NumberOfGuests(-1));

		assertThatThrownBy(() -> {
			tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블_생성_요청);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블의 손님 수를 변경한다 - 변경할 주문 테이블이 존재하지 않으면 변경할 수 없다.")
	@Test
	void 주문_테이블의_손님_수_변경_변경할_주문_테이블이_존재하지_않으면_변경할_수_없다() {
		OrderTableRequest 주문테이블_생성_요청 = new OrderTableRequest(-1);
		주문테이블.changeNumberOfGuests(new NumberOfGuests(4));
		given(tableRepository.findById(주문테이블.getId())).willReturn(Optional.ofNullable(null));

		assertThatThrownBy(() -> {
			tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블_생성_요청);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블의 손님 수를 변경한다 - 변경할 주문 테이블이 비워져 있다면 변경할 수 없다.")
	@Test
	void 주문_테이블의_손님_수_변경_변경할_주문_테이블이_비워져_있다면_변경할_수_없다() {
		OrderTableRequest 주문테이블_생성_요청 = new OrderTableRequest(4);
		Order order = new Order(); //todo
		주문테이블.changeNumberOfGuests(new NumberOfGuests(4));
		주문테이블.changeEmpty(true, order);
		given(tableRepository.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));

		assertThatThrownBy(() -> {
			tableService.changeNumberOfGuests(주문테이블.getId(), 주문테이블_생성_요청);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	private void 주문_테이블의_손님_수_변경_확인(int changedNumberOfGuests, int expected) {
		assertThat(changedNumberOfGuests).isEqualTo(expected);
	}

	private void 주문테이블_비우거나_채움_확인(OrderTable changed, OrderTable expected) {
		assertThat(changed.isEmpty()).isEqualTo(expected.isEmpty());
	}

	private void 주문테이블_리스트_조회_확인(List<OrderTableResponse> created) {
		assertThat(created).isNotNull();
		assertThat(created).isNotEmpty();
	}

	private void 주문테이블_생성_확인(OrderTableResponse created, OrderTable expected) {
		//todo
		//assertThat(created.getTableGroupId()).isNull();
		assertThat(created.getId()).isEqualTo(expected.getId());
		assertThat(created.isEmpty()).isEqualTo(expected.isEmpty());
		assertThat(created.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
	}

	public static OrderTable 주문테이블생성(Long id, NumberOfGuests numberOfGuests) {
		OrderTable orderTable = new OrderTable(id, numberOfGuests);
		return orderTable;
	}

	public static OrderTable 주문테이블생성(Long id, NumberOfGuests numberOfGuests, boolean isEmpty) {
		OrderTable orderTable = new OrderTable(id, numberOfGuests, isEmpty);
		return orderTable;
	}

}
