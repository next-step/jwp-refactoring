package kitchenpos.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
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
        mockMvc = MockMvcBuilders.standaloneSetup(menuRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        menu1 = new Menu();
        menu1.setId(1L);
        menu1.setName("간장레드반반치킨");
        menu1.setPrice(new BigDecimal(18000));
        menu1.setMenuGroupId(1L);

        menu2 = new Menu();
        menu2.setId(2L);
        menu2.setName("허니치킨");
        menu2.setPrice(new BigDecimal(18000));
        menu2.setMenuGroupId(1L);
    }

    @Test
    @DisplayName("메뉴를 등록한다.")
    public void create() throws Exception {
        given(menuService.create(any())).willReturn(menu1);

        final ResultActions actions = mockMvc.perform(post(MENUS_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toString(menu1)));

        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", MENUS_URI + "/1"))
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("간장레드반반치킨")))
                .andExpect(content().string(containsString("18000")))
                .andExpect(content().string(containsString("1")));
    }

    @Test
    @DisplayName("메뉴의 목록을 조회한다.")
    public void list() throws Exception {
        given(menuService.list()).willReturn(Arrays.asList(menu1, menu2));

        final ResultActions actions = mockMvc.perform(get(MENUS_URI)
                .contentType(MediaType.APPLICATION_JSON));

        actions
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("1")))
                .andExpect(content().string(containsString("간장레드반반치킨")))
                .andExpect(content().string(containsString("18000")))
                .andExpect(content().string(containsString("2")))
                .andExpect(content().string(containsString("허니치킨")));
    }

    public String toString(Menu menu) throws JsonProcessingException {
        return objectMapper.writeValueAsString(menu);
    }
}
