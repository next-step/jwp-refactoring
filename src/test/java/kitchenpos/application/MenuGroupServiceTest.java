package kitchenpos.application;

import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 그룹 서비스에 관련한 기능")
@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("`메뉴 그룹`을 생성한다.")
    @Test
    void createMenuGroup() {
        // Given
        MenuGroupRequest request = new MenuGroupRequest("세트메뉴");

        // When
        MenuGroupResponse actual = menuGroupService.create(request);

        // Then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(request.getName())
        );
    }

    @DisplayName("모든 `메뉴 그룹` 목록을 조회한다.")
    @Test
    void findAllMenuGroups() {
        // Given
        MenuGroupRequest request = new MenuGroupRequest("세트메뉴");
        MenuGroupResponse savedMenuGroup = menuGroupService.create(request);

        // When
        List<MenuGroupResponse> actual = menuGroupService.list();

        // Then
        assertAll(
                () -> assertThat(actual).extracting(MenuGroupResponse::getId)
                        .containsAnyElementsOf(Collections.singletonList(savedMenuGroup.getId())),
                () -> assertThat(actual).extracting(MenuGroupResponse::getName)
                        .containsAnyElementsOf(Collections.singletonList(savedMenuGroup.getName()))
        );
    }
}
