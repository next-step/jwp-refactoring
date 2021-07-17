package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_주류;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_치킨류;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@TestInstance(PER_CLASS)
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        // given
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @MethodSource("methodSource_create_성공")
    @ParameterizedTest
    void create_성공(MenuGroup menuGroup) {
        // when
        when(menuGroupDao.save(menuGroup)).thenReturn(menuGroup);
        MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(createdMenuGroup).isEqualTo(menuGroup);
        verify(menuGroupDao, times(1)).save(menuGroup);
    }

    Stream<Arguments> methodSource_create_성공() {
        return Stream.of(
                Arguments.of(메뉴그룹_치킨류),
                Arguments.of(메뉴그룹_주류)
        );
    }

    @Test
    void list_성공() {
        // given
        List<MenuGroup> menuGroups = asList(
                메뉴그룹_치킨류,
                메뉴그룹_주류);

        // when
        when(menuGroupDao.findAll()).thenReturn(menuGroups);
        List<MenuGroup> foundMenuGroups = menuGroupService.list();

        // then
        assertThat(foundMenuGroups).containsExactlyElementsOf(menuGroups);
        verify(menuGroupDao, times(1)).findAll();
    }
}