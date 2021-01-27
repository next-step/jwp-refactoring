package kitchenpos.menu.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.ui.MenuRestController;
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
        MenuRequest menuRequest = new MenuRequest("쭈꾸미", 1000, 1L, Arrays.asList(new MenuProductRequest(1L, 10L)));
        MenuResponse menuResponse = new MenuResponse(1L, "쭈꾸미", 1000, Arrays.asList(new MenuProductResponse(1L, 10L)));
        given(menuService.create(any())).willReturn(menuResponse);

        mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJsonString(menuRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("메뉴 리스트")
    @Test
    public void list() throws Exception {
        List<MenuResponse> results = Arrays.asList(
                new MenuResponse(1L, "쭈꾸미", 1000, Arrays.asList(new MenuProductResponse(1L, 10L))),
                new MenuResponse(2L, "볶음밥", 2000, Arrays.asList(new MenuProductResponse(2L, 10L)))
        );

        given(menuService.list()).willReturn(results);

        mockMvc.perform(get("/api/menus"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].name").value("쭈꾸미"))
                .andExpect(jsonPath("$.[1].name").value("볶음밥"));
    }

    private String makeJsonString(Object request) throws JsonProcessingException {
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(request);
    }
}