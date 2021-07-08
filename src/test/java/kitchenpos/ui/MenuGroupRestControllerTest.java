package kitchenpos.ui;

import kitchenpos.ui.dto.menu.MenuGroupRequest;
import kitchenpos.ui.dto.menu.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("메뉴 그룹 통합테스트")
class MenuGroupRestControllerTest extends IntegrationSupportTest {
    private static final String URI = "/api/menu-groups";

    @DisplayName("메뉴 그룹을 추가한다.")
    @Test
    void create() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(postAsJson(URI, MenuGroupRequest.of("착한세트")));

        //then
        actions.andExpect(status().isCreated());
        //and then
        MenuGroupResponse response = toObject(actions.andReturn(), MenuGroupResponse.class);
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo("착한세트");
    }

    @DisplayName("메뉴 그룹을 모두 조회한다.")
    @Test
    void list() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(URI));

        //then
        actions.andExpect(status().isOk());
        //and then
        List<MenuGroupResponse> response = toList(actions.andReturn(), MenuGroupResponse.class);
        assertThat(response).isNotEmpty();
    }
}
