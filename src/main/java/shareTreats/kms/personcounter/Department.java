package shareTreats.kms.personcounter;

import java.util.ArrayList;
import java.util.List;

public class Department implements Comparable<Department>{
    private String name;
    private int cnt;
    private String parent;
    private List<Department> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Department> getChildren() {
        return children;
    }

    public void addChild(Department child) {
        this.children.add(child);
    }
    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }


    public Department(String name, int cnt) {
        this.name = name;
        this.cnt = cnt;
        this.parent = "";
        this.children = new ArrayList<>();
    }

    @Override
    public int compareTo(Department o) {
        // 부모가 같은 경우, 이름을 알파벳 순으로 비교하여 정렬
        if (this.parent.equals(o.parent)) {
            return this.name.compareTo(o.name);
        }
        // 부모가 다른 경우, *은 가장 먼저 나오도록 정렬
        if (this.parent.equals("*")) {
            return -1;
        }
        if (o.parent.equals("*")) {
            return 1;
        }
        // 부모가 다르고 *이 아닌 경우, 부모 이름을 알파벳 순으로 비교하여 정렬
        return this.parent.compareTo(o.parent);
    }

    public int getTotalCount() {
        int count = this.cnt;
        for (Department child : children) {
            count += child.getTotalCount();
        }
        return count;
    }

}
