import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;


import javax.swing.*;

public class Board extends JPanel{
    JLabel lbScore, lbLife,labelCharacter,labelBack;
    int score = 0;
    int life=5;
    int elapsed;

    //왜군 단어
    private ArrayList<FallingWord> words = new ArrayList<>();
    private String[] wordpool = {
            "가토","고니시",
            "일본해군","조총","왜성",
            "사무라이"

    }; //단어풀에 있는 단어 중에서 랜덤하게 골라서 출현하도록
    //조선군 단어
    private ArrayList<FallingWord2> words2 = new ArrayList<>();
    private String[] wordpool2 = {
            "광해군","원균","곽재우","김시민",
            "이순신","이이","권율","국궁","의병","거북선"
    }; //단어풀에 있는 단어 중에서 랜덤하게 골라서 출현하도록
    //보너스 적장 단어->크기를 바꾸기 위해 한ㅂㄴ더 사용하게됨.
    private ArrayList<FallingWord3> words3 = new ArrayList<>();
    private String[] wordpool3 = {
            "도요토미 히데요시"
    }; //단어풀에 있는 단어 중에서 랜덤하게 골라서 출현하도록

    //icons의 배열
    ImageIcon[] icons = {
            new ImageIcon( "images/life/life5.jpg" ),
            new ImageIcon( "images/life/life4.jpg" ),
            new ImageIcon( "images/life/life3.jpg" ),
            new ImageIcon( "images/life/life2.jpg" ),
            new ImageIcon( "images/life/life1.jpg" ),
            new ImageIcon( "images/life/life0.jpg" )
    };
    //enemyIc의 배열(적군 아이콘)
    ImageIcon[] enemyIc= {
            new ImageIcon("images/enemy/enemy1.png"),
            new ImageIcon("images/enemy/enemy2.png"),
            new ImageIcon("images/enemy/enemy3.png"),
            new ImageIcon("images/enemy/enemy4.png"),
            new ImageIcon("images/enemy/enemy5.png"),
            new ImageIcon("images/enemy/enemy6.png")
    };
    //characterIc의 배열(이순신 아이콘)
    ImageIcon[] characterIc= {
            new ImageIcon("images/score/score1.png"),
            new ImageIcon("images/score/score2.png"),
            new ImageIcon("images/score/score3.png"),
            new ImageIcon("images/score/score4.png"),
            new ImageIcon("images/score/score5.png"),
            new ImageIcon("images/score/score6.png"),
            new ImageIcon("images/score/score7.png")

    };

    //배경화면의 이미지변수를 전역변수로 선언1,2,3몯 마찬가지
    ImageIcon backmoveIc =new ImageIcon("images/background_move.jpg");
    Image backmoveImg=backmoveIc.getImage();

    ImageIcon backmoveIc2 =new ImageIcon("images/background_move2.png");
    Image backmoveImg2=backmoveIc2.getImage();

    ImageIcon backmoveIc3 =new ImageIcon("images/background_move3.jpg");
    Image backmoveImg3=backmoveIc3.getImage();

    ImageIcon backmoveIc4 =new ImageIcon("images/background_move4.jpg");
    Image backmoveImg4=backmoveIc4.getImage();

    ImageIcon backmoveIc5 =new ImageIcon("images/background_move5.jpg");
    Image backmoveImg5=backmoveIc5.getImage();

    ImageIcon backmoveIc6 =new ImageIcon("images/background_move6.jpg");
    Image backmoveImg6=backmoveIc6.getImage();

    int backX=0;//x좌표의 값을 0으로 초기화(Thread를 통해 움직임)


    private int numOfWords = 10; //총 몇개의 단어가 나타날 것인가? 상수로 설정할 수도..
    private Timer timer;
    private boolean isPlaying = false;

    JPanel lifepanel;
    Image imgHeart;//이미기 하트 선언


    JPanel mainpanel; //게임 메인 패널
    JButton btnStart; //시작버튼
    JTextField msgInput; //단어 입력하는 곳

    public Board() {
        initBoard(); // 초기 화면 그리기. 컴포넌트 배치
        setEvent(); // 버튼, 입력필드에 이벤트 설정

    }
    public int Board(){
        return score;
    }


