package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.menu.MenuProductRequest;
import kitchenpos.ui.dto.menu.MenuRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class MenuRestControllerTest {
    private MenuRestController menuRestController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private MenuService menuService;

    @BeforeEach
    void setup() {
        menuRestController = new MenuRestController(menuService);

        mockMvc = MockMvcBuilders.standaloneSetup(menuRestController).build();

        objectMapper = new ObjectMapper();
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void createMenuTest() throws Exception {
        // given
        String url = "/api/menus";
        String menuName = "new menu";
        List<MenuProductRequest> menuProductRequests = Collections.singletonList(MenuProductRequest.of(1L, 1L));
        MenuRequest menuRequest = MenuRequest.of(menuName, BigDecimal.ONE, 1L, menuProductRequests);
        Menu savedMenu = new Menu();
        savedMenu.setId(1L);

        given(menuService.create(any())).willReturn(savedMenu);

        // when, then
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", url + "/" + savedMenu.getId()))
        ;
    }

    @DisplayName("메뉴 목록을 불러올 수 있다.")
    @Test
    void getMenusTest() throws Exception {
        // given
        String url = "/api/menus";

        given(menuService.list()).willReturn(Arrays.asList(new Menu(), new Menu()));

        // when, then
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
        ;
    }
}
