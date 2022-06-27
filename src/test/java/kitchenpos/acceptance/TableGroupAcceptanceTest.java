package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.acceptance.OrderTableAcceptanceFactory.주문테이블_등록_요청;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_등록_요청;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_등록성공;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_등록실패;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_삭제_요청;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_삭제성공;
import static kitchenpos.acceptance.TableGroupAcceptanceFactory.테이블그룹_삭제실패;

@DisplayName("테이블그룹 관련")
public class TableGroupAcceptanceTest extends AcceptanceTest {


    @Test
    void 테이블그룹_등록_성공() {
        OrderTable 테이블1 = 주문테이블_등록_요청(true, 5).as(OrderTable.class);
        OrderTable 테이블2 = 주문테이블_등록_요청(true, 5).as(OrderTable.class);
        List<OrderTable> 주문테이블_리스트 = Arrays.asList(테이블1, 테이블2);

        ExtractableResponse<Response> 테이블그룹_등록_결과 = 테이블그룹_등록_요청(주문테이블_리스트);

        테이블그룹_등록성공(테이블그룹_등록_결과);
    }

    @Test
    void 테이블그룹_등록_실패_주문테이블이_1개() {
        OrderTable 테이블1 = 주문테이블_등록_요청(true, 5).as(OrderTable.class);
        List<OrderTable> 주문테이블_리스트 = Arrays.asList(테이블1);

        ExtractableResponse<Response> 테이블그룹_등록_결과 = 테이블그룹_등록_요청(주문테이블_리스트);

        테이블그룹_등록실패(테이블그룹_등록_결과);
    }

    @Test
    void 테이블그룹_등록_실패_존재하지않는_주문테이블이존재할때() {
        OrderTable 테이블1 = 주문테이블_등록_요청(true, 5).as(OrderTable.class);
        OrderTable 테이블2 = new OrderTable();
        List<OrderTable> 주문테이블_리스트 = Arrays.asList(테이블1, 테이블2);

        ExtractableResponse<Response> 테이블그룹_등록_결과 = 테이블그룹_등록_요청(주문테이블_리스트);

        테이블그룹_등록실패(테이블그룹_등록_결과);
    }

    @Test
    void 테이블그룹_등록_실패_비어있지_않은_주문테이블이_존재할때() {
        OrderTable 테이블1 = 주문테이블_등록_요청(true, 5).as(OrderTable.class);
        OrderTable 테이블2 = 주문테이블_등록_요청(false, 5).as(OrderTable.class);
        List<OrderTable> 주문테이블_리스트 = Arrays.asList(테이블1, 테이블2);

        ExtractableResponse<Response> 테이블그룹_등록_결과 = 테이블그룹_등록_요청(주문테이블_리스트);

        테이블그룹_등록실패(테이블그룹_등록_결과);
    }

    @Test
    void 테이블그룹_삭제_성공() {
        OrderTable 테이블1 = 주문테이블_등록_요청(true, 5).as(OrderTable.class);
        OrderTable 테이블2 = 주문테이블_등록_요청(true, 5).as(OrderTable.class);
        List<OrderTable> 주문테이블_리스트 = Arrays.asList(테이블1, 테이블2);
        TableGroup 등록된_테이블그룹 = 테이블그룹_등록_요청(주문테이블_리스트).as(TableGroup.class);

        ExtractableResponse<Response> 테이블그룹_삭제_결과 = 테이블그룹_삭제_요청(등록된_테이블그룹.getId());

        테이블그룹_삭제성공(테이블그룹_삭제_결과);
    }

    @Test
    void 테이블그룹_삭제실패_주문상태가_요리중이거나_식사중() {
        OrderTable 테이블1 = 주문테이블_등록_요청(true, 5).as(OrderTable.class);
        OrderTable 테이블2 = 주문테이블_등록_요청(true, 5).as(OrderTable.class);
        List<OrderTable> 주문테이블_리스트 = Arrays.asList(테이블1, 테이블2);
        TableGroup 등록된_테이블그룹 = 테이블그룹_등록_요청(주문테이블_리스트).as(TableGroup.class);

        //TODO 주문생성, 주문상태 변경이 필요함 현재 실패하는 테스트

        ExtractableResponse<Response> 테이블그룹_삭제_결과 = 테이블그룹_삭제_요청(등록된_테이블그룹.getId());

        테이블그룹_삭제실패(테이블그룹_삭제_결과);
    }

}
