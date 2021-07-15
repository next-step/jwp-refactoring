package kitchenpos.menu.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.utils.MockMvcControllerTest;

@DisplayName("메뉴 관리 기능 - SpringBootTest")
@SpringBootTest
class MenuRestControllerSpringBootTest extends MockMvcControllerTest {
    private static final String REQUEST_URL = "/api/menus";
    @Autowired
    private MenuRestController menuRestController;

    @Override
    protected Object controller() {
        return menuRestController;
    }

    @Test
    @DisplayName("메뉴 목록을 조회할 수 있다.")
    void retrieve_menuList1() throws Exception {
        // then
        mockMvc.perform(get(REQUEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(1))
                .andExpect(jsonPath("[0].name").value("후라이드치킨"))
                .andExpect(jsonPath("[0].price").value(BigDecimal.valueOf(16_000.00)))
                .andExpect(jsonPath("[5].id").value(6))
                .andExpect(jsonPath("[5].name").value("순살치킨"))
                .andExpect(jsonPath("[5].price").value(BigDecimal.valueOf(17_000.00)))
        ;
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    void save_menu1() throws Exception {
        // given
        MenuRequest menuRequest = new MenuRequest("A", BigDecimal.valueOf(32_000.00), 1L,
                Arrays.asList(new MenuProductRequest(1L, 2L)));

        // then
        mockMvc.perform(post(REQUEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(menuRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value(menuRequest.getName()))
                .andExpect(jsonPath("price").value(menuRequest.getPrice()))
        ;
    }
}
