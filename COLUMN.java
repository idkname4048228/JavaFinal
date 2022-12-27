public enum COLUMN {
    ID(0, "ID"),
    NAME(1, "Name"),
    START(2, "Start"),
    END(3, "End"),
    DEGREE(4, "Degree"),
    STATE(5, "State"),
    NUMBER(6, "Number"),
    CATALOG(7, "Catalog"),
    WORK(8, "Work");

    private int index;
    private String describe;

    COLUMN(int num, String desc) {
        index = num;
        describe = desc;
    }

    public int getIndex() {
        return this.index;
    }

    public String getDesc() {
        return this.describe;
    }
}
