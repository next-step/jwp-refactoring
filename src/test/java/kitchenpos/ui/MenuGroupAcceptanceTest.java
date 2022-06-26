package kitchenpos.ui;

import static kitchenpos.helper.AcceptanceApiHelper.MenuGroupApiHelper.메뉴그룹_등록하기;
import static kitchenpos.helper.AcceptanceApiHelper.MenuGroupApiHelper.메뉴그룹_리스트_조회하기;
import static kitchenpos.helper.AcceptanceAssertionHelper.MenuGroupAssertionHelper.메뉴그룹_리스트_조회됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.helper.AcceptanceAssertionHelper.MenuGroupAssertionHelper;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = {"/test/db/cleanUp.sql"})
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    /**
     * given : 메뉴그룹 정보를 생성하고
     *  when : 저장하면
     *  then : 정상적으로 저장된다.
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
     * given : 메뉴그룹 정보를 3개 저장후
     *  when : 메뉴그룹 리스트를 조회시
     *  then : 정상적으로 조회된다.
     */
    @Test
    public void 메뉴그룹_조회하기_테스트() {
        //given
        MenuGroup 반반세트 = 메뉴그룹_등록하기("반반세트").as(MenuGroup.class);
        MenuGroup 큰세트 = 메뉴그룹_등록하기("큰세트").as(MenuGroup.class);
        MenuGroup 반마리세트 = 메뉴그룹_등록하기("반마리세트").as(MenuGroup.class);

        //when
        ExtractableResponse<Response> 메뉴그룹_리스트_조회하기_response = 메뉴그룹_리스트_조회하기();

        //then
        메뉴그룹_리스트_조회됨(메뉴그룹_리스트_조회하기_response, Arrays.asList(반반세트, 큰세트, 반마리세트));
    }
}