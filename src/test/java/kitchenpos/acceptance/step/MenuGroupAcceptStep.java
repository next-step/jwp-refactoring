package kitchenpos.acceptance.step;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.function.Consumer;

import static kitchenpos.utils.RestAssuredUtil.get;
import static kitchenpos.utils.RestAssuredUtil.post;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MenuGroupAcceptStep {
    private static final String BASE_URL = "/api/menu-groups";

    public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(MenuGroup 등록_요청_데이터) {
        return post(BASE_URL, 등록_요청_데이터);
    }

    public static MenuGroup 메뉴_그룹이_등록되어_있음(String name) {
        MenuGroup 등록_요청_데이터 = new MenuGroup();
        등록_요청_데이터.setName(name);

        return 메뉴_그룹_등록_요청(등록_요청_데이터).as(MenuGroup.class);
    }

    public static MenuGroup 메뉴_그룹_등록_확인(ExtractableResponse<Response> 메뉴_그룹_등록_응답, MenuGroup 등록_요청_데이터) {
        MenuGroup 등록된_메뉴_그룹 = 메뉴_그룹_등록_응답.as(MenuGroup.class);

        assertAll(
                () -> assertThat(메뉴_그룹_등록_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(등록된_메뉴_그룹).satisfies(등록된_메뉴_그룹_확인(등록_요청_데이터))
        );

        return 등록된_메뉴_그룹;
    }

    private static Consumer<MenuGroup> 등록된_메뉴_그룹_확인(MenuGroup 등록_요청_데이터) {
        return menuGroup -> {
            assertThat(menuGroup.getId()).isNotNull();
            assertThat(menuGroup.getName()).isEqualTo(등록_요청_데이터.getName());
        };
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return get(BASE_URL);
    }

    public static void 메뉴_그룹_조회_확인(ExtractableResponse<Response> 메뉴_그룹_목록_조회_응답, MenuGroup 등록된_메뉴_그룹) {
        List<MenuGroup> 조회된_메뉴_그룹_목록 = 메뉴_그룹_목록_조회_응답.as(new TypeRef<List<MenuGroup>>() {
        });

        assertAll(
                () -> assertThat(메뉴_그룹_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(조회된_메뉴_그룹_목록).satisfies(조회된_메뉴_그룹_목록_확인(등록된_메뉴_그룹))
        );
    }

    private static Consumer<List<? extends MenuGroup>> 조회된_메뉴_그룹_목록_확인(MenuGroup 등록된_메뉴_그룹) {
        return menuGroups -> {
            assertThat(menuGroups.size()).isOne();
            assertThat(menuGroups).first()
                    .satisfies(menuGroup -> {
                        assertThat(menuGroup.getId()).isEqualTo(등록된_메뉴_그룹.getId());
                        assertThat(menuGroup.getName()).isEqualTo(등록된_메뉴_그룹.getName());
                    });
        };
    }
}
