package kitchenpos.menugroup.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("메뉴 그룹 컨트롤러 테스트")
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/db/test_data.sql")
class MenuRequestGroupRestControllerTest {
    private static final String DEFAULT_MENU_GROUP_URI = "/api/menu-groups";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    public void 메뉴_그룹_생성() throws Exception {
        final Long menuGroupId = 1L;
        final String menuGroupName = "테스트용 추천 메뉴";

        final MenuGroup expectedMenuGroup = new MenuGroup();
        expectedMenuGroup.setId(menuGroupId);
        expectedMenuGroup.setName(menuGroupName);

        final MenuGroup menuGroupRequest = new MenuGroup();
        menuGroupRequest.setId(menuGroupId);
        menuGroupRequest.setName(menuGroupName);

        final String jsonTypeMenuGroup = objectMapper.writeValueAsString(menuGroupRequest);

        mockMvc.perform(post(DEFAULT_MENU_GROUP_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonTypeMenuGroup))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").value(menuGroupId))
            .andExpect(jsonPath("name").value(menuGroupName));
    }

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    public void 메뉴_그룹_조회() throws Exception {
        final List<MenuGroup> expectedMenuGroups = new ArrayList<>();
        final MenuGroup firstMenuGroup = new MenuGroup();
        firstMenuGroup.setId(11L);
        firstMenuGroup.setName("순살파닭두마리메뉴");

        final MenuGroup secondMenuGroup = new MenuGroup();
        secondMenuGroup.setId(22L);
        secondMenuGroup.setName("한마리메뉴");

        expectedMenuGroups.add(firstMenuGroup);
        expectedMenuGroups.add(secondMenuGroup);

        menuGroupService.create(firstMenuGroup);
        menuGroupService.create(secondMenuGroup);

        mockMvc.perform(get(DEFAULT_MENU_GROUP_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(6)))
            .andExpect(jsonPath("$[4].id").exists())
            .andExpect(jsonPath("$[4].name").value(firstMenuGroup.getName()))
            .andExpect(jsonPath("$[5].id").exists())
            .andExpect(jsonPath("$[5].name").value(secondMenuGroup.getName()));
    }
}
