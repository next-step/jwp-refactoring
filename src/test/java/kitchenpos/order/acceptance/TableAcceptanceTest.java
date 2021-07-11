package kitchenpos.order.acceptance;

import static kitchenpos.utils.acceptan.RequestHelper.*;
import static kitchenpos.utils.acceptan.ResponseHelper.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import kitchenpos.AcceptanceTest;
import kitchenpos.order.domain.OrderStatus;

@DisplayName("주문 테이블 인수 테스트")
public class TableAcceptanceTest extends AcceptanceTest {

    private static final String 메뉴_그룹_이름_국밥 = "국밥";
    private static final String 상품_이름_순대 = "순대";
    private static final int 상품_가격 = 8000;
    public static final String 메뉴_이름_순대국 = "순대국";
    public static final int 메뉴_가격 = 8000;
    public static final Long 상품_수량 = 1L;
    public static final int 고객_수_2명 = 2;
    public static final int 고객_수_5명 = 5;
    public static final boolean 비어있음 = true;
    public static final boolean 비어있지_않음 = false;

    private Long 메뉴_그룹_번호;
    private Long 상품_번호;
    private Long 주문_테이블_번호;
    private Long 메뉴_번호;
    private Long 주문_번호;

    @BeforeEach
    void setup() {
        ExtractableResponse<Response> 메뉴_그룹_생성_요청_응답 = 메뉴_그룹_생성_요청(메뉴_그룹_이름_국밥);
        메뉴_그룹_번호 = 메뉴_그룹_번호_추출(메뉴_그룹_생성_요청_응답);

        ExtractableResponse<Response> 상품_생성_요청_응답 = 상품_생성_요청(상품_이름_순대, 상품_가격);
        상품_번호 = 공통_번호_추출(상품_생성_요청_응답);

        ExtractableResponse<Response> 메뉴_생성_요청 = 메뉴_생성_요청(메뉴_이름_순대국, 메뉴_가격, 메뉴_그룹_번호, 상품_번호, 상품_수량);
        메뉴_번호 = 공통_번호_추출(메뉴_생성_요청);

        ExtractableResponse<Response> 주문_테이블_생성_요청_응답 = 주문_테이블_생성_요청(고객_수_2명, 비어있지_않음);
        주문_테이블_번호 = 공통_번호_추출(주문_테이블_생성_요청_응답);

        ExtractableResponse<Response> 주문_생성_요청_응답 = 주문_생성_요청(주문_테이블_번호, 메뉴_번호, 1);
        주문_번호 = 공통_번호_추출(주문_생성_요청_응답);
    }

    @DisplayName("사용자는 주문 테이블을 생성 할 수 있다.")
    @Test
    void create() {
        // given
        // when
        ExtractableResponse<Response> 주문_테이블_생성_요청_응답 = 주문_테이블_생성_요청(고객_수_2명, 비어있지_않음);
        // then
        생성_요청_확인(주문_테이블_생성_요청_응답);
    }

    @DisplayName("사용자는 주문 테이블 전체를 조회 할 수 있다.")
    @Test
    void list() {
        // given
        주문_테이블_생성_요청(고객_수_2명, 비어있지_않음);
        // when
        ExtractableResponse<Response> 주문_테이블_조회_요청_응답 = 주문_테이블_조회_요청();
        // then
        주문_테이블_조회_요청_응답_확인(주문_테이블_조회_요청_응답);
    }

    @DisplayName("사용자는 테이블 비어 있음을 변경 할 수 있다.")
    @Test
    void changeOrderTableEmpty() {
        // given
        주문_상태_변경_요청(주문_번호, OrderStatus.COMPLETION);
        // when
        ExtractableResponse<Response> 주문_테이블_비어있음_요청_응답 = 주문_테이블_비어있음_요청(주문_테이블_번호, 비어있음);
        // then
        주문_테이블_비어있음_요청_응답_확인(주문_테이블_비어있음_요청_응답);
    }

    @DisplayName("테이블 비어 있음 변경 실패 - 주문의 상태가 COOKING")
    @Test
    void changeOrderTableEmptyFailedByOrderStatus() {
        // given
        // when
        ExtractableResponse<Response> 주문_테이블_비어있음_요청_응답_실패 = 주문_테이블_비어있음_요청(주문_테이블_번호, 비어있음);
        // then
        요청_실패_확인(주문_테이블_비어있음_요청_응답_실패);
    }

    @DisplayName("사용자는 고객수를 변경 할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        // when
        ExtractableResponse<Response> 주문_테이블_고객수_변경_요청_응답 = 주문_테이블_고객수_변경_요청(주문_테이블_번호, 고객_수_5명);
        // then
        주문_테이블_고객수_변경_요청_응답_확인(주문_테이블_고객수_변경_요청_응답, 고객_수_5명);
    }

    @DisplayName("고객 수 변경 실패 - 고객 수가 음수")
    @Test
    void changeNumberOfGuestsFailedByNotFoundOrderTable() {
        // given
        // when
        ExtractableResponse<Response> 주문_테이블_고객수_변경_실패 = 주문_테이블_고객수_변경_요청(주문_테이블_번호, -5);
        // then
        요청_실패_확인(주문_테이블_고객수_변경_실패);
    }
}
