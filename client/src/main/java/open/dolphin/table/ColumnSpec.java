package open.dolphin.table;

/**
 *
 * @author Kazushi Minagawa.
 */
public final class ColumnSpec {

    private String name;
    private String method;
    private String cls;
    private int width;

    public ColumnSpec() {
    }

    public ColumnSpec(String name, String method, String cls, int width) {
        this();
        this.name = name;
        this.method = method;
        this.cls = cls;
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int cls) {
        this.width = cls;
    }
    
    public boolean isSameSpec(ColumnSpec test) {
        boolean same = true;
        same &= (name != null && name.equals(test.getName()));
        same &= (cls != null && cls.equals(test.getCls()));
        same &= (method != null && method.equals(test.getMethod()));
        return same;
    }
}
