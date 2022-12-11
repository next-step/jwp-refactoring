package kitchenpos.acceptance;


import static kitchenpos.acceptance.TableAcceptanceTest.주문테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.Test;
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
                    MenuGroup 정상_메뉴그룹 = new MenuGroup(null, "중식");
                    // when
                    ExtractableResponse<Response> 메뉴그룹_생성_결과 = 메뉴그룹_생성_요청(정상_메뉴그룹);
                    // then
                    메뉴그룹_정상_생성됨(메뉴그룹_생성_결과);
                }),
                dynamicTest("이름이 없는 메뉴그룹을 등록할 수 없다.", () -> {
                    // given
                    MenuGroup 이름_없는_메뉴그룹 = new MenuGroup(null, null);
                    // when
                    ExtractableResponse<Response> 이름없는_메뉴그룹_생성_결과 = 메뉴그룹_생성_요청(이름_없는_메뉴그룹);
                    // then
                    메뉴그룹_생성_실패됨(이름없는_메뉴그룹_생성_결과);
                }),
                dynamicTest("메뉴그룹룹 목록을 조회한다.", () -> {
                    // when
                    ExtractableResponse<Response> 메뉴그룹_목록 = 메뉴그룹_목록_조회_요청();
                    // then
                    메뉴그룹_목록_정상_응답됨(메뉴그룹_목록, "중식");
                })
        );
    }

    public static MenuGroup 메뉴그룹_등록됨(String name){
        return 메뉴그룹_생성_요청(new MenuGroup(null, name)).as(MenuGroup.class);
    }

    public static ExtractableResponse<Response> 메뉴그룹_생성_요청(MenuGroup menuGroup) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroup)
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 메뉴그룹_목록_정상_응답됨(ExtractableResponse<Response> response, String... 메뉴그룹_목록) {
        List<String> 메뉴그룹_조회_결과_목록 = response.jsonPath()
                .getList(".", MenuGroup.class)
                .stream()
                .map(MenuGroup::getName)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(메뉴그룹_조회_결과_목록).containsAll(Arrays.asList(메뉴그룹_목록))
        );
    }

}
