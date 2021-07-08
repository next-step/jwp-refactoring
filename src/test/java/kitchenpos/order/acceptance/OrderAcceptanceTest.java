package kitchenpos.order.acceptance;

import static kitchenpos.utils.acceptan.RequestHelper.*;
import static kitchenpos.utils.acceptan.ResponseHelper.*;


import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.AcceptanceTest;
import kitchenpos.order.domain.OrderStatus;

@DisplayName("주문 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    private static final String 메뉴_그룹_이름_국밥 = "국밥";
    private static final String 상품_이름_순대 = "순대";
    private static final int 상품_가격 = 8000;
    public static final String 메뉴_이름_순대국 = "순대국";
    public static final int 메뉴_가격 = 8000;
    public static final Long 상품_수량 = 1L;
    public static final int 손님_수 = 1;
    public static final boolean 비어있음_여부 = true;

    private Long 메뉴_그룹_번호;
    private Long 상품_번호;
    private Long 주문_테이블_번호;
    private Long 메뉴_번호;

    @BeforeEach
    void setup() {
        ExtractableResponse<Response> 메뉴_그룹_생성_요청_응답 = 메뉴_그룹_생성_요청(메뉴_그룹_이름_국밥);
        메뉴_그룹_번호 = 메뉴_그룹_번호_추출(메뉴_그룹_생성_요청_응답);

        ExtractableResponse<Response> 상품_생성_요청_응답 = 상품_생성_요청(상품_이름_순대, 상품_가격);
        상품_번호 = 공통_번호_추출(상품_생성_요청_응답);

        ExtractableResponse<Response> 주문_테이블_생성_요청_응답 = 주문_테이블_생성_요청(손님_수, 비어있음_여부);
        주문_테이블_번호 = 공통_번호_추출(주문_테이블_생성_요청_응답);

        ExtractableResponse<Response> 메뉴_생성_요청 = 메뉴_생성_요청(메뉴_이름_순대국, 메뉴_가격, 메뉴_그룹_번호, 상품_번호, 상품_수량);
        메뉴_번호 = 공통_번호_추출(메뉴_생성_요청);
    }

    @DisplayName("사용자는 주문을 생성 할 수 있다.")
    @Test
    void create() {
        // given
        // when
        ExtractableResponse<Response> 주문_생성_요청_응답 = 주문_생성_요청(주문_테이블_번호, 메뉴_번호, 1);
        // then
        주문_생성_요청_응답_확인(주문_생성_요청_응답);
    }

    @DisplayName("사용자는 주문 전체를 조회 할 수 있다.")
    @Test
    void list() {
        // given
        주문_생성_요청(주문_테이블_번호, 메뉴_번호, 1);
        // when
        ExtractableResponse<Response> 주문_조회_요청_응답 = 주문_조회_요청();
        // then
        주문_조회_요청_응답_확인(주문_조회_요청_응답);
    }

    @DisplayName("사용자는 주문 상태를 변경 할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given

        // when
        ExtractableResponse<Response> 주문_생성_요청_응답 = 주문_생성_요청(주문_테이블_번호, 메뉴_번호, 1);
        // then
        주문_생성_요청_응답_확인(주문_생성_요청_응답);
        Long 주문_번호 = 공통_번호_추출(주문_생성_요청_응답);

        //when
        ExtractableResponse<Response> 주문_상태_변경_요청_응답 = 주문_상태_변경_요청(주문_번호, OrderStatus.COMPLETION);
        주문_상태_변경_확인(주문_상태_변경_요청_응답, OrderStatus.COMPLETION);
    }

    @DisplayName("주문 생성 실패 - 주문 테이블이 없음")
    @Test
    void createFailedByOrderTable() {
        // given
        // when
        ExtractableResponse<Response> 주문_생성_요청_응답 = 주문_생성_요청(0L, 메뉴_번호, 1);
        // then
        요청_실패_확인(주문_생성_요청_응답);
    }

    @DisplayName("주문 생성 실패 - 메뉴가 없음")
    @Test
    void createFailedByMenu() {
        // given
        // when
        ExtractableResponse<Response> 주문_생성_요청_응답 = 주문_생성_요청(주문_테이블_번호, 0L, 1);
        // then
        요청_실패_확인(주문_생성_요청_응답);
    }

    @DisplayName("주문 생성 실패 - 메뉴 수량이 음수")
    @Test
    void creatFailedByAmount() {
        // given
        // when
        ExtractableResponse<Response> 주문_생성_요청_응답 = 주문_생성_요청(주문_테이블_번호, 메뉴_번호, -1);
        // then
        요청_실패_확인(주문_생성_요청_응답);
    }
}
