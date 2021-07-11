package kitchenpos.tablegroup.ui;

import kitchenpos.MockMvcTestHelper;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TableGroupRestController.class)
class TableGroupRestControllerTest extends MockMvcTestHelper {

    @MockBean
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupRestController tableGroupRestController;

    @Override
    protected Object controller() {
        return tableGroupRestController;
    }

    @DisplayName("테이블 그룹 생성 요청")
    @Test
    void createTest() throws Exception {
        // given
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(new OrderTableIdRequest(1L),
                                                                        new OrderTableIdRequest(2L)));
        Mockito.when(tableGroupService.create(any())).thenReturn(new TableGroupResponse());

        // when
        ResultActions resultActions = 테이블_그룹_생성_요청(request);

        // then
        테이블_그룹_생성_성공(resultActions);
    }

    @DisplayName("테이블 그룹 삭제 요청")
    @Test
    void ungroupTest() throws Exception {
        // when
        ResultActions resultActions = 테이블_그룹_삭제_요청(1l);

        // then
        테이블_그룹_삭제_성공(resultActions);
    }


    private ResultActions 테이블_그룹_생성_요청(final TableGroupRequest request) throws Exception {
        return postRequest("/api/table-groups", request);
    }

    private MvcResult 테이블_그룹_생성_성공(final ResultActions resultActions) throws Exception {
        return resultActions.andExpect(status().isCreated()).andReturn();
    }

    private ResultActions 테이블_그룹_삭제_요청(final Long tableGroupId) throws Exception {
        String uri = String.format("/api/table-groups/%s", tableGroupId);
        return deleteRequest(uri);
    }

    private void 테이블_그룹_삭제_성공(final ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isNoContent());
    }
}
