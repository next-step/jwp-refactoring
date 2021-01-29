package kitchenpos.application;

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
import static org.mockito.BDDMockito.given;

@DisplayName("애플리케이션 테스트 보호 - 메뉴 그룹 서비스")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    private MenuGroup menuGroup;

    @BeforeEach
    public void setup() {
        menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("후리이드치킨반마리+양념치킨반마리세트");
    }

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        given(menuGroupDao.save(menuGroup)).willReturn(menuGroup);

        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertThat(savedMenuGroup).isEqualTo(menuGroup);
    }

    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    void list() {
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(menuGroup));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).containsExactly(menuGroup);
    }

}
