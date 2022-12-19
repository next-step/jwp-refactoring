package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 그룹 관련 인수 테스트")
class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        ExtractableResponse<Response> response = 메뉴_그룹_생성_요청(MenuGroupRequest.from("프리미엄 치킨"));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        ExtractableResponse<Response> createResponse1 = 메뉴_그룹_등록되어_있음(MenuGroupRequest.from("프리미엄 치킨"));
        ExtractableResponse<Response> createResponse2 = 메뉴_그룹_등록되어_있음(MenuGroupRequest.from("스탠다드 치킨"));

        ExtractableResponse<Response> listResponse = 메뉴_그룹_목록_조회_요청();

        assertThat(listResponse.statusCode()).isEqualTo(HttpStatus.OK.value());List<MenuGroupResponse> menuGroups = listResponse.jsonPath().getList(".", MenuGroupResponse.class);
        List<MenuGroupResponse> createdMenuGroups = Arrays.asList(createResponse1, createResponse2).stream()
                .map(it -> it.as(MenuGroupResponse.class))
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(menuGroups).hasSize(2),
                () -> assertThat(menuGroups).containsAll(createdMenuGroups)
        );
    }


}
