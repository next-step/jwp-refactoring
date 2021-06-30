package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.UTF8MockMvcTest;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@UTF8MockMvcTest(controllers = {TableGroupRestController.class})
class TableGroupRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private TableGroupService tableGroupService;

  @DisplayName("새로운 테이블그룹을 저장한다.")
  @Test
  void createTest() throws Exception {
    //given
    OrderTable savedOrderTable1 = new OrderTable(1L, 1L, 4, true);
    OrderTable savedOrderTable2 = new OrderTable(2L, 1L, 2, true);
    TableGroup createdTableGroup = new TableGroup(1L,
                                                  LocalDateTime.of(2021, 6, 29, 3, 45),
                                                  Arrays.asList(savedOrderTable1, savedOrderTable2));
    when(tableGroupService.create(any())).thenReturn(createdTableGroup);
    OrderTable requestBodyOrderTable1 = new OrderTable();
    requestBodyOrderTable1.setId(1L);
    OrderTable requestBodyOrderTable2 = new OrderTable();
    requestBodyOrderTable2.setId(2L);
    TableGroup createRequestBody = new TableGroup(Arrays.asList(requestBodyOrderTable1, requestBodyOrderTable2));
    String requestBody = objectMapper.writeValueAsString(createRequestBody);

    //when & then
    mockMvc
        .perform(post("/api/table-groups")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(status().isCreated());
  }

  @DisplayName("지정 되어있던 테이블 그룹을 해제한다.")
  @Test
  void ungroupTest() throws Exception {
    //given
    long ungroupId = 1L;
    doNothing().when(tableGroupService).ungroup(ungroupId);


    //when & then
    mockMvc
        .perform(delete("/api/table-groups/{tableGroupId}", ungroupId))
        .andExpect(status().isNoContent());
  }

}
