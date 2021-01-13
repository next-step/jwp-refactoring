package kitchenpos.order.application;

import static kitchenpos.domain.TestFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.BaseServiceTest;
import kitchenpos.order.application.TableService;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;

public class TableServiceTest extends BaseServiceTest {
	@Autowired
	private TableService tableService;

	@Test
	@DisplayName("메뉴를 등록할 수 있다.")
	void create() {
		//given
		OrderTableRequest orderTableRequest = new OrderTableRequest(테이블_신규_NUM_OF_GUESTS, 테이블_신규_EMPTY);

		//when
		OrderTableResponse result = tableService.create(orderTableRequest);

		//then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getTableGroupId()).isNull();
		assertThat(result.getNumberOfGuests()).isEqualTo(테이블_신규_NUM_OF_GUESTS);
		assertThat(result.isEmpty()).isEqualTo(테이블_신규_EMPTY);
	}

	@Test
	@DisplayName("빈 테이블로 설정 또는 해지할 수 있다.")
	void changeEmpty() {
		//given
		Long orderTableId = 테이블_비어있는_0명_1.getId();
		boolean changeEmpty = !테이블_비어있는_0명_1.isEmpty();
		OrderTableRequest changeEmptyRequest = new OrderTableRequest(changeEmpty);

		//when
		OrderTableResponse result = tableService.changeEmpty(orderTableId, changeEmptyRequest);

		//then
		assertThat(result.getId()).isEqualTo(orderTableId);
		assertThat(result.isEmpty()).isEqualTo(changeEmpty);
	}

	@Test
	@DisplayName("빈 테이블 설정 변경 시, 테이블이 등록되어있지 않으면 IllegalArgumentException을 throw 해야한다.")
	void changeEmptyNotExistOrderTable() {
		//given
		OrderTableRequest changeEmptyRequest = new OrderTableRequest(true);

		//when-then
		assertThatThrownBy(() -> tableService.changeEmpty(존재하지않는_ID, changeEmptyRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("빈 테이블 설정 변경 시, 단체 지정된 테이블이면 IllegalArgumentException을 throw 해야한다.")
	void changeEmptyGroupTable() {
		//given
		Long orderTableId = 테이블_단체지정됨_0명_10.getId();
		OrderTableRequest changeEmptyRequest = new OrderTableRequest(true);

		//when-then
		assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, changeEmptyRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("빈 테이블 설정 변경 시, 주문 상태가 조리 또는 식사인 테이블이면 IllegalArgumentException을 throw 해야한다.")
	void changeEmptyCookingTable() {
		//given
		Long orderTableId = 테이블_요리중_3명_11.getId();
		OrderTableRequest changeEmptyRequest = new OrderTableRequest(true);

		//when-then
		assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, changeEmptyRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("방문한 손님 수를 입력할 수 있다.")
	void changeNumberOfGuests() {
		//given
		Long orderTableId = 테이블_단체지정됨_0명_10.getId();
		int changeNumberOfGuests = 3;
		OrderTableRequest changeNumberOfGuestsRequest = new OrderTableRequest(changeNumberOfGuests);

		//when
		OrderTableResponse result = tableService.changeNumberOfGuests(orderTableId, changeNumberOfGuestsRequest);

		//then
		assertThat(result.getId()).isEqualTo(orderTableId);
		assertThat(result.getNumberOfGuests()).isEqualTo(changeNumberOfGuests);
	}

	@Test
	@DisplayName("방문한 손님 수를 입력 시, 손님 수를 0 명 미만으로 입력하면 IllegalArgumentException을 throw 해야한다.")
	void changeNegativeNumberOfGuests() {
		//given
		Long orderTableId = 테이블_단체지정됨_0명_10.getId();
		OrderTableRequest changeNumberOfGuestsRequest = new OrderTableRequest(-10);

		//when-then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, changeNumberOfGuestsRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("방문한 손님 수를 입력 시, 빈 테이블이면 IllegalArgumentException을 throw 해야한다.")
	void changeNumberOfGuestsEmptyTable() {
		//given
		Long orderTableId = 테이블_비어있는_0명_1.getId();
		OrderTableRequest changeNumberOfGuestsRequest = new OrderTableRequest(2);

		//when-then
		assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, changeNumberOfGuestsRequest))
			.isInstanceOf(IllegalArgumentException.class);
	}
}
