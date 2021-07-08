package kitchenpos.domain.menu;

import kitchenpos.domain.product.Products;

public interface MenuCreateValidator {
    void validate(Menu menu, Products products, MenuCreate menuCreate) throws RuntimeException;
}
