package kitchenpos.menu.ui;

import kitchenpos.ControllerTest;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.ui.menu.MenuGroupRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuGroupRestController.class)
public class MenuGroupRestControllerTest extends ControllerTest {
    @MockBean
    private MenuGroupService menuGroupService;

    private MenuGroup 양식;
    private MenuGroup 중식;

    @BeforeEach
    public void setUp() {
        super.setUp();

        양식 = new MenuGroup("양식");
        중식 = new MenuGroup("중식");

        ReflectionTestUtils.setField(양식, "id", 1L);
        ReflectionTestUtils.setField(중식, "id", 2L);
    }

    @Test
    void 메뉴_그룹_등록() throws Exception {
        MenuGroupRequest request = new MenuGroupRequest("양식");
        given(menuGroupService.create(any(MenuGroupRequest.class)))
                .willReturn(MenuGroupResponse.of(양식));

        webMvc.perform(post("/api/menu-groups")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(양식.getId().intValue())))
                .andExpect(jsonPath("$.name", is(양식.getName())));
    }

    @Test
    void 메뉴_그룹_목록_조회() throws Exception {
        given(menuGroupService.list())
                .willReturn(Arrays.asList(MenuGroupResponse.of(양식), MenuGroupResponse.of(중식)));

        webMvc.perform(get("/api/menu-groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
