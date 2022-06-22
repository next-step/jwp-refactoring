package kitchenpos.domain;

public class MenuGroup {
    private Long id;
    private String name;

    public MenuGroup() {
    }

    public MenuGroup(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
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

    public static class Builder {
        private Long id;
        private String name;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public MenuGroup build() {
            return new MenuGroup(this);
        }
    }
}
