package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import kitchenpos.common.BaseControllerTest;
import kitchenpos.common.TestDataUtil;
import kitchenpos.domain.OrderTable;

@DisplayName("TableRestController 테스트")
class TableRestControllerTest extends BaseControllerTest {

	@DisplayName("OrderTable 생성 요청")
	@Test
	void create() throws Exception {
		int expectedId = 9;
		OrderTable table = TestDataUtil.createOrderTable();

		mockMvc.perform(post("/api/tables")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(table)))
			.andDo(print())
			.andExpect(header().string("Location", "/api/tables/" + expectedId))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(expectedId));
	}

	@DisplayName("OrderTable 목록 조회")
	@Test
	void list() throws Exception {
		mockMvc.perform(get("/api/tables"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(8)));
	}

	@DisplayName("OrderTable empty 상태 변경")
	@Test
	public void changeEmpty() throws Exception {
		long targetId = 1L;
		boolean isEmpty = false;
		OrderTable table = TestDataUtil.createOrderTable();
		table.setId(targetId);
		table.setEmpty(isEmpty);

		mockMvc.perform(put("/api/tables/{orderTableId}/empty", targetId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(table)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(targetId))
			.andExpect(jsonPath("$.empty").value(isEmpty));

	}

	@DisplayName("OrderTable 손님 수 변경")
	@Test
	void changeNumberOfGuests() throws Exception {
		changeEmpty();
		int guestNumber = 5;
		long targetId = 1L;
		OrderTable table = TestDataUtil.createOrderTable();
		table.setId(targetId);
		table.setNumberOfGuests(5);

		mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", targetId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(table)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(targetId))
			.andExpect(jsonPath("$.numberOfGuests").value(guestNumber));
	}

}