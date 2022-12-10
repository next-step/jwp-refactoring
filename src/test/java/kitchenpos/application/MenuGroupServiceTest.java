package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    @InjectMocks
    private MenuGroupService menuGroupService;
    private MenuGroup 메뉴_그룹;

    @BeforeEach
    void setUp() {
        메뉴_그룹 = new MenuGroup("한마리메뉴");
    }

    @Test
    void 생성() {
        given(menuGroupDao.save(any())).willReturn(메뉴_그룹);

        MenuGroup menuGroup = menuGroupService.create(메뉴_그룹);

        assertThat(menuGroup.getName()).isEqualTo("한마리메뉴");
    }
}
