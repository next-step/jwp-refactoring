package kitchenpos.application;

import static kitchenpos.domain.MenuGroupTest.메뉴그룹_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 식사;
    private MenuGroup 요리;
    private MenuGroup 안주;

    @BeforeEach
    public void setUp() {
        식사 = 메뉴그룹_생성(1L, "식사");
        요리 = 메뉴그룹_생성(2L, "요리");
        안주 = 메뉴그룹_생성(3L, "안주");
    }

    @Test
    @DisplayName("메뉴그룹 등록")
    void create() {
        // given
        when(menuGroupDao.save(식사)).thenReturn(식사);

        // when
        MenuGroup 등록된_식사 = menuGroupService.create(식사);

        // then
        assertThat(등록된_식사).isEqualTo(식사);
    }

    @Test
    @DisplayName("메뉴그룹 목록을 조회")
    void list() {
        // given
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(식사, 요리, 안주));

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).hasSize(3);
        assertThat(menuGroups).contains(식사, 요리, 안주);
    }
}