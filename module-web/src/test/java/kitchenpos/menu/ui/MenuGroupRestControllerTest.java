package kitchenpos.menu.ui;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.RestControllerTest;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("메뉴 그룹 API")
@WebMvcTest(MenuGroupRestController.class)
public class MenuGroupRestControllerTest extends RestControllerTest<MenuGroupRequest> {

    public static final String BASE_URL = "/api/menu-groups";
    private static final MenuGroup 한식 = new MenuGroup("한식");

    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() throws Exception {
        // Given
        given(menuGroupService.create(any())).willReturn(MenuGroupResponse.of(한식));

        // When & Then
        post(BASE_URL, MenuGroupRequest.of(한식))
            .andExpect(jsonPath("$.name").value(한식.getName()));
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() throws Exception {
        // Given
        List<MenuGroupResponse> menuGroups = new ArrayList<>(Arrays.asList(MenuGroupResponse.of(한식)));
        given(menuGroupService.list()).willReturn(menuGroups);

        // When & Then
        get(BASE_URL)
            .andExpect(jsonPath("$.*", hasSize(menuGroups.size())));
    }
}
