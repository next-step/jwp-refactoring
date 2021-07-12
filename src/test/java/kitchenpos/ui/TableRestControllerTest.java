package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TableRestControllerTest {

    private OrderTable table;
    private OrderTable table2;
    private OrderTable table3;
    private OrderTable table4;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TableService tableService;

    @BeforeEach
    void setUp() {
        table = new OrderTable();
        table.setId(1L);
        table.setNumberOfGuests(0);
        table.setEmpty(true);

        table2 = new OrderTable();
        table2.setId(2L);
        table2.setNumberOfGuests(0);
        table2.setEmpty(true);

        table3 = new OrderTable();
        table3.setId(3L);
        table3.setNumberOfGuests(0);
        table3.setEmpty(false);

        table4 = new OrderTable();
        table4.setId(4L);
        table4.setNumberOfGuests(2);
        table4.setEmpty(false);
    }

    @DisplayName("테이블 정보를 등록한다")
    @Test
    void create() throws Exception {
        // given
        given(tableService.create(any())).willReturn(table);

        // when then
        mockMvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(table)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("등록한 테이블 목록을 조회한다")
    @Test
    void list() throws Exception {
        // given
        given(tableService.list()).willReturn(Arrays.asList(table, table2, table3, table4));

        // when then
        mockMvc.perform(get("/api/tables"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("테이블을 빈 상태로 변경한다")
    @Test
    void changeEmpty() throws Exception {
        // given
        given(tableService.changeEmpty(table4.getId(), table4)).willReturn(table4);

        // when then
        mockMvc.perform(put("/api/tables/" + table4.getId() + "/empty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(table4)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("테이블에 착석한 손님의 수를 변경한다")
    @Test
    void changeNumberOfGuests() throws Exception {
        // given
        given(tableService.changeNumberOfGuests(table4.getId(), table4)).willReturn(table4);

        // when then
        mockMvc.perform(put("/api/tables/" + table4.getId() + "/number-of-guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(table4)))
                .andDo(print())
                .andExpect(status().isOk());
    }

}