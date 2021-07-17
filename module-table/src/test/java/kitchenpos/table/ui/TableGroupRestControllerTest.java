package kitchenpos.table.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import kitchenpos.table.RestControllerTest;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("단체 지정 API")
@WebMvcTest(TableGroupRestController.class)
public class TableGroupRestControllerTest extends RestControllerTest<TableGroupRequest> {

    public static final String BASE_URL = "/api/table-groups";

    @MockBean
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 등록한다.")
    @Test
    void create() throws Exception {
        // Given
        OrderTable 주문테이블1 = new OrderTable(1L, 2, true);
        OrderTable 주문테이블2 = new OrderTable(1L, 2, true);
        TableGroup 단체지정 = new TableGroup(1L, new ArrayList<>(Arrays.asList(주문테이블1, 주문테이블2)));
        given(tableGroupService.create(any())).willReturn(TableGroupResponse.of(단체지정));

        // When & Then
        post(BASE_URL, TableGroupRequest.of(단체지정))
            .andExpect(jsonPath("$.orderTables[0].id").value(단체지정.getId()));
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() throws Exception {
        // Given
        TableGroupRequest 단체지정_요청 = new TableGroupRequest(1L);

        // When & Then
        delete(BASE_URL + "/" + 단체지정_요청.getId())
            .andExpect(status().isNoContent());
    }

}
