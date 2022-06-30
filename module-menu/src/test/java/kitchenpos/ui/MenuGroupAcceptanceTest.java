package kitchenpos.ui;

import static kitchenpos.helper.AcceptanceApiHelper.MenuGroupApiHelper.메뉴그룹_등록하기;
import static kitchenpos.helper.AcceptanceApiHelper.MenuGroupApiHelper.메뉴그룹_리스트_조회하기;
import static kitchenpos.helper.AcceptanceAssertionHelper.MenuGroupAssertionHelper.메뉴그룹_리스트_조회됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.AcceptanceTest;
import kitchenpos.helper.AcceptanceAssertionHelper.MenuGroupAssertionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class MenuGroupAcceptanceTest extends AcceptanceTest {

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
     * given : 메뉴그룹 정보를 생성하고 when : 저장하면 then : 정상적으로 저장된다.
     */
    @Test
    public void 메뉴그룹_생성하기_테스트() {
        //given
        String 메뉴그룹_이름 = "반반세트";

        //when
        ExtractableResponse<Response> 메뉴그룹_등록하기_response = 메뉴그룹_등록하기(메뉴그룹_이름);

        //then
        MenuGroupAssertionHelper.메뉴그룹_등록되어있음(메뉴그룹_등록하기_response, 메뉴그룹_이름);
    }

    /**
     * background given : 메뉴그룹 3개를 저장 후 두마리메뉴 세마리메뉴 반반메뉴 when : 메뉴그룹 리스트를 조회시 then : 정상적으로 조회된다.
     */
    @Test
    public void 메뉴그룹_조회하기_테스트() {
        //when
        ExtractableResponse<Response> 메뉴그룹_리스트_조회하기_response = 메뉴그룹_리스트_조회하기();

        //then
        메뉴그룹_리스트_조회됨(메뉴그룹_리스트_조회하기_response, Arrays.asList(두마리메뉴, 세마리메뉴, 반반메뉴));
    }
}