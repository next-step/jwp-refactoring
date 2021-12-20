package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

class TableRestControllerTest extends IntegrationTest {

    private static final String BASE_PATH = "/api/tables";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    @DisplayName("주문 테이블 등록")
    @Test
    void create() throws Exception {
        //given
        int numOfGuests = 6;
        boolean empty = false;
        Map<String, String> params = 테이블_정보(numOfGuests, empty);
        OrderTable expectedOrderTable = new OrderTable(1L, numOfGuests, empty);
        given(tableService.create(any())).willReturn(expectedOrderTable);

        //when
        MockHttpServletResponse response = mockMvc.perform(
            post(BASE_PATH).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params))).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        OrderTable savedTableOrder = objectMapper.readValue(response.getContentAsString(),
            OrderTable.class);
        assertThat(savedTableOrder).isEqualTo(expectedOrderTable);
    }

    @DisplayName("주문 테이블 조회")
    @Test
    void list() throws Exception {
        //given
        List<OrderTable> orderTables = Arrays.asList(new OrderTable(1L, 6, false),
            new OrderTable(2L, 3, false));

        given(tableService.list()).willReturn(orderTables);

        //when
        MockHttpServletResponse response = mockMvc.perform(get(BASE_PATH)).andReturn()
            .getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        List<OrderTable> findOrderTables = objectMapper.readValue(response.getContentAsString(),
            new TypeReference<List<OrderTable>>() {
            });
        assertThat(findOrderTables).containsAll(orderTables);
    }

    @DisplayName("테이블 상태 변경")
    @Test
    void changeEmpty() throws Exception {
        //given
        boolean changeEmpty = false;
        Map<String, String> params = 테이블_상태_변경_정보(changeEmpty);
        OrderTable expectedOrderTable = new OrderTable(1L, 1L, 6, changeEmpty);
        given(tableService.changeEmpty(any(), any())).willReturn(expectedOrderTable);

        //when
        MockHttpServletResponse response = mockMvc.perform(put(BASE_PATH + "/{orderTableId}/empty",
            expectedOrderTable.getTableGroupId()).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(params))).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        OrderTable changedTableOrder = objectMapper.readValue(response.getContentAsString(),
            OrderTable.class);
        assertThat(changedTableOrder).isEqualTo(expectedOrderTable);
    }

    @DisplayName("테이블 방문자 수 변경")
    @Test
    void changeNumberOfGuests() throws Exception {
        //given
        int numberOfGuests = 5;
        Map<String, String> params = 테이블_방문자수_변경_정보(numberOfGuests);
        OrderTable expectedOrderTable = new OrderTable(1L, 1L, numberOfGuests, false);
        given(tableService.changeNumberOfGuests(any(), any())).willReturn(expectedOrderTable);

        //when
        MockHttpServletResponse response = mockMvc.perform(
            put(BASE_PATH + "/{numberOfGuests}/number-of-guests",
                expectedOrderTable.getNumberOfGuests()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(params))).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        OrderTable changedOrderTable = objectMapper.readValue(response.getContentAsString(),
            OrderTable.class);
        assertThat(changedOrderTable).isEqualTo(expectedOrderTable);
    }

    private Map<String, String> 테이블_정보(int numOfGuests, boolean empty) {
        Map<String, String> params = new HashMap<>();
        params.put("numOfGuests", String.valueOf(numOfGuests));
        params.put("empty", String.valueOf(empty));
        return params;
    }

    private Map<String, String> 테이블_상태_변경_정보(boolean empty) {
        Map<String, String> params = new HashMap<>();
        params.put("empty", String.valueOf(empty));
        return params;
    }

    private Map<String, String> 테이블_방문자수_변경_정보(int numberOfGuests) {
        Map<String, String> params = new HashMap<>();
        params.put("numberOfGuests", String.valueOf(numberOfGuests));
        return params;
    }
}
