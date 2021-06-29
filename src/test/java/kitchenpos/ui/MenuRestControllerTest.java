package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.UTF8MockMvcTest;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@UTF8MockMvcTest(controllers = {MenuRestController.class})
class MenuRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private MenuService menuService;

  @DisplayName("새로운 메뉴를 저장한다.")
  @Test
  void createTest() throws Exception {
    //given
    Menu createdMenu = new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(19000), 1L, Arrays.asList(new MenuProduct(1L, 1L, 2)));
    when(menuService.create(any())).thenReturn(createdMenu);
    Menu createRequestBody = new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), 1L, Arrays.asList(new MenuProduct(1L, 2)));
    String requestBody = objectMapper.writeValueAsString(createRequestBody);

    //when & then
    mockMvc
        .perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(status().isCreated());
  }

  @DisplayName("메뉴그룹 목록을 조회한다.")
  @Test
  void listTest() throws Exception {
    Menu createdMenu1 = new Menu(1L, "후라이드+후라이드", BigDecimal.valueOf(19000), 1L, Arrays.asList(new MenuProduct(1L, 1L, 2)));
    Menu createdMenu2 = new Menu(2L, "후라이드+양념", BigDecimal.valueOf(20000), 1L, Arrays.asList(new MenuProduct(2L, 2L, 1)));
    when(menuService.list()).thenReturn(Arrays.asList(createdMenu1, createdMenu2));
    mockMvc
        .perform(get("/api/menus"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("후라이드+후라이드")))
        .andExpect(content().string(containsString("후라이드+양념")));
  }
}
