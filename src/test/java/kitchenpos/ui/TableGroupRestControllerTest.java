package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
				.alwaysDo(print())
				.build();
	}

	@Test
	void createTest() throws Exception {
		TableGroup tableGroup = new TableGroup(1L, null, null);
		given(tableGroupService.create(tableGroup)).willReturn(tableGroup);

		mockMvc.perform(
				post(BASE_URL)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(tableGroup)))
				.andExpect(status().isCreated())
				.andExpect(header().string("location", BASE_URL + "/1"));
	}

	@Test
	void ungroupTest() throws Exception {
		mockMvc.perform(
				delete(BASE_URL + "/{tableGroupId}", 1L))
				.andExpect(status().isNoContent());
	}
}
