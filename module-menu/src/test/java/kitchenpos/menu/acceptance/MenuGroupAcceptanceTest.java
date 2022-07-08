package kitchenpos.menu.acceptance;

import acceptance.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static acceptance.MenuGroupAcceptanceMethods.*;

@DisplayName("메뉴그룹 관련 기능 인수테스트")
public class MenuGroupAcceptanceTest extends AcceptanceTest {
    private MenuGroupRequest 한식_menuGroupRequest;
    private MenuGroupRequest 양식_menuGroupRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();

        한식_menuGroupRequest = MenuGroupRequest.from("한식");
        양식_menuGroupRequest = MenuGroupRequest.from("양식");
    }

    /**
     * Feature: 메뉴그룹 관련 기능
     *
     *   Scenario: 메뉴그룹을 관리
     *     When 한식 메뉴그룹 등록 요청
     *     Then 한식 메뉴그룹 등록됨
     *     When 양식 메뉴그룹 등록 요청
     *     Then 양식 메뉴그룹 등록됨
     *     When 메뉴그룹 조회 요청
     *     Then 한식, 양식 메뉴그룹 조회됨
     */
    @DisplayName("메뉴그룹을 관리한다")
    @Test
    void 메뉴그룹_관리_정상_시나리오() {
        ExtractableResponse<Response> 한식_등록 = 메뉴그룹_등록_요청(한식_menuGroupRequest);
        메뉴그룹_등록됨(한식_등록);

        ExtractableResponse<Response> 양식_등록 = 메뉴그룹_등록_요청(양식_menuGroupRequest);
        메뉴그룹_등록됨(양식_등록);

        ExtractableResponse<Response> 메뉴그룹_목록_조회 = 메뉴그룹_목록_조회_요청();
        메뉴그룹_목록_응답됨(메뉴그룹_목록_조회);
        메뉴그룹_목록_포함됨(메뉴그룹_목록_조회, Arrays.asList(한식_등록, 양식_등록));
    }
}
