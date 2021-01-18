package kitchenpos.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴 등록")
    @Test
    public void create() throws Exception {
        Menu menu = new Menu("쭈꾸미", new BigDecimal(1000), 1L, null);
        given(menuService.create(any())).willReturn(menu);
        mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJsonString(menu)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("메뉴 리스트")
    @Test
    public void list() throws Exception {
        Menu menu1 = new Menu("쭈꾸미", new BigDecimal(2000), 1L, null);
        Menu menu2 = new Menu("볶음밥", new BigDecimal(1000), 1L, null);
        List<Menu> menus = Arrays.asList(menu1, menu2) ;
        given(menuService.list()).willReturn(menus);
        mockMvc.perform(get("/api/menus"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].name").value("쭈꾸미"))
                .andExpect(jsonPath("$.[1].name").value("볶음밥"));
    }

    private String makeJsonString(Object request) throws JsonProcessingException {
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(request);
    }
}