package kitchenpos.tablegroup;

import kitchenpos.ControllerTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.ui.TableGroupController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import javax.annotation.PostConstruct;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TableGroupControllerTest extends ControllerTest {

    @MockBean
    protected TableGroupService tableGroupService;

    @PostConstruct
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new TableGroupController(tableGroupService)).build();
    }

    @DisplayName("단체 테이블 지정하기")
    @Test
    void groupingTable() throws Exception {

        //given
        TableGroupRequest tableGroupRequest = new TableGroupRequest();

        OrderTableRequest orderTableA = new OrderTableRequest();
        ReflectionTestUtils.setField(orderTableA, "numberOfGuests", 10);

        OrderTableRequest orderTableB = new OrderTableRequest();
        ReflectionTestUtils.setField(orderTableB, "numberOfGuests", 4);

        ReflectionTestUtils.setField(tableGroupRequest, "orderTables", Arrays.asList(orderTableA, orderTableB));

        TableGroupResponse tableGroupResponse = TableGroupResponse.of(tableGroupRequest.toEntity());
        when(tableGroupService.create(any())).thenReturn(tableGroupResponse);

        //when
        ResultActions resultActions = post("/api/table-groups", tableGroupRequest);

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
