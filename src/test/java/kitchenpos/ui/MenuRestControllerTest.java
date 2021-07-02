package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;

@WebMvcTest(controllers = MenuRestController.class)
@ExtendWith(MockitoExtension.class)
public class MenuRestControllerTest {

    public static final String BASE_URL = "/api/menus";
    public static final Menu 후라이드_후라이드 = new Menu("후라이드_후라이드", BigDecimal.valueOf(14000), 1L, Collections.emptyList());
    public static final Menu 후라이드_양념치킨 = new Menu("후라이드_양념치킨", BigDecimal.valueOf(20000), 1L, Collections.emptyList());

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("메뉴 이름, 메뉴의 가격, 메뉴 그룹, 메뉴에 속하는 상품들과 상품들의 수량을 입력받아 신규 메뉴를 만들 수 있다.")
    void create() throws Exception {
        // given
        String content = objectMapper.writeValueAsString(후라이드_후라이드);
        given(menuService.create(any(Menu.class))).willReturn(후라이드_후라이드);

        // when
        mockMvc.perform(
            post(BASE_URL)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(후라이드_후라이드.getName()))
            .andExpect(jsonPath("$.price").value(후라이드_후라이드.getPrice()));
    }

    @Test
    @DisplayName("등록된 상품들의 리스트를 볼 수 있다.")
    void list() throws Exception {
        // given
        given(menuService.list())
            .willReturn(Arrays.asList(후라이드_후라이드, 후라이드_양념치킨));

        // when
        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value(후라이드_후라이드.getName()))
            .andExpect(jsonPath("$[1].name").value(후라이드_양념치킨.getName()));
    }

}
