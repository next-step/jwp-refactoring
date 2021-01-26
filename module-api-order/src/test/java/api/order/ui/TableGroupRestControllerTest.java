package api.order.ui;

import api.order.MockMvcTest;
import api.order.dto.TableGroupRequest_Create;
import api.order.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TableGroupRestControllerTest extends MockMvcTest {

	@DisplayName("여러 테이블을 단체지정 한다.")
	@Test
	void create() throws Exception {
		TableGroupResponse created = createTableGroup(3, 4);

		assertThat(created.getId()).isNotNull();
	}

	private TableGroupResponse createTableGroup(long id1, long id2) throws Exception {
		final List<Long> tableIds = Arrays.asList(id1, id2);
		TableGroupRequest_Create request = new TableGroupRequest_Create(tableIds);

		MvcResult mvcResult = mockMvc.perform(postAsJson("/api/table-groups", request))
				.andExpect(status().isCreated())
				.andReturn();
		return toObject(mvcResult, TableGroupResponse.class);
	}

	@DisplayName("단체지정 된 테이블을 해제한다.")
	@Test
	void ungroup() throws Exception {
		// given
		TableGroupResponse created = createTableGroup(5, 6);

		// when
		mockMvc.perform(delete(String.format("/api/table-groups/%d", created.getId())))
				.andExpect(status().isNoContent());
	}
}
