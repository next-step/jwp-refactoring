package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.ui.dto.MenuGroupCreateRequest;
import kitchenpos.menu.ui.dto.MenuGroupCreateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("메뉴 그룹 관리")
public class MenuGroupAcceptanceTest extends AcceptanceTest {
    public static final MenuGroupCreateRequest 세마리메뉴 = new MenuGroupCreateRequest("세마리메뉴");

    @BeforeEach
    public void setUp() {
        super.setUp();
    }
    
    @TestFactory
    Stream<DynamicTest> 메뉴_그룹_관리_시나리오() {
        return Stream.of(
                dynamicTest("메뉴 그룹을 등록한다", this::메뉴_그룹을_등록한다),
                dynamicTest("메뉴 그룹의 목록을 조회한다.", this::메뉴_그룹의_목록을_조회한다)
        );
    }

    private void 메뉴_그룹을_등록한다() {
        // when
        ExtractableResponse<Response> 메뉴_그룹_등록_응답 = 메뉴_그룹_등록_요청(세마리메뉴);

        // then
        메뉴_그룹이_등록됨(메뉴_그룹_등록_응답);
    }

    private void 메뉴_그룹의_목록을_조회한다() {
        // when
        ExtractableResponse<Response> 메뉴_그룹_목록_조회_응답 = 메뉴_그룹_목록_조회_요청();

        // then
        메뉴_그룹_목록_조회됨(메뉴_그룹_목록_조회_응답);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(MenuGroupCreateRequest request) {
        return post("/api/menu-groups", request);
    }

    public static void 메뉴_그룹이_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static MenuGroupCreateResponse 메뉴_그룹_등록됨(MenuGroupCreateRequest request) {
        return 메뉴_그룹_등록_요청(request).as(MenuGroupCreateResponse.class);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return get("/api/menu-groups");
    }

    public static void 메뉴_그룹_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
