package kitchenpos.order.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.order.fixture.OrderTableTestFixture.주문테이블_생성_요청;
import static kitchenpos.order.fixture.TableGroupTestFixture.*;

@DisplayName("테이블그룹(단체) 관련 인수 테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    /**
     * Given : 단체로 지정할 주문테이블이 2개 이상 존재한다.
     * When : 단체테이블로 지정 요청한다.
     * Then : 단체테이블로 그룹화가 된다.
     */
    @DisplayName("단체 테이블 생성 요청 인수 테스트")
    @Test
    void createTableGroup() {
        // given
        OrderTable 주문테이블1 = 주문테이블_생성_요청(4, true).as(OrderTable.class);
        OrderTable 주문테이블2 = 주문테이블_생성_요청(6, true).as(OrderTable.class);

        // when
        ExtractableResponse<Response> response = 단체테이블_생성_요청(주문테이블1, 주문테이블2);

        // then
        단체테이블_생성됨(response);
    }

    /**
     * Given : 주문 테이블 2개가 존재하고, 그 중 1개는 빈 테이블로 존재한다.
     * When : 단체테이블로 지정 요청한다.
     * Then : 단체테이블로 그룹화가 실패한다.
     */
    @DisplayName("단체 테이블 생성 요청 인수 테스트")
    @Test
    void createTableGroupExceptionNotEmptyTable() {
        // given
        OrderTable 주문테이블1 = 주문테이블_생성_요청(4, true).as(OrderTable.class);
        OrderTable 주문테이블3 = 주문테이블_생성_요청(4, false).as(OrderTable.class);

        // when
        ExtractableResponse<Response> response = 단체테이블_생성_요청(주문테이블1, 주문테이블3);

        // then
        단체테이블_생성_실패됨(response);
    }

    /**
     * Given : 단체테이블로 지정된 테이블이 존재한다.
     * When : 단체테이블을 각각의 주문테이블로 분리한다.
     * Then : 단체 테이블이 해체된다.
     */
    @DisplayName("단체테이블 해체 요청 인수테스트")
    @Test
    void ungroupTables() {
        // given
        OrderTable 주문테이블1 = 주문테이블_생성_요청(4, true).as(OrderTable.class);
        OrderTable 주문테이블2 = 주문테이블_생성_요청(6, true).as(OrderTable.class);
        TableGroup tableGroup = 단체테이블_생성_요청(주문테이블1, 주문테이블2).as(TableGroup.class);

        // when
        ExtractableResponse<Response> response = 단체테이블_해체_요청(tableGroup.getId());

        // then
        단체테이블_해체됨(response);
    }
}
