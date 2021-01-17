package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import kitchenpos.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableEmptyChangeRequest;
import kitchenpos.dto.TableGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-09
 */
class TableRestControllerTest extends BaseControllerTest {

    @DisplayName("테이블 생성 테스트")
    @Test
    void tableCreateTest() throws Exception {

        OrderTableCreateRequest orderTable = new OrderTableCreateRequest(null, 4, true);

        mockMvc.perform(post("/api/tables")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(orderTable)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("테이블 조회 테스트")
    @Test
    void tableListTest() throws Exception  {
        mockMvc.perform(get("/api/tables"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("테이블 상태 변경 테스트")
    @Test
    void tableChangeEmptyTest() throws Exception  {
        Long tableId = createGroupedTable();

        OrderTableEmptyChangeRequest request = new OrderTableEmptyChangeRequest(false);

        mockMvc.perform(put("/api/tables/"+ tableId +"/empty")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("empty")
                        .value(false));
    }

    @DisplayName("테이블 인원 변경 테스트")
    @Test
    void tableChangeNumberOfGuestTest() throws Exception  {
        Long tableId = createGroupedTable();
        
        OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(8);

        mockMvc.perform(put("/api/tables/"+ tableId +"/empty")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(put("/api/tables/"+ tableId +"/number-of-guests")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("numberOfGuests")
                        .value(8));
    }


    private Long getCreateTableId() throws Exception {
        String url = mockMvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new OrderTableCreateRequest(null, 4, true))))
                .andReturn()
                .getResponse()
                .getHeader("Location").split("/")[3];

        return Long.valueOf(url);
    }

    private void createdTableGroupId(Long ...tableId) throws Exception {
        TableGroupCreateRequest createRequest = new TableGroupCreateRequest(Arrays.asList(tableId));

        mockMvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists());
    }

    private Long createGroupedTable() throws Exception {
        Long tableId = getCreateTableId();

        createdTableGroupId(tableId, getCreateTableId());

        return tableId;
    }

}
