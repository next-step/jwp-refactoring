package kitchenpos.menu.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static kitchenpos.menu.acceptance.MenuGroupRestAssured.메뉴그룹_생성_요청;
import static kitchenpos.menu.acceptance.MenuGroupRestAssured.메뉴그룹_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    /**
     * When 메뉴 그룹 생성을 요청하면
     * Then 메뉴 그룹이 생성된다.
     */
    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenu() {
        // when
        ExtractableResponse<Response> response = 메뉴그룹_생성_요청("양식");

        // then
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
    }

    /**
     * When 메뉴 그룹의 이름을 빈 값으로 하여 메뉴 그룹 생성을 요청하면
     * Then 메뉴 그룹을 생성할 수 없다.
     */
    @DisplayName("빈 값의 이름을 입력하여 메뉴 그룹을 생성한다.")
    @Test
    void createMenuWithNullName() {
        // when
        ExtractableResponse<Response> response = 메뉴그룹_생성_요청(null);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹을 등록하고
     * When 메뉴 그룹 목록 조회를 요청하면
     * Then 메뉴 그룹 목록이 조회된다.
     */
    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        // given
        메뉴그룹_생성_요청("양식");
        메뉴그룹_생성_요청("중식");

        // when
        ExtractableResponse<Response> response = 메뉴그룹_조회_요청();

        // then
        assertThat(response.jsonPath().getList(".", MenuGroupResponse.class)).hasSize(2);
    }
}
