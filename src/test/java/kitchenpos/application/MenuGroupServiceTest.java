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
import static org.mockito.Mockito.when;

@DisplayName("메뉴 그룹 관련 비즈니스 기능 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 생성 테스트")
    @Test
    void createMenuGroupTest() {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, "한식");
        when(menuGroupDao.save(menuGroup)).thenReturn(menuGroup);

        // when
        MenuGroup result = menuGroupService.create(menuGroup);

        // then
        checkForCreateMenuGroup(result, menuGroup);
    }

    public void checkForCreateMenuGroup(MenuGroup createMenuGroup, MenuGroup sourceMenuGroup) {
        assertAll(
                () -> assertThat(createMenuGroup.getId()).isEqualTo(sourceMenuGroup.getId()),
                () -> assertThat(createMenuGroup.getName()).isEqualTo(sourceMenuGroup.getName())
        );
    }

    @DisplayName("메뉴 그룹 목록 조회 테스트")
    @Test
    void findAllMenuGroup() {
        // given
        MenuGroup menuGroup = new MenuGroup(1L, "한식");
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(menuGroup));

        // when
        List<MenuGroup> results = menuGroupService.list();

        // then
        assertThat(results).hasSize(1)
                .containsExactly(menuGroup);
    }
}