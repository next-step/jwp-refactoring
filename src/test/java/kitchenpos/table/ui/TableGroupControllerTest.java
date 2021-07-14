package kitchenpos.table.ui;

import kitchenpos.ordertable.application.TableGroupService;
import kitchenpos.common.ControllerTest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.dto.TableGroupRequest;
import kitchenpos.ordertable.dto.TableGroupResponse;
import kitchenpos.ordertable.ui.TableGroupRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = TableGroupRestController.class)
public class TableGroupControllerTest extends ControllerTest<TableGroupRequest> {

    private static final String BASE_URI = "/api/table-groups";

    @MockBean
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupRestController tableGroupRestController;

    @Override
    protected Object controller() {
        return tableGroupRestController;
    }

    private final boolean 비어있음 = true;
    private final OrderTable 비어있는_첫번째_테이블 = new OrderTable( 3, 비어있음);
    private final OrderTable 비어있는_두번째_테이블 = new OrderTable(3, 비어있음);
    private TableGroup 테이블그룹 = new TableGroup(1L, new OrderTables(Arrays.asList(비어있는_첫번째_테이블, 비어있는_두번째_테이블)));

    @DisplayName("단체지정 생성요청")
    @Test
    void 단체지정_생성요청() throws Exception {
        //Given
        when(tableGroupService.create(any())).thenReturn(TableGroupResponse.of(테이블그룹));

        //When
        ResultActions 결과 = postRequest(BASE_URI, TableGroupRequest.of(테이블그룹));

        //Then
        생성성공(결과);
    }

    @DisplayName("단체지정 취소요청")
    @Test
    void 단체지정_취소요청() throws Exception {
        //When
        ResultActions 결과 = deleteRequest(BASE_URI + "/" + 테이블그룹.getId());

        //Then
        삭제성공(결과);
    }
}
