package kitchenpos.application;


import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴그룹 테스트")
class MenuGroupServiceAcceptanceTest extends AcceptanceTest {


    @DisplayName("메뉴그룹을 등록한다")
    @Test
    void createTest() {

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("치킨");
        ExtractableResponse<Response> createResponse = MenuGroupFactory.메뉴그룹_생성_요청(menuGroup);
        MenuGroup createdMenuGroup = 메뉴그룹이_생성됨(createResponse);

    }

    @DisplayName("메뉴그룹을 조회한다")
    @Test
    void getListTest() {

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("치킨");

        ExtractableResponse<Response> createResponse = MenuGroupFactory.메뉴그룹_생성_요청(menuGroup);
        MenuGroup createdMenuGroup = 메뉴그룹이_생성됨(createResponse);
        ExtractableResponse<Response> getResponse = MenuGroupFactory.메뉴그룹_조회_요청();
        List<MenuGroup> menuGroups = Arrays.asList(getResponse.as(MenuGroup[].class));
        assertThat(menuGroups).contains(createdMenuGroup);
    }

    public static MenuGroup 메뉴그룹이_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(MenuGroup.class);
    }
}
