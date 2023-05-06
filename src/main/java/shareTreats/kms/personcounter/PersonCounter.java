package shareTreats.kms.personcounter;

import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class PersonCounter {

    private List<Department> departmentList;
    private Boolean root; //최상단이 선택되었나?
    public PersonCounter() {
        this.root = false;
        this.departmentList = new ArrayList<>();
    }

    public void save(String name, String cnt) {
        if(!name.matches("[A-Z]+")) {
            System.out.println("대문자만 가능합니다.");
            return;
        }
        if(Integer.parseInt(cnt) < 0 || Integer.parseInt(cnt) >1000){
            System.out.println("0~1000명만 입력가능합니다.");
            return;
        }
        departmentList.add(new Department(name, Integer.parseInt(cnt)));
    }

    public void connectTree(String data) {
        String[] info = data.replaceAll(" ", "").split(">");
        if(info.length != 2) {
            System.out.println("> 로 표시되않았거나, 한번에 하나씩만 입력가능합니다.");
            return;
        }

        String parent = info[0];
        String child = info[1];

        for (Department department : departmentList) {
            if (department.getName().equals(child)) { // 부서명이 일치하는 경우
                if (department.getParent().equals("")) {

                    for (Department parentDepartment : departmentList) {// 부모 부서명을 찾아 설정
                        if (parentDepartment.getName().equals(parent)) {
                            department.setParent(parent);
                            parentDepartment.addChild(department); // 자식으로 등록
                            break;
                        }
                    }
                    if(!root && parent.equals("*")) {
                        department.setParent(parent);//* 최상단의 경우
                        root = true;
                    }else if(root && parent.equals("*"))
                        System.out.println("이미 최상위 부서는 정해져있습니다.");
                    else
                        department.setParent(parent); //독립적인 부서인경우
                }
                else {
                    System.out.println("1개의 하위 부서는 1개의 상위 부서만을 가질 수 있습니다.");
                }
                break;
            }
        }
    }


    public void showInfo() {
        if(departmentList.size() <1){ System.out.println("회사내의 각 부서는 1개 이상입니다."); return;}
        for (Department department : departmentList) {
            System.out.println(department.getName()+", "+department.getCnt());
        }
    }

    public void showDepartment(){
        Collections.sort(departmentList);
        for (Department department : departmentList) {
            System.out.println(department.getParent()+">"+department.getName());
        }
    }

    public void showTotalCount() {
        if(departmentList.size()<1) {System.out.println("데이터가 없습니다."); return;}
        Collections.sort(departmentList);
        HashMap<String, Boolean> checker = new HashMap<>(); // 계산 여부 확인
        for(Department department : departmentList){
            checker.put(department.getName(), false);
        }


        HashMap<String, Integer> answer = new HashMap<>(); // 계산결과 출력

        for (Department department : departmentList) {
            String name = department.getName();
            if(!checker.get(name)) {
                checker.put(name, true);
                answer.put(name, department.getCnt());
                List<Department> listOfChild = department.getChildren();

                for (Department child : listOfChild) {
                    if(!checker.get(child.getName())){
                        checker.put(child.getName(), true);
                        answer.put(name, answer.get(name)+child.getCnt());
                    }
                }
            }
        }

        for (Map.Entry<String, Integer> map : answer.entrySet()) {
            System.out.println(map.getKey()+"는 "+map.getValue()+"명 입니다.");
        }
    }


    public void dataInsert(){
        save("AS", "10");
        save("DEV", "0");
        save("QA", "990");

        connectTree("* > AS");
        connectTree("AS > DEV");
        connectTree("AS > QA");
    }
}
