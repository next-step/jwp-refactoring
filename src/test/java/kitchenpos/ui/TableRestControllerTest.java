package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableCreate;
import kitchenpos.dto.request.ChangeEmptyRequest;
import kitchenpos.dto.request.ChangeNumberOfGuestsRequest;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.fixture.CleanUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;

import static kitchenpos.fixture.OrderTableFixture.사용중인_1명_1건_결제완료_1건_식사;
import static kitchenpos.fixture.OrderTableFixture.사용중인_1명_테이블;
import static kitchenpos.ui.JsonUtil.toJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TableRestController.class)
@ExtendWith(MockitoExtension.class)
class TableRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    @BeforeEach
    void setUp() {
        CleanUp.cleanUpOrderFirst();
    }

    @Test
    @DisplayName("인원수 수정시 인원수가 0명미만이면 BadRequest가 발생한다")
    void 인원수_수정시_인원수가_0명미만이면_BadRequest가_발생한다() throws Exception {
        // given
        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(-1);

        // when & then
        mockMvc.perform(
                put("/api/tables/{orderTableId}/number-of-guests", 1)
                        .content(toJson(changeNumberOfGuestsRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("정상적으로 테이블의 빈상태를 바꿀경우")
    void 정상적으로_테이블의_빈상태를_바꿀경우() throws Exception {
        // given
        ChangeEmptyRequest changeEmptyRequest = new ChangeEmptyRequest(true);

        given(tableService.changeEmpty(1L, changeEmptyRequest.isEmpty()))
                .willReturn(사용중인_1명_테이블);

        // when & then
        mockMvc.perform(
                put("/api/tables/1/empty")
                        .content(toJson(changeEmptyRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(validateOrderTable("$", 사용중인_1명_테이블));
    }

    @Test
    @DisplayName("정상적으로 테이블의 리스트를 가져올경우")
    void 정상적으로_테이블의_리스트를_가져올경우() throws Exception {
        // given
        given(tableService.list())
                .willReturn(Arrays.asList(사용중인_1명_1건_결제완료_1건_식사, 사용중인_1명_테이블));

        // when & then
        mockMvc.perform(
                get("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(validateOrderTable("$.[0]", 사용중인_1명_1건_결제완료_1건_식사))
                .andExpect(validateOrderTable("$.[1]", 사용중인_1명_테이블));
    }

    @Test
    @DisplayName("정상적으로 생성을 성공할경우")
    void 정상적으로_생성을_성공할경우() throws Exception {
        // given
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(1, false);


        given(tableService.create(any(OrderTableCreate.class)))
                .willReturn(사용중인_1명_1건_결제완료_1건_식사);

        // when & then
        mockMvc.perform(
                post("/api/tables")
                        .content(toJson(orderTableCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(validateOrderTable("$", 사용중인_1명_1건_결제완료_1건_식사));
    }

    private ResultMatcher validateOrderTable(String prefix, OrderTable orderTable) {
        return result -> {
            ResultMatcher.matchAll(
                    jsonPath(prefix + ".id").value(orderTable.getId()),
                    jsonPath(prefix + ".tableGroupId").value(orderTable.getTableGroup().getId()),
                    jsonPath(prefix + ".numberOfGuests").value(orderTable.getNumberOfGuests().toInt()),
                    jsonPath(prefix + ".empty").value(orderTable.isEmpty())
            ).match(result);
        };
    }

}