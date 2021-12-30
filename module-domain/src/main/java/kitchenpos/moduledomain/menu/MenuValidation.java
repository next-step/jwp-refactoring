package kitchenpos.moduledomain.menu;


import java.util.List;
import kitchenpos.moduledomain.common.exception.Message;
import kitchenpos.moduledomain.common.exception.NoResultDataException;
import kitchenpos.moduledomain.product.Amount;
import kitchenpos.moduledomain.product.Product;

import kitchenpos.moduledomain.product.ProductDao;
import org.springframework.stereotype.Component;

@Component
public class MenuValidation {

    private final ProductDao productDao;
    private final MenuGroupDao menuGroupDao;

    public MenuValidation(ProductDao productDao, MenuGroupDao menuGroupDao) {
        this.productDao = productDao;
        this.menuGroupDao = menuGroupDao;
    }

    public void validExistsMenuGroup(Long menuGroupId) {
        if (menuGroupId == null) {
            throw new IllegalArgumentException();
        }
        menuGroupDao.findById(menuGroupId);
    }

    public  void validSumProductPrice(Amount price, MenuProducts menuProducts) {
        if (price.grateThan(sum(menuProducts))) {
            throw new IllegalArgumentException(Message.MENU_AMOUNT_IS_TOO_LAGE.getMessage());
        }
    }

    private Amount sum(MenuProducts menuProducts) {
        return menuProducts.getMenuProducts().stream()
            .map(s -> {
                Product savedProduct = productDao.findById(s.getProductId())
                    .orElseThrow(NoResultDataException::new);
                return savedProduct.multiply(s.getQuantity());
            }).reduce(Amount.ZERO, Amount::add);
    }

}
