package kitchenpos.ui;

import static kitchenpos.util.TestDataSet.원플원_양념;
import static kitchenpos.util.TestDataSet.원플원_후라이드;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

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
import kitchenpos.domain.menu.Menu;

@WebMvcTest(controllers = MenuRestController.class)
@ExtendWith(MockitoExtension.class)
public class MenuRestControllerTest {

    public static final String BASE_URL = "/api/menus";

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
        String content = objectMapper.writeValueAsString(원플원_후라이드);
        given(menuService.create(any(Menu.class))).willReturn(원플원_후라이드);

        // when
        mockMvc.perform(
            post(BASE_URL)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(원플원_후라이드.getName()))
            .andExpect(jsonPath("$.price").value(원플원_후라이드.getPrice()));
    }

    @Test
    @DisplayName("등록된 상품들의 리스트를 볼 수 있다.")
    void list() throws Exception {
        // given
        given(menuService.list())
            .willReturn(Arrays.asList(원플원_후라이드, 원플원_양념));

        // when
        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value(원플원_후라이드.getName()))
            .andExpect(jsonPath("$[1].name").value(원플원_양념.getName()));
    }

}
