package kitchenpos.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.ProductService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
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

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("메뉴 그룹 관련 기능 테스트")
@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {
    public static final String MENU_GROUP_URI = "/api/menu-groups";
    private MockMvc mockMvc;
    private MenuGroup menuGroup1;
    private MenuGroup menuGroup2;

    @MockBean
    private MenuGroupService menuGroupService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MenuGroupRestController menuGroupRestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(menuGroupRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        menuGroup1 = new MenuGroup();
        menuGroup1.setId(1L);
        menuGroup1.setName("반반시리즈");

        menuGroup2 = new MenuGroup();
        menuGroup2.setId(2L);
        menuGroup2.setName("허니시리즈");
    }

    @DisplayName("메뉴 그룹 등록한다.")
    @Test
    void create() throws Exception {
        given(menuGroupService.create(any())).willReturn(menuGroup1);

        final ResultActions actions = mockMvc.perform(post(MENU_GROUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(menuGroup1)));

        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/menu-groups/1"))
                .andExpect(content().string(containsString("반반시리즈")));
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() throws Exception {
        given(menuGroupService.list()).willReturn(Arrays.asList(menuGroup1, menuGroup2));

        final ResultActions actions = mockMvc.perform(get(MENU_GROUP_URI)
                .contentType(MediaType.APPLICATION_JSON));

        actions
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("반반시리즈")))
                .andExpect(content().string(containsString("허니시리즈")));

    }

    public String toJson(MenuGroup product) throws JsonProcessingException {
        return objectMapper.writeValueAsString(product);
    }
}