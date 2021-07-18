package kitchenpos.menugroup.acceptance;

import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTestMethod.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menuGroup.dto.MenuGroupRequest;
import kitchenpos.menuGroup.dto.MenuGroupResponse;

public class MenuGroupAcceptanceTest extends AcceptanceTest {
	@DisplayName("메뉴 그룹 등록 및 조회 시나리오")
	@Test
	void createMenuGroupAndFindMenuGroupScenario() {
		// Scenario
		// When
		ExtractableResponse<Response> menuGroupCreatedResponse = createMenuGroup(new MenuGroupRequest("인기 메뉴"));
		MenuGroupResponse createdMenuGroup = menuGroupCreatedResponse.as(MenuGroupResponse.class);
		// Then
		assertThat(menuGroupCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(createdMenuGroup.getName()).isEqualTo("인기 메뉴");
		// When
		ExtractableResponse<Response> menuGroupResponse = findMenuGroup();
		String menuName = menuGroupResponse.jsonPath().getList(".", MenuGroupResponse.class).stream()
			.filter(menuGroup -> menuGroup.getId() == createdMenuGroup.getId())
			.map(MenuGroupResponse::getName)
			.findFirst()
			.get();
		assertThat(menuName).isEqualTo("인기 메뉴");
	}
}
