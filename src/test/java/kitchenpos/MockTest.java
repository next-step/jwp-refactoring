package kitchenpos;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MockTest {
    @Mock
    public MenuGroupDao menuGroupDao;

    @Mock
    public MenuDao menuDao;

    @Mock
    public MenuProductDao menuProductDao;

    @Mock
    public ProductDao productDao;
}
