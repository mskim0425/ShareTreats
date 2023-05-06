package shareTreats.kms.commend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import shareTreats.kms.personcounter.PersonCounter;
import shareTreats.kms.prizedraw.PrizeDraw;
import shareTreats.kms.productExchange.ProductExchange;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

@Component
public class CommandLine implements CommandLineRunner {

    private final ProductExchange productExchange;
    private final PrizeDraw prizeDraw;
    private final PersonCounter personCounter;

    public CommandLine(ProductExchange productExchange, PrizeDraw prizeDraw, PersonCounter personCounter) {
        this.productExchange = productExchange;
        this.prizeDraw = prizeDraw;
        this.personCounter = personCounter;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("어떤 서비스를 하시겠습니까? 번호를 입력하세요");
        System.out.println("[1]  상품 교환 서비스, [2] 빠칭코 상품 뽑기 서비스, [3]  회사 조직(부서) 인원수 파악 서비스");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int serviceNumber = Integer.parseInt(br.readLine());

        while (true) {
            //상품교환 서비스
            if(serviceNumber == 1){
                System.out.println("\n상품 교환 서비스 입니다.");
                productExchange.help();
                System.out.println();
                productExchange.showStatus();
                String[] data = br.readLine().split(" ");
                String key = data[0].toUpperCase();
                if(key.equals("CHECK")){
                    productExchange.check(data);
                }
                else if(key.equals("HELP")){
                    productExchange.help();
                }
                else if(key.equals("CLAIM")){
                    productExchange.claim(data);
                }
                else if(key.equals("STOP")){
                    break;
                }
                else {
                    System.out.println("올바른 명령어가 아닙니다. 처음으로 돌아갑니다");
                    run(args);
                }

            }

            //빠칭코 상품 뽑기 서비스
            else if(serviceNumber == 2){
                System.out.println("\n빠칭코 상품 뽑기 서비스 입니다.\n[1] 충전 [2] 게임하기 [3] 잔액 조회하기 [4] 끝내기");
                int choose = Integer.parseInt(br.readLine());
                if(choose==1){
                    System.out.println("충전하실 금액을 적어주세요!");
                    prizeDraw.charge(Integer.parseInt(br.readLine()));
                } else if (choose == 2) {
                    System.out.println("[게임 시도 횟수],[기간 작성]\n예시) 5, 2023-03-23T02:20:19");
                    String[] data = br.readLine().replaceAll(" ","").split(",");
                    int cnt = Integer.parseInt(data[0]);
                    if(data[1].matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")) {
                        LocalDateTime time = LocalDateTime.parse(data[1]);
                        prizeDraw.draw(cnt, time);
                    } else
                        System.out.println("올바르지 않은 기간입니다.");
                } else if (choose == 3) {
                    prizeDraw.info();
                }else break;
            }

            //부서 인원 수 파악 서비스
            else if(serviceNumber == 3) {
                System.out.println("\n부서 인원수 파악 서비스 입니다. \n[1] 부서와 인원수 입력 [2]부서간의 관계 [3]부서인원정보 [4]부서구성도 [5]모든 직원수 보기 [6] 가상데이터 넣기");
                int choose = Integer.parseInt(br.readLine());
                if(choose==1){
                    System.out.println("부서명, 인원수를 적어주세요 EX) A, 10");
                    String[] department1 = br.readLine().replaceAll(" ","").split(",");
                    if(department1.length !=2) {
                        System.out.println("올바르지 않은 입력입니다.");
                       break;
                    }
                    personCounter.save(department1[0],department1[1]);
                }
                else if (choose==2){
                    System.out.println("부서간의 관계를 > 표시하세요 EX) a>b or *>a");
                    personCounter.connectTree(br.readLine());
                }
                else if(choose ==3){
                    personCounter.showInfo();
                }
                else if(choose ==4){
                    personCounter.showDepartment();
                }
                else if(choose == 5){
                   personCounter.showTotalCount();
                } else if (choose ==6) {
                    personCounter.dataInsert();
                } else break;


            }

            else break;//서비스 외 선택시
        }

        System.out.println("서비스를 종료합니다");
        br.close();

    }



}
