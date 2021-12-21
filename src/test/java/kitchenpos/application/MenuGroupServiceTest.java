package kitchenpos.application;

import static kitchenpos.application.fixture.MenuGroupFixture.요청_메뉴그룹_치킨류;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dto.menu.MenuGroupRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴그룹 기능 관리")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("`메뉴그룹`을 등록할 수 있다.")
    void 메뉴그룹_등록() {
        // given
        MenuGroupRequest 메뉴그룹_치킨류 = 요청_메뉴그룹_치킨류();
        given(menuGroupDao.save(any())).willReturn(메뉴그룹_치킨류.toMenuGroup());

        // when
        MenuGroupResponse 등록된_메뉴 = menuGroupService.create(메뉴그룹_치킨류);

        // then
        assertThat(등록된_메뉴).isNotNull();
    }

    @Test
    @DisplayName("`메뉴그룹`목록을 조회할 수 있다.")
    void 메뉴그룹_목록_조회() {
        // given
        MenuGroupRequest 메뉴그룹_치킨류 = 요청_메뉴그룹_치킨류();
        given(menuGroupDao.findAll()).willReturn(Collections.singletonList(메뉴그룹_치킨류.toMenuGroup()));

        // when
        List<MenuGroupResponse> 메뉴목록 = menuGroupService.list();

        // then
        assertThat(메뉴목록).isNotEmpty();
    }
}
