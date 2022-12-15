package kitchenpos.menugroup.acceptance;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("메뉴 그룹 관련 기능")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹 기능 통합 테스트")
    @TestFactory
    Stream<DynamicNode> menuGroup() {
        return Stream.of(
                dynamicTest("메뉴그룹을 등록한다.", () -> {
                    // given
                    MenuGroupRequest 정상_메뉴그룹 = MenuGroupRequest.of("중식");
                    // when
                    ExtractableResponse<Response> 메뉴그룹_생성_결과 = 메뉴그룹_생성_요청(정상_메뉴그룹);
                    // then
                    메뉴그룹_정상_생성됨(메뉴그룹_생성_결과);
                }),
                dynamicTest("이름이 없는 메뉴그룹을 등록할 수 없다.", () -> {
                    // given
                    MenuGroupRequest 이름_없는_메뉴그룹 = MenuGroupRequest.of("");
                    // when
                    ExtractableResponse<Response> 이름없는_메뉴그룹_생성_결과 = 메뉴그룹_생성_요청(이름_없는_메뉴그룹);
                    // then
                    메뉴그룹_생성_실패됨(이름없는_메뉴그룹_생성_결과);
                }),
                dynamicTest("메뉴그룹룹 목록을 조회한다.", () -> {
                    // given
                    메뉴그룹_등록됨("한식");
                    메뉴그룹_등록됨("양식");
                    // when
                    ExtractableResponse<Response> 메뉴그룹_목록 = 메뉴그룹_목록_조회_요청();
                    // then
                    메뉴그룹_목록_정상_응답됨(메뉴그룹_목록, "한식", "양식");
                })
        );
    }

    public static MenuGroupResponse 메뉴그룹_등록됨(String name){
        return 메뉴그룹_생성_요청(MenuGroupRequest.of(name)).as(MenuGroupResponse.class);
    }

    public static ExtractableResponse<Response> 메뉴그룹_생성_요청(MenuGroupRequest menuGroupRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroupRequest)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 메뉴그룹_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }
    private void 메뉴그룹_정상_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
    private void 메뉴그룹_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 메뉴그룹_목록_정상_응답됨(ExtractableResponse<Response> response, String... 메뉴그룹_목록) {
        List<String> 메뉴그룹_조회_결과_목록 = response.jsonPath()
                .getList(".", MenuGroupResponse.class)
                .stream()
                .map(MenuGroupResponse::getName)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(메뉴그룹_조회_결과_목록).containsAll(Arrays.asList(메뉴그룹_목록))
        );
    }

}
