package kitchenpos.table.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.ui.TableRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TableRestController.class)
class TableRestControllerTest {
    private static final String TABLE_API_URI = "/api/tables";

    @Autowired
    private TableRestController tableRestController;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableService tableService;

    private MockMvc mockMvc;
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tableRestController)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .alwaysDo(print())
                .build();

        orderTable1 = new OrderTable(1L, null, 1, true);
        orderTable1 = new OrderTable(2L, null, 1, true);
    }

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void createTest() throws Exception {
        // given
        given(tableService.create(any())).willReturn(orderTable1);

        // when
        ResultActions actions = mockMvc.perform(post(TABLE_API_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable1)));

        // then
        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.tableGroupId").isEmpty())
                .andExpect(jsonPath("$.numberOfGuests").value(orderTable1.getNumberOfGuests()))
                .andExpect(jsonPath("$.empty").value(orderTable1.isEmpty()));
    }

    @DisplayName("주문 테이블의 빈 테이블 여부를 변경할 수 있다.")
    @Test
    void changeEmptyTest() throws Exception {
        // given
        given(tableService.changeEmpty(orderTable1.getId(), orderTable1)).willReturn(orderTable1);

        // when
        ResultActions actions = mockMvc.perform(put(TABLE_API_URI +"/{orderTableId}/empty", orderTable1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable1)));

        // then
        actions.andExpect(status().isOk());
    }

    @DisplayName("주문 테이블의 손님수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuestsTest() throws Exception {
        // given
        given(tableService.changeEmpty(orderTable1.getId(), orderTable1)).willReturn(orderTable1);

        // when
        ResultActions actions = mockMvc.perform(put(TABLE_API_URI +"/{orderTableId}/number-of-guests", orderTable1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable1)));

        // then
        actions.andExpect(status().isOk());
    }

    @DisplayName("주문 테이블의 목록을 조회할 수 있다.")
    @Test
    void listTest() throws Exception {
        // given
        given(tableService.list()).willReturn(Arrays.asList(orderTable1, orderTable2));

        // when
        ResultActions actions = mockMvc.perform(get(TABLE_API_URI));

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].tableGroupId").isEmpty())
                .andExpect(jsonPath("$[0].numberOfGuests").value(orderTable1.getNumberOfGuests()))
                .andExpect(jsonPath("$[0].empty").value(orderTable1.isEmpty()));
    }
}
