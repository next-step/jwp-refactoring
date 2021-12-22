package kitchenpos.application;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupDao;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.시즌_메뉴_그룹;
import static kitchenpos.fixture.MenuGroupFixture.추천_메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴 그룹 생성 테스트")
    @Test
    void create() {
        // given
        MenuGroupRequest 요청_메뉴_그룹 = MenuGroupRequest.of("추천_메뉴_그룹");

        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(추천_메뉴_그룹);

        // when
        MenuGroupResponse 생성된_메뉴_그룹 = menuGroupService.create(요청_메뉴_그룹);

        // then
        assertThat(생성된_메뉴_그룹).isEqualTo(MenuGroupResponse.of(추천_메뉴_그룹));
    }

    @DisplayName("메뉴 그룹 목록 조회 테스트")
    @Test
    void list() {
        // given
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(추천_메뉴_그룹, 시즌_메뉴_그룹));

        // when
        List<MenuGroupResponse> 조회된_메뉴_그룹_목록 = menuGroupService.list();

        // then
        assertThat(조회된_메뉴_그룹_목록).containsExactly(MenuGroupResponse.of(추천_메뉴_그룹), MenuGroupResponse.of(시즌_메뉴_그룹));
    }
}
