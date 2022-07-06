package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.menu.MenuGroupService;
import kitchenpos.common.menu.domain.MenuGroup;
import kitchenpos.common.menu.repository.MenuGroupRepository;
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
    private MenuGroupRepository menuGroupRepository;

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
        when(menuGroupRepository.save(any(MenuGroup.class))).thenReturn(given);

        // when
        final MenuGroup actual = menuGroupService.create(given);

        // then
        assertThat(actual).isEqualTo(given);
    }

    @Test
    void 메뉴_그룹_목록을_조회할_수_있어야_한다() {
        // given
        when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(식당_포스.구이류, 식당_포스.식사류));

        // when
        final List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual).containsExactly(식당_포스.구이류, 식당_포스.식사류);
    }

    @Test
    void 아이디로_메뉴_그룹의_존재_여부를_조회할_수_있어야_한다() {
        // given
        final MenuGroup given = new MenuGroup(1L, "새로운 메뉴 그룹");
        when(menuGroupRepository.existsById(given.getId())).thenReturn(true);

        // when and then
        assertThat(menuGroupService.existsById(given.getId())).isTrue();
    }
}
