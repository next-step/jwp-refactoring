package kitchenpos.menu.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.service.MenuServiceJpa;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuServiceJpa menuServiceJpa;

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void createMenu() throws Exception {
        MenuResponse menuResponse = new MenuResponse(1L, "후라이드+양념", 30000, "추천메뉴", getMenuProducts(
                new MenuProductResponse(1L, "후라이드치킨", 1),
                new MenuProductResponse(2L, "양념치킨", 1)));
        when(menuServiceJpa.create(any())).thenReturn(menuResponse);

        mockMvc.perform(post("/api/menus")
                .content(objectMapper.writeValueAsString(menuResponse)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(redirectedUrl("/api/menus/" + menuResponse.getId()))
                .andExpect(status().isCreated());
    }

    @DisplayName("매뉴의 목록을 조회할 수 있다.")
    @Test
    void getMenus() throws Exception {

        MenuResponse menuResponse = new MenuResponse(1L, "후라이드+양념", 30000, "추천메뉴", getMenuProducts(
                new MenuProductResponse(1L, "후라이드치킨", 1),
                new MenuProductResponse(2L, "양념치킨", 1)));

        MenuResponse menuResponse2 = new MenuResponse(1L, "두마리메뉴", 35000, "추천메뉴", getMenuProducts(
                new MenuProductResponse(3L, "반반치킨", 1),
                new MenuProductResponse(4L, "통구이", 1)));

        when(menuServiceJpa.list()).thenReturn(Arrays.asList(menuResponse, menuResponse2));

        mockMvc.perform(get("/api/menus"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", Matchers.containsInAnyOrder("후라이드+양념", "두마리메뉴")))
                .andExpect(jsonPath("$[*].price", Matchers.containsInAnyOrder(30000, 35000)));
    }

    private List<MenuProductResponse> getMenuProducts(MenuProductResponse... productResponses) {
        return Arrays.asList(
                productResponses
        );
    }
}