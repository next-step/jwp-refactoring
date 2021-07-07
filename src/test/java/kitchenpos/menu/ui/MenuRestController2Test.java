package kitchenpos.menu.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.UTF8MockMvcTest;
import kitchenpos.menu.application.MenuService2;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@UTF8MockMvcTest(controllers = {MenuRestController2.class})
class MenuRestController2Test {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private MenuService2 menuService;

  @DisplayName("새로운 메뉴를 저장한다.")
  @Test
  void createTest() throws Exception {
    //given
    MenuResponse createdMenu = new MenuResponse(1L, "후라이드+후라이드", BigDecimal.valueOf(19_000D), 1L, Collections.singletonList(new MenuResponse.MenuProductResponse(1L, 1L, 1L, 2)));
    when(menuService.create(any())).thenReturn(createdMenu);
    MenuRequest createRequestBody = new MenuRequest("후라이드+후라이드", 19_000D, 1L, Collections.singletonList(new MenuRequest.MenuProductRequest(1L, 2L)));
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
    MenuResponse createdMenu1 = new MenuResponse(1L, "후라이드+후라이드", BigDecimal.valueOf(19_000D), 1L, Collections.singletonList(new MenuResponse.MenuProductResponse(1L, 1L, 1L, 2)));
    MenuResponse createdMenu2 = new MenuResponse(2L, "후라이드+양념", BigDecimal.valueOf(20_000D), 1L, Collections.singletonList(new MenuResponse.MenuProductResponse(2L, 2L, 2L, 1)));
    when(menuService.findAllMenus()).thenReturn(Arrays.asList(createdMenu1, createdMenu2));
    mockMvc
        .perform(get("/api/menus"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("후라이드+후라이드")))
        .andExpect(content().string(containsString("후라이드+양념")));
  }
}
