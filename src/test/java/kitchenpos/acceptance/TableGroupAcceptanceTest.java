package kitchenpos.acceptance;

import static kitchenpos.acceptance.TableAcceptanceTestFixture.주문_테이블_생성되어_있음;
import static kitchenpos.acceptance.TableGroupAcceptanceTestFixture.테이블_그룹_생성_요청;
import static kitchenpos.acceptance.TableGroupAcceptanceTestFixture.테이블_그룹_생성되어_있음;
import static kitchenpos.acceptance.TableGroupAcceptanceTestFixture.테이블_그룹_생성됨;
import static kitchenpos.acceptance.TableGroupAcceptanceTestFixture.테이블_그룹_해제_요청;
import static kitchenpos.acceptance.TableGroupAcceptanceTestFixture.테이블_그룹_해제됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("테이블 그룹 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    private TableGroup 단체테이블;

    @BeforeEach
    void tableGroupSetUp() {
        super.setUp();

        OrderTable 주문테이블_A = 주문_테이블_생성되어_있음(new OrderTable(null, null, 5, true)).as(OrderTable.class);
        OrderTable 주문테이블_B = 주문_테이블_생성되어_있음(new OrderTable(null, null, 6, true)).as(OrderTable.class);

        단체테이블 = new TableGroup(null, null, Arrays.asList(주문테이블_A, 주문테이블_B));
    }

    @DisplayName("테이블 그룹을 등록한다.")
    @Test
    void createTableGroup() {
        ExtractableResponse<Response> response = 테이블_그룹_생성_요청(단체테이블);

        테이블_그룹_생성됨(response);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        TableGroup tableGroup = 테이블_그룹_생성되어_있음(this.단체테이블).as(TableGroup.class);

        ExtractableResponse<Response> response = 테이블_그룹_해제_요청(tableGroup.getId());

        테이블_그룹_해제됨(response);
    }
}
