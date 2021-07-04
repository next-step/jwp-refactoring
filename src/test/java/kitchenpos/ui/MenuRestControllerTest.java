package kitchenpos.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.MenuServiceTest.메뉴_생성;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("메뉴 관련 기능 테스트")
@WebMvcTest(MenuRestController.class)
public class MenuRestControllerTest {
    public static final String MENUS_URI = "/api/menus";

    private Menu menu1;
    private Menu menu2;

    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MenuRestController menuRestController;

    @BeforeEach
    void setUp() {
        setUpMockMvc(menuRestController);

        menu1 = 메뉴_생성(1L, "간장레드반반치킨", new BigDecimal(18000), 1L);

        menu2 = 메뉴_생성(2L, "허니치킨", new BigDecimal(18000), 1L);
    }

    @Test
    @DisplayName("메뉴를 등록한다.")
    public void create() throws Exception {
        given(menuService.create(any())).willReturn(menu1);

        final ResultActions actions =  메뉴_등록_요청();

        메뉴_등록됨(actions);
    }

    @Test
    @DisplayName("메뉴의 목록을 조회한다.")
    public void list() throws Exception {
        given(menuService.list()).willReturn(Arrays.asList(menu1, menu2));

        final ResultActions actions = 메뉴_목록_조회_요청();

        메뉴_목록_조회됨(actions);
    }

    public String toString(Menu menu) throws JsonProcessingException {
        return objectMapper.writeValueAsString(menu);
    }

    private void setUpMockMvc(MenuRestController menuRestController) {
        mockMvc = MockMvcBuilders.standaloneSetup(menuRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();
    }
    
    private void 메뉴_등록됨(ResultActions actions) throws Exception {
        actions.andExpect(status().isCreated())
                .andExpect(header().string("location", MENUS_URI + "/1"))
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("간장레드반반치킨")))
                .andExpect(content().string(containsString("18000")))
                .andExpect(content().string(containsString("1")));
    }

    private ResultActions 메뉴_등록_요청() throws Exception {
        return mockMvc.perform(post(MENUS_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toString(menu1)));
    }

    private void 메뉴_목록_조회됨(ResultActions actions) throws Exception {
        actions.andExpect(status().isOk())
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("간장레드반반치킨")))
                .andExpect(content().string(containsString("18000")))
                .andExpect(content().string(containsString("2")))
                .andExpect(content().string(containsString("허니치킨")));
    }

    private ResultActions 메뉴_목록_조회_요청() throws Exception {
        return mockMvc.perform(get(MENUS_URI)
                .contentType(MediaType.APPLICATION_JSON));
    }
}
