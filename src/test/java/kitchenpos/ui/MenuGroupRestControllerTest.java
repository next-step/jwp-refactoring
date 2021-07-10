package kitchenpos.ui;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@DisplayName("메뉴 그룹 관련 테스트")
@SpringBootTest
class MenuGroupRestControllerTest {
    public static final String MENU_GROUPS_URI = "/api/menu-groups";

    private MenuGroup menuGroup1;
    private MenuGroup menuGroup2;

    private MockMvc mockMvc;

    @MockBean
    private MenuGroupService menuGroupService;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp(@Autowired MenuGroupRestController menuGroupRestController) {
        // MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(menuGroupRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        menuGroup1 = new MenuGroup();
        menuGroup1.setId(1L);
        menuGroup1.setName("두마리메뉴");
        
        menuGroup2 = new MenuGroup();
        menuGroup2.setId(2L);
        menuGroup2.setName("한마리메뉴");
    }

    public String toJsonString(MenuGroup menuGroup) throws JsonProcessingException {
        return objectMapper.writeValueAsString(menuGroup);
    }

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    public void create() throws Exception {
        // given
        given(menuGroupService.create(any())).willReturn(menuGroup1);

        // when
        final ResultActions actions = mockMvc.perform(post(MENU_GROUPS_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(menuGroup1)));

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", MENU_GROUPS_URI + "/1"))
                .andExpect(content().string(containsString("두마리메뉴")));
    }

    @Test
    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    public void list() throws Exception {
        // given
        given(menuGroupService.list()).willReturn(Arrays.asList(menuGroup1, menuGroup2));

        // when
        final ResultActions actions = mockMvc.perform(get(MENU_GROUPS_URI)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("두마리메뉴")))
                .andExpect(content().string(containsString("한마리메뉴")));
    }
}
