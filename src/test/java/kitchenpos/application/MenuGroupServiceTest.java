package kitchenpos.application;

import static kitchenpos.fixture.DomainFactory.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
    private MenuGroup 빅맥세트;
    private MenuGroup 상하이버거세트;

    @BeforeEach
    void setUp() {
        빅맥세트 = createMenuGroup(1L, "빅맥세트");
        상하이버거세트 = createMenuGroup(1L, "상하이버거세트");
    }

    @Test
    void 메뉴그룹_생성() {
        when(menuGroupDao.save(빅맥세트)).thenReturn(빅맥세트);
        MenuGroup result = menuGroupService.create(빅맥세트);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(빅맥세트.getId()),
                () -> assertThat(result.getName()).isEqualTo(빅맥세트.getName())
        );
    }

    @Test
    void 메뉴그룹_목록_조회() {
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(빅맥세트, 상하이버거세트));
        List<MenuGroup> result = menuGroupService.list();
        assertThat(toIdList(result)).containsExactlyElementsOf(toIdList(Arrays.asList(빅맥세트, 상하이버거세트)));
    }

    private List<Long> toIdList(List<MenuGroup> products) {
        return products.stream()
                .map(MenuGroup::getId)
                .collect(Collectors.toList());
    }
}