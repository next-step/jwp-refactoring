package kitchenpos.menu.acceptance.step;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.function.Consumer;

import static kitchenpos.utils.RestAssuredUtil.get;
import static kitchenpos.utils.RestAssuredUtil.post;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MenuGroupAcceptStep {
    private static final String BASE_URL = "/api/menu-groups";

    public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(MenuGroupRequest 등록_요청_데이터) {
        return post(BASE_URL, 등록_요청_데이터);
    }

    public static MenuGroupResponse 메뉴_그룹이_등록되어_있음(String name) {
        MenuGroupRequest 등록_요청_데이터 = MenuGroupRequest.of(name);

        return 메뉴_그룹_등록_요청(등록_요청_데이터).as(MenuGroupResponse.class);
    }

    public static MenuGroupResponse 메뉴_그룹_등록_확인(ExtractableResponse<Response> 메뉴_그룹_등록_응답, MenuGroupRequest 등록_요청_데이터) {
        MenuGroupResponse 등록된_메뉴_그룹 = 메뉴_그룹_등록_응답.as(MenuGroupResponse.class);

        assertAll(
                () -> assertThat(메뉴_그룹_등록_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(등록된_메뉴_그룹).satisfies(등록된_메뉴_그룹_확인(등록_요청_데이터))
        );

        return 등록된_메뉴_그룹;
    }

    private static Consumer<MenuGroupResponse> 등록된_메뉴_그룹_확인(MenuGroupRequest 등록_요청_데이터) {
        return menuGroupResponse -> {
            assertThat(menuGroupResponse.getId()).isNotNull();
            assertThat(menuGroupResponse.getName()).isEqualTo(등록_요청_데이터.getName());
        };
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return get(BASE_URL);
    }

    public static void 메뉴_그룹_조회_확인(ExtractableResponse<Response> 메뉴_그룹_목록_조회_응답, MenuGroupResponse 등록된_메뉴_그룹) {
        List<MenuGroupResponse> 조회된_메뉴_그룹_목록 = 메뉴_그룹_목록_조회_응답.as(new TypeRef<List<MenuGroupResponse>>() {
        });

        assertAll(
                () -> assertThat(메뉴_그룹_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(조회된_메뉴_그룹_목록).satisfies(조회된_메뉴_그룹_목록_확인(등록된_메뉴_그룹))
        );
    }

    private static Consumer<List<? extends MenuGroupResponse>> 조회된_메뉴_그룹_목록_확인(MenuGroupResponse 등록된_메뉴_그룹) {
        return menuGroupResponses -> {
            assertThat(menuGroupResponses.size()).isOne();
            assertThat(menuGroupResponses).first()
                    .satisfies(menuGroupResponse -> {
                        assertThat(menuGroupResponse.getId()).isEqualTo(등록된_메뉴_그룹.getId());
                        assertThat(menuGroupResponse.getName()).isEqualTo(등록된_메뉴_그룹.getName());
                    });
        };
    }
}
