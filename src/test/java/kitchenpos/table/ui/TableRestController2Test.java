package kitchenpos.table.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.UTF8MockMvcTest;
import kitchenpos.table.application.TableService2;
import kitchenpos.table.dto.TableRequest;
import kitchenpos.table.dto.TableResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@UTF8MockMvcTest(controllers = {TableRestController2.class})
class TableRestController2Test {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private TableService2 tableService;

  @DisplayName("새로운 테이블을 저장한다.")
  @Test
  void createTest() throws Exception {
    //given
    TableResponse createdOrderTable = new TableResponse(1L, null, 2, true);
    when(tableService.create(any())).thenReturn(createdOrderTable);
    TableRequest request = new TableRequest(2, true);
    String requestBody = objectMapper.writeValueAsString(request);

    //when & then
    mockMvc
        .perform(post("/api/v2/tables")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(status().isCreated());
  }

  @DisplayName("테이블 목록을 조회한다.")
  @Test
  void listTest() throws Exception {
    //given
    TableResponse createdOrderTable1 = new TableResponse(1L, null, 4, true);
    TableResponse createdOrderTable2 = new TableResponse(2L, null, 2, true);
    when(tableService.findAllTables()).thenReturn(Arrays.asList(createdOrderTable1, createdOrderTable2));

    //when & then
    mockMvc
        .perform(get("/api/v2/tables"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("\"id\":1")))
        .andExpect(content().string(containsString("\"id\":2")));
  }

  @DisplayName("주문 등록할 수 있는 테이블 여부를 변경할 수 있다.")
  @Test
  void changeEmptyTest() throws Exception {
    //given
    TableResponse modifiedOrderTable = new TableResponse(1L, null, 4, false);
    when(tableService.changeEmpty(any(), any())).thenReturn(modifiedOrderTable);
    TableRequest createRequestBody = new TableRequest(null, false);
    String requestBody = objectMapper.writeValueAsString(createRequestBody);

    //when & then
    mockMvc
        .perform(put("/api/v2/tables/{orderTableId}/empty", modifiedOrderTable.getId())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("\"empty\":false")));
  }

  @DisplayName("테이블의 손님수를 변경할 수 있다.")
  @Test
  void changeNumberOfGuestsTest() throws Exception {
    //given
    TableResponse modifiedOrderTable = new TableResponse(1L, null, 10, false);
    when(tableService.changeNumberOfGuests(any(), any())).thenReturn(modifiedOrderTable);
    TableRequest createRequestBody = new TableRequest(10, null);
    String requestBody = objectMapper.writeValueAsString(createRequestBody);

    //when & then
    mockMvc
        .perform(put("/api/v2/tables/{orderTableId}/number-of-guests", modifiedOrderTable.getId())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("\"numberOfGuests\":10")));
  }

}
