package kitchenpos.menugroup.domain;

public class MenuGroup {
    private Long id;
    private String name;

    private MenuGroup(MenuGroupBuilder builder){
        this.id = builder.id;
        this.name = builder.name;
    }

    public static MenuGroupBuilder builder() {
        return new MenuGroupBuilder();
    }

    public static class MenuGroupBuilder {
        private Long id;
        private String name;

        public MenuGroupBuilder id(Long id){
            this.id = id;
            return this;
        }

        public MenuGroupBuilder name(String name){
            this.name = name;
            return this;
        }

        public MenuGroup build(){
            return new MenuGroup(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
