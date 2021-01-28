package kitchenpos.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest
@AutoConfigureMockMvc
class MenuAcceptanceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴 등록")
    @Test
    public void createMenu() throws Exception {
        Menu menu = new Menu("치킨", new BigDecimal(100), 1L, null);
        given(menuService.create(any())).willReturn(menu);
        mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(serialize(menu)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("메뉴 리스트")
    @Test
    public void listMenu() throws Exception {
        Menu menu1 = new Menu("치킨", new BigDecimal(200), 1L, null);
        Menu menu2 = new Menu("피자", new BigDecimal(100), 1L, null);
        List<Menu> menus = Arrays.asList(menu1, menu2) ;
        given(menuService.list()).willReturn(menus);
        mockMvc.perform(get("/api/menus"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].name").value("치킨"))
                .andExpect(jsonPath("$.[1].name").value("피자"));
    }

    public String serialize(Object request) throws JsonProcessingException {
        mapper = new ObjectMapper();
        return mapper.writeValueAsString(request);
    }
}
