package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.menu.application.MenuGroupService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("메뉴그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;


    private MenuGroup 메뉴_그룹_기본;
    private MenuGroup 메뉴_그룹_요일;

    @BeforeEach
    void set_up() {
        메뉴_그룹_기본 = MenuGroupFixture.create(1L, "메뉴 그룹 기본");
        메뉴_그룹_요일 = MenuGroupFixture.create(2L, "메뉴 그룹 요일");
    }

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    void create_menu() {
        // given
        when(menuGroupDao.save(any())).thenReturn(메뉴_그룹_기본);

        // when
        MenuGroup 추천메뉴_등록 = menuGroupService.create(메뉴_그룹_기본);

        // then
        assertThat(추천메뉴_등록).isEqualTo(메뉴_그룹_기본);
    }

    @Test
    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    void find_menus() {
        // given
        when(menuGroupService.list()).thenReturn(Arrays.asList(메뉴_그룹_기본, 메뉴_그룹_요일));

        // when
        List<MenuGroup> 메뉴_그룹 = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(메뉴_그룹).hasSize(2),
                () -> assertThat(메뉴_그룹).containsExactly(메뉴_그룹_기본, 메뉴_그룹_요일)
        );
    }
}

