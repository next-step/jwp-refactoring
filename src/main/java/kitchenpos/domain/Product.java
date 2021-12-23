package kitchenpos.domain;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Product {

    private static final String KEY_COLUMN_NAME = "id";

    private Long id;
    private String name;
    private BigDecimal price;

    protected Product() {}

    public Product(long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product from(final ResultSet resultSet) throws SQLException {
        return new Product(resultSet.getLong(KEY_COLUMN_NAME), resultSet.getString("name"), resultSet.getBigDecimal("price"));
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
