package kitchenpos.order.ui;

import static kitchenpos.utils.TestFixture.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import kitchenpos.BaseControllerTest;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.OrderTableRequest;

@DisplayName("테이블 단체 Controller 테스트")
public class TableGroupRestControllerTest extends BaseControllerTest {
	@Test
	@DisplayName("단체를 지정할 수 있다 - 단체 지정 후, 지정된 단체의 아이디를 포함한 정보를 반환하며, 테이블은 모두 비어있지 않은 상태여야 한다.")
	void create() throws Exception {
		//given
		OrderTableRequest orderTableRequest1 = new OrderTableRequest(테이블_비어있는_0명_1.getId());
		OrderTableRequest orderTableRequest2 = new OrderTableRequest(테이블_비어있는_0명_2.getId());

		TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTableRequest1, orderTableRequest2));

		//when-then
		mockMvc.perform(post("/api/table-groups")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(tableGroupRequest)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andExpect(jsonPath("$.createdDate").isNotEmpty())
			.andExpect(jsonPath("$.orderTables[?(@.empty==true)]").doesNotExist());
	}

	@Test
	@DisplayName("단체 지정을 해지할 수 있다.")
	void ungroup() throws Exception {
		//when-then
		mockMvc.perform(delete("/api/table-groups/" + 테이블단체_1.getId()))
			.andDo(print())
			.andExpect(status().isNoContent());
	}
}

