package kitchenpos.application;

import kitchenpos.application.fixture.MenuGroupFixture;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    MenuGroupDao menuGroupDao;

    @InjectMocks
    MenuGroupService menuGroupService;

    private MenuGroup 추천메뉴;
    private MenuGroup 사이드메뉴;

    @BeforeEach
    void setup() {
        추천메뉴 = MenuGroupFixture.create(1L, "추천메뉴");
        사이드메뉴 = MenuGroupFixture.create(1L, "사이드메뉴");
    }

    @DisplayName("메뉴 그룹 등록 확인")
    @Test
    void 메뉴_그룹_등록_확인() {
        // given
        MenuGroup 메뉴_그룹_등록_요청_데이터 = new MenuGroup();
        메뉴_그룹_등록_요청_데이터.setName("추천메뉴");

        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(추천메뉴);

        // when
        MenuGroup 등록된_메뉴_그룹 = menuGroupService.create(메뉴_그룹_등록_요청_데이터);

        // then
        assertThat(등록된_메뉴_그룹).isEqualTo(추천메뉴);
    }

    @DisplayName("메뉴 그룹 목록 확인")
    @Test
    void 메뉴_그룹_목록_확인() {
        // given
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(추천메뉴, 사이드메뉴));

        // when
        List<MenuGroup> 메뉴_그룹_목록 = menuGroupService.list();

        // then
        assertThat(메뉴_그룹_목록).containsExactly(추천메뉴, 사이드메뉴);
    }
}
