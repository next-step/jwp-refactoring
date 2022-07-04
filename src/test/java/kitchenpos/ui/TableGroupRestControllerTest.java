package kitchenpos.ui;

import static kitchenpos.utils.MockMvcUtil.as;
import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있는_주문_테이블_생성_요청;
import static kitchenpos.utils.generator.TableGroupFixtureGenerator.테이블_그룹_생성_요청;
import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.dto.table.TableGroupResponse;
import kitchenpos.utils.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("API:TableGroup")
public class TableGroupRestControllerTest extends BaseTest {

    public static final String TABLE_GROUP_API_BASE_URL = "/api/table-groups";
    public static final String DELETE_TABLE_GROUP_API_URL_TEMPLATE = TABLE_GROUP_API_BASE_URL.concat("/{tableGroupId}");

    private OrderTableResponse firstOrderTable, secondOrderTable;

    /**
     * @Given 비어있는 주문 테이블을 2개 생성한다.
     */
    @BeforeEach
    void setUp() throws Exception {
        firstOrderTable = as(mockMvcUtil.post(비어있는_주문_테이블_생성_요청()), OrderTableResponse.class);
        secondOrderTable = as(mockMvcUtil.post(비어있는_주문_테이블_생성_요청()), OrderTableResponse.class);
    }

    /**
     * @When 생성한 주문 테이블을 테이블 그룹으로 묶는다.
     */
    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    public void createTableGroup() throws Exception {
        // When
        ResultActions resultActions = mockMvcUtil.post(테이블_그룹_생성_요청(firstOrderTable, secondOrderTable));

        // Then
        resultActions
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(
                jsonPath("$.orderTables[*].id", contains(
                    firstOrderTable.getId().intValue(),
                    secondOrderTable.getId().intValue()
                )));
    }

    /**
     * @Given 테이블 그룹을 생성한다.
     * @When 테이블 그룹을 해제한다.
     * @Then 테이블 그룹이 해제된다.
     */
    @Test
    @DisplayName("특정 테이블 그룹을 해제한다.")
    public void getAllTableGroups() throws Exception {
        // Given
        TableGroupResponse savedTableGroup = as(mockMvcUtil.post(테이블_그룹_생성_요청(firstOrderTable, secondOrderTable)), TableGroupResponse.class);

        // When
        ResultActions resultActions = mockMvcUtil.delete(DELETE_TABLE_GROUP_API_URL_TEMPLATE, savedTableGroup.getId());

        // Then
        resultActions
            .andDo(print())
            .andExpect(status().isNoContent());
    }
}
