package kitchenpos.tablegroup.ui;

import com.navercorp.fixturemonkey.FixtureMonkey;
import kitchenpos.ControllerTest;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TableGroupRestController.class)
public class TableGroupRestControllerTest extends ControllerTest {
    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("테이블그룹 생성을 요청하면 생성된 테이블그룹응답")
    @Test
    public void returnTableGroup() throws Exception {
        TableGroup tableGroup = getTableGroup();
        doReturn(tableGroup).when(tableGroupService).create(any(TableGroup.class));

        webMvc.perform(post("/api/table-groups")
                        .content(mapper.writeValueAsString(new TableGroup()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(tableGroup.getId().intValue())))
                .andExpect(jsonPath("$.orderTables", hasSize(tableGroup.getOrderTables().size())))
                .andExpect(status().isCreated());
    }

    @DisplayName("테이블그룹 생성을 요청하면 실패응답")
    @Test
    public void throwsExceptionWhenTableGroupCreate() throws Exception {
        doThrow(new IllegalArgumentException()).when(tableGroupService).create(any(TableGroup.class));

        webMvc.perform(post("/api/table-groups")
                        .content(mapper.writeValueAsString(new TableGroup()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("테이블그룹 해재를 요청하면 성공응답")
    @Test
    public void unTableGroup() throws Exception {
        TableGroup tableGroup = getTableGroup();
        webMvc.perform(delete("/api/table-groups/" + tableGroup.getId()))
                .andExpect(status().isNoContent());
    }

    @DisplayName("테이블그룹 해재를 요청하면 실패응답")
    @Test
    public void throwsExceptionWhenUnTableGroup() throws Exception {
        TableGroup tableGroup = getTableGroup();
        doThrow(new IllegalArgumentException()).when(tableGroupService).ungroup(tableGroup.getId());
        webMvc.perform(delete("/api/table-groups/" + tableGroup.getId()))
                .andExpect(status().isBadRequest());
    }

    private TableGroup getTableGroup() {
        return FixtureMonkey.create()
                .giveMeBuilder(TableGroup.class)
                .set("id", Arbitraries.longs().between(1, 100))
                .set("createdDate", LocalDateTime.now())
                .set("orderTables", getTables())
                .set("empty", true)
                .sample();
    }

    private List<OrderTable> getTables() {
        return FixtureMonkey.create()
                .giveMeBuilder(OrderTable.class)
                .sampleList(Arbitraries.integers().between(1, 3).sample());
    }
}

