package kitchenpos.menugroup;

import kitchenpos.application.MenuGroupService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 그룹")
class MenuGroupTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;


    @Test
    @DisplayName("메뉴 그룹 이름이 비어있을 경우 예외가 발생한다.")
    void emptyMenuGroupName() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            menuGroupService.create(new MenuGroup(" "));
        });
    }
}
