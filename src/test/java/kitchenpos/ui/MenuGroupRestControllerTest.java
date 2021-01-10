package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import kitchenpos.BaseControllerTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.TestDomainConstructor;

@DisplayName("메뉴 그룹 Controller 테스트")
public class MenuGroupRestControllerTest extends BaseControllerTest {

	@Test
	@DisplayName("메뉴그룹을 등록할 수 있다 - 메뉴그룹 등록 후, 등록된 메뉴그룹의 아이디를 포함한 정보를 반환한다.")
	void create() throws Exception {
		//given
		String name = "메뉴그룹1";
		MenuGroup menuGroup = TestDomainConstructor.menuGroup(name);

		//when-then
		mockMvc.perform(post("/api/menu-groups")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(menuGroup)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").isNotEmpty())
			.andExpect(jsonPath("$.name").value(name));
	}

	@Test
	@DisplayName("메뉴그룹의 목록을 조회할 수 있다.")
	void list() throws Exception {
		//when-then
		mockMvc.perform(get("/api/menu-groups"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isNotEmpty())
			.andExpect(jsonPath("$..id").isNotEmpty())
			.andExpect(jsonPath("$..name").isNotEmpty());
	}
}

