package kitchenpos.ui;

import kitchenpos.MockMvcTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TableGroupRestControllerTest extends MockMvcTest {

	@DisplayName("여러 테이블을 단체지정 한다.")
	@Test
	void create() throws Exception {
		TableGroup created = createTableGroup(3, 4);

		assertThat(created.getId()).isNotNull();
	}

	private TableGroup createTableGroup(long id1, long id2) throws Exception {
		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(Arrays.asList(getOrderTable(id1), getOrderTable(id2)));

		MvcResult mvcResult = mockMvc.perform(postAsJson("/api/table-groups", tableGroup))
				.andExpect(status().isCreated())
				.andReturn();
		return toObject(mvcResult, TableGroup.class);
	}

	private OrderTable getOrderTable(long id) {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(id);
		return orderTable;
	}

	@DisplayName("단체지정 된 테이블을 해제한다.")
	@Test
	void ungroup() throws Exception {
		// given
		TableGroup created = createTableGroup(5, 6);

		// when
		mockMvc.perform(delete(String.format("/api/table-groups/%d", created.getId())))
				.andExpect(status().isNoContent());
	}
}
