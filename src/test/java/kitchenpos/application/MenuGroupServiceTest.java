package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setup() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void createMenuGroupTest() {
        // given
        Long menuGroupId = 1L;
        MenuGroup menuGroup = new MenuGroup();
        MenuGroup menuGroupWithId = new MenuGroup();
        menuGroupWithId.setId(menuGroupId);
        given(menuGroupDao.save(any())).willReturn(menuGroupWithId);

        // when
        MenuGroup saved = menuGroupService.create(menuGroup);

        // then
        assertThat(saved.getId()).isEqualTo(menuGroupId);
    }
}