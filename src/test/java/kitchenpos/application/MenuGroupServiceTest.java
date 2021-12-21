package kitchenpos.application;

import kitchenpos.common.fixtrue.MenuGroupFixture;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    MenuGroup 한마리메뉴;
    MenuGroup 두마리메뉴;

    @BeforeEach
    void setUp() {
        한마리메뉴 = MenuGroupFixture.of(1L, "한마리메뉴");
        두마리메뉴 = MenuGroupFixture.of(2L, "두마리메뉴");
    }

    @Test
    void 메뉴_그룹_생성() {
        // given
        given(menuGroupDao.save(any())).willReturn(두마리메뉴);

        // when
        MenuGroup actual = menuGroupService.create(두마리메뉴);

        // then
        assertThat(actual).isEqualTo(두마리메뉴);
    }

    @Test
    void 메뉴_그룹_조회() {
        // given
        List<MenuGroup> menuGroups = Arrays.asList(한마리메뉴, 두마리메뉴);
        given(menuGroupDao.findAll()).willReturn(menuGroups);

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertAll(() -> {
            assertThat(actual).hasSize(2);
            assertThat(actual).containsExactlyElementsOf(Arrays.asList(한마리메뉴, 두마리메뉴));
        });
    }
}
