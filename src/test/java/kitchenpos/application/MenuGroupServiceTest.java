package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
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
        MenuGroup 요청_메뉴_그룹 = new MenuGroup();
        요청_메뉴_그룹.setName("추천_메뉴_그룹");

        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(추천_메뉴_그룹);

        // when
        MenuGroup 생성된_메뉴_그룹 = menuGroupService.create(요청_메뉴_그룹);

        // then
        assertThat(생성된_메뉴_그룹).isEqualTo(추천_메뉴_그룹);
    }

    @DisplayName("메뉴 그룹 목록 조회 테스트")
    @Test
    void list() {
        // given
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(추천_메뉴_그룹, 시즌_메뉴_그룹));

        // when
        List<MenuGroup> 조회된_메뉴_그룹_목록 = menuGroupService.list();

        // then
        assertThat(조회된_메뉴_그룹_목록).containsExactly(추천_메뉴_그룹, 시즌_메뉴_그룹);
    }
}
