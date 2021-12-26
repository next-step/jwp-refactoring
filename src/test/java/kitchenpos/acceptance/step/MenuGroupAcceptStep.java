package kitchenpos.acceptance.step;

import static kitchenpos.utils.RestAssuredUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.http.HttpStatus;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;

public class MenuGroupAcceptStep {

	private static final String BASE_URL = "/api/menu-groups";

	public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(MenuGroup 생성_요청_메뉴_그룹) {
		return post(BASE_URL, 생성_요청_메뉴_그룹);
	}

	public static MenuGroup 메뉴_그룹_생성_확인(ExtractableResponse<Response> 메뉴_그룹_생성_응답, MenuGroup 생성_요청_메뉴_그룹) {
		MenuGroup 생성된_메뉴_그룹 = 메뉴_그룹_생성_응답.as(MenuGroup.class);
		assertAll(
			() -> assertThat(메뉴_그룹_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(생성된_메뉴_그룹).satisfies(생성된_메뉴_그룹_확인(생성_요청_메뉴_그룹))
		);
		return 생성된_메뉴_그룹;
	}

	private static Consumer<MenuGroup> 생성된_메뉴_그룹_확인(MenuGroup 생성_요청_메뉴_그룹) {
		return menuGroup -> assertAll(
			() -> assertThat(menuGroup.getId()).isNotNull(),
			() -> assertThat(menuGroup.getName()).isEqualTo(생성_요청_메뉴_그룹.getName())
		);
	}

	public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
		return get(BASE_URL);
	}

	public static void 메뉴_그룹_목록_조회_확인(ExtractableResponse<Response> 메뉴_그룹_목록_조회_응답, MenuGroup 생성된_메뉴_그룹) {
		List<MenuGroup> 조회된_메뉴_그룹_목록 = 메뉴_그룹_목록_조회_응답.as(new TypeRef<List<MenuGroup>>() {
		});

		assertAll(
			() -> assertThat(메뉴_그룹_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(조회된_메뉴_그룹_목록).satisfies(조회된_메뉴_그룹_목록_확인(생성된_메뉴_그룹))
		);
	}

	private static Consumer<List<? extends MenuGroup>> 조회된_메뉴_그룹_목록_확인(MenuGroup 생성된_메뉴_그룹) {
		return menuGroups -> {
			assertThat(menuGroups.size()).isOne();
			assertThat(menuGroups).first()
				.satisfies(menuGroup -> assertAll(
					() -> assertThat(menuGroup.getId()).isNotNull(),
					() -> assertThat(menuGroup.getName()).isEqualTo(생성된_메뉴_그룹.getName())
				));
		};
	}

	public static MenuGroup 메뉴_그룹이_등록되어_있음(String name) {
		MenuGroup 메뉴_그룹_등록_데이터 = new MenuGroup();
		메뉴_그룹_등록_데이터.setName(name);
		return 메뉴_그룹_생성_요청(메뉴_그룹_등록_데이터).as(MenuGroup.class);
	}
}
