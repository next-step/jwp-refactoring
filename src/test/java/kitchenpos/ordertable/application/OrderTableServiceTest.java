package kitchenpos.ordertable.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;

@SpringBootTest
@Transactional
class OrderTableServiceTest {
	private static OrderTableRequest changeEmptyRequest = new OrderTableRequest(true);
	private static OrderTableRequest changeGuestsRequest = new OrderTableRequest(4);

	@Autowired
	private OrderTableService orderTableService;

	@Test
	@DisplayName("테이블 생성 테스트")
	public void createTableSuccessTest() {
		//given
		OrderTableRequest orderTableRequest = new OrderTableRequest(0, true);
		//when
		OrderTable save = orderTableService.create(orderTableRequest);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getId()).isEqualTo(10L);
	}

	@Test
	@DisplayName("테이블 목록조회 테스트")
	public void findAllTest() {
		//given
		//when
		List<OrderTable> OrderTables = orderTableService.list();

		//then
		assertThat(OrderTables).hasSize(9);
	}

	@Test
	@DisplayName("테이블 빈 상태로 변경 테스트")
	public void changeEmptyTableTest() {
		//given
		//when
		OrderTable orderTable = orderTableService.changeEmpty(3L, changeEmptyRequest);

		//then
		assertThat(orderTable.isNotUse()).isTrue();
	}

	@Test
	@DisplayName("주문테이블 없어서 빈 상태로 변경 실패")
	public void changeEmptyTableFailNotExistedOrderTableTest() {
		assertThatThrownBy(() -> orderTableService.changeEmpty(99L, changeEmptyRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("테이블이 존재하지 않습니다");
	}

	@Test
	@DisplayName("테이블그룹Id를 가지고 있어서 주문테이블 빈 상태로 변경 실패")
	public void changeEmptyTableFailExistedTableGroupIdTest() {
		assertThatThrownBy(() -> orderTableService.changeEmpty(8L, changeEmptyRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("그룹화 된 테이블은 상태를 변경 할 수 없습니다");
	}

	@Test
	@DisplayName("테이블이 계산완료되지 않아서 빈 상태로 변경 실패")
	public void changeEmptyTableFailNotCompleteTest() {
		assertThatThrownBy(() -> orderTableService.changeEmpty(4L, changeEmptyRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("테이블의 주문이 계산완료 되지 않았습니다");
	}

	@Test
	@DisplayName("테이블 인원수 변경 테스트")
	public void changeNumberOfGuestsTest() {
		//given
		//when
		OrderTable update = orderTableService.changeNumberOfGuests(5L, changeGuestsRequest);

		//then
		assertThat(update.getNumberOfGuests()).isEqualTo(changeGuestsRequest.getNumberOfGuests());
	}

	@Test
	@DisplayName("테이블 인원수가 0보다 작아서 변경 실패")
	public void changeNumberOfGuestsLessThanZeroFailTest() {
		OrderTableRequest changeGuestsRequest = new OrderTableRequest(-1);
		assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(5L, changeGuestsRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("인원수는 0보다 작을 수 없습니다");
	}

	@Test
	@DisplayName("테이블이 비어있어서 인원수 변경 실패")
	public void changeNumberOfGuestsFailTableIsEmptyTest() {
		assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(1L, changeGuestsRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("비어 있는 테이블입니다");
	}

	@Test
	@DisplayName("테이블 id 여러개로 조회하기")
	public void findOrderTableByIdIn() {
		//given
		//when
		List<OrderTable> orderTables = orderTableService.findOrderTableByIdIn(Lists.newArrayList(1L, 2L, 3L));

		//then
		assertThat(orderTables.size()).isEqualTo(3);
	}


}
