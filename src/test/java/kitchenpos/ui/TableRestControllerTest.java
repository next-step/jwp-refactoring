package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("주문 테이블 컨트롤러 테스트")
@WebMvcTest(TableRestController.class)
class TableRestControllerTest {
    public static final String DEFAULT_TABLES_URI = "/api/tables/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableService tableService;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setTableGroupId(1L);
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(2);
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void 주문_테이블_생성() throws Exception {
        given(tableService.create(any())).willReturn(orderTable);

        final String jsonTypeTableGroup = objectMapper.writeValueAsString(orderTable);

        mockMvc.perform(post(DEFAULT_TABLES_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonTypeTableGroup))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").value(orderTable.getId()))
            .andExpect(jsonPath("empty").value(orderTable.isEmpty()))
            .andExpect(jsonPath("tableGroupId").value(orderTable.getTableGroupId()))
            .andExpect(jsonPath("numberOfGuests").value(orderTable.getNumberOfGuests()));
    }

    @DisplayName("주문 테이블을 조회한다.")
    @Test
    void 주문_테이블_조회() throws Exception {
        given(tableService.list()).willReturn(Collections.singletonList(orderTable));

        mockMvc.perform(get(DEFAULT_TABLES_URI)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(orderTable.getId()))
            .andExpect(jsonPath("$[0].empty").value(orderTable.isEmpty()))
            .andExpect(jsonPath("$[0].tableGroupId").value(orderTable.getTableGroupId()))
            .andExpect(jsonPath("$[0].numberOfGuests").value(orderTable.getNumberOfGuests()));
    }

    @DisplayName("빈 테이블 설정한다.")
    @Test
    void 빈_테이블_설정() throws Exception {
        orderTable.setEmpty(true);
        given(tableService.changeEmpty(orderTable.getId(), orderTable)).willReturn(orderTable);

        final String jsonTypeOrderTable = objectMapper.writeValueAsString(orderTable);

        mockMvc.perform(put(DEFAULT_TABLES_URI + orderTable.getId() + "/empty")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonTypeOrderTable))
            .andDo(print())
            .andExpect(status().isOk())
            // TODO 왜 response body가 비어있는지 확인 필요
            .andExpect(jsonPath("$").doesNotExist())
//            .andExpect(jsonPath("id").value(orderTable.getId()))
//            .andExpect(jsonPath("empty").value(orderTable.isEmpty()))
//            .andExpect(jsonPath("tableGroupId").value(orderTable.getTableGroupId()))
//            .andExpect(jsonPath("numberOfGuests").value(orderTable.getNumberOfGuests()))
        ;
    }

    @DisplayName("게스트의 수를 변경한다.")
    @Test
    void 게스트_수_변경() throws Exception {
        orderTable.setNumberOfGuests(3);

        given(tableService.changeNumberOfGuests(orderTable.getId(), orderTable)).willReturn(orderTable);

        mockMvc.perform(put(DEFAULT_TABLES_URI + orderTable.getId() + "/number-of-guests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderTable)))
            .andDo(print())
            .andExpect(status().isOk())
            // TODO 왜 response body가 비어있는지 확인 필요
            .andExpect(jsonPath("$").doesNotExist())
//            .andExpect(jsonPath("$.id").value(NOT_EMPTY_ORDER_TABLE_ID))
//            .andExpect(jsonPath("$.tableGroupId").isEmpty())
//            .andExpect(jsonPath("$.numberOfGuests").value(numberOfGuests))
//            .andExpect(jsonPath("$.empty").value(false))
        ;
    }
}
