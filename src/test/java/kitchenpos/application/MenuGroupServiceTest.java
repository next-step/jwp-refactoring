package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.UnitTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private UnitTestFixture 식당_포스;

    @BeforeEach
    void setUp() {
        식당_포스 = new UnitTestFixture();
    }

    @Test
    void 메뉴_그룹을_등록할_수_있어야_한다() {
        // given
        final MenuGroup given = new MenuGroup(1L, "새로운 메뉴 그룹");
        when(menuGroupDao.save(any(MenuGroup.class))).thenReturn(given);

        // when
        final MenuGroup actual = menuGroupService.create(given);

        // then
        assertThat(given).isEqualTo(actual);
    }

    @Test
    void 메뉴_그룹_목록을_조회할_수_있어야_한다() {
        // given
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(식당_포스.구이류, 식당_포스.식사류));

        // when
        final List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual).containsExactly(식당_포스.구이류, 식당_포스.식사류);
    }
}
