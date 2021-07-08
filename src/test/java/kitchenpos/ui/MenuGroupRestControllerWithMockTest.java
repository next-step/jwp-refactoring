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
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MenuGroupRestController.class)
class MenuGroupRestControllerWithMockTest {
    private MockMvc mockMvc;

    @Autowired
    private MenuGroupRestController menuGroupRestController;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private MenuGroupService menuGroupService;

    private MenuGroup 메뉴그룹_추천메뉴;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(menuGroupRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        메뉴그룹_추천메뉴 = new MenuGroup();
        메뉴그룹_추천메뉴.setId(1L);
        메뉴그룹_추천메뉴.setName("추천메뉴");
    }

    @Test
    @DisplayName("메뉴그룹을 생성한다.")
    void create() throws Exception {
        //given
        when(menuGroupService.create(any())).thenReturn(메뉴그룹_추천메뉴);
        String requestBody = objectMapper.writeValueAsString(메뉴그룹_추천메뉴);

        //when && then
        mockMvc
                .perform(post("/api/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("전체 메뉴그룹을 조회한다.")
    void list() throws Exception {
        //given
        when(menuGroupService.list()).thenReturn(Arrays.asList(메뉴그룹_추천메뉴));

        //when && then
        mockMvc
                .perform(get("/api/menu-groups"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("추천메뉴")));

    }
}