package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static kitchenpos.fixture.OrderTableFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TableRestControllerTest extends BaseRestControllerTest {

    @Mock
    private TableService tableService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new TableRestController(tableService)).build();
    }

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() throws Exception {
        //given
        OrderTable request = 주문테이블_생성요청_데이터_생성(4);
        String requestBody = objectMapper.writeValueAsString(request);

        given(tableService.create(any())).willReturn(주문테이블_데이터_생성(1L, null, 4, false));

        //when //then
        mockMvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @DisplayName("테이블 전체를 조회한다.")
    @Test
    void list() throws Exception {
        //given
        given(tableService.list()).willReturn(Arrays.asList(주문테이블_데이터_생성(1L, null, 4, false)));

        //when //then
        mockMvc.perform(get("/api/tables"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @DisplayName("빈테이블로 변경한다.")
    @Test
    void changeEmpty() throws Exception {
        //given
        Long orderTableId = 1L;
        OrderTable request = 주문테이블_비우기_데이터_생성(true);
        String requestBody = objectMapper.writeValueAsString(request);

        given(tableService.changeEmpty(any(), any()))
                .willReturn(주문테이블_데이터_생성(1L, null, 4, true));

        //when //then
        mockMvc.perform(put("/api/tables/{orderTableId}/empty", orderTableId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empty").value(true));
    }

    @DisplayName("테이블의 손님수를 변경한다.")
    @Test
    void changeNumberOfGuests() throws Exception {
        //given
        Long orderTableId = 1L;
        OrderTable request = 주문테이블_손님수변경_데이터_생성(3);
        String requestBody = objectMapper.writeValueAsString(request);

        given(tableService.changeNumberOfGuests(any(), any()))
                .willReturn(주문테이블_데이터_생성(1L, null, 3, false));

        //when //then
        mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfGuests").value(3));
    }
}