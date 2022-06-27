package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_등록_요청;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_등록성공;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_빈테이블_변경성공;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_빈테이블_변경실패;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_빈테이블로_변경요청;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_조회_요청;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_조회성공;
import static kitchenpos.acceptance.OrderTableAcceptanceFactory.테이블그룹에속한_주문테이블_등록_요청;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_등록_요청;

@DisplayName("주문테이블 관련")
public class OrderTableAcceptanceTest extends AcceptanceTest {

    @Test
    void 주문테이블_등록_성공() {
        ExtractableResponse<Response> 주문테이블_등록_결과 = 주문테이블_등록_요청(true, 5);

        주문테이블_등록성공(주문테이블_등록_결과);
    }

    @Test
    void 주문테이블_조회_성공() {
        ExtractableResponse<Response> 주문테이블_조회_결과 = 주문테이블_조회_요청();

        주문테이블_조회성공(주문테이블_조회_결과);
    }

    @Test
    void 주문테이블_빈테이블로_변경_성공() {
        OrderTable 주문테이블 = 주문테이블_등록_요청(true, 5).as(OrderTable.class);
        ExtractableResponse<Response> 주문테이블_빈테이블로_변경_결과 = 주문테이블_빈테이블로_변경요청(주문테이블);

        주문테이블_빈테이블_변경성공(주문테이블_빈테이블로_변경_결과);
    }

    @Test
    void 주문테이블_빈테이블로_변경_실패_존재하지않는_주문테이블() {
        long 존재하지않는_주문테이블번호 = 999L;
        OrderTable 존재하지않는_주문테이블 = new OrderTable();
        존재하지않는_주문테이블.setId(존재하지않는_주문테이블번호);

        ExtractableResponse<Response> 주문테이블_빈테이블로_변경_결과 = 주문테이블_빈테이블로_변경요청(존재하지않는_주문테이블);

        주문테이블_빈테이블_변경실패(주문테이블_빈테이블로_변경_결과);
    }

    @Test
    void 주문테이블_빈테이블로_변경_실패_테이블그룹에속한_주문테이블() {
        OrderTable 테이블그룹에속한_주문테이블 = 테이블그룹에속한_주문테이블_등록_요청(true, 5).as(OrderTable.class);
        OrderTable 테이블그룹에속한_주문테이블2 = 테이블그룹에속한_주문테이블_등록_요청(true, 5).as(OrderTable.class);
        List<OrderTable> 주문테이블_리스트 = Arrays.asList(테이블그룹에속한_주문테이블, 테이블그룹에속한_주문테이블2);
        ExtractableResponse<Response> 테이블그룹_등록_결과 = 테이블그룹_등록_요청(주문테이블_리스트);

        ExtractableResponse<Response> 주문테이블_빈테이블로_변경_결과 = 주문테이블_빈테이블로_변경요청(테이블그룹에속한_주문테이블);

        주문테이블_빈테이블_변경실패(주문테이블_빈테이블로_변경_결과);
    }
    /**
     * TODO
     * - 주문 상태가 요리중이거나 식사중이면 변경할 수 없다.
     *     - 주문 테이블 손님수 변경
     *       - 손님의 수는 0명 이상이어야 한다.
     *       - 존재하는 주문 테이블이어야 한다.
     *             - 주문테이블이 비어있지 않아야한다.
     */

}
