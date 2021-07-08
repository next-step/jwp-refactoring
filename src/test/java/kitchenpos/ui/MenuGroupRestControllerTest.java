package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.api.MenuGroupRestController;
import kitchenpos.ui.dto.menu.MenuGroupRequest;
import kitchenpos.ui.dto.menu.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MenuGroupRestController.class)
class MenuGroupRestControllerTest {
    private static final String URI = "/api/menu-groups";

    @Autowired
    private MenuGroupRestController menuGroupRestController;

    @MockBean
    private MenuGroupService menuGroupService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private MenuGroup 착한세트;
    private MenuGroup 더착한세트;
    private MenuGroupRequest 착한세트_request;
    private MenuGroupResponse 착한세트_response;
    private MenuGroupResponse 더착한세트_response;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(menuGroupRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        착한세트 = MenuGroup.of(1L, "착한세트");
        착한세트_request = MenuGroupRequest.of(착한세트);
        착한세트_response = MenuGroupResponse.of(착한세트);
        더착한세트 = MenuGroup.of(2L, "더착한세트");
        더착한세트_response = MenuGroupResponse.of(더착한세트);
    }

    @DisplayName("메뉴 그룹을 추가한다.")
    @Test
    void create() throws Exception {
        //given
        given(menuGroupService.create(any())).willReturn(착한세트);

        //when
        ResultActions actions = mockMvc.perform(post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(착한세트_request)));

        //then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("location", URI + "/" + 착한세트_response.getId()))
                .andExpect(jsonPath("$.name").value(착한세트_response.getName()));
    }

    @DisplayName("메뉴 그룹을 모두 조회한다.")
    @Test
    void list() throws Exception {
        //given
        given(menuGroupService.list()).willReturn(Arrays.asList(착한세트, 더착한세트));

        //when
        ResultActions actions = mockMvc.perform(get(URI));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(착한세트_response.getId()))
                .andExpect(jsonPath("$[0].name").value(착한세트_response.getName()))
                .andExpect(jsonPath("$[1].id").value(더착한세트_response.getId()))
                .andExpect(jsonPath("$[1].name").value(더착한세트_response.getName()));
    }
}
