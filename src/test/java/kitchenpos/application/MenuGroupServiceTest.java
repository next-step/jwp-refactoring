package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class MenuGroupServiceTest {
    @MockBean
    private MenuGroupDao menuGroupDao;
    private MenuGroupService menuGroupService;
    private MenuGroup menuGroup;

    @Autowired
    public MenuGroupServiceTest(MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("추천메뉴");
        when(menuGroupDao.save(any(MenuGroup.class))).thenReturn(menuGroup);
        when(menuGroupDao.findAll()).thenReturn(Collections.singletonList(menuGroup));
    }

    @DisplayName("제품을 등록한다")
    @Test
    void createProduct() {
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);
        assertThat(savedMenuGroup.getId()).isEqualTo(menuGroup.getId());
        assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("제품을 조회한다")
    @Test
    void findAllProduct() {
        List<MenuGroup> menuGroups = menuGroupService.list();
        assertThat(menuGroups.size()).isEqualTo(1);
        assertThat(menuGroups.get(0)).isEqualTo(menuGroup);
    }
}
