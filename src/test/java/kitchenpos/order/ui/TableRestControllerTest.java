package kitchenpos.order.ui;

import kitchenpos.MockMvcTest;
import kitchenpos.order.dto.OrderTableRequest_ChangeEmpty;
import kitchenpos.order.dto.OrderTableRequest_ChangeGuests;
import kitchenpos.order.dto.OrderTableRequest_Create;
import kitchenpos.order.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TableRestControllerTest extends MockMvcTest {

	@DisplayName("주문 테이블을 생성한다.")
	@Test
	void create() throws Exception {
		OrderTableRequest_Create request = new OrderTableRequest_Create(10, false);

		MvcResult mvcResult = mockMvc.perform(postAsJson("/api/tables", request))
				.andExpect(status().isCreated())
				.andReturn();
		OrderTableResponse created = toObject(mvcResult, OrderTableResponse.class);
		assertThat(created.getId()).isNotNull();
		assertThat(created.getNumberOfGuests()).isEqualTo(10);
		assertThat(created.isEmpty()).isEqualTo(false);
	}

	@DisplayName("주문 테이블을 조회한다.")
	@Test
	void list() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/api/tables")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		List<OrderTableResponse> orderTables = toList(mvcResult, OrderTableResponse.class);
		assertThat(orderTables).isNotEmpty();
	}

	@DisplayName("주문 테이블의 비어있는 상태를 변경한다.")
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void changeEmpty(boolean isEmpty) throws Exception {
		OrderTableResponse orderTable = 주문_테이블_생성됨(20, false);
		OrderTableRequest_ChangeEmpty request = new OrderTableRequest_ChangeEmpty(isEmpty);
		String uri = String.format("/api/tables/%d/empty", orderTable.getId());

		mockMvc.perform(putAsJson(uri, request))
				.andExpect(status().isOk());
	}

	@DisplayName("주문 테이블의 인원수를 변경한다.")
	@Test
	void changeNumberOfGuests() throws Exception {
		OrderTableResponse orderTable = 주문_테이블_생성됨(20, false);
		OrderTableRequest_ChangeGuests request = new OrderTableRequest_ChangeGuests(50);
		String uri = String.format("/api/tables/%d/number-of-guests", orderTable.getId());

		mockMvc.perform(putAsJson(uri, request))
				.andExpect(status().isOk());
	}

	private OrderTableResponse 주문_테이블_생성됨(int numberOfGuests, boolean empty) throws Exception {
		OrderTableRequest_Create request = new OrderTableRequest_Create(numberOfGuests, empty);
		MvcResult mvcResult = mockMvc.perform(postAsJson("/api/tables", request))
				.andExpect(status().isCreated())
				.andReturn();
		return toObject(mvcResult, OrderTableResponse.class);
	}
}
