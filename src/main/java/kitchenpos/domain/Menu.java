package kitchenpos.domain;

import static kitchenpos.exception.ErrorCode.PRICE_IS_NULL_OR_MINUS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.exception.ErrorCode;
import kitchenpos.exception.KitchenposException;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = setMenu(menuProducts, this);
    }

    private List<MenuProduct> setMenu(List<MenuProduct> menuProducts, Menu menu){
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(menu));
        return menuProducts;
    }

    public void validatePrice() {
        if(validatePriceNull() || validatePriceLessThanZero()){
            throw new KitchenposException(PRICE_IS_NULL_OR_MINUS);
        }
    }

    public boolean validatePriceNull(){
        return Objects.isNull(price);
    }

    public boolean validatePriceLessThanZero(){
        return this.price.compareTo(BigDecimal.ZERO) < 0;
    }

    public void validatePriceGreaterThanSum(BigDecimal sum) {
        if (this.price.compareTo(sum) > 0) {
            throw new KitchenposException(ErrorCode.PRICE_GREATER_THAN_SUM);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

}
