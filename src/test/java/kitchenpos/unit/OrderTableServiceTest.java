package kitchenpos.unit;

import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class OrderTableServiceTest {

	@Autowired
	private OrderTableService orderTableService;


	@Test
	@DisplayName("주문 테이블을 등록한다")
	void create() {
		OrderTableRequest orderTableRequest = new OrderTableRequest();
		OrderTableResponse orderTableResponse = orderTableService.create(orderTableRequest);
		assertThat(orderTableResponse).isNotNull();
	}

	@Test
	@DisplayName("주문 테이블 목록을 조회한다")
	void listOrderTable() {
		List<OrderTableResponse> orderTableResponse = orderTableService.listTables();
		assertThat(orderTableResponse).isNotNull();
		assertThat(orderTableResponse.size()).isEqualTo(8);
	}

	@Test
	@DisplayName("주문 테이블의 empty 여부를 업데이트할 수 있다")
	void changeEmpty() {
		OrderTableRequest orderTableRequest = new OrderTableRequest(4, false);
		OrderTableResponse orderTableResponse = orderTableService.changeEmpty(1L, orderTableRequest);
		assertThat(orderTableResponse.isEmpty()).isFalse();

	}
	@Test
	@DisplayName("주문 테이블의 손님수를 업데이트할 수 있다")
	void changeNumberOfGuests() {
		this.changeEmpty();
		OrderTableRequest orderTableRequest = new OrderTableRequest(3);
		OrderTableResponse orderTableResponse = orderTableService.changeNumberOfGuests(1L, orderTableRequest);
		assertThat(orderTableResponse.isEmpty()).isFalse();
	}

}
