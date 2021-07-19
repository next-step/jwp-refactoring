package kitchenpos.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.Arrays;

@DisplayName("테이블 그룹 컨트롤러 테스트")
@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {
	private static final String BASE_URL = "/api/table-groups";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private TableGroupRestController tableGroupRestController;

	@MockBean
	private TableGroupService tableGroupService;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(tableGroupRestController)
				.addFilters(new CharacterEncodingFilter("UTF-8", true))
				.alwaysDo(MockMvcResultHandlers.print())
				.build();
	}

	@Test
	void createTest() throws Exception {
		TableGroupRequest tableGroupRequest = new TableGroupRequest(LocalDateTime.of(2021, 1, 1, 1, 1, 1), Arrays.asList(0L, 1L));
		TableGroupResponse tableGroupResponse = new TableGroupResponse(1L, LocalDateTime.of(2021, 1, 1, 1, 1, 1), Arrays.asList(new OrderTableResponse(), new OrderTableResponse()));

		BDDMockito.given(tableGroupService.create(tableGroupRequest)).willReturn(tableGroupResponse);

		mockMvc.perform(
				MockMvcRequestBuilders.post(BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(tableGroupRequest)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.header().string("location", BASE_URL + "/1"));
	}

	@Test
	void ungroupTest() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.delete(BASE_URL + "/{tableGroupId}", 1L))
				.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
}
