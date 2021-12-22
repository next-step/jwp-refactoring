package kitchenpos.order.table;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.TableGroupRestController;
import kitchenpos.utils.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TableGroupControllerTest extends ControllerTest {

    @PostConstruct
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new TableGroupRestController(tableGroupService)).build();
    }

    @DisplayName("단체 테이블 지정하기")
    @Test
    void groupingTable() throws Exception {

        //given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);

        OrderTable orderTableA = new OrderTable();
        OrderTable orderTableB = new OrderTable();

        orderTableA.setTableGroupId(tableGroup.getId());
        orderTableA.setNumberOfGuests(10);
        orderTableB.setTableGroupId(tableGroup.getId());
        orderTableB.setNumberOfGuests(3);

        tableGroup.setOrderTables(Arrays.asList(orderTableA, orderTableB));

        when(tableGroupService.create(any())).thenReturn(tableGroup);

        //when
        ResultActions resultActions = post("/api/table-groups", tableGroup);

        //then
        resultActions.andExpect(status().isCreated());
    }

    @DisplayName("지정된 단체 테이블 해제하기")
    @Test
    void unGroupingTable() throws Exception {

        //given
        doNothing().when(tableGroupService).ungroup(anyLong());

        //when
        ResultActions resultActions = delete("/api/table-groups/1", new LinkedMultiValueMap<>());

        //then
        resultActions.andExpect(status().isNoContent());
    }
}
