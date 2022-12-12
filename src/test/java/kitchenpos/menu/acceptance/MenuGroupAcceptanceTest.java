package kitchenpos.menu.acceptance;

import static kitchenpos.menu.acceptance.MenuGroupRestAssured.메뉴_그룹_등록되어_있음;
import static kitchenpos.menu.acceptance.MenuGroupRestAssured.메뉴_그룹_목록_조회_요청;
import static kitchenpos.menu.acceptance.MenuGroupRestAssured.메뉴_그룹_생성_요청;
import static kitchenpos.menu.domain.MenuGroupTestFixture.generateMenuGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.acceptance.AcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴 그룹 관련 인수 테스트")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    private MenuGroupRequest 햄버거세트;
    private MenuGroupRequest 햄버거단품;

    @BeforeEach
    public void setUp() {
        super.setUp();
        햄버거세트 = generateMenuGroupRequest("햄버거세트");
        햄버거단품 = generateMenuGroupRequest("햄버거단품");
    }

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenuGroup() {
        // when
        ExtractableResponse<Response> response = 메뉴_그룹_생성_요청(햄버거세트);

        // then
        메뉴_그룹_생성됨(response);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void findAllMenuGroups() {
        // given
        ExtractableResponse<Response> createResponse1 = 메뉴_그룹_등록되어_있음(햄버거세트);
        ExtractableResponse<Response> createResponse2 = 메뉴_그룹_등록되어_있음(햄버거단품);

        // when
        ExtractableResponse<Response> response = 메뉴_그룹_목록_조회_요청();

        // then
        메뉴_그룹_목록_응답됨(response);
        메뉴_그룹_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    private static void 메뉴_그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private static void 메뉴_그룹_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 메뉴_그룹_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedMenuGroupIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultMenuGroupIds = response.jsonPath().getList(".", MenuGroupResponse.class).stream()
                .map(MenuGroupResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultMenuGroupIds).containsAll(expectedMenuGroupIds);
    }
}
