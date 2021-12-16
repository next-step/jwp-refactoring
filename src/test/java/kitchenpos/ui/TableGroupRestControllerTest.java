package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * packageName : kitchenpos.ui
 * fileName : TableGroupRestControllerTest
 * author : haedoang
 * date : 2021/12/15
 * description :
 */
@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest {
    private TableGroup tableGroup;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableGroup = new TableGroup();
        tableGroup.setId(1L);

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(false);
        orderTable1.setTableGroupId(tableGroup.getId());
        orderTable1.setNumberOfGuests(2);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(false);
        orderTable2.setTableGroupId(tableGroup.getId());
        orderTable2.setNumberOfGuests(4);

        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
    }

    @Test
    @DisplayName("테이블들을 단체로 등록한다.")
    public void postTableGroup() throws Exception {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        given(tableGroupService.create(any(TableGroup.class))).willReturn(tableGroup);

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/table-groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tableGroup))
        ).andDo(print());

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.orderTables", hasSize(2)));
    }

    @Test
    @DisplayName("단체 테이블을 취소한다.")
    public void deleteTableGroup() throws Exception {
        // when
        ResultActions actions = mockMvc.perform(
                delete("/api/table-groups/" + tableGroup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        actions.andExpect(status().isNoContent());
    }
}