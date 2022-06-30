package kitchenpos.ui;

import static kitchenpos.helper.AcceptanceApiHelper.OrderApiHelper.주문_상태_변경하기;
import static kitchenpos.helper.AcceptanceApiHelper.OrderApiHelper.주문_생성하기;
import static kitchenpos.helper.AcceptanceApiHelper.TableApiHelper.빈테이블_생성하기;
import static kitchenpos.helper.AcceptanceApiHelper.TableApiHelper.유휴테이블_여부_설정하기;
import static kitchenpos.helper.AcceptanceApiHelper.TableApiHelper.테이블_리스트_조회하기;
import static kitchenpos.helper.AcceptanceApiHelper.TableApiHelper.테이블_손님_인원_설정하기;
import static kitchenpos.helper.AcceptanceAssertionHelper.TableAssertionHelper.테이블_리스트_조회됨;
import static kitchenpos.helper.AcceptanceAssertionHelper.TableAssertionHelper.테이블_손님수_설정_에러;
import static kitchenpos.helper.AcceptanceAssertionHelper.TableAssertionHelper.테이블_손님수_설정됨;
import static kitchenpos.helper.AcceptanceAssertionHelper.TableAssertionHelper.테이블_유휴여부_설정_에러;
import static kitchenpos.helper.AcceptanceAssertionHelper.TableAssertionHelper.테이블_유휴여부_설정됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.AcceptanceTest;
import kitchenpos.helper.AcceptanceAssertionHelper.TableAssertionHelper;
import kitchenpos.helper.testDTO.response.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class TableAcceptanceTest extends AcceptanceTest {

    public static final String 비어있음 = "true";
    public static final String 사용중 = "false";

    /**
     * 테이블 테이블_1 : 사용중 , 2명 테이블_2 : 사용중 , 3명 빈테이블_1 : 미사용중, 0명 빈테이블_2 : 미사용중, 0명 메뉴그룹 두마리메뉴 세마리메뉴
     * 반반메뉴 상품 후라이드 : 17000원 양념 : 15000원 메뉴 양념두마리메뉴 : 2222원, 두마리메뉴(그룹명), 양념한마리 양념세마리메뉴 : 3333원,
     * 세마리메뉴(그룹명), 양념세마리 반반메뉴 : 1111원, 반반메뉴(그룹명), 양념한마리&후라이드한마리 주문 양념두마리메뉴,2개
     */

    @BeforeEach
    public void init() {
        super.init();
    }

    /**
     * when : 빈 테이블 저장시 then : 성공적으로 저장되고 테이블 정보가 반환된다.
     */
    @Test
    public void 테이블_생성하기_테스트() {
        //when
        ExtractableResponse<Response> 테이블_등록하기_response = 빈테이블_생성하기();

        //then
        TableAssertionHelper.테이블_등록되어있음(테이블_등록하기_response);
    }


    /**
     * background given : 테이블 4개 정보를 생성한뒤 테이블_1 : 사용중 , 2명 테이블_2 : 사용중 , 3명 빈테이블_1 : 미사용중, 0명 빈테이블_2
     * : 미사용중, 0명 when : 테이블 리스트를 조회하면 then : 정상적으로 조회가 된다.
     */
    @Test
    public void 테이블_리스트_조회하기_테스트() {
        //when
        ExtractableResponse<Response> 테이블_리스트_조회하기_response = 테이블_리스트_조회하기();

        //then
        테이블_리스트_조회됨(테이블_리스트_조회하기_response, Arrays.asList(테이블_1, 테이블_2, 빈테이블_1, 빈테이블_2));
    }

    /**
     * background given : 인원이 없는 테이블 1개 저장후 빈테이블_1 : 미사용중, 0명 when : 테이블을 빈 테이블로 설정하면 then : 정상적으로
     * 빈테이블로 설정된다.
     */
    @Test
    public void 유휴테이블_설정하기_테스트() {
        //when
        ExtractableResponse<Response> 유휴테이블_여부_설정하기_response = 유휴테이블_여부_설정하기(비어있음,
            빈테이블_1.getId());

        //then
        테이블_유휴여부_설정됨(유휴테이블_여부_설정하기_response, 비어있음);
    }

    /**
     * background given : 인원이 없는 테이블 1개 저장후 빈테이블_1 : 미사용중, 0명 given : 테이블을 사용중으로 변경 후 when : 테이블의 손님
     * 수를 설정하면 then : 정상적으로 손님 수가 설정된다.
     */
    @Test
    public void 테이블_손님수_설정하기_테스트() {
        //given
        int 손님수 = 4;
        유휴테이블_여부_설정하기(사용중, 빈테이블_1.getId());

        //when
        ExtractableResponse<Response> 테이블_손님_인원_설정하기_response = 테이블_손님_인원_설정하기(손님수,
            빈테이블_1.getId());

        //then
        테이블_손님수_설정됨(테이블_손님_인원_설정하기_response, 손님수);
    }

    /**
     * background given : 인원이 없는 테이블 1개 저장후 빈테이블_1 : 미사용중, 0명 when : 테이블의 손님 수를 설정하면 then : 에러가
     * 발생한다
     */
    @Test
    public void 테이블_손님수_설정_빈테이블일때_에러발생_테스트() {
        //given
        int 손님수 = 4;

        //when
        ExtractableResponse<Response> 테이블_손님_인원_설정하기_response = 테이블_손님_인원_설정하기(손님수,
            빈테이블_1.getId());

        //then
        테이블_손님수_설정_에러(테이블_손님_인원_설정하기_response);
    }

    /**
     * background given : 인원이 없는 테이블 1개 저장후 빈테이블_1 : 미사용중, 0명 given : 테이블을 사용중으로 변경 후 when : 테이블의 손님
     * 수를 음수로 설정하면 then : 에러가 발생한다
     */
    @Test
    public void 테이블_손님수_설정_손님_음수일때_에러발생_테스트() {
        //given
        int 손님수 = -1;
        유휴테이블_여부_설정하기(사용중, 빈테이블_1.getId());

        //when
        ExtractableResponse<Response> 테이블_손님_인원_설정하기_response = 테이블_손님_인원_설정하기(손님수,
            빈테이블_1.getId());

        //then
        테이블_손님수_설정_에러(테이블_손님_인원_설정하기_response);
    }

    /**
     * background given : 메뉴를 생성하고 given : 주문을 생성하여 given : 테이블을 생성 후 빈테이블_1 : 미사용중, 0명 given : 해당
     * 테이블에 주문을 매핑 후 when : 테이블 빈테이블 설정시 then : 에러발생
     */
    @Test
    public void 테이블_먹고있거나_요리중에_빈테이블_삭제시_에러발생_테스트() {

        유휴테이블_여부_설정하기(사용중, 빈테이블_1.getId());
        //given(테이블-주문 매핑)
        주문_생성하기(빈테이블_1.getId(), Arrays.asList(주문)).as(OrderResponse.class);
        주문_상태_변경하기(먹는중, 빈테이블_1.getId());

        //when
        ExtractableResponse<Response> 유휴테이블_여부_설정하기_response = 유휴테이블_여부_설정하기(비어있음,
            빈테이블_1.getId());

        //then
        테이블_유휴여부_설정_에러(유휴테이블_여부_설정하기_response);
    }

}