    private void initBoard() {
        setLayout(new BorderLayout()); // 전체적으로 동서남북 구조, 북쪽은 게임정보, 센터는 단어들, 아래는 입력창

        JPanel info = new JPanel(new FlowLayout(FlowLayout.LEFT)); //new FlowLayout(FlowLayout.LEFT)
        JLabel l1 = new JLabel("점수:", JLabel.RIGHT); // 텍스트 오른쪽으로 배치
        l1.setPreferredSize(new Dimension(50,30));

        lbScore = new JLabel("" + score, JLabel.LEFT); // 텍스트를 왼쪽으로, 멤버변수로 설정된 변수 값을 설정한 것임
        lbScore.setPreferredSize(new Dimension(50,30));

        JLabel l3 = new JLabel("생명:", JLabel.RIGHT); // 텍스트를 가운데
        l3.setPreferredSize(new Dimension(50,30)); // 레이블의 크기 지정

        lbLife = new JLabel("" + life, JLabel.LEFT); // 텍스트를 왼쪽에
        lbLife.setPreferredSize(new Dimension(50,30));

        info.add(l1); // 상단 패널에 레이블 4개를 순서대로 추가 (점수와 생명 확인용)
        //        info.add(Box.createRigidArea(new Dimension(50, 0))); // 눈에 안보이는 공백 영역 추가
        info.add(lbScore);
        info.add(l3);
        info.add(lbLife);

        //info에 라이프패널 추가
        lifepanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lifepanel.setPreferredSize(new Dimension(120,30));
        ImageIcon imageIcon = new ImageIcon("images/heart_nobg.png");
        imgHeart = imageIcon.getImage().getScaledInstance(18, 18,  Image.SCALE_SMOOTH);
        updateLifeImage();
        info.add(lifepanel);

        btnStart = new JButton("전쟁시작");
        info.add(btnStart);

        //info에 이순신 초상화 추가
        labelCharacter = new JLabel(icons[1]);
        updateCharacterImage();
        labelCharacter.setPreferredSize(new Dimension(150,150));
        info.add(labelCharacter);

        add(info, BorderLayout.NORTH);

        //main패널 작성(여기서 웬만한 배경이미지,이미지 재생됨)
        mainpanel = new GamePanel();
        mainpanel.setBackground(Color.black);
        add(mainpanel, BorderLayout.CENTER);

        msgInput = new JTextField(); // 텍스트 입력 콤포넌트
        add(msgInput, BorderLayout.SOUTH); // 프레임 남쪽에 텍스트 입력 필드 배치

    }
    private void setEvent() {
        // 입력창에 대한 이벤트 설정
        msgInput.addActionListener(new ActionListener() { // 이벤트 처리 기능 부여
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("키보드 엔터키 눌렀군");
                String input = msgInput.getText();
                System.out.println("네가 입력한 문자:" + input);
                msgInput.setText(""); // 입력창에 쓴 글씨 지우기

                for (FallingWord word : words) {
                    // 살아있는 단어에 대해서만 정답처리
                    if (word.isActive && word.getWord().equals(input)) {
                        System.out.println("정답입니다.");
                        word.isActive = false;
                        words.remove(word);
                        score += 100; // 100점 증가
                        lbScore.setText("" + score);
                        break; // break하면 같은 단어중 1개만 정답처리.
                        // break하지 않고 for루프를 돌면 for루프의 대상인 words의 개수가 바뀌면서 동시성오류 발생
                        // 그러면, 같은 단어가 여러개일 때 가장 아래를 처리하려면?
                        // word.find()함수를 만들 수 있음. 가장 아래의 word 객체를 찾아 리턴하는 함수. 없으면 null
                        // 있으면 해당 객체에 대해서만 정답처리, null이면 정답없음
                    }
                }
                for (FallingWord2 word : words2) {
                    // 살아있는 단어에 대해서만 정답처리
                    if (word.isActive && word.getWord().equals(input)) {
                        System.out.println("정답입니다.");
                        word.isActive = false;
                        words2.remove(word);
                        life--; // 조선군 텍스트를 쳤을시, 라이프 한개 감소
                        lbLife.setText("" + life);
                        break;
                    }
                }
                for (FallingWord3 word : words3) {
                    // 살아있는 단어에 대해서만 정답처리
                    if (word.isActive && word.getWord().equals(input)) {
                        System.out.println("정답입니다.");
                        word.isActive = false;
                        words3.remove(word);
                        score += 200; // 200점 증가
                        lbScore.setText("" + score);
                        break;
                    }
                }

                // 단어를 제거한 이후, 더이상 남은 단어가 없다면?
                if(words.size()==0&&words2.size()==0&&words3.size()==0) {
                    isPlaying = false;
                }
            }
        });

