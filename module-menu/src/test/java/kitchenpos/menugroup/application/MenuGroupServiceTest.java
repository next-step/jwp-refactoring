package kitchenpos.menugroup.application;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    private MenuGroupService menuGroupService;
    private MenuGroup A세트;
    private MenuGroup B세트;

    private MenuGroupRequest A세트_요청;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupRepository);
        A세트 = new MenuGroup(1L, "메뉴 이름");
        B세트 = new MenuGroup(2L, "메뉴 이름2");

        A세트_요청 = new MenuGroupRequest(1L, "메뉴 이름");
    }

    @DisplayName("메뉴 그룹 생성 테스트")
    @Test
    void createMenuGroupTest() {
        when(menuGroupRepository.save(any())).thenReturn(A세트);

        // when
        final MenuGroupResponse createdMenuGroup = A세트를_만든다();

        // then
        assertAll(
                () -> assertThat(createdMenuGroup.getId()).isNotNull(),
                () -> assertThat(createdMenuGroup.getName()).isEqualTo("메뉴 이름")
        );
    }

    private MenuGroupResponse A세트를_만든다() {
        return menuGroupService.create(A세트_요청);
    }

    @DisplayName("메뉴 그룹 목록 조회 테스트")
    @Test
    void getListMenuGroupTest() {
        when(menuGroupRepository.findAll()).thenReturn(Lists.newArrayList(A세트, B세트));

        // when
        final List<MenuGroupResponse> createdMenuGroups = 메뉴_목록들을_조회한다();

        // then
        assertAll(
                () -> assertThat(createdMenuGroups.size()).isPositive()
        );
    }

    private List<MenuGroupResponse> 메뉴_목록들을_조회한다() {
        return menuGroupService.list();
    }
}