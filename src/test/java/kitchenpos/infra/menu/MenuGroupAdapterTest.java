package kitchenpos.infra.menu;

import kitchenpos.application.MenuGroupService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.menu.exceptions.MenuGroupEntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupAdapterTest {
    private MenuGroupAdapter menuGroupAdapter;

    @Mock
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setup() {
        menuGroupAdapter = new MenuGroupAdapter(menuGroupDao);
    }

    @DisplayName("해당 메뉴 그룹이 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void isExistMenuGroupTest() {
        // given
        Long menuGroupId = 1L;
        given(menuGroupDao.existsById(menuGroupId)).willReturn(false);

        // when, then
        assertThatThrownBy(() -> menuGroupAdapter.isExistMenuGroup(menuGroupId))
                .isInstanceOf(MenuGroupEntityNotFoundException.class)
                .hasMessage("존재하지 않은 메뉴 그룹으로 메뉴를 등록할 수 없습니다.");
    }
}