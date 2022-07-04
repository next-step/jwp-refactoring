package kitchenpos.menu.application;

import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.dto.MenuGroupCreateRequest;
import kitchenpos.menuGroup.dto.MenuGroupResponse;
import kitchenpos.menuGroup.service.MenuGroupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static kitchenpos.menu.MenuGenerator.메뉴_그룹_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest
@ActiveProfiles("test")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성 시 정상 생성되어야 한다")
    @Test
    void createMenuGroupTest() {
        // given
        String 메뉴_그룹_이름 = "메뉴 그룹";
        MenuGroupCreateRequest 메뉴_그룹 = 메뉴_그룹_생성_요청(메뉴_그룹_이름);

        // when
        MenuGroupResponse 메뉴_그룹_생성_결과 = menuGroupService.create(메뉴_그룹);

        // then
        메뉴_그룹_생성_성공됨(메뉴_그룹_생성_결과, 메뉴_그룹_이름);
    }

    @DisplayName("메뉴 그룹 전체 조회 시 정상 조회되어야 한다")
    @Test
    void findAllMenuGroupTest() {
        // given
        List<Long> 포함_되어야_할_아이디들 = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            포함_되어야_할_아이디들.add(
                    menuGroupService.create(메뉴_그룹_생성_요청("메뉴 그룹")).getId()
            );
        }

        // when
        List<MenuGroupResponse> 메뉴_그룹_조회_결과 = menuGroupService.listResponse();

        // then
        메뉴_그룹_목록_정상_조회됨(메뉴_그룹_조회_결과, 포함_되어야_할_아이디들);
    }

    @DisplayName("없는 메뉴 그룹을 조회하면 예외가 발생해야 한다")
    @Test
    void findMenuGroupByNotSavedTest() {
        메뉴_그룹_조회_실패됨(() -> menuGroupService.getMenuGroup(-1L));
    }

    @DisplayName("정상 메뉴 그룹 조회 시 정상 조회되어야 한다")
    @Test
    void findMenuGroupTest() {
        // given
        Long 메뉴_그룹_아이디 = menuGroupService.create(메뉴_그룹_생성_요청("메뉴 그룹")).getId();

        // when
        MenuGroup 메뉴_그룹 = menuGroupService.getMenuGroup(메뉴_그룹_아이디);

        // then
        assertThat(메뉴_그룹).isNotNull();
        assertThat(메뉴_그룹.getId()).isEqualTo(메뉴_그룹_아이디);
    }

    private void 메뉴_그룹_생성_성공됨(MenuGroupResponse menuGroup, String name) {
        assertThat(menuGroup.getId()).isNotNull();
        assertThat(menuGroup.getName()).isEqualTo(name);
    }

    void 메뉴_그룹_목록_정상_조회됨(List<MenuGroupResponse> menuGroups, List<Long> containIds) {
        assertThat(menuGroups.size()).isGreaterThanOrEqualTo(containIds.size());
        assertThat(menuGroups.stream().mapToLong(MenuGroupResponse::getId)).containsAll(containIds);
    }

    void 메뉴_그룹_조회_실패됨(Runnable runnable) {
        assertThatIllegalArgumentException().isThrownBy(runnable::run);
    }
}
