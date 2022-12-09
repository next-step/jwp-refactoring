package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
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

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 뼈치킨;
    private MenuGroup 순살치킨;

    @BeforeEach
    void setUp() {
        뼈치킨 = MenuGroup.of(1L, "뼈치킨");
        순살치킨 = MenuGroup.of(2L, "순살치킨");
    }

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void 메뉴그룹_생성() {
        // given
        when(menuGroupDao.save(뼈치킨)).thenReturn(뼈치킨);

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(뼈치킨);

        // then
        assertAll(
                () -> assertThat(savedMenuGroup.getId()).isNotNull(),
                () -> assertThat(savedMenuGroup.getName()).isEqualTo(뼈치킨.getName())
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void 메뉴그룹_목록_조회() {
        // given
        List<MenuGroup> menuGroups = Arrays.asList(뼈치킨, 순살치킨);
        when(menuGroupDao.findAll()).thenReturn(menuGroups);

        // when
        List<MenuGroup> selectMenuGroups = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(selectMenuGroups).hasSize(menuGroups.size()),
                () -> assertThat(selectMenuGroups).containsExactly(뼈치킨, 순살치킨)
        );
    }
}
