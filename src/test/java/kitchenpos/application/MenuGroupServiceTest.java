package kitchenpos.application;

import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import org.junit.jupiter.api.BeforeEach;
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

    private MenuGroupRequest request;
    private MenuGroupResponse response;

    @BeforeEach
    void beforeEach() {
        request = new MenuGroupRequest("세트메뉴");
        response = menuGroupService.create(request);
    }

    @DisplayName("`메뉴 그룹`을 생성한다.")
    @Test
    void createMenuGroup() {
        // Then
        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getName()).isEqualTo(request.getName())
        );
    }

    @DisplayName("모든 `메뉴 그룹` 목록을 조회한다.")
    @Test
    void findAllMenuGroups() {
        // When
        List<MenuGroupResponse> actual = menuGroupService.list();

        // Then
        assertThat(actual).containsAnyElementsOf(Collections.singletonList(response));
    }
}
