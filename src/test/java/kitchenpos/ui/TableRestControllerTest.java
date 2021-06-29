package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.UTF8MockMvcTest;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
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

@UTF8MockMvcTest(controllers = {TableRestController.class})
class TableRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private TableService tableService;

  @DisplayName("새로운 테이블을 저장한다.")
  @Test
  void createTest() throws Exception {
    //given
    OrderTable createdOrderTable = new OrderTable(1L, null, 2, true);
    when(tableService.create(any())).thenReturn(createdOrderTable);
    OrderTable createRequestBody = new OrderTable(0, true);
    String requestBody = objectMapper.writeValueAsString(createRequestBody);

    //when & then
    mockMvc
        .perform(post("/api/tables")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(status().isCreated());
  }

  @DisplayName("테이블 목록을 조회한다.")
  @Test
  void listTest() throws Exception {
    OrderTable createdOrderTable1 = new OrderTable(1L, null, 4, true);
    OrderTable createdOrderTable2 = new OrderTable(2L, null, 2, true);
    when(tableService.list()).thenReturn(Arrays.asList(createdOrderTable1, createdOrderTable2));
    mockMvc
        .perform(get("/api/tables"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("\"id\":1")))
        .andExpect(content().string(containsString("\"id\":2")));
  }

  @DisplayName("주문 등록할 수 있는 테이블 여부를 변경할 수 있다.")
  @Test
  void changeEmptyTest() throws Exception {
    //given
    OrderTable modifiedOrderTable = new OrderTable(1L, null, 4, false);
    when(tableService.changeEmpty(any(), any())).thenReturn(modifiedOrderTable);
    OrderTable createRequestBody = new OrderTable();
    createRequestBody.setEmpty(false);
    String requestBody = objectMapper.writeValueAsString(createRequestBody);

    //when & then
    mockMvc
        .perform(put("/api/tables/{orderTableId}/empty", modifiedOrderTable.getId())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("\"empty\":false")));
  }

  @DisplayName("테이블의 손님수를 변경할 수 있다.")
  @Test
  void changeNumberOfGuestsTest() throws Exception {
    //given
    OrderTable modifiedOrderTable = new OrderTable(1L, null, 10, false);
    when(tableService.changeNumberOfGuests(any(), any())).thenReturn(modifiedOrderTable);
    OrderTable createRequestBody = new OrderTable();
    createRequestBody.setNumberOfGuests(10);
    String requestBody = objectMapper.writeValueAsString(createRequestBody);

    //when & then
    mockMvc
        .perform(put("/api/tables/{orderTableId}/number-of-guests", modifiedOrderTable.getId())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(requestBody))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("\"numberOfGuests\":10")));
  }

}
