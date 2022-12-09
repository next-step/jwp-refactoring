package kitchenpos.menugroup.application;

import com.navercorp.fixturemonkey.FixtureMonkey;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.ProductService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴그룹 추가할 경우 추가된 메뉴그룹정보를 반환")
    @ParameterizedTest
    @ValueSource(longs = {1,342,21,3423,4})
    public void returnProduct(long id) {
        MenuGroup menuGroup = new MenuGroup();
        MenuGroup mockProduct = new MenuGroup();
        mockProduct.setId(id);
        doReturn(mockProduct).when(menuGroupDao).save(menuGroup);

        MenuGroup savedProduct = menuGroupService.create(menuGroup);

        assertThat(savedProduct.getId()).isEqualTo(id);
    }

    @DisplayName("메뉴그룹목록을 조회할 경우 저장된 메뉴그룹목록반환")
    @Test
    public void returnProducts() {
        FixtureMonkey sut = FixtureMonkey.create();
        List<MenuGroup> mockProducts = sut.giveMeBuilder(MenuGroup.class)
                .set("id", Arbitraries.longs().between(1, 5))
                .sampleList(5);
        doReturn(mockProducts).when(menuGroupDao).findAll();

        List<MenuGroup> products = menuGroupService.list();

        List<Long> productIds = products.stream().map(MenuGroup::getId).collect(Collectors.toList());
        assertAll(() -> assertThat(productIds).containsAnyOf(1l, 2l, 3l, 4l, 5l));
    }
}
