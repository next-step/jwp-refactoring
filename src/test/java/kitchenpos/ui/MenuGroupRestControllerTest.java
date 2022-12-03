package kitchenpos.ui;

import kitchenpos.ControllerTest;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("MenuGroupRestController 테스트")
@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest extends ControllerTest {

    @MockBean
    private MenuGroupService menuGroupService;

    private MenuGroup 양식;
    private MenuGroup 중식;

    @BeforeEach
    public void setUp() {
        super.setUp();

        양식 = new MenuGroup(1L, "양식");
        중식 = new MenuGroup(2L, "중식");
    }

    @Test
    void 메뉴_그룹_등록() throws Exception {
        given(menuGroupService.create(any(MenuGroup.class))).willReturn(양식);

        webMvc.perform(post("/api/menu-groups")
                    .content(mapper.writeValueAsString(양식))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(양식.getId().intValue())))
                .andExpect(jsonPath("$.name", is(양식.getName())));
    }

    @Test
    void 메뉴_그룹_목록_조회() throws Exception {
        given(menuGroupService.list()).willReturn(Arrays.asList(양식, 중식));

        webMvc.perform(get("/api/menu-groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