        // 시작버튼 이벤트 처리
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnStart.getText().equals("일시정지")) {


                } else {
                    startGame();
                    btnStart.setText("후퇴란 없다");
                }
                // btnStart.setVisible(false); // 안보이게 할 수도
            }
        });
    }

        private void startGame() {
        generateWords(); // 단어 생성
        isPlaying = true; // 게임 시작
        timer = new Timer(1000, new PeriodicTask() ); //1초 주기 타이머 객체 생성
        timer.start(); // 타이머 시작
    }

    // 단어를 생성하고 쓰레드를 시작시키는 기능 수행 1,2,3각자 체크
    private void generateWords() {
        for (int i = 0; i < 15; i++) {
            words.add( new FallingWord() );
            words.get(i).start();
        }
        for (int i = 0; i < 5; i++) {
            words2.add( new FallingWord2() );
            words2.get(i).start();
        }
        for (int i = 0; i < 1; i++) {
            words3.add( new FallingWord3() );
            words3.get(i).start();
        }
    }

    // Timer 객체에 전달할 ActionListener 인터페이스를 구현한 객체
    // 지정된 주기마다 타이머 이벤트 발생하고 actionPerformed()호출됨
    private class PeriodicTask implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            // 주기적 타이머 이벤트 발생시마다 호출되는 메소드
            if( !isPlaying ) {
                timer.stop();
                System.out.println("타이머 종료");
            }
            // 여러가지 작업을 할 수 있겠지만, 단순히 주기마다 숫자 올라가는 예제
            elapsed++;
            repaint();
        }
    }


    // lifepanel 상에 있는 heart 레이블 업데이트
    private void updateLifeImage() {
        lifepanel.removeAll();
        for (int i = 0; i < life; i++) {
            lifepanel.add(new JLabel(new ImageIcon(imgHeart)));
        }
        validate();
        repaint();
    }
    //life에 따른 icon 레이블 업데이트
    private void updateCharacterImage() {
        labelCharacter.setIcon( icons[5-life] );
        validate();
        repaint();
    }

    public class FallingWord extends Thread{ //나만의 움직이는 문자열
        String word;
        int x, y;
        Random rand = new Random();
        boolean isActive = false; // 단어가 살아있는 상태인지 관리하는 변수

        public FallingWord() { // 가로 자표와, 출발 딜레이를 매개변수로 할수도
            word = wordpool[ rand.nextInt(wordpool.length) ];// 랜덤하게 단어풀에서 골라서 지정
            y = 0; // 세로 시작 위치
            x = rand.nextInt(WordGame.WIDTH - 100); // 랜덤 가로 시작위치. -100은 단어길이만큼 오른쪽 여유 적당히
        }

        @Override
        public void run() {
            // 게임이 진행중이면서 단어를 맞추지 못한 상태면 바닥에 닿을때까지 떨어짐. 즉, y++
            try {
                sleep(rand.nextInt(9000)); //초기 랜덤 쉬는 시간
                isActive = true;

                while(isPlaying && isActive) {
                    y++; // 계속 아래로 이동

                    if (mainpanel.getHeight() != 0 && y >= mainpanel.getHeight()) {
                        System.out.println("바닥 도착");
                        isActive = false; // isActive가 false이면 안 그리도록 처리 mainpanel에서
                        life--; // 생명 감소
                        lbLife.setText("" + life);
                        words.remove(this);

                    }

                    repaint(); // 화면 업데이트
                    sleep(50); // 쉬는 시간. 단위는 ms

                    if( life <= 0 ||words.isEmpty() ){ //.isEmpty()대신 words.size()==0도 가능
                        isPlaying = false;
                    }
                }
            } catch (Exception e) {    }
        }

        public String getWord() {
            return word;
        }
        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
    }
    private class FallingWord2 extends Thread{ //나만의 움직이는 문자열
        String word;
        int x, y;
        Random rand = new Random();
        boolean isActive = false; // 단어가 살아있는 상태인지 관리하는 변수

        public FallingWord2() { // 가로 자표와, 출발 딜레이를 매개변수로 할수도
            word = wordpool2[ rand.nextInt(wordpool2.length) ];// 랜덤하게 단어풀에서 골라서 지정
            y = 0; // 세로 시작 위치
            x = rand.nextInt(WordGame.WIDTH - 100); // 랜덤 가로 시작위치. -100은 단어길이만큼 오른쪽 여유 적당히
        }

        @Override
        public void run() {
            // 게임이 진행중이면서 단어를 맞추지 못한 상태면 바닥에 닿을때까지 떨어짐. 즉, y++
            try {
                sleep(rand.nextInt(9000)); //초기 랜덤 쉬는 시간
                isActive = true;

                while(isPlaying && isActive) {
                    y++; // 계속 아래로 이동

                    if (mainpanel.getHeight() != 0 && y >= mainpanel.getHeight()) {
                        System.out.println("바닥 도착");
                        isActive = false; // isActive가 false이면 안 그리도록 처리 mainpanel에서
                        //lbLife.setText("" + life);
                        words2.remove(this);

                    }

                    repaint(); // 화면 업데이트
                    sleep(50); // 쉬는 시간. 단위는 ms

                    if( life <= 0  ) { //.isEmpty()대신 words.size()==0도 가능
                        isPlaying = false;
                    }
                }
            } catch (Exception e) {    }
        }

        public String getWord() {
            return word;
        }
        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
    }
    public class FallingWord3 extends Thread{ //나만의 움직이는 문자열
        String word;
        int x, y;
        Random rand = new Random();
        boolean isActive = false; // 단어가 살아있는 상태인지 관리하는 변수

        public FallingWord3() { // 가로 자표와, 출발 딜레이를 매개변수로 할수도
            word = wordpool3[ rand.nextInt(wordpool3.length) ];// 랜덤하게 단어풀에서 골라서 지정
            y = 0; // 세로 시작 위치
            x = rand.nextInt(WordGame.WIDTH - 100); // 랜덤 가로 시작위치. -100은 단어길이만큼 오른쪽 여유 적당히
        }

        @Override
        public void run() {
            // 게임이 진행중이면서 단어를 맞추지 못한 상태면 바닥에 닿을때까지 떨어짐. 즉, y++
            try {
                sleep(rand.nextInt(9000)); //초기 랜덤 쉬는 시간
                isActive = true;

                while(isPlaying && isActive) {
                    y++; // 계속 아래로 이동

                    if (mainpanel.getHeight() != 0 && y >= mainpanel.getHeight()) {
                        System.out.println("바닥 도착");
                        isActive = false; // isActive가 false이면 안 그리도록 처리 mainpanel에서
                        life--; // 생명 감소
                        lbLife.setText("" + life);
                        words3.remove(this);

                    }

                    repaint(); // 화면 업데이트
                    sleep(50); // 쉬는 시간. 단위는 ms

                    if( life <= 0){ //.isEmpty()대신 words.size()==0도 가능
                        isPlaying = false;
                    }
                }
            } catch (Exception e) {    }
        }

        public String getWord() {
            return word;
        }
        public int getX() {
            return x;
        }
        public int getY() {
            return y;
        }
    }

    class GamePanel extends JPanel { //implements MouseListener 이렇게도 가능
        //배경화면을 움직여주는 Thread생성
        public GamePanel(){
            setFocusable(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        backX--;
                        repaint();
                        try {
                            Thread.sleep(250);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        }
        @Override
        public void paintComponent(Graphics g) { // repaint()할 때마다 호출됨
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            Graphics2D g3 = (Graphics2D) g;

            g.drawImage(backmoveImg,backX,0,this);

            // 배경이미지 그림!
            ImageIcon imageIcon = new ImageIcon("images/background.jpg");
            Image image1 = imageIcon.getImage();
            //g2.drawImage(image1, 0, 0, this);


            //히트콤보 아이콘
            ImageIcon hitIc = new ImageIcon("images/hit.jpg");
            Image hitIcImage = hitIc.getImage();

            //이순신 기본 이미지 그림!
            Image character1 = characterIc[0].getImage();

            //생명이 줄때마다 적군의 이미지가 바뀜
            Image image2 = enemyIc[5-life].getImage();


            //시작화면에서 파란글씨 설명
            Font font4 = new Font("SansSerif", Font.BOLD, 30);
            g2.setFont(font4);
            g2.setColor(Color.RED);
            FontMetrics fm4 = getFontMetrics(font4);
            int width4 = fm4.stringWidth("빨간글씨를 맞추시오" );
            g.drawString("빨간글씨를 맞추시오", 0, 400);

            //시작화면에서 파란글씨 설명
            Font font5 = new Font("SansSerif", Font.BOLD, 30);
            g2.setFont(font5);
            g2.setColor(Color.BLUE);
            FontMetrics fm5 = getFontMetrics(font5);
            int width5 = fm5.stringWidth("파란글씨는 아군(목숨감소)" );
            g.drawString("파란글씨는 아군(목숨감소)", 0, 430);

            //시작화면에서 노란글씨 설명
            Font font6 = new Font("SansSerif", Font.BOLD, 20);
            g2.setFont(font6);
            g2.setColor(Color.YELLOW);
            FontMetrics fm6 = getFontMetrics(font6);
            int width6 = fm6.stringWidth("*노란색은 적장(200점 추가)*");
            g.drawString("*노란색은 적장(200점 추가)*", 0, 460);



            //생명 이미지 업데이트 (상단 패널에 있는 레이블만처리)
            updateLifeImage();
            updateCharacterImage();

            //게임시작시 배경바뀜 및 이미지 추가
            if(btnStart.getText().equals("후퇴란 없다")){
                g.drawImage(backmoveImg3,backX,0,this);
                // 경과시간 표시해보기
                g.setColor(Color.WHITE);
                g.drawString("조선함락까지 남은시간: " + elapsed, 10, 20);
                Font font2 = new Font("SansSerif", Font.BOLD, 50);
                g2.setFont(font2);
                g2.setColor(Color.BLACK);
                FontMetrics fm1 = getFontMetrics(font2);
                int width1 = fm1.stringWidth(score/100+ " HIT !!" );
                g.drawString(score/100+"   HIT !!", 0, 400);
                g2.drawImage(character1, 55*life, 480, this);//이순신 캐릭터 불러오기
                g2.drawImage(image2, 55*life+230, 450+10*(7-life), this);//적군이미지 불러오기


            }
            //게임에서 졌을때 엔딩 크레딧
            if(life==0){
                g.drawImage(backmoveImg5,backX,0,this);
                ImageIcon gameoverIc = new ImageIcon("images/gameover.jpg");
                Image image4 = gameoverIc.getImage();
                g2.drawImage(image2, 55*life+230, 450+10*(7-life), this);
                g2.drawImage(image4, 90, 100, this);
                Image character2 = characterIc[5].getImage();
                g2.drawImage(character2, 55*life, 480, this);//이순신 캐릭터만 불러오기
            }
            //300점을 받았을때 팝업 이벤트
            if(score==300&&life!=0){
                g.drawImage(backmoveImg2,backX,0,this);
                g2.drawImage(image2, 55*life+230, 450+10*(7-life), this);
                ImageIcon score300Ic = new ImageIcon("images/score300.jpg");
                Image image8 = score300Ic.getImage();
                g2.drawImage(image8, 90, 100, this);
                Image character2 = characterIc[1].getImage();
                g2.drawImage(character2, 55*life, 480, this);
                g2.drawImage(hitIcImage, 55*life+180, 450, this);//히트 이미지 추가시키기

            }
            //500점을 받았을때 팝업 이벤트
            if(score==500&&life!=0){
                g.drawImage(backmoveImg2,backX,0,this);
                g2.drawImage(image2, 55*life+230, 450+10*(7-life), this);
                ImageIcon score500Ic = new ImageIcon("images/score500.jpg");
                Image image5 = score500Ic.getImage();
                g2.drawImage(image5, 90, 100, this);
                Image character2 = characterIc[2].getImage();
                g2.drawImage(character2, 55*life, 480, this);
                g2.drawImage(hitIcImage, 55*life+180, 450, this);//히트 이미지 두개 만들기
                g2.drawImage(hitIcImage, 55*life+180, 500, this);

            }
            //1000점을 받았을때 팝업 이벤트
            if(score==1000&&life!=0){
                g.drawImage(backmoveImg2,backX,0,this);
                g2.drawImage(image2, 55*life+230, 450+10*(7-life), this);
                ImageIcon score1000Ic = new ImageIcon("images/score1000.jpg");
                Image image6 = score1000Ic.getImage();
                g2.drawImage(image6, 90, 100, this);
                Image character2 = characterIc[3].getImage();
                g2.drawImage(character2, 55*life, 480, this);
                g2.drawImage(hitIcImage, 55*life+230, 450, this);//히트 이미지 3개
                g2.drawImage(hitIcImage, 55*life+230, 500, this);
                g2.drawImage(hitIcImage, 55*life+230, 550, this);
            }
            //1200점을 받았을때 팝업 이벤트
            if(score==1200&&life!=0){
                g.drawImage(backmoveImg4,backX,0,this);
                g2.drawImage(image2, 55*life+230, 450+10*(7-life), this);
                ImageIcon score1200Ic = new ImageIcon("images/score1200.jpg");
                Image image9 = score1200Ic.getImage();
                g2.drawImage(image9, 90, 100, this);
                Image character2 = characterIc[4].getImage();
                g2.drawImage(character2, 55*life, 300, this);//이순신 캐릭터 점프
                g2.drawImage(hitIcImage, 55*life+180, 450, this);//히트이미지 4개
                g2.drawImage(hitIcImage, 55*life+180, 500, this);
                g2.drawImage(hitIcImage, 55*life+180, 550, this);
                g2.drawImage(hitIcImage, 55*life+180, 600, this);

            }
            //게임을 이겼을때 엔딩 크레딧
            if(score==1500||!isPlaying && !btnStart.getText().equals("전쟁시작")&&life!=0){

                g.drawImage(backmoveImg6,backX,0,this);
                ImageIcon clearIc = new ImageIcon("images/clear.jpg");
                Image image7 = clearIc.getImage();
                g2.drawImage(image7, 90, 100, this);
                /*Image character3 = characterIc[0].getImage();
                g2.drawImage(character3, getWidth()/2-90, 480, this);*/
            }
            //적장을 베었을때 팝업 이벤트
            /*if(&!btnStart.getText().equals("전쟁시작")){
                ImageIcon eventBack = new ImageIcon("images/background_move4.jpg");
                Image image10 = eventBack.getImage();
                g2.drawImage(image10, 0, 0, this);
                ImageIcon bonusIc = new ImageIcon("images/bonus.jpg");
                Image image11 = bonusIc.getImage();
                g2.drawImage(image11, 90, 100, this);
                g2.drawImage(image2, 500, 450+10*(7-life), this);
                Image character2 = characterIc[6].getImage();
                g2.drawImage(character2, 400, 300, this);
                g2.drawImage(hitIcImage, 440, 450, this);
                g2.drawImage(hitIcImage, 440, 500, this);
                g2.drawImage(hitIcImage, 440, 550, this);
                g2.drawImage(hitIcImage, 440, 600, this);

            }*/
            //단어 그리기(일본군)
            for (FallingWord word : words) {
                if(word.isActive ) {
                    Font font2 = new Font("SansSerif", Font.BOLD, 15);
                    g2.setFont(font2);
                    g2.setColor(Color.RED);
                    FontMetrics fm1 = getFontMetrics(font2);
                    g2.drawString(word.getWord(), word.getX(), word.getY());
                }
            }
            //단어그리기(조선군)
            for (FallingWord2 word : words2) {
                if(word.isActive ) {
                    Font font2 = new Font("SansSerif", Font.BOLD, 15);
                    g3.setFont(font2);
                    g3.setColor(Color.BLUE);
                    FontMetrics fm1 = getFontMetrics(font2);
                    g3.drawString(word.getWord(), word.getX(), word.getY());
                }
            }
            //단어그리기(적장)
            for (FallingWord3 word : words3) {
                if(word.isActive ) {
                    Font font2 = new Font("SansSerif", Font.BOLD, 8);
                    g3.setFont(font2);
                    g3.setColor(Color.YELLOW);
                    FontMetrics fm1 = getFontMetrics(font2);
                    g3.drawString(word.getWord(), word.getX(), word.getY());
                }
            }


            // 종료조건 처리
            if( !isPlaying && !btnStart.getText().equals("전쟁시작") ) { //life<=0, words.size()==0, ...
                Font font3 = new Font("SansSerif", Font.BOLD, 20);
                g2.setFont(font3);
                g2.setColor(Color.WHITE);
                FontMetrics fm2 = getFontMetrics(font3);
                int width2 = fm2.stringWidth("처리한 적군은   " + score);
                g2.drawString("처리한 적군은  " + score/100 + "명 입니다", 300, 430);
            }

            // Toolkit.getDefaultToolkit().sync(); //그래픽 최신화시킴
        }
    }
}