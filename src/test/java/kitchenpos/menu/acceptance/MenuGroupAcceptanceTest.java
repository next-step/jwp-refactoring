package kitchenpos.menu.acceptance;

import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTestUtils.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.stream.Stream;
import kitchenpos.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

@DisplayName("메뉴 그룹 인수 테스트")
class MenuGroupAcceptanceTest extends AcceptanceTest {
    @DisplayName("메뉴 그룹 인수 테스트")
    @TestFactory
    Stream<DynamicNode> menuGroupAcceptance() {
        return Stream.of(
                dynamicTest("메뉴 그룹을 등록한다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 메뉴_그룹_생성_요청("면류");

                    // then
                    메뉴_그룹_생성됨(response);
                }),
                dynamicTest("메뉴 그룹 목록을 조회하면 메뉴 그룹 목록이 반환된다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 메뉴_그룹_목록_조회_요청();

                    // then
                    메뉴_그룹_목록_조회됨(response);
                    메뉴_그룹_목록_포함됨(response, "면류");
                })
        );
    }
}
