package kitchenpos.table;

import kitchenpos.common.BaseContollerTest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TableGroupControllerTest extends BaseContollerTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("테이블을 그룹화/비그룹화 합니다.")
    void createTableGroup() throws Exception {

        Long tableGroupId = 테이블_그룹화_요청();

        테이블_그룹화_삭제_요청(tableGroupId);

    }

    private Long 테이블_그룹화_요청() throws Exception {
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
                this.orderTableDao.findAll().stream().map(OrderTable::getId).collect(Collectors.toList()));

        MvcResult mvcResult = this.mockMvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(tableGroupRequest))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
        ;

        String responseTableGroup = mvcResult.getResponse().getContentAsString();
        return this.objectMapper.readValue(responseTableGroup, TableGroupResponse.class).getId();
    }

    private void 테이블_그룹화_삭제_요청(Long tableGroupId) throws Exception {
        this.mockMvc.perform(delete("/api/table-groups/" + tableGroupId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
