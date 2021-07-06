package kitchenpos.ui;

import kitchenpos.ApiTest;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest extends ApiTest {

    @MockBean
    private MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    public void setUp() {
        super.setUp();

        menuGroup = new MenuGroup();
        menuGroup.setId(2L);
        menuGroup.setName("한마리메뉴");
    }

    @Test
    @DisplayName("메뉴 그룹을 생성한다")
    void createTest() throws Exception {

        // given
        when(menuGroupService.create(any())).thenReturn(menuGroup);

        // then
        mockMvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(menuGroup)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다")
    void listTest() throws Exception {

        // given
        when(menuGroupService.list()).thenReturn(Collections.singletonList(menuGroup));

        // then
        mockMvc.perform(get("/api/menu-groups"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("한마리메뉴")));
    }
}