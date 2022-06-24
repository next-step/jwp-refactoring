package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void createMenuGroup() {
        // given
        final MenuGroup 세트메뉴 = new MenuGroup(1L, "세트메뉴");
        when(menuGroupDao.save(any())).thenReturn(세트메뉴);
        // when
        final MenuGroup actual = menuGroupService.create(new MenuGroupRequest("세트메뉴"));
        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isEqualTo(세트메뉴.getId()),
                () -> assertThat(actual.getName()).isEqualTo(세트메뉴.getName())
        );
    }

    @Test
    @DisplayName("메뉴 그룹들을 조회한다.")
    void searchMenuGroups() {
        // given
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(new MenuGroup(), new MenuGroup()));
        // when
        final List<MenuGroup> actual = menuGroupService.list();
        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).hasSize(2)
        );
    }
}
