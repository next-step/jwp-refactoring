package kitchenpos.ui;

import static kitchenpos.util.TestDataSet.테이블_1번;
import static kitchenpos.util.TestDataSet.테이블_2번;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;

@WebMvcTest(controllers = TableRestController.class)
@ExtendWith(MockitoExtension.class)
public class TableRestControllerTest {

    public static final String BASE_URL = "/api/tables";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableService tableService;

    @Test
    @DisplayName("테이블의 앉은 손님의 수와, 현재 테이블이 비었는지 입력 받아 주문_테이블을 만들 수 있다.")
    void create() throws Exception {
        // given
        String content = objectMapper.writeValueAsString(테이블_1번);
        given(tableService.create(any(OrderTable.class))).willReturn(테이블_1번);

        // when
        mockMvc.perform(
            post(BASE_URL)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.numberOfGuests").value(테이블_1번.getNumberOfGuests()))
            .andExpect(jsonPath("$.empty").value(테이블_1번.isEmpty()));
    }

    @Test
    @DisplayName("주문_테이블 리스트를 출력할 수 있다.")
    void list() throws Exception {
        // given
        given(tableService.list())
            .willReturn(Arrays.asList(테이블_1번, 테이블_2번));

        // when
        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].numberOfGuests").value(테이블_1번.getNumberOfGuests()))
            .andExpect(jsonPath("$[1].numberOfGuests").value(테이블_2번.getNumberOfGuests()));
    }

    @Test
    @DisplayName("주문_테이블의 손님 수를 업데이트를 할 수 있다.")
    void changeNumberOfGuests() throws Exception {
        // given
        OrderTable 인원수_업데이트된_테이블 = new OrderTable(1L, 10, false);
        String content = objectMapper.writeValueAsString(인원수_업데이트된_테이블);

        given(tableService.changeNumberOfGuests(any(), any()))
            .willReturn(인원수_업데이트된_테이블);

        // when
        mockMvc.perform(
            put(BASE_URL + "/{orderTableId}/number-of-guests", 테이블_1번.getId())
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(인원수_업데이트된_테이블.getId()))
            .andExpect(jsonPath("$.numberOfGuests").value(인원수_업데이트된_테이블.getNumberOfGuests()));
    }

    @Test
    @DisplayName("주문_테이블이 비었는지에 업데이트를 할 수 있다.")
    void changeEmpty() throws Exception {
        // given
        OrderTable 사용유부_업데이트된_테이블 = new OrderTable(1L, 0, true);
        String content = objectMapper.writeValueAsString(사용유부_업데이트된_테이블);

        given(tableService.changeEmpty(any(), any()))
            .willReturn(사용유부_업데이트된_테이블);

        // when
        mockMvc.perform(
            put(BASE_URL + "/{orderTableId}/empty", 테이블_1번.getId())
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(사용유부_업데이트된_테이블.getId()))
            .andExpect(jsonPath("$.empty").value(사용유부_업데이트된_테이블.isEmpty()));
    }

}
