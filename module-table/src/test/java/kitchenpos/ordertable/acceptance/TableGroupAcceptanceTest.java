package kitchenpos.ordertable.acceptance;

import acceptance.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static acceptance.TableAcceptanceMethods.테이블_등록되어_있음;
import static acceptance.TableGroupAcceptanceMethods.*;

@DisplayName("테이블그룹 관련 기능 인수테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    OrderTableResponse 테이블1;
    OrderTableResponse 테이블2;

    TableGroupRequest 테이블그룹;

    @BeforeEach
    public void setUp() {
        super.setUp();

        테이블1 = 테이블_등록되어_있음(0, true).as(OrderTableResponse.class);
        테이블2 = 테이블_등록되어_있음(0, true).as(OrderTableResponse.class);
        테이블그룹 = TableGroupRequest.from(Arrays.asList(테이블1.getId(), 테이블2.getId()));
    }

    /**
     * Feature: 테이블그룹 관련 기능
     *
     *   Scenario: 테이블그룹을 관리
     *     When 테이블그룹 등록 요청
     *     Then 테이블그룹 등록됨
     *     When 테이블그룹 삭제 요청
     *     Then 테이블그룹 삭제됨
     */
    @DisplayName("테이블그룹을 관리한다")
    @Test
    void 테이블그룹_관리_정상_시나리오() {
        ExtractableResponse<Response> 테이블그룹_등록 = 테이블그룹_등록_요청(테이블그룹);
        테이블그룹_등록됨(테이블그룹_등록);

        ExtractableResponse<Response> 테이블그룹_삭제 = 테이블그룹_삭제_요청(테이블그룹_등록);
        테이블그룹_삭제됨(테이블그룹_삭제);
    }

    /**
     * Feature: 테이블그룹 관련 기능
     *
     *   Scenario: 테이블그룹을 관리 실패
     *     When 테이블 1개만으로 테이블그룹 등록 요청
     *     Then 테이블그룹 등록 실패
     *     When 존재하지 않는 테이블 사용하여 테이블그룹 등록 요청
     *     Then 테이블그룹 등록 실패
     *     When 테이블 중복하여 테이블그룹 등록 요청
     *     Then 테이블그룹 등록 실패
     *     When 비어있지 않는 테이블 사용하여 테이블그룹 등록 요청
     *     Then 테이블그룹 등록 실패
     *     When 이미 그룹에 등록된 테이블 사용하여 테이블그룹 등록 요청
     *     Then 테이블그룹 등록 실패
     */
    @DisplayName("테이블그룹을 관리 실패한다")
    @Test
    void 테이블그룹_관리_비정상_시나리오() {
        TableGroupRequest 테이블1개만_사용한_테이블그룹 = TableGroupRequest.from(Arrays.asList(테이블1.getId()));
        ExtractableResponse<Response> 테이블1개만_사용한_테이블그룹_등록 = 테이블그룹_등록_요청(테이블1개만_사용한_테이블그룹);
        테이블그룹_등록_실패됨(테이블1개만_사용한_테이블그룹_등록);

        TableGroupRequest 없는_테이블_사용한_테이블그룹 = TableGroupRequest.from(Arrays.asList(0L));
        ExtractableResponse<Response> 없는_테이블_사용한_테이블그룹_등록 = 테이블그룹_등록_요청(없는_테이블_사용한_테이블그룹);
        테이블그룹_등록_실패됨(없는_테이블_사용한_테이블그룹_등록);

        TableGroupRequest 중복_테이블_사용한_테이블그룹 = TableGroupRequest.from(Arrays.asList(테이블1.getId(), 테이블1.getId()));
        ExtractableResponse<Response> 중복_테이블_사용한_테이블그룹_등록 = 테이블그룹_등록_요청(중복_테이블_사용한_테이블그룹);
        테이블그룹_등록_실패됨(중복_테이블_사용한_테이블그룹_등록);

        OrderTableResponse 비어있지_않은_테이블 = 테이블_등록되어_있음(4, false).as(OrderTableResponse.class);
        TableGroupRequest 비어있지_않은_테이블_사용한_테이블그룹 = TableGroupRequest.from(Arrays.asList(테이블1.getId(), 비어있지_않은_테이블.getId()));
        ExtractableResponse<Response> 비어있지_않은_테이블_사용한_테이블그룹_등록 = 테이블그룹_등록_요청(비어있지_않은_테이블_사용한_테이블그룹);
        테이블그룹_등록_실패됨(비어있지_않은_테이블_사용한_테이블그룹_등록);

        ExtractableResponse<Response> 테이블그룹_등록 = 테이블그룹_등록_요청(테이블그룹);
        OrderTableResponse 테이블3 = 테이블_등록되어_있음(4, false).as(OrderTableResponse.class);
        TableGroupRequest 이미_등록된_테이블_사용한_테이블그룹 = TableGroupRequest.from(Arrays.asList(테이블1.getId(), 테이블3.getId()));
        ExtractableResponse<Response> 이미_등록된_테이블_사용한_테이블그룹_등록 = 테이블그룹_등록_요청(이미_등록된_테이블_사용한_테이블그룹);
        테이블그룹_등록_실패됨(이미_등록된_테이블_사용한_테이블그룹_등록);
    }
}
