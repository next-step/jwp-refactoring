package kitchenpos.menugroup.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.UTF8MockMvcTest;
import kitchenpos.menugroup.application.MenuGroupService2;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
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

@UTF8MockMvcTest(controllers = {MenuGroupRestController2.class})
class MenuGroupRestController2Test {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private MenuGroupService2 menuGroupService;

  @DisplayName("새로운 메뉴그룹을 저장한다.")
  @Test
  void createTest() throws Exception {
    //given
    MenuGroupResponse createdMenuGroup = new MenuGroupResponse(1L, "추천메뉴");
    when(menuGroupService.create(any())).thenReturn(createdMenuGroup);
    MenuGroupRequest createRequestBody = new MenuGroupRequest("추천메뉴");
    String requestBody = objectMapper.writeValueAsString(createRequestBody);

    //when & then
    mockMvc
        .perform(post("/api/v2/menu-groups")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(status().isCreated());
  }

  @DisplayName("메뉴그룹 목록을 조회한다.")
  @Test
  void findAllMenuGroupsTest() throws Exception {
    MenuGroupResponse createdMenuGroup1 = new MenuGroupResponse(1L, "추천메뉴");
    MenuGroupResponse createdMenuGroup2 = new MenuGroupResponse(2L, "오늘의메뉴");
    when(menuGroupService.findAllMenuGroups()).thenReturn(Arrays.asList(createdMenuGroup1, createdMenuGroup2));
    mockMvc
        .perform(get("/api/v2/menu-groups"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("추천메뉴")))
        .andExpect(content().string(containsString("오늘의메뉴")));
  }

}
