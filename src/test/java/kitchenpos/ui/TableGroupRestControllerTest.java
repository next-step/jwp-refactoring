package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import kitchenpos.BaseControllerTest;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TestDomainConstructor;

@DisplayName("테이블 단체 Controller 테스트")
public class TableGroupRestControllerTest extends BaseControllerTest {
	@Test
	@DisplayName("단체를 지정할 수 있다 - 단체 지정 후, 지정된 단체의 아이디를 포함한 정보를 반환하며, 테이블은 모두 비어있지 않은 상태여야 한다.")
	void create() throws Exception {
		//given
		OrderTable orderTable = TestDomainConstructor.orderTableWithId(null, 0, true, 1L);
		OrderTable orderTable2 = TestDomainConstructor.orderTableWithId(null, 0, true, 2L);
		TableGroup tableGroup = TestDomainConstructor.tableGroup(Arrays.asList(orderTable, orderTable2), null);

		//when-then
		mockMvc.perform(post("/api/table-groups")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(tableGroup)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andExpect(jsonPath("$.createdDate").isNotEmpty())
			.andExpect(jsonPath("$.orderTables[?(@.empty==true)]").doesNotExist());
	}

	@Test
	@DisplayName("단체 지정을 해지할 수 있다.")
	void ungroup() throws Exception {
		create();
		Long tableGroupId = 1L;

		//when-then
		mockMvc.perform(delete("/api/table-groups/" + tableGroupId))
			.andDo(print())
			.andExpect(status().isNoContent());
	}
}

