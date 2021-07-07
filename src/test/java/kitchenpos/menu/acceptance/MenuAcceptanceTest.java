package kitchenpos.menu.acceptance;

import static kitchenpos.utils.acceptan.RequestHelper.*;
import static kitchenpos.utils.acceptan.ResponseHelper.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.AcceptanceTest;

@DisplayName("메뉴 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    private static final String 메뉴_그룹_이름_국밥 = "국밥";
    private static final String 상품_이름_순대 = "순대";
    private static final int 상품_가격 = 8000;
    public static final String 메뉴_이름_순대국 = "순대국";
    public static final int 메뉴_가격 = 8000;
    public static final Long 상품_수량 = 1L;

    private Long 메뉴_그룹_번호;
    private Long 상품_번호;

    @BeforeEach
    void setup() {
        ExtractableResponse<Response> 메뉴_그룹_생성_요청_응답 = 메뉴_그룹_생성_요청(메뉴_그룹_이름_국밥);
        ExtractableResponse<Response> 상품_생성_요청_응답 = 상품_생성_요청(상품_이름_순대, 상품_가격);

        메뉴_그룹_번호 = 메뉴_그룹_번호_추출(메뉴_그룹_생성_요청_응답);
        상품_번호 = 상품_번호_추출(상품_생성_요청_응답);
    }

    @DisplayName("사용자는 메뉴를 생성 할 수 있다.")
    @Test
    void create() {
        // given
        // when
        ExtractableResponse<Response> 메뉴_그룹_생성_요청_응답 = 메뉴_생성_요청(메뉴_이름_순대국, 메뉴_가격, 메뉴_그룹_번호, 상품_번호, 상품_수량);

        // then
        메뉴_그룹_생성_요청_응답_확인(메뉴_그룹_생성_요청_응답);
    }

    @DisplayName("사용자는 메뉴 전체를 조회 할 수 있다.")
    @Test
    void list() {
        // given
        메뉴_생성_요청(메뉴_이름_순대국, 메뉴_가격, 메뉴_그룹_번호, 상품_번호, 상품_수량);
        // when
        ExtractableResponse<Response> 메뉴_조회_요청_응답 = 메뉴_조회_요청();
        // then
        메뉴_조회_요청_응답_확인(메뉴_조회_요청_응답, 메뉴_이름_순대국, 상품_수량, 상품_가격, 상품_이름_순대, 메뉴_그룹_이름_국밥);
    }
}
