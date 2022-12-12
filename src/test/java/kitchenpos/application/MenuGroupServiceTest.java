package kitchenpos.application;

import static kitchenpos.domain.MenuGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 등록 API")
    @Test
    void create() {
        // given
        MenuGroup menuGroup = menuGroupParam("추천메뉴");
        MenuGroup savedMenuGroup = savedMenuGroup(1L, "추천메뉴");
        given(menuGroupDao.save(menuGroup)).willReturn(savedMenuGroup);

        // when
        MenuGroup actual = menuGroupService.create(menuGroup);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(savedMenuGroup.getName())
        );
    }

    @DisplayName("메뉴 그룹 목록 조회 API")
    @Test
    void list() {
        // given
        MenuGroup savedMenuGroup1 = savedMenuGroup(1L, "미뉴 그룹1");
        MenuGroup savedMenuGroup2 = savedMenuGroup(2L, "메뉴 그룹2");
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(savedMenuGroup1, savedMenuGroup2));

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertAll(
            () -> assertThat(menuGroups).hasSize(2),
            () -> assertThat(menuGroups).contains(savedMenuGroup1, savedMenuGroup2)
        );
    }
}
