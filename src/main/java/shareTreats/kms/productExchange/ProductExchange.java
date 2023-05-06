package shareTreats.kms.productExchange;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProductExchange {

    private static HashMap<String,Boolean> productMap;
    private static HashMap<String, String> storeMap;
    public ProductExchange() {
        this.productMap = new HashMap<>();
        this.storeMap = new HashMap<>();
        //상품 코드는 0~9 자연수 글자로 이루어져 있으며 9문자로 이루어져 있습니다.
        //상품 코드는 9개의 숫자 문자열로 구성된 총 20개를 개발자가 임의로 제공합니다.
        for (int i = 1; i <= 20; i++) {
            String productCode = String.format("%09d", i);
            productMap.put(productCode, true);
            storeMap.put(productCode,"");
        }
    }

    public void check(String[] inputArr) {

        if (validation(inputArr)) {
            System.out.println("잘못된 입력입니다. 상품코드를 입력하세요" );
            return;
        }

        for(int i = 1; i< inputArr.length; i++){
            String productCode = inputArr[i];
            //9문자 이상의 데이터가 들어왔을 때
            if(productCode.length() < 9) productCode = String.format("%09d", Integer.valueOf(productCode));

            //상품코드의 존재여부
            if(!productMap.containsKey(productCode)) System.out.println(productCode +"는 존재하지않습니다.");
            else {
                if(productMap.get(productCode)) System.out.println(productCode +"는 교환 가능합니다.");
                else System.out.println(productCode +"는 교환 불가능합니다.");
            }
        }


    }


    public void help() {
        System.out.println("=== SHARETREATS 사용법 안내 ===");
        System.out.println("CHECK [상품코드] : 해당 상품의 교환 가능 여부를 확인합니다.");
        System.out.println("HELP  사용법을 안내합니다.");
        System.out.println("CLAIM [상점코드] [상품코드... 상품코드] : 해당 상품을 교환합니다.");
        System.out.println("STOP 서비스를 종료합니다.");
    }

    public void claim(String[] inputArr) {
        if (validation(inputArr)) {
            System.out.println("잘못된 입력입니다. 상품코드를 입력하세요" );
            return;
        }


        String storeCode = inputArr[1];
        if(!storeCode.matches("^[a-zA-Z]{1,6}$")) {
            System.out.println("올바르지 않은 상점 코드입니다.");
            return;
        }


        for (int i = 2; i < inputArr.length; i++) {
            String productCode =inputArr[i];
            if(productCode.length() < 9) productCode = String.format("%09d", Integer.valueOf(productCode));

            if(productMap.containsKey(productCode)) {
                if(productMap.get(productCode)) {
                    System.out.println(productCode+"가 교환되었습니다.");
                    productMap.put(productCode, false);
                    storeMap.put(productCode, storeCode);
                }else
                    System.out.println(productCode+"는 이미 교환된 상품입니다.");
            } else {
                System.out.println(productCode+ "는 존재 하지않은 상품입니다.");
            }
        }

    }

    public void showStatus(){
        int cnt = 0;
        for(Map.Entry<String, Boolean> map :productMap.entrySet()){

            String yn = "";
            if(map.getValue()) {
                yn = "Y";
                System.out.print("[상품코드 :"+map.getKey() + " 교환 :" + yn+"] ");
            }
            else {
                yn = "N";
                System.out.print("[상품코드 :"+map.getKey() + " 교환 :" + yn +" 사유 :"+storeMap.get(map.getKey()) +"] ");
            }

            if(cnt++==3) {
                cnt =0;
                System.out.println();
            }
        }
    }

    private static boolean validation(String[] inputArr) {
        return inputArr.length < 2 || !inputArr[1].matches("^[0-9a-zA-Z\\s]*$") || inputArr[1].length() > 30;
    }

}
