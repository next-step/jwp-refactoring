package kitchenpos.ui;

import static kitchenpos.utils.generator.MenuGroupFixtureGenerator.메뉴_그룹_생성_요청;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.utils.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("API:MenuGroup")
public class MenuGroupRestControllerTest extends BaseTest {

    public static final String MENU_GROUP_API_URL_TEMPLATE = "/api/menu-groups";

    @Test
    @DisplayName("메뉴 그룹을 조회한다.")
    public void getMenuGroups() throws Exception {
        // When
        ResultActions resultActions = mockMvcUtil.get(MENU_GROUP_API_URL_TEMPLATE);

        // Then
        resultActions
            .andDo(print())
            .andExpect(jsonPath("$.[*].id").exists())
            .andExpect(jsonPath("$.[*].name").exists());
    }

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    public void createMenuGroup() throws Exception {
        // Given
        final String menuGroupName = "오늘의 메뉴";

        // When
        ResultActions resultActions = mockMvcUtil.post(메뉴_그룹_생성_요청(menuGroupName));

        // Then
        resultActions.
            andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(menuGroupName));
    }
}
