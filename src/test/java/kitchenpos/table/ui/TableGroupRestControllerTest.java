package kitchenpos.table.ui;

import kitchenpos.ControllerTest;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.ui.TableGroupRestController;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TableGroupRestController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class TableGroupRestControllerTest extends ControllerTest {
    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("테이블그룹 생성을 요청하면 생성된 테이블그룹응답")
    @Test
    public void returnTableGroup() throws Exception {
        TableGroupResponse tableGroup = getTableGroup();
        doReturn(tableGroup).when(tableGroupService).create(any(TableGroupRequest.class));

        webMvc.perform(post("/api/table-groups")
                .content(mapper.writeValueAsString(new TableGroup()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(tableGroup.getId().intValue())))
                .andExpect(status().isCreated());
    }

    @DisplayName("테이블그룹 생성을 요청하면 실패응답")
    @Test
    public void throwsExceptionWhenTableGroupCreate() throws Exception {
        doThrow(new IllegalArgumentException()).when(tableGroupService).create(any(TableGroupRequest.class));

        webMvc.perform(post("/api/table-groups")
                .content(mapper.writeValueAsString(new TableGroup()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("테이블그룹 해재를 요청하면 성공응답")
    @Test
    public void unTableGroup() throws Exception {
        OrderTables orderTables = OrderTables.of(Arrays.asList(OrderTable.builder().id(1l).build()));
        TableGroup tableGroup = TableGroup.builder().id(13l)
                .orderTables(orderTables)
                .build();
        webMvc.perform(delete("/api/table-groups/" + tableGroup.getId()))
                .andExpect(status().isNoContent());
    }

    @DisplayName("테이블그룹 해재를 요청하면 실패응답")
    @Test
    public void throwsExceptionWhenUnTableGroup() throws Exception {
        OrderTables orderTables = OrderTables.of(Arrays.asList(OrderTable.builder().id(1l).build()));
        TableGroup tableGroup = TableGroup.builder().id(13l)
                .orderTables(orderTables)
                .build();
        doThrow(new IllegalArgumentException()).when(tableGroupService).ungroup(anyLong());
        webMvc.perform(delete("/api/table-groups/" + tableGroup.getId()))
                .andExpect(status().isBadRequest());
    }

    private TableGroupResponse getTableGroup() {
        return TableGroupResponse.of(TableGroup.builder()
                .id(Arbitraries.longs().between(1, 100).sample())
                .orderTables(OrderTables.of(getTables()))
                .build());
    }

    private List<OrderTable> getTables() {
        return IntStream.rangeClosed(1, 3)
                .mapToObj(value -> OrderTable.builder()
                        .tableGroup(TableGroup.builder().build())
                        .build())
                .collect(Collectors.toList());
    }
}

