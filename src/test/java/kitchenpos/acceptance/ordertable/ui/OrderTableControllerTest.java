package kitchenpos.acceptance.ordertable.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.ui.OrderTableController;

@DisplayName("테이블 Controller 테스트")
@WebMvcTest(OrderTableController.class)
@MockBean(JpaMetamodelMappingContext.class)
class OrderTableControllerTest {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper mapper;
	@MockBean
	private OrderTableService orderTableService;

	@DisplayName("주문 테이블을 생성한다.")
	@Test
	void createOrderTableTest() throws Exception {
		// given
		OrderTableRequest request = OrderTableRequest.of(0, true);
		OrderTableResponse response = OrderTableResponse.of(OrderTable.of(1L, 0, true));
		given(orderTableService.create(any())).willReturn(response);

		// when
		final ResultActions resultActions = mvc.perform(post("/api/tables")
			.content(mapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(redirectedUrl("/api/tables" + "/" + response.getId()))
			.andExpect(jsonPath("$.numberOfGuests").value(response.getNumberOfGuests()))
			.andDo(log());
	}

	@DisplayName("주문 테이블을 조회한다.")
	@Test
	void selectOrderTableListTest() throws Exception {
		// given
		OrderTableRequest request = OrderTableRequest.of(0, true);
		OrderTableResponse response1 = OrderTableResponse.of(request.toEntity());
		OrderTableResponse response2 = OrderTableResponse.of(request.toEntity());
		given(orderTableService.list()).willReturn(Arrays.asList(response1, response2));

		// when
		final ResultActions resultActions = mvc.perform(get("/api/tables")
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andDo(log());
	}

	@DisplayName("비어있는 테이블로 설정한다.")
	@Test
	void setEmptyTableTest() throws Exception {
		// given
		OrderTableRequest request = OrderTableRequest.of(1, false);
		OrderTableResponse response = OrderTableResponse.of(OrderTable.of(1L, 0, true));
		given(orderTableService.changeEmpty(any(), any())).willReturn(response);

		// when
		final ResultActions resultActions = mvc.perform(put("/api/tables/{orderTableId}/empty", response.getId())
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(request)))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.empty").value(response.isEmpty()));
	}

	@DisplayName("테이블 손님 수를 변경한다.")
	@Test
	void changeNumberOfGuestsTest() throws Exception {
		// given
		OrderTableRequest request = OrderTableRequest.of(1, false);
		OrderTableResponse response = OrderTableResponse.of(OrderTable.of(5, false));
		given(orderTableService.changeNumberOfGuests(any(), any())).willReturn(response);

		// when
		final ResultActions resultActions = mvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 1L)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(request)))
			.andDo(print());

		// then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.numberOfGuests").value(response.getNumberOfGuests()));
	}
}
