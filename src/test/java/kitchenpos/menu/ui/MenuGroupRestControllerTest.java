package kitchenpos.menu.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Collections;
import java.util.List;
import kitchenpos.RestControllerTest;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.ui.MenuGroupRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("메뉴 그룹 API")
@WebMvcTest(MenuGroupRestController.class)
public class MenuGroupRestControllerTest extends RestControllerTest<MenuGroup> {

    private static final String BASE_URL = "/api/menu-groups";
    private static final MenuGroup 한식 = new MenuGroup(1L, "한식");

    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() throws Exception {
        // Given
        given(menuGroupService.create(any())).willReturn(한식);

        // When & Then
        String responseBody = objectMapper.writeValueAsString(한식);
        post(BASE_URL, 한식)
            .andExpect(content().string(responseBody));
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() throws Exception {
        // Given
        List<MenuGroup> menuGroups = Collections.singletonList(한식);
        given(menuGroupService.list()).willReturn(menuGroups);

        // When & Then
        String responseBody = objectMapper.writeValueAsString(menuGroups);
        get(BASE_URL)
            .andExpect(content().string(responseBody));
    }
}
