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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 그룹 관련 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    MenuGroupDao menuGroupDao;

    @InjectMocks
    MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        // given
        MenuGroup request = new MenuGroup(null, "런치메뉴");
        MenuGroup 예상값 = new MenuGroup(1L, "런치메뉴");
        given(menuGroupDao.save(request)).willReturn(예상값);

        // when
        MenuGroup 메뉴_그룹_생성_결과 = 메뉴_그룹_생성(request);

        // then
        메뉴_그룹_값_비교(메뉴_그룹_생성_결과, 예상값);
    }

    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    void list() {
        // given
        List<MenuGroup> 예상값 = Arrays.asList(
                new MenuGroup(1L, "런치메뉴"),
                new MenuGroup(1L, "디너메뉴")
        );
        given(menuGroupDao.findAll()).willReturn(예상값);

        // when
        List<MenuGroup> 메뉴_그룹_목록_조회_결과 = menuGroupService.list();

        // then
        assertAll(
                () -> 메뉴_그룹_값_비교(메뉴_그룹_목록_조회_결과.get(0), 예상값.get(0)),
                () -> 메뉴_그룹_값_비교(메뉴_그룹_목록_조회_결과.get(1), 예상값.get(1))
        );
    }

    private MenuGroup 메뉴_그룹_생성(MenuGroup request) {
        return menuGroupService.create(request);
    }

    private void 메뉴_그룹_값_비교(MenuGroup result, MenuGroup expectation) {
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(expectation.getId()),
                () -> assertThat(result.getName()).isEqualTo(expectation.getName())
        );
    }
}
