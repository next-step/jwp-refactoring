package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import kitchenpos.BaseControllerTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TestDomainConstructor;

@DisplayName("주문 테이블 Controller 테스트")
public class TableRestControllerTest extends BaseControllerTest {

	private static final Long EMPTY_ORDER_TABLE_ID = 1L;
	private static final Long NOT_EMPTY_ORDER_TABLE_ID = 9L;

	@Test
	@DisplayName("주문 테이블을 등록할 수 있다 - 테이블 등록 후, 등록된 테이블의 아이디를 포함한 정보를 반환한다.")
	void create() throws Exception {
		//given
		int numberOfGuests = 0;
		OrderTable orderTable = TestDomainConstructor.orderTable(null, numberOfGuests, true);

		//when-then
		mockMvc.perform(post("/api/tables")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(orderTable)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andExpect(jsonPath("$.tableGroupId").isEmpty())
			.andExpect(jsonPath("$.numberOfGuests").value(numberOfGuests))
			.andExpect(jsonPath("$.empty").value(true));
	}

	@Test
	@DisplayName("메뉴의 목록을 조회할 수 있다.")
	void list() throws Exception {
		//when-then
		mockMvc.perform(get("/api/tables"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isNotEmpty())
			.andExpect(jsonPath("$..id").isNotEmpty())
			.andExpect(jsonPath("$..numberOfGuests").isNotEmpty())
			.andExpect(jsonPath("$..empty").isNotEmpty());
	}

	@Test
	@DisplayName("빈 테이블 설정을 해제 할 수 있다 - 상태 변경 후, 변경된 테이블 정보를 반환한다.")
	void changeToNotEmpty() throws Exception {
		//given
		OrderTable orderTable = TestDomainConstructor.orderTableWithId(null, 0, false, EMPTY_ORDER_TABLE_ID);

		//when-then
		mockMvc.perform(put(String.format("/api/tables/%d/empty", EMPTY_ORDER_TABLE_ID))
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(orderTable)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(EMPTY_ORDER_TABLE_ID))
			.andExpect(jsonPath("$.tableGroupId").isEmpty())
			.andExpect(jsonPath("$.empty").value(false));
	}

	@Test
	@DisplayName("빈 테이블 설정 할 수 있다 - 상태 변경 후, 변경된 테이블 정보를 반환한다.")
	void changeToEmpty() throws Exception {
		//given
		OrderTable orderTable = TestDomainConstructor.orderTableWithId(null, 0, true, NOT_EMPTY_ORDER_TABLE_ID);

		//when-then
		mockMvc.perform(put(String.format("/api/tables/%d/empty", NOT_EMPTY_ORDER_TABLE_ID))
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(orderTable)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(NOT_EMPTY_ORDER_TABLE_ID))
			.andExpect(jsonPath("$.tableGroupId").isEmpty())
			.andExpect(jsonPath("$.empty").value(true));
	}

	@Test
	@DisplayName("방문한 손님 수를 입력할 수 있다 - 손님 수 변경 후, 변경된 테이블 정보를 반환한다.")
	void changeNumberOfGuests() throws Exception {
		//given
		int numberOfGuests = 4;
		OrderTable orderTable = TestDomainConstructor.orderTableWithId(null, numberOfGuests, false, NOT_EMPTY_ORDER_TABLE_ID);

		//when-then
		mockMvc.perform(put(String.format("/api/tables/%d/number-of-guests", NOT_EMPTY_ORDER_TABLE_ID))
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(orderTable)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(NOT_EMPTY_ORDER_TABLE_ID))
			.andExpect(jsonPath("$.tableGroupId").isEmpty())
			.andExpect(jsonPath("$.numberOfGuests").value(numberOfGuests))
			.andExpect(jsonPath("$.empty").value(false));
	}
}

