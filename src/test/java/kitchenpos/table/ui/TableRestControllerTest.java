package kitchenpos.table.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.ui.TableRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private TableService tableService;


    @DisplayName("테이블 등록")
    @Test
    public void create() throws Exception {
        OrderTable expectedTable = new OrderTable(3, true);
        given(tableService.create(any())).willReturn(expectedTable);

        mockMvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJsonString(expectedTable)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("테이블 목록")
    @Test
    public void list() throws Exception {
        OrderTable table1 = new OrderTable(1L, 1L, 3, true);
        OrderTable table2 = new OrderTable(2L, 2L, 3, true);
        given(tableService.list()).willReturn(Arrays.asList(table1, table2));

        mockMvc.perform(get("/api/tables"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[1].id").value(2L));
    }

    @DisplayName("빈 테이블 지정")
    @Test
    public void changeEmpty() throws Exception {
        OrderTable expectedTable = new OrderTable(1L, 1L, 3, false);
        given(tableService.changeEmpty(any(), any())).willReturn(expectedTable);

        mockMvc.perform(put("/api/tables/{orderTableId}/empty", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJsonString(expectedTable)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empty").value(false));
    }

    @DisplayName("테이블 손님수 변경")
    @Test
    public void ungroup() throws Exception {
        OrderTable expectedTable = new OrderTable(1L, 1L, 3, true);
        given(tableService.changeNumberOfGuests(1L, expectedTable)).willReturn(expectedTable);

        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJsonString(expectedTable)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfGuests").value(3));

    }

    private String makeJsonString(Object request) throws JsonProcessingException {
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(request);
    }

}