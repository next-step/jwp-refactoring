package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import kitchenpos.common.BaseControllerTest;
import kitchenpos.common.TestDataUtil;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@DisplayName("TableGroupRestController 테스트")
class TableGroupRestControllerTest extends BaseControllerTest {

	@DisplayName("TableGroup 생성 요청")
	@Test
	void create() throws Exception {
		int expectedId = 3;

		OrderTable table1 = TestDataUtil.createOrderTableById(1L);
		OrderTable table2 = TestDataUtil.createOrderTableById(2L);
		TableGroup tableGroup = TestDataUtil.createTableGroup(Arrays.asList(table1, table2));

		mockMvc.perform(post("/api/table-groups")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(tableGroup)))
			.andDo(print())
			.andExpect(header().string("Location", "/api/table-groups/" + expectedId))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(expectedId))
			.andExpect(jsonPath("$.orderTables", Matchers.hasSize(2)));
	}

	@DisplayName("TableGroup 해제 요청")
	@Test
	void ungroup() throws Exception {
		long expectedId = 1L;

		mockMvc.perform(delete("/api/table-groups/{groupId}", expectedId))
			.andDo(print())
			.andExpect(status().isNoContent());
	}
}