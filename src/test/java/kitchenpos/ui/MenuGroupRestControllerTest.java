package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest extends ControllerTest {
    @MockBean
    private MenuGroupService menuGroupService;

    private MenuGroup lunchMenuGroup;
    private MenuGroup dinnerMenuGroup;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();

        lunchMenuGroup = new MenuGroup(1L, "lunch-menu-group");
        dinnerMenuGroup = new MenuGroup(2L, "dinner-menu-group");
    }

    @DisplayName("[POST] 메뉴그룹 생성")
    @Test
    void create() throws Exception {
        given(menuGroupService.create(any(MenuGroup.class))).willReturn(lunchMenuGroup);

        perform(postAsJson("/api/menu-groups", lunchMenuGroup))
            .andExpect(status().isCreated())
            .andExpect(header().string("location", "/api/menu-groups/1"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("lunch-menu-group"));
    }

    @DisplayName("[GET] 메뉴그룹 목록 조회")
    @Test
    void list() throws Exception {
        given(menuGroupService.list()).willReturn(Arrays.asList(lunchMenuGroup, dinnerMenuGroup));

        perform(get("/api/menu-groups"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].name").value("lunch-menu-group"))
            .andExpect(jsonPath("$[1].id").value(2L))
            .andExpect(jsonPath("$[1].name").value("dinner-menu-group"));
    }

}
