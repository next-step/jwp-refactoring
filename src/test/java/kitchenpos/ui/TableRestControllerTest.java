package kitchenpos.ui;

import kitchenpos.application.TableService;
import kitchenpos.domain.NumberOfGuest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.ChangeEmptyRequest;
import kitchenpos.dto.request.ChangeNumberOfGuestsRequest;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.ui.JsonUtil.toJson;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TableRestController.class)
@ExtendWith(MockitoExtension.class)
class TableRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

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
    @DisplayName("인원수 수정시 인원수가 0명 이상이면 정상이다")
    void 인원수_수정시_인원수가_0명_이상이면_정상이다() throws Exception {
        // given
        ChangeNumberOfGuestsRequest changeNumberOfGuestsRequest = new ChangeNumberOfGuestsRequest(2);

        OrderTable orderTable = new OrderTable(1L,
                new TableGroup(1L, LocalDateTime.now(), Arrays.asList()),
                null,
                null,
                changeNumberOfGuestsRequest.getNumberOfGuests(),
                false);

        given(tableService.changeNumberOfGuests(anyLong(), any(NumberOfGuest.class)))
                .willReturn(orderTable);

        // when & then
        mockMvc.perform(
                put("/api/tables/1/number-of-guests")
                        .content(toJson(changeNumberOfGuestsRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(validateOrderTable("$", orderTable));
    }



    @Test
    @DisplayName("정상적으로 테이블의 빈상태를 바꿀경우")
    void 정상적으로_테이블의_빈상태를_바꿀경우() throws Exception {
        // given
        ChangeEmptyRequest changeEmptyRequest = new ChangeEmptyRequest(true);

        OrderTable orderTable = new OrderTable(1L,
                new TableGroup(1L, LocalDateTime.now(), Arrays.asList()),
                null,
                null,
                1,
                changeEmptyRequest.isEmpty());

        given(tableService.changeEmpty(1L, changeEmptyRequest.isEmpty()))
                .willReturn(orderTable);

        // when & then
        mockMvc.perform(
                put("/api/tables/1/empty")
                        .content(toJson(changeEmptyRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(validateOrderTable("$", orderTable));
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