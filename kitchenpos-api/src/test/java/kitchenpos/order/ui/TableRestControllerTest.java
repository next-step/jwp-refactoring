package kitchenpos.order.ui;

import static kitchenpos.utils.TestFixture.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import kitchenpos.BaseControllerTest;
import kitchenpos.order.dto.OrderTableRequest;

@DisplayName("주문 테이블 Controller 테스트")
public class TableRestControllerTest extends BaseControllerTest {

	@Test
	@DisplayName("주문 테이블을 등록할 수 있다 - 테이블 등록 후, 등록된 테이블의 아이디를 포함한 정보를 반환한다.")
	void create() throws Exception {
		//given
		OrderTableRequest orderTableRequest = new OrderTableRequest(테이블_신규_NUM_OF_GUESTS, 테이블_신규_EMPTY);

		//when-then
		mockMvc.perform(post("/api/tables")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(orderTableRequest)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andExpect(jsonPath("$.tableGroupId").isEmpty())
			.andExpect(jsonPath("$.numberOfGuests").value(테이블_신규_NUM_OF_GUESTS))
			.andExpect(jsonPath("$.empty").value(테이블_신규_EMPTY));
	}

	@Test
	@DisplayName("메뉴의 목록을 조회할 수 있다.")
	void findAll() throws Exception {
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
		Long orderTableId = 테이블_비어있는_0명_1.getId();
		OrderTableRequest orderTableRequest = new OrderTableRequest(false);

		//when-then
		mockMvc.perform(put(String.format("/api/tables/%d/empty", orderTableId))
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(orderTableRequest)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(orderTableId))
			.andExpect(jsonPath("$.tableGroupId").isEmpty())
			.andExpect(jsonPath("$.empty").value(false));
	}

	@Test
	@DisplayName("빈 테이블 설정 할 수 있다 - 상태 변경 후, 변경된 테이블 정보를 반환한다.")
	void changeToEmpty() throws Exception {
		//given
		Long orderTableId = 테이블_비어있지않은_2명_9.getId();
		OrderTableRequest orderTableRequest = new OrderTableRequest(true);

		//when-then
		mockMvc.perform(put(String.format("/api/tables/%d/empty", orderTableId))
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(orderTableRequest)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(orderTableId))
			.andExpect(jsonPath("$.tableGroupId").isEmpty())
			.andExpect(jsonPath("$.empty").value(true));
	}

	@Test
	@DisplayName("방문한 손님 수를 입력할 수 있다 - 손님 수 변경 후, 변경된 테이블 정보를 반환한다.")
	void changeNumberOfGuests() throws Exception {
		//given
		Long orderTableId = 테이블_비어있지않은_2명_9.getId();
		int changeNumberOfGuests = 5;
		OrderTableRequest orderTableRequest = new OrderTableRequest(changeNumberOfGuests);

		//when-then
		mockMvc.perform(put(String.format("/api/tables/%d/number-of-guests", orderTableId))
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(orderTableRequest)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(orderTableId))
			.andExpect(jsonPath("$.tableGroupId").isEmpty())
			.andExpect(jsonPath("$.numberOfGuests").value(changeNumberOfGuests))
			.andExpect(jsonPath("$.empty").value(false));
	}
}

