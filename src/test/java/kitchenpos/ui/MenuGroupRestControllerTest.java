package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.UTF8MockMvcTest;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@UTF8MockMvcTest(controllers = {MenuGroupRestController.class})
class MenuGroupRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private MenuGroupService menuGroupService;

  @DisplayName("새로운 메뉴그룹을 저장한다.")
  @Test
  void createTest() throws Exception {
    //given
    MenuGroup createdMenuGroup = new MenuGroup(1L, "추천메뉴");
    when(menuGroupService.create(any())).thenReturn(createdMenuGroup);
    MenuGroup createRequestBody = new MenuGroup("추천메뉴");
    String requestBody = objectMapper.writeValueAsString(createRequestBody);

    //when & then
    mockMvc
        .perform(post("/api/menu-groups")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(status().isCreated());
  }

  @DisplayName("메뉴그룹 목록을 조회한다.")
  @Test
  void listTest() throws Exception {
    MenuGroup createdMenuGroup1 = new MenuGroup(1L, "추천메뉴");
    MenuGroup createdMenuGroup2 = new MenuGroup(2L, "오늘의메뉴");
    when(menuGroupService.list()).thenReturn(Arrays.asList(createdMenuGroup1, createdMenuGroup2));
    mockMvc
        .perform(get("/api/menu-groups"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("추천메뉴")))
        .andExpect(content().string(containsString("오늘의메뉴")));
  }
}
