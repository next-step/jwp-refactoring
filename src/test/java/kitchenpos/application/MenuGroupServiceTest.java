package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    MenuGroupDao menuGroupDao;

    @InjectMocks
    MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        // Given
        MenuGroup menuGroupRequest = new MenuGroup("추천메뉴");
        given(menuGroupDao.save(menuGroupRequest)).willReturn(new MenuGroup(1L, menuGroupRequest.getName()));

        // When
        MenuGroup menuGroupResponse = menuGroupService.create(menuGroupRequest);

        // Then
        assertAll(
                () -> assertThat(menuGroupResponse.getId()).isNotNull(),
                () -> assertThat(menuGroupResponse.getName()).isEqualTo(menuGroupRequest.getName())
        );
    }

    @DisplayName("메뉴 그룹의 리스트를 조회한다.")
    @Test
    void findAll() {
        // Given
        MenuGroup recommendedMenuGroup = new MenuGroup(1L, "추천메뉴");
        MenuGroup discountedMenuGroup = new MenuGroup(2L, "할인메뉴");
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(recommendedMenuGroup, discountedMenuGroup));

        // When
        List<MenuGroup> menuGroups = menuGroupService.list();

        // Then
        assertAll(
                () -> assertThat(menuGroups.size()).isEqualTo(2),
                () -> assertThat(menuGroups).containsExactly(recommendedMenuGroup, discountedMenuGroup)
        );
    }

}