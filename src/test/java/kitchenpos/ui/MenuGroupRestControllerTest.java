package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private MenuGroupService menuGroupService;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) //한글 깨짐 처리
                .build();
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() throws Exception {
        MenuGroup menuGroup = new MenuGroup(5L, "기타안주메뉴");
        String params = mapper.writeValueAsString(menuGroup);
        given(menuGroupService.create(any())).willReturn(menuGroup);

        mockMvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(params))
                .andExpect(status().isCreated());
    }

    @DisplayName("메뉴 그룹 리스트를 조회한다.")
    @Test
    void list() throws Exception {
        List<MenuGroup> menuGroups = new ArrayList<>();
        menuGroups.add(new MenuGroup(5L, "기타안주메뉴"));

        given(menuGroupService.list()).willReturn(menuGroups);

        mockMvc.perform(get("/api/menu-groups"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("기타안주메뉴")));
    }
}
