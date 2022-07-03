package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_데이터_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    private MenuGroup 메뉴_그룹;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
        메뉴_그룹 = 메뉴_그룹_데이터_생성(1L, "세트 메뉴");
    }

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        // given
        given(menuGroupDao.save(any())).willReturn(메뉴_그룹);

        // when
        MenuGroup menuGroup = menuGroupService.create(메뉴_그룹);

        // then
        assertThat(menuGroup).isEqualTo(메뉴_그룹);
    }

    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    void list() {
        // given
        given(menuGroupDao.findAll()).willReturn(Collections.singletonList(메뉴_그룹));

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(menuGroups).isNotEmpty(),
                () -> assertThat(menuGroups).hasSize(1),
                () -> assertThat(menuGroups).containsExactly(메뉴_그룹)
        );
    }
}
