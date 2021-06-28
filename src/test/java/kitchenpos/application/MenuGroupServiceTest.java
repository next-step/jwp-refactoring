package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        this.menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @Test
    @DisplayName("create - 정상적인 메뉴 그룹 저장")
    void 정상적인_메뉴_그룹_저장() {

    }

    @Test
    @DisplayName("list - 정상적인 메뉴 그룹 조회")
    void 정상적인_메뉴_그룹_조회() {

    }
}