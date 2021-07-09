package kitchenpos.tablegroup.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.UTF8MockMvcTest;
import kitchenpos.table.domain.OrderTableEntity;
import kitchenpos.table.dto.TableResponse;
import kitchenpos.tablegroup.application.TableGroupService2;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
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

@UTF8MockMvcTest(controllers = {TableGroupRestController2.class})
class TableGroupRestController2Test {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private TableGroupService2 tableGroupService;

  @DisplayName("새로운 테이블그룹을 저장한다.")
  @Test
  void createTest() throws Exception {
    //given
    OrderTableEntity savedOrderTable1 = OrderTableEntity.initWithAll(1L, 1L, 4, true);
    OrderTableEntity savedOrderTable2 = OrderTableEntity.initWithAll(2L, 1L, 2, true);
    TableGroupResponse createdTableGroup = new TableGroupResponse(1L,
                                                  LocalDateTime.of(2021, 6, 29, 3, 45),
                                                  Arrays.asList(TableResponse.from(savedOrderTable1), TableResponse.from(savedOrderTable2)));
    when(tableGroupService.create(any())).thenReturn(createdTableGroup);
    TableGroupRequest createRequestBody = new TableGroupRequest(Arrays.asList(new TableGroupRequest.TableGroupId(savedOrderTable1.getId()),
                                                                              new TableGroupRequest.TableGroupId(savedOrderTable2.getId())));
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
