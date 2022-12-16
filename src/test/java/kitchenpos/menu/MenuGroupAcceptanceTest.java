package kitchenpos.menu;

import static kitchenpos.menu.MenuGroupFixture.메뉴_그룹_등록;
import static kitchenpos.menu.MenuGroupFixture.메뉴_그룹_목록_조회;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    /*
    Feature: 메뉴 그룹을 관리 한다

      Scenario: 메뉴 그룹 관리
        When 메뉴 그룹 등록
        Then 메뉴 그룹 등록됨
        When 메뉴 그룹 목록 조회
        Then 상품 목록 조회됨
     */
    @Test
    void 메뉴_그룹_관리() {
        //when
        ExtractableResponse<Response> createResponse = 메뉴_그룹_등록("추천 메뉴");

        //then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        List<MenuGroupResponse> menuGroups = 메뉴_그룹_목록_조회().jsonPath()
            .getList(".", MenuGroupResponse.class);

        //then
        assertThat(menuGroups)
            .hasSize(1)
            .extracting(MenuGroupResponse::getName)
            .containsExactly("추천 메뉴");
    }

}
