package theater;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


//메인 프레임: 로그인 창 크기의 프레임: 로그인 크기의 프레임은 로그인 panel만 사용하기 때문에 프레임 안에 panel을 생성하였음
public class MainFrame extends JFrame {

	static Theater theater;			//영화관 객체
	static Screen screen;			//상영관 객체
	
	private JPanel LoginPanel; // 로그인 panel

	String TicketPath = "TICKET.png";

	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
		
		//DB 연결
		/*
		//진주 db
		String url = "jdbc:mariadb://localhost:3306/theater";			//마리아DB 드라이버가 메모리에 로딩됨
		String id = "root";												//마리아DB id
		String pw = "wlswn0408";										//마리아DB password
		*/
		
		String url = "jdbc:mariadb://192.168.0.9:3306/theater";
		//String url = "jdbc:mariadb://172.30.1.25:3306/theater";
		
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");						//드라이버 로드
			//Connection con = DriverManager.getConnection(url, id, pw);		//메모리에 로딩된 드라이버와 자바를 연결
			Connection con = DriverManager.getConnection(url, "hello", "1013");
			//Connection con = DriverManager.getConnection(url, "root", "1013");
			
			theater = new Theater(con);
			screen = new Screen(con);
		} catch (Exception e) {
			e.printStackTrace();
		}										//theater 생성
	}

	// 생성자
	public MainFrame() {

		// 메인 프레임 설정(로그인 프레임)
		setTitle("Theater Program"); // 프레임 title 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 370); // 프레임 크기 설정
		LoginPanel = new JPanel();
		LoginPanel.setBackground(Color.WHITE); // 프레임 배경 설정
		LoginPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(LoginPanel);
		LoginPanel.setLayout(null);

		// 티켓이미지(메인화면)라벨
		JLabel Ticket_main = new JLabel("티켓_메인화면");
		Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
		Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
		Ticket_main.setBounds(190, 10, 126, 80);
		LoginPanel.add(Ticket_main);

//		ImageIcon ticektIcon = new ImageIcon(MainFrame.class.getResource(TicketPath));				//ImageIcon 객체 생성
//		Image ticketImg = ticektIcon.getImage();																//ImageIcon에서 Image 추출
//		Image changedticektImg = ticketImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);					//추출된 Image의 크기를 조절하여 새로운 Image 객체 생성
//		ImageIcon changedticektIcon = new ImageIcon(changedticektImg);
//		Ticket_main.setIcon(changedticektIcon);

		// 아이디 라벨
		JLabel lbl_id = new JLabel("\uC544\uC774\uB514");
		lbl_id.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		lbl_id.setBounds(69, 159, 50, 24);
		LoginPanel.add(lbl_id);

		// 비밀번호 라벨
		JLabel lbl_pw = new JLabel("\uBE44\uBC00\uBC88\uD638");
		lbl_pw.setFont(new Font("맑은 고딕", Font.BOLD, 14));
		lbl_pw.setBounds(54, 212, 65, 24);
		LoginPanel.add(lbl_pw);

		// 아이디 입력 텍스트필드
		JTextField tf_id = new JTextField();
		tf_id.setBounds(131, 155, 185, 34);
		LoginPanel.add(tf_id);
		tf_id.setColumns(10);

		// 비밀번호 입력 텍스트필드
		JTextField tf_pw = new JTextField();
		tf_pw.setColumns(10);
		tf_pw.setBounds(131, 208, 185, 34);
		LoginPanel.add(tf_pw);

		// 로그인 버튼
		JButton btn_Login = new JButton("\uB85C\uADF8\uC778");
		btn_Login.setBackground(Color.LIGHT_GRAY);
		btn_Login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//로그인 버튼 이벤트
				//1. 입력한 id와 pw를 가져온다.
				String id = tf_id.getText();
				String pw = tf_pw.getText();
				
				//2. 만약 입력한 id와 비밀번호가 관리자의 계정이라면
				//관리자 아이디: yk0309, 관리자 비밀번호: 930309
				if (id.equals("yk0309")) {
					try {
						boolean tryLogin = MainFrame.theater.logIn(id, pw);
						
						if (tryLogin)
						{
							//3. 관리자 frame을 생성하고
							ManagerFrame manager = new ManagerFrame();
							manager.setVisible(true);

							// 4. 로그인 frame을 닫는다.
							dispose();
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else if (id.equals("")) {
					//5. 만약 입력한 아이디가 공백이라면
					//6. 알림메시지 발생
					JOptionPane.showMessageDialog(null, "아이디를 입력해주세요.", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
				} else if (pw.equals("")) {
					// 5. 만약 입력한 비밀번호가 공백이라면
					// 6. 알림메시지 발생
					JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요.", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
				} else {
					
					// 5. 만약 관리자 계정이 아니라면
					// 6. theater의 로그인 함수를 실행시킨다.
					try {
						boolean tryLogin = MainFrame.theater.logIn(id, pw);
						
						if (tryLogin)
						{
							// 7. 만약 로그인이 성공하였다면(아이디가 존재하며, 비밀번호가 일치하다면)
							// 8. 클라이언트 frame을 생성하고
							ClientFrame client = new ClientFrame();
							client.setVisible(true);
							// 9. 로그인 frame을 닫는다.
							dispose();	
						}
						else {
							//10. 로그인에 실패하였다면
							//11. 알림메시지 발생
							JOptionPane.showMessageDialog(null, "아이디나 비밀번호를 확인해주세요.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btn_Login.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		btn_Login.setBounds(346, 148, 79, 41);
		LoginPanel.add(btn_Login);

		// 회원가입버튼
		JButton btn_SignUp = new JButton("\uD68C\uC6D0\uAC00\uC785");
		btn_SignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 회원가입 버튼 이벤트
				// 1. 회원가입 프레임을 생성한다.
				SignUpFrame SignUp = new SignUpFrame();
				SignUp.setVisible(true);
			}
		});
		btn_SignUp.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
		btn_SignUp.setBackground(Color.LIGHT_GRAY);
		btn_SignUp.setBounds(346, 202, 79, 41);
		LoginPanel.add(btn_SignUp);

	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.drawRect(40, 135, 430, 185);
		
	}

}

//회원가입 프레임: 회원가입 크기의 프레임은 회원가입 panel만 사용하기 때문에 프레임 안에 panel을 생성하였음
class SignUpFrame extends JFrame {

	private JPanel SignUpPanel; // 회원가입 panel

	// 생성자
	public SignUpFrame() {

		// 회원가입 프레임 설정
		setTitle("SignUp"); // 프레임 title 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 519);
		SignUpPanel = new JPanel();
		SignUpPanel.setBackground(Color.WHITE);
		SignUpPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(SignUpPanel);
		SignUpPanel.setLayout(null);

		// 이름 텍스트필드
		JTextField Name_field = new JTextField();
		Name_field.setBounds(103, 65, 158, 33);
		SignUpPanel.add(Name_field);
		Name_field.setColumns(10);

		// 아이디 텍스트필드
		JTextField Id_field = new JTextField();
		Id_field.setColumns(10);
		Id_field.setBounds(103, 178, 158, 33);
		SignUpPanel.add(Id_field);

		// 비밀번호 텍스트필드
		JTextField pw_field = new JTextField();
		pw_field.setColumns(10);
		pw_field.setBounds(103, 238, 158, 33);
		SignUpPanel.add(pw_field);

		// 비밀번호 확인 텍스트필드
		JTextField pwCheck_field = new JTextField();
		pwCheck_field.setColumns(10);
		pwCheck_field.setBounds(103, 298, 158, 33);
		SignUpPanel.add(pwCheck_field);

		// 생일 텍스트 필드
		JTextField birth_field = new JTextField();
		birth_field.setColumns(10);
		birth_field.setBounds(103, 358, 158, 33);
		SignUpPanel.add(birth_field);

		// 전화번호 텍스트필드
		JTextField Phone_field = new JTextField();
		Phone_field.setColumns(10);
		Phone_field.setBounds(103, 122, 158, 33);
		SignUpPanel.add(Phone_field);

		// 아이디 중복확인 버튼
		JButton CheckSame_Button = new JButton("\uC911\uBCF5\uD655\uC778");
		CheckSame_Button.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		CheckSame_Button.setBackground(Color.LIGHT_GRAY);
		CheckSame_Button.setBounds(283, 238, 99, 33);
		SignUpPanel.add(CheckSame_Button);
		CheckSame_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 아이디 중복 검사 버튼 이벤트
				// 1. 입력한 id를 가져온다.
				String id = Id_field.getText();

				// 2. 만약 입력한 아이디가 공백이라면
				if (id.equals("")) {
					// 3. 알림 메시지 발생
					JOptionPane.showMessageDialog(null, "아이디를 입력해주세요.", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
				} else {
					// 4. 공백이 아니라면
					try {
						// 5. 입력한 id가 존재하는지 확인한다.
						boolean isExist = MainFrame.theater.isExistId(id);

						// 6. 만약 id가 존재한다면
						if (isExist) {
							// 7. 아이디 중복 알림 메시지 발생
							JOptionPane.showMessageDialog(null, "아이디가 존재합니다.", "NOTICE",
									JOptionPane.INFORMATION_MESSAGE);

							// 8. 입력한 id 텍스트 필드 지우기
							Id_field.setText("");
						} else {
							// 9. 만약 id가 존재하지 않는다면
							// 10. 아이디 사용 가능 알림 메시지 발생
							JOptionPane.showMessageDialog(null, "사용가능한 아이디입니다.", "NOTICE",
									JOptionPane.INFORMATION_MESSAGE);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		// 회원가입 버튼
		JButton SignUp_button = new JButton("\uAC00\uC785");
		SignUp_button.setBackground(Color.LIGHT_GRAY);
		SignUp_button.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		SignUp_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 회원가입 버튼 이벤트
				// 1. 입력한 비밀번호를가져온다.
				String pw = pw_field.getText();
				String pw_check = pw_field.getText();
				// 2. 만약 입력한 비밀번호나 비밀번호 확인이 공백이라면
				if (pw.equals("") || pwCheck_field.getText().equals("")) {
					// 3. 알림 메시지 발생
					JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요.", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
				} else {
					// 4. 공백이 아니라면
					// 5. 비밀번호와 비밀번호 확인 데이터를 비교한다.
					if (pw.equals(pw_check)) {
						// 6. 만약 비밀번호와 비밀번호 확인 데이터가 일치한다면
						// 7. 입력한 다른 회원 정보를 가져온다. (이름, 전화번호, 생년월일, 아이디)
						String name = Name_field.getText();
						String phone = Phone_field.getText();
						String id = Id_field.getText();
						String birth = birth_field.getText();

						// 5. 입력한 데이터로 Person 객체를 만든다.
						Person person = new Person(name, phone, birth, id, pw);

						try {
							// 6. 회원가입 함수를 실행시킨다.
							MainFrame.theater.signUp(person);
						} catch (Exception e1) {
							e1.printStackTrace();
						}

						// 7. 회원가입 완료 알림 메시지 발생
						JOptionPane.showMessageDialog(null, "회원가입 되었습니다.", "NOTICE", JOptionPane.INFORMATION_MESSAGE);

						// 8. 입력한 텍스트필드를 모두 지운다.
						Name_field.setText("");
						Phone_field.setText("");
						pw_field.setText("");
						pwCheck_field.setText("");
						birth_field.setText("");
						Id_field.setText("");

						// 9. 회원가입 frame을 종료한다.
						dispose();
					} else {
						// 9. 비밀번호와 비밀번호 확인 데이터가 일치하지 않는다면
						// 10. 비밀번호 오류 알림 메시지 발생
						JOptionPane.showMessageDialog(null, "비밀번호 확인이 일치하지 않습니다.", "NOTICE", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		SignUp_button.setBounds(212, 423, 99, 33);
		SignUpPanel.add(SignUp_button);

		// 취소 버튼
		JButton Cancel_button = new JButton("\uCDE8\uC18C");
		Cancel_button.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		Cancel_button.setBackground(Color.LIGHT_GRAY);
		Cancel_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//회원가입 취소 버튼 이벤트
				//1. 텍스트필드를 모두 지운다.
				Name_field.setText("");
				Phone_field.setText("");
				pw_field.setText("");
				pwCheck_field.setText("");
				birth_field.setText("");
				Id_field.setText("");
				
				//2. 회원가입 frame을 종료한다.
				dispose();
			}
		});
		Cancel_button.setBounds(325, 423, 99, 33);
		SignUpPanel.add(Cancel_button);

		// 회원가입 타이틀 제목
		JLabel lbl_title = new JLabel("\uD68C\uC6D0\uAC00\uC785");
		lbl_title.setFont(new Font("맑은 고딕", Font.BOLD, 19));
		lbl_title.setBounds(24, 10, 102, 33);
		SignUpPanel.add(lbl_title);

		// 이름 라벨
		JLabel lbl_name = new JLabel("\uC774\uB984");
		lbl_name.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		lbl_name.setBounds(48, 70, 48, 21);
		SignUpPanel.add(lbl_name);

		// 아이디 라벨
		JLabel lbl_id = new JLabel("\uC544\uC774\uB514");
		lbl_id.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		lbl_id.setBounds(36, 242, 55, 21);
		SignUpPanel.add(lbl_id);

		// 비밀번호 라벨
		JLabel lbl_pw = new JLabel("\uBE44\uBC00\uBC88\uD638");
		lbl_pw.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		lbl_pw.setBounds(26, 302, 65, 21);
		SignUpPanel.add(lbl_pw);

		// 비밀번호 확인 라벨
		JLabel lbl_checkPw = new JLabel(
				"<html><body><center>\uBE44\uBC00\uBC88\uD638<br>\uD655\uC778</center></body></html>");
		lbl_checkPw.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		lbl_checkPw.setBounds(25, 359, 71, 33);
		SignUpPanel.add(lbl_checkPw);

		// 생일 라벨
		JLabel lbl_birth = new JLabel("\uC0DD\uB144\uC6D4\uC77C");
		lbl_birth.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		lbl_birth.setBounds(26, 180, 65, 21);
		SignUpPanel.add(lbl_birth);

		// 생일 6자리로 입력해라는 라벨
		JLabel sixbirth = new JLabel("\u203B6\uC790\uB9AC\uB85C \uC785\uB825");
		sixbirth.setForeground(Color.RED);
		sixbirth.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		sixbirth.setBounds(280, 183, 135, 15);
		SignUpPanel.add(sixbirth);

		// 전화 번호 라벨
		JLabel lbl_phoneNum = new JLabel("\uC804\uD654\uBC88\uD638");
		lbl_phoneNum.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		lbl_phoneNum.setBounds(24, 126, 65, 21);
		SignUpPanel.add(lbl_phoneNum);

	}
}

//사용자 프레임: 클라이언트 크기의 프레임은 panel을 여러개 사용하기 때문에 panel 교체 함수를 만들어 사용하였음
class ClientFrame extends JFrame {
	public ClientMainPanel clientMain = new ClientMainPanel(); // 클라이언트 메인 panel
	public MyPagePanel myPage = null; // 마이페이지 panel
	public DDayPlusMoviePanel ddayplusMovie = new DDayPlusMoviePanel(); // 상영예정작 panel
	public ReservePanel reserve = null; // 예매화면 panel
	public OtherMoviePanel otherMovie = new OtherMoviePanel(); // 영화 더보기 panel
	public ClientModifyPanel clientModify = new ClientModifyPanel();; // 회원 정보 수정 panel
	public TicketingPanel ticketing; // 티켓 예매 panel
	public SearchMoviePanel searchMovie = null; // 영화 검색 panel

	public ClientFrame() {
		// 1. ClientMainPanel 생성
		clientMain = new ClientMainPanel();
		// 2. ClientMainPanel 추가
		add(clientMain);

		// 사용자 프레임 설정
		setTitle("Theater Program"); // 프레임 title 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 501, 677); // 프레임 크기 설정
		setBackground(Color.WHITE); // 프레임 배경 설정
		setLayout(null); // 프레임 레이아웃 설정
		setVisible(true);
	}

	// panel 바꾸는 함수
	public void PanelChange(String panelName) {
		if (panelName.equals("ClientMainPanel")) {
			// 만약 바꿀 panel이 ClientMainPanel이라면
			getContentPane().removeAll();
			getContentPane().add(clientMain);
			revalidate();
			repaint();
		} else if (panelName.equals("MyPagePanel")) {
			// 만약 바꿀 panel이 MyPagePanel이라면
			getContentPane().removeAll();
			myPage = new MyPagePanel();
			getContentPane().add(myPage);
			revalidate();
			repaint();
		} else if (panelName.equals("DDayPlusMoviePanel")) {
			// 만약 바꿀 panel이 DDayPlusMoviePanel이라면
			getContentPane().removeAll();
			getContentPane().add(ddayplusMovie);
			revalidate();
			repaint();
		} else if (panelName.equals("ReservePanel")) {
			// 만약 바꿀 panel이 ReservePanel이라면
			getContentPane().removeAll();
			reserve = new ReservePanel();
			getContentPane().add(reserve);
			revalidate();
			repaint();
		} else if (panelName.equals("OtherMoviePanel")) {
			// 만약 바꿀 panel이 OtherMoviePanel이라면
			getContentPane().removeAll();
			getContentPane().add(otherMovie);
			revalidate();
			repaint();
		}
		else if (panelName.equals("ClientModifyPanel")) {
			getContentPane().removeAll();
			getContentPane().add(clientModify);
			revalidate();
			repaint();
		}
	}

	// 영화 제목으로 바로 예매하는 창으로 넘어가는 함수(ClientMain panel에서 예매 버튼을 누르면)
	public void ReserveSelectdMovie(String movieTitle) {
		getContentPane().removeAll();
		reserve = new ReservePanel(movieTitle);
		getContentPane().add(reserve);
		revalidate();
		repaint();
	}

	//영화 검색 결과를 보여주는 창으로 넘어가는 함수
	public void ShowSearchMovie(ResultSet rs) {
		getContentPane().removeAll();
		searchMovie = new SearchMoviePanel(rs);
		getContentPane().add(searchMovie);
		revalidate();
		repaint();
	}

	//좌석 예매 창으로 넘어가는 함수
	public void ChangeTicketingPanel(MovieNow movieNow)
	{
		getContentPane().removeAll();
		ticketing = new TicketingPanel(movieNow);
		getContentPane().add(ticketing);
		revalidate();
		repaint();
	}

	class ClientMainPanel extends JPanel {

		//현재 접속중인 회원 이름
		String userName = Theater.personNow.getName();

		// 예매 순위별 포스터 경로
		String FirstPosterPath;
		String SecondPosterPath;
		String ThirdPosterPath;
		String TicketPath;

		// 예매 순위별 영화 제목
		String FirstTitle;
		String SecondTitle;
		String ThirdTitle;

		private LineBorder bb = new LineBorder(Color.black, 1, true);

		public ClientMainPanel() {
			setBounds(0, 0, 501, 677);
			setBackground(Color.WHITE);
			setLayout(null);
			
			//영화 3개 선정하기
			Movie[] movies = new Movie[3];
			try {
				ResultSet rs = MainFrame.theater.selectRandomMovie();
				int i = 0;
				while(rs.next()) {
					String title = rs.getString("title");
					int runTime = rs.getInt("runTime");
					String posterURL = rs.getString("posterURL");
					String dday = rs.getString("dday");
					
					Movie movie = new Movie(title, runTime, posterURL, dday);
					movies[i] = movie;
					i++;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
			//오늘의 영화 3개 등록
			FirstPosterPath = movies[0].getPosterURL();
			SecondPosterPath = movies[1].getPosterURL();
			ThirdPosterPath = movies[2].getPosterURL();
			FirstTitle = movies[0].getTitle();
			SecondTitle = movies[1].getTitle();
			ThirdTitle = movies[2].getTitle();

			
			// 티켓이미지(메인화면)라벨
			JLabel Ticket_main = new JLabel("티켓_메인화면");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 티켓 이미지 클릭 리스터
					// 1. ManagerMain panel로 변경한다.
					PanelChange("ManagerMainPanel");
				}
			});

			// 접속 중인 회원 이름 표시 라벨
			JLabel lblUserName = new JLabel(userName + "님  /");
			//JLabel lblUserName = new JLabel(userName + "\uB2D8 /");
			lblUserName.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblUserName.setBounds(350, 83, 66, 15);
			add(lblUserName);

			// 로그아웃 라벨
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblLogout.setBounds(420, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 로그아웃 라벨 클릭 리스터
					// 1. 로그아웃 알림 메시지 발생
					//2. 로그아웃을 한다.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)을 생성한다.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame을 제거한다.
					dispose();
				}
			});

			// 예매 상단바 라벨
			JLabel lbl_ticketing = new JLabel("\uC608\uB9E4");
			lbl_ticketing.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_ticketing.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_ticketing.setBorder(bb);
			lbl_ticketing.setBounds(0, 109, 162, 42);
			add(lbl_ticketing);
			lbl_ticketing.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 예매 카테고리 클릭 리스너
					// 1. Reserve panel로 변경한다.
					PanelChange("ReservePanel");
				}
			});

			// 개봉 예정 영화 상단바 라벨
			JLabel lbl_scheduleMovie = new JLabel("\uAC1C\uBD09\uC608\uC815\uC601\uD654");
			lbl_scheduleMovie.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_scheduleMovie.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_scheduleMovie.setBorder(bb);
			lbl_scheduleMovie.setBounds(161, 109, 164, 42);
			add(lbl_scheduleMovie);
			lbl_scheduleMovie.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 상영예정작 클릭 리스터
					// 1. DDayPlusMovie panel로 변경한다.
					PanelChange("DDayPlusMoviePanel");
				}
			});

			// 마이페이지 상단바 라벨
			JLabel lbl_myPage = new JLabel("\uB9C8\uC774\uD398\uC774\uC9C0");
			lbl_myPage.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_myPage.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_myPage.setBorder(bb);
			lbl_myPage.setBounds(324, 109, 162, 42);
			add(lbl_myPage);
			lbl_myPage.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 마이페이지 클릭 리스너
					// 1. 마이페이지 panel로 변경한다.
					PanelChange("MyPagePanel");
				}
			});

			// 오늘의 영화 더보기 라벨
			JLabel lblAddLanking = new JLabel("\u25B6\uB354\uBCF4\uAE30");
			lblAddLanking.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
			lblAddLanking.setBounds(414, 218, 62, 15);
			add(lblAddLanking);
			lblAddLanking.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 오늘의 영화 더보기 클릭 리스터
					// 1. OtherMovie panel로 변경한다.
					PanelChange("OtherMoviePanel");
				}
			});

			// 오늘의 영화 라벨
			JLabel lbl_title = new JLabel("\u2665 \uC624\uB298\uC758 \uC601\uD654 \u2665");
			lbl_title.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_title.setFont(new Font("맑은 고딕", Font.BOLD, 18));
			lbl_title.setBounds(0, 175, 486, 34);
			add(lbl_title);

			// 영화 포스터(이미지X 이미지는 밑에서 설정)
			JLabel lblFirstPoster = new JLabel("영화 포스터1");
			lblFirstPoster.setIcon(new ImageIcon("C:\\Users\\82104\\OneDrive\\Desktop\\\uB77C\uB77C\uB79C\uB4DC.jpg"));
			lblFirstPoster.setHorizontalAlignment(SwingConstants.CENTER);
			lblFirstPoster.setBounds(32, 265, 118, 160);
			add(lblFirstPoster);

			JLabel lblSecondPoster = new JLabel("영화포스터2");
			lblSecondPoster.setIcon(new ImageIcon("C:\\Users\\82104\\OneDrive\\Desktop\\\uC54C\uB77C\uB518.jpg"));
			lblSecondPoster.setHorizontalAlignment(SwingConstants.CENTER);
			lblSecondPoster.setBounds(190, 265, 118, 160);
			add(lblSecondPoster);

			JLabel lblThirdPoster = new JLabel("영화포스터3");
			lblThirdPoster
					.setIcon(new ImageIcon("C:\\Users\\82104\\OneDrive\\Desktop\\\uC704\uB300\uD55C\uC1FC\uB9E8.jpg"));
			lblThirdPoster.setHorizontalAlignment(SwingConstants.CENTER);
			lblThirdPoster.setBounds(348, 265, 118, 160);
			add(lblThirdPoster);

			// 영화 제목 라벨
			JLabel lblFirstTitle = new JLabel("제목1");
			lblFirstTitle.setHorizontalAlignment(SwingConstants.CENTER);
			lblFirstTitle.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblFirstTitle.setBounds(42, 435, 108, 15);
			lblFirstTitle.setText(FirstTitle);
			add(lblFirstTitle);

			JLabel lblSecondTitle = new JLabel("제목2");
			lblSecondTitle.setHorizontalAlignment(SwingConstants.CENTER);
			lblSecondTitle.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblSecondTitle.setBounds(200, 435, 108, 15);
			lblSecondTitle.setText(SecondTitle);
			add(lblSecondTitle);

			JLabel lblThirdTitle = new JLabel("제목3");
			lblThirdTitle.setHorizontalAlignment(SwingConstants.CENTER);
			lblThirdTitle.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblThirdTitle.setBounds(358, 435, 108, 15);
			lblThirdTitle.setText(ThirdTitle);
			add(lblThirdTitle);

			// 예매버튼
			JButton btnFirstBooking = new JButton("예매");
			btnFirstBooking.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btnFirstBooking.setForeground(new Color(255, 255, 255));
			btnFirstBooking.setBackground(new Color(255, 69, 0));
			btnFirstBooking.setBounds(67, 460, 60, 20);
			add(btnFirstBooking);
			btnFirstBooking.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 첫번째 오늘의 영화 예매 버튼 이벤트
					// 1. 첫번째 오늘의 영화의 제목을 가져온다.
					String reserveTitle = lblFirstTitle.getText();
					//String title = movies[0].getTitle();
					// 2. 가져온 제목을 파라미터로 예매 화면 panel로 바꾼다.
					ReserveSelectdMovie(reserveTitle);
				}
			});

			JButton btnSecondBooking = new JButton("예매");
			btnSecondBooking.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btnSecondBooking.setForeground(Color.WHITE);
			btnSecondBooking.setBackground(new Color(255, 69, 0));
			btnSecondBooking.setBounds(227, 460, 60, 20);
			add(btnSecondBooking);
			btnSecondBooking.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 두번째 오늘의 영화 예매 버튼 이벤트
					// 1. 두번째 오늘의 영화의 제목을 가져온다.
					String reserveTitle = lblSecondTitle.getText();
					//String title = movies[1].getTitle();
					// 2. 가져온 제목을 파라미터로 예매 화면 panel로 바꾼다.
					ReserveSelectdMovie(reserveTitle);
				}
			});

			JButton btnThirBooking = new JButton("예매");
			btnThirBooking.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btnThirBooking.setForeground(Color.WHITE);
			btnThirBooking.setBackground(new Color(255, 69, 0));
			btnThirBooking.setBounds(385, 460, 60, 20);
			add(btnThirBooking);
			btnThirBooking.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 세번째 오늘의 영화 예매 버튼 이벤트
					// 1. 세번째 오늘의 영화의 제목을 가져온다.
					String reserveTitle = lblThirdTitle.getText();
					//String title = movies[2].getTitle();
					// 2. 가져온 제목을 파라미터로 예매 화면 panel로 바꾼다.
					ReserveSelectdMovie(reserveTitle);
				}
			});

			// 영화 검색 텍스트필드
			JTextField tfSearch = new JTextField();
			tfSearch.setBounds(12, 589, 384, 30);
			add(tfSearch);
			tfSearch.setColumns(10);

			// 영화 검색 버튼
			JButton btnSearch = new JButton("검색");
			btnSearch.setBackground(Color.LIGHT_GRAY);
			btnSearch.setBounds(408, 588, 70, 30);
			add(btnSearch);
			btnSearch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 영화 검색 버튼 이벤트
					// 1. 텍스트필드에 입력받은 데이터를 가져온다.
					String keyword = tfSearch.getText();
					// 2. 만약 입력받은 텍스트가 공백이라면
					if (keyword.equals("")) {
						// 3. 알림 메시지를 발생시킨다.
						JOptionPane.showMessageDialog(null, "검색할 영화 제을 입력해주세요..", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
					} else {
						// 4. 만약 입력받은 텍스트가 공백이 아니라면
						try {
							// 5. 입력받은 키워드로 영화를 검색한다.
							ResultSet rs = MainFrame.theater.searchMovie(keyword);
							
							// 6. 텍스트필드를 지우고
							tfSearch.setText("");
							
							// 7. SearchMovie panel로 변경한다.	
							// 이때, 입력받은 키워드의 검색 결과를 매개변수로 전달한다.
							ShowSearchMovie(rs);
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});

			// 영화 포스터 표시
			// 영화 포스터 크기 설정
			// ImageIcon의 크기를 품질이 깨지지 않고 변환하기 위해 Image로 변환하여 크기를 설정함
			// Image 함수인 getScaledInstance()를 통해 품질을 유지한 채 사이즈 변경
			// 사용을 위해 다시 ImageIcon으로 변환
			Image FirstImg = null;
			Image SecondImg = null;
			Image ThirdImg = null;
			try {
				URL url1 = new URL(FirstPosterPath);
				FirstImg = ImageIO.read(url1);
				URL url2 = new URL(SecondPosterPath);
				SecondImg = ImageIO.read(url2);
				URL url3 = new URL(ThirdPosterPath);
				ThirdImg = ImageIO.read(url3);
			} catch (MalformedURLException e1) {
				System.out.println(e1.getMessage());
			} catch (IOException e1) {
				System.out.println(e1.getMessage());
			}
			Image changedFirstImg = FirstImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);					//추출된 Image의 크기를 조절하여 새로운 Image 객체 생성
			ImageIcon changedFirstIcon = new ImageIcon(changedFirstImg);
			lblFirstPoster.setIcon(changedFirstIcon);
			
			Image changedSecondImg = SecondImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);					//추출된 Image의 크기를 조절하여 새로운 Image 객체 생성
			ImageIcon changedSecondIcon = new ImageIcon(changedSecondImg);
			lblSecondPoster.setIcon(changedSecondIcon);
			
			Image changedThirdImg = ThirdImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);					//추출된 Image의 크기를 조절하여 새로운 Image 객체 생성
			ImageIcon changedThirdIcon = new ImageIcon(changedThirdImg);
			lblThirdPoster.setIcon(changedThirdIcon);

			// 영화 검색 라벨
			JLabel lbl_search = new JLabel("\uC601\uD654 \uAC80\uC0C9");
			lbl_search.setFont(new Font("맑은 고딕", Font.BOLD, 18));
			lbl_search.setBounds(200, 535, 96, 34);
			add(lbl_search);
		}
	}

	class MyPagePanel extends JPanel {

		//현재 접속중인 사용자 정보
		String userName = Theater.personNow.getName();
		String userPhoneNum = Theater.personNow.getPhone();
		String userId = Theater.personNow.getId();
		String userBirth = Theater.personNow.getBirth();

		int selectedInfoIndex;
		ReservInfo[] reservInfos;
		//String[] string;
		
		Vector<String> reserveVector = new Vector<>();
		JList<String> reservelist;
		
		String TicketPath = "TICKET.png";

		private LineBorder bb = new LineBorder(Color.black, 1, true);

		public MyPagePanel() {
			setBounds(0, 0, 501, 677);
			setBackground(Color.WHITE);
			setLayout(null);

			// 티켓이미지(메인화면)라벨
			JLabel Ticket_main = new JLabel("티켓_메인화면");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 티켓 이미지 클릭 리스터
					// 1. ClientMain panel로 변경한다.
					PanelChange("ClientMainPanel");
				}
			});

//			// ImageIcon 객체 생성 => Image 추출 및 크기 조절해서 새로운 객체 생성
//			ImageIcon ticektIcon = new ImageIcon(MainFrame.class.getResource(TicketPath));
//			Image ticketImg = ticektIcon.getImage();
//			Image changedticektImg = ticketImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);
//			ImageIcon changedticektIcon = new ImageIcon(changedticektImg);
//			Ticket_main.setIcon(changedticektIcon);
			
			// 예매 카테고리
			JLabel lbl_ticketing = new JLabel("\uC608\uB9E4");
			lbl_ticketing.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_ticketing.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_ticketing.setBorder(bb);
			lbl_ticketing.setBounds(0, 109, 162, 42);
			add(lbl_ticketing);
			lbl_ticketing.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 예매 카테고리 클릭 리스너
					// 1. Reserve panel로 변경한다.
					PanelChange("ReservePanel");
				}
			});

			// 예매예정 카테고리
			JLabel lbl_scheduleMovie = new JLabel("\uAC1C\uBD09\uC608\uC815\uC601\uD654");
			lbl_scheduleMovie.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_scheduleMovie.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_scheduleMovie.setBorder(bb);
			lbl_scheduleMovie.setBounds(161, 109, 164, 42);
			add(lbl_scheduleMovie);
			lbl_scheduleMovie.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 상영예정작 클릭 리스터
					// 1. DDayPlusMovie panel로 변경한다.
					PanelChange("DDayPlusMoviePanel");
				}
			});

			// 마이페이지 카테고리
			JLabel lbl_myPage = new JLabel("\uB9C8\uC774\uD398\uC774\uC9C0");
			lbl_myPage.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_myPage.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_myPage.setBorder(bb);
			lbl_myPage.setBounds(324, 109, 162, 42);
			add(lbl_myPage);
			lbl_myPage.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 마이페이지 클릭 리스너
					// 1. 마이페이지 panel로 변경한다.
					PanelChange("MyPagePanel");
				}
			});

			// 접속 중인 회원 이름 표시 라벨
			JLabel lblUserName = new JLabel(userName + "님  /");
			lblUserName.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblUserName.setBounds(350, 83, 66, 15);
			add(lblUserName);

			// 로그아웃 라벨
			JLabel lblLogout = new JLabel("로그아웃");
			lblLogout.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblLogout.setBounds(420, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 로그아웃 라벨 클릭 리스터
					// 1. 로그아웃 알림 메시지 발생
					//2. 로그아웃을 한다.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)을 생성한다.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame을 제거한다.
					dispose();
				}
			});

			// 환영합니다 라벨
			JLabel lbl_welcome = new JLabel("\uD658\uC601\uD569\uB2C8\uB2E4");
			lbl_welcome.setFont(new Font("맑은 고딕", Font.BOLD, 18));
			lbl_welcome.setBounds(197, 175, 117, 34);
			add(lbl_welcome);

			// 개인정보확인 라벨
			JLabel lbl_personCheck = new JLabel("\uAC1C\uC778\uC815\uBCF4\uD655\uC778");
			lbl_personCheck.setFont(new Font("맑은 고딕", Font.BOLD, 14));
			lbl_personCheck.setBounds(43, 232, 86, 15);
			add(lbl_personCheck);

			// 이름 라벨
			JLabel lbl_name = new JLabel("\uC774\uB984");
			lbl_name.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lbl_name.setBounds(43, 259, 50, 15);
			add(lbl_name);

			// 전화번호 라벨
			JLabel labelphoneNum = new JLabel("\uC804\uD654\uBC88\uD638");
			labelphoneNum.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			labelphoneNum.setBounds(43, 282, 50, 15);
			add(labelphoneNum);

			// 아이디 라벨
			JLabel lbl_id = new JLabel("\uC544\uC774\uB514");
			lbl_id.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lbl_id.setBounds(43, 305, 50, 15);
			add(lbl_id);

			// 생일라벨
			JLabel lbl_birth = new JLabel("\uC0DD\uB144\uC6D4\uC77C");
			lbl_birth.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lbl_birth.setBounds(43, 328, 66, 15);
			add(lbl_birth);

			// 수정버튼
			JButton btn_modify = new JButton("\uC218\uC815\r\n");
			btn_modify.setBackground(Color.LIGHT_GRAY);
			btn_modify.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btn_modify.setBounds(348, 345, 91, 23);
			add(btn_modify);
			btn_modify.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 수정 버튼 이벤트
					// 1. ClientModify panel로 변경한다.
					PanelChange("ClientModifyPanel");
				}
			});

			// 예매확인라벨
			JLabel lbl_ticketCheck = new JLabel("\uC608\uB9E4\uD655\uC778");
			lbl_ticketCheck.setFont(new Font("맑은 고딕", Font.BOLD, 14));
			lbl_ticketCheck.setBounds(43, 408, 86, 15);
			add(lbl_ticketCheck);

			// 이름 라벨 답
			JLabel lbl_a_name = new JLabel(userName);
			lbl_a_name.setBounds(105, 259, 100, 15);
			add(lbl_a_name);

			// 전화번호 라벨 답
			JLabel lbl_a_phoneNum = new JLabel(userPhoneNum);
			lbl_a_phoneNum.setBounds(105, 284, 100, 15);
			add(lbl_a_phoneNum);

			// 아이디 라벨 답
			JLabel lbl_a_id = new JLabel(userId);
			lbl_a_id.setBounds(105, 305, 100, 15);
			add(lbl_a_id);

			// 생일 라벨 답
			JLabel lbl_a_birth = new JLabel(userBirth);
			lbl_a_birth.setBounds(105, 330, 100, 15);
			add(lbl_a_birth);
			
			// 삭제 버튼
			JButton btn_delete = new JButton("\uC0AD\uC81C");
			btn_delete.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btn_delete.setBackground(Color.LIGHT_GRAY);
			btn_delete.setBounds(368, 592, 91, 23);
			add(btn_delete);
			btn_delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//예매 정보 삭제 버튼 이벤트
					//1. 삭제할 예매 정보를 가져온다.
					ReservInfo selectedInfo = reservInfos[selectedInfoIndex];
					
					//2. 삭제 확인 다이얼로그
					int result = JOptionPane.showConfirmDialog(null, "선택한 영화를 취소합니다." + '\n' + "계속하시겠습니까?", "Confirm",
							JOptionPane.YES_NO_OPTION);
					if(result == JOptionPane.YES_OPTION) {
						//3. yes버튼을 눌렀다면
						try {
							//4. 예매 정보를 삭제 함수를 사용한다.
							MainFrame.screen.cancelReserv(selectedInfo);
							
							//5. 예매 정보 배열에서 삭제한다.
							reservInfos[selectedInfoIndex] = null;
							
							//6. 벡터에서 지우고 리스트를 초기화한다.
							reserveVector.remove(selectedInfoIndex);
							reservelist.removeAll();
							reservelist.setListData(reserveVector);
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
			
				}
				
			});
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(34, 444, 425, 131);
			add(scrollPane);
			
			//1. 예매한 영화 목록에 넣을 벡터를 초기화 하고 예매 리스트를 생성한다.
			reserveVector.clear();
			reservelist = new JList<>(reserveVector);
			
			
			//2. 고객이 예매한 모든 영화를 저장할 배열을 생성한다.
			reservInfos = new ReservInfo[10];

			try {
				//3. 사용자 아이디를 가져온다. 
				String userID = Theater.personNow.getId();
				
				//4. 예매정보 검색 결과를 가져온다.
				ResultSet rs = MainFrame.screen.searchReservInfo(userID);
				
				//5. 만약 검색 결과가 존재한다면
				int i = 0;
				while(rs.next()) {
					//6. 예매정보 데이터를 가져온다.
					String id = rs.getString("id");
					String title = rs.getString("title");
					String date = rs.getString("date");
					int round = rs.getInt("round");
					int seat = rs.getInt("seat");
					
					//7. 가져온 데이터를 예매 정보 배열에 넣는다.
					reservInfos[i] = new ReservInfo(id, title, date, round, seat);
					
					//8. 가져온 예매 정보를 벡터에 추가한다.
					reserveVector.add(new String("<html>" + title + "<br><span style=\\\"color:gray\\\"><small>" + round + "회차   "
							+ seat + "번 좌석</small></span></html>"));

					i++;
				}
			} catch (Exception e) {
				e.getMessage();
			}
			
			//9. 리스트에 벡터를 넣는다.
			reservelist.setListData(reserveVector);
			
			//10. 리스트를 scrollPane에 추가한다.
			scrollPane.setViewportView(reservelist);
			
			//리스트에서 선택된 아이템 알아내기
			reservelist.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					selectedInfoIndex = reservelist.getSelectedIndex();	
				}
				
			});
		}
	}

	class DDayPlusMoviePanel extends JPanel {

		// 접속중인 회원 
		Person p = MainFrame.theater.personNow;
		String userName = MainFrame.theater.personNow.getName();

		String FirstPosterPath = "라라랜드.jpg";
		String SecondPosterPath = "알라딘.jpg";
		String ThirdPosterPath = "위대한쇼맨.jpg";
		String TicketPath = "TICKET.png";

		String FirstTitle = "라라랜드";
		String SecondTitle = "알라딘";
		String ThirdTitle = "위대한쇼맨";

		private LineBorder bb = new LineBorder(Color.black, 1, true);
		
		   ResultSet rs;
		   int movieCount;

		public DDayPlusMoviePanel() {
			setBounds(0, 0, 501, 677);
			setBackground(Color.WHITE);
			setLayout(null);

			// 티켓이미지(메인화면)라벨
			JLabel Ticket_main = new JLabel("티켓_메인화면");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 티켓 이미지 클릭 리스터
					// 1. ClientMain panel로 변경한다.
					PanelChange("ClientMainPanel");
				}
			});

//			// ImageIcon 객체 생성 => Image 추출 및 크기 조절해서 새로운 객체 생성
//			ImageIcon ticektIcon = new ImageIcon(MainFrame.class.getResource(TicketPath));
//			Image ticketImg = ticektIcon.getImage();
//			Image changedticektImg = ticketImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);
//			ImageIcon changedticektIcon = new ImageIcon(changedticektImg);
//			Ticket_main.setIcon(changedticektIcon);

			// 스크롤
			JScrollPane scrollPane = new JScrollPane();
			JPanel firstpanel = new JPanel();
			Dimension size = new Dimension();
			size.setSize(447, 375);
			firstpanel.setPreferredSize(size);
			scrollPane.setViewportView(firstpanel);
			scrollPane.setBounds(10, 170, 465, 361);
			add(scrollPane);

			// 첫번째 패널(영화들 목록 보여주는(?)패널)
			firstpanel.setBackground(Color.WHITE);
			size.setSize(200, 200);
			firstpanel.setLayout(new FlowLayout(FlowLayout.LEFT, 18, 15));

			// 영화 하나 패널
			JPanel moviepanel1 = new JPanel();
			moviepanel1.setBackground(Color.WHITE);
			firstpanel.add(moviepanel1);

			// 캡슐화
			Dimension dim = new Dimension(130, 200);
			
		      // 오늘 날짜
		      LocalDateTime current = LocalDateTime.now();
		      DateTimeFormatter format = DateTimeFormatter.ofPattern("yyMMdd");
		      String today = current.format(format);
		      int today_int = Integer.parseInt(today);

		      // 전체 영화 반환하기
		      try {
		         rs = MainFrame.theater.movieAll();
		         movieCount = MainFrame.theater.countMovie();
		      } catch (Exception e) {
		         e.getMessage();
		      }

		      // rs로 검색결과 보여주기
		      JPanel[] moviepanels = new JPanel[movieCount];
		      Image[] images = new Image[movieCount];
		      ImageIcon[] imageIcons = new ImageIcon[movieCount];
		      try {
		         int i = 0;
		         while (rs.next()) {
		            // 개봉 예정일 가져오기
		            String dday = rs.getString("dday");
		            int dday_int = Integer.parseInt(dday);

		            if (dday_int > today_int) {// 아직 개봉일 전
		               // 패널 설정
		               moviepanels[i] = new JPanel();
		               moviepanels[i].setBackground(Color.WHITE);
		               firstpanel.add(moviepanels[i]);
		               moviepanels[i].setPreferredSize(dim);
		               moviepanels[i].setLayout(null);
		               // 영화정보 가져오기
		               String title = rs.getString("title");
		               String posterURL = rs.getString("posterURL");
		               // 이미지 아이콘 설정
		               URL url = new URL(posterURL);
		               images[i] = ImageIO.read(url);
		               images[i] = images[i].getScaledInstance(128, 160, Image.SCALE_SMOOTH);
		               imageIcons[i] = new ImageIcon(images[i]);
		               // 영화 포스터
		               JLabel lblPoster = new JLabel("영화 포스터");
		               lblPoster.setBounds(0, 0, 130, 167);
		               moviepanels[i].add(lblPoster);
		               lblPoster.setHorizontalAlignment(SwingConstants.CENTER);
		               lblPoster.setIcon(imageIcons[i]);
		               // 라벨: 영화 제목
		               JLabel lblTitle = new JLabel(title);
		               lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		               lblTitle.setBounds(10, 172, 106, 22);
		               moviepanels[i].add(lblTitle);
		            }

		         }
		      } catch (Exception e) {
		         e.printStackTrace();
		      }


			// 로그인 한 이름 보여줌
			JLabel lblUserName = new JLabel(userName + "\uB2D8  /");
			lblUserName.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblUserName.setBounds(350, 83, 66, 15);
			add(lblUserName);

			// 로그아웃
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblLogout.setBounds(420, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 로그아웃 라벨 클릭 리스터
					// 1. 로그아웃 알림 메시지 발생
					//2. 로그아웃을 한다.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)을 생성한다.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame을 제거한다.
					dispose();
				}
			});

			// 상단바 예매 라벨
			JLabel lbl_ticketing = new JLabel("\uC608\uB9E4");
			lbl_ticketing.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_ticketing.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_ticketing.setBorder(bb);
			lbl_ticketing.setBounds(0, 109, 162, 42);
			add(lbl_ticketing);
			lbl_ticketing.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 예매 카테고리 클릭 리스너
					// 1. Reserve panel로 변경한다.
					PanelChange("ReservePanel");
				}
			});

			// 상단바 개봉예정영화 라벨
			JLabel lbl_scheduleMovie = new JLabel("\uAC1C\uBD09\uC608\uC815\uC601\uD654");
			lbl_scheduleMovie.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_scheduleMovie.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_scheduleMovie.setBorder(bb);
			lbl_scheduleMovie.setBounds(161, 109, 164, 42);
			add(lbl_scheduleMovie);
			lbl_scheduleMovie.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 상영예정작 클릭 리스터
					// 1. DDayPlusMovie panel로 변경한다.
					PanelChange("DDayPlusMoviePanel");
				}
			});

			// 마이페이지 라벨
			JLabel lbl_myPage = new JLabel("\uB9C8\uC774\uD398\uC774\uC9C0");
			lbl_myPage.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_myPage.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_myPage.setBorder(bb);
			lbl_myPage.setBounds(324, 109, 162, 42);
			add(lbl_myPage);
			lbl_myPage.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 마이페이지 클릭 리스너
					// 1. 마이페이지 panel로 변경한다.
					PanelChange("MyPagePanel");
				}
			});

			// 검색 텍스트필드
			JTextField tfSearch = new JTextField();
			tfSearch.setBounds(100, 589, 296, 30);
			add(tfSearch);
			tfSearch.setColumns(10);

			// 검색 버튼
			JButton btnSearch = new JButton("\uAC80\uC0C9");
			btnSearch.setBackground(Color.LIGHT_GRAY);
			btnSearch.setBounds(408, 588, 70, 30);
			add(btnSearch);
			btnSearch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 영화 검색 버튼 이벤트
					// 1. 텍스트필드에 입력받은 데이터를 가져온다.
					String keyword = tfSearch.getText();
					// 2. 만약 입력받은 텍스트가 공백이라면
					if (keyword.equals("")) {
						// 3. 알림 메시지를 발생시킨다.
						JOptionPane.showMessageDialog(null, "검색할 영화 제을 입력해주세요..", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
					} else {
						// 4. 만약 입력받은 텍스트가 공백이 아니라면
						try {
							// 5. 입력받은 키워드로 영화를 검색한다.
							ResultSet rs = MainFrame.theater.searchMovie(keyword);
							
							// 6. 텍스트필드를 지우고
							tfSearch.setText("");
							
							// 7. SearchMovie panel로 변경한다.	
							// 이때, 입력받은 키워드의 검색 결과를 매개변수로 전달한다.
							ShowSearchMovie(rs);
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});

			// 검색라벨
			JLabel lbl_search = new JLabel("\uC601\uD654 \uAC80\uC0C9");
			lbl_search.setFont(new Font("맑은 고딕", Font.BOLD, 18));
			lbl_search.setBounds(14, 586, 96, 34);
			add(lbl_search);
		}
	}

	class ReservePanel extends JPanel {

		String userName = Theater.personNow.getName();
		String TicketPath = "TICKET.png";
		
		// 오늘 날짜
		LocalDateTime current = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyMMdd");
		String today = current.format(format);
		
		int round;
		
		String selectMovieTitle;
		int selectTimeIndex;
		
		MovieNow[] movieOnScreen;
		Vector<String> movieVector = new Vector<>();
		Vector<String> timeVector = new Vector<>();
		JList<String> movielist;
		JList<String> timelist;

		private LineBorder bb = new LineBorder(Color.black, 1, true);

		public ReservePanel() {
			setBounds(0, 0, 501, 677);
			setBackground(Color.WHITE);
			setLayout(null);
			
			// 티켓이미지(메인화면)라벨
			JLabel Ticket_main = new JLabel("티켓_메인화면");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 티켓 이미지 클릭 리스터
					// 1. ClientMain panel로 변경한다.
					PanelChange("ClientMainPanel");
				}
			});

//			// ImageIcon 객체 생성 => Image 추출 및 크기 조절해서 새로운 객체 생성
//			ImageIcon ticektIcon = new ImageIcon(MainFrame.class.getResource(TicketPath));
//			Image ticketImg = ticektIcon.getImage();
//			Image changedticektImg = ticketImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);
//			ImageIcon changedticektIcon = new ImageIcon(changedticektImg);
//			Ticket_main.setIcon(changedticektIcon);

			// 예매 카테고리
			JLabel lbl_ticketing = new JLabel("\uC608\uB9E4");
			lbl_ticketing.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_ticketing.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_ticketing.setBorder(bb);
			lbl_ticketing.setBounds(0, 109, 162, 42);
			add(lbl_ticketing);
			lbl_ticketing.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 예매 카테고리 클릭 리스너
					// 1. Reserve panel로 변경한다.
					PanelChange("ReservePanel");
				}
			});

			// 개봉 예정작 카테고리
			JLabel lbl_scheduleMovie = new JLabel("\uAC1C\uBD09\uC608\uC815\uC601\uD654");
			lbl_scheduleMovie.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_scheduleMovie.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_scheduleMovie.setBorder(bb);
			lbl_scheduleMovie.setBounds(161, 109, 164, 42);
			add(lbl_scheduleMovie);
			lbl_scheduleMovie.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 상영예정작 클릭 리스터
					// 1. DDayPlusMovie panel로 변경한다.
					PanelChange("DDayPlusMoviePanel");
				}
			});

			// 마이페이지 카테고리
			JLabel lbl_myPage = new JLabel("\uB9C8\uC774\uD398\uC774\uC9C0");
			lbl_myPage.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_myPage.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_myPage.setBorder(bb);
			lbl_myPage.setBounds(324, 109, 162, 42);
			add(lbl_myPage);
			lbl_myPage.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 마이페이지 클릭 리스너
					// 1. 마이페이지 panel로 변경한다.
					PanelChange("MyPagePanel");
				}
			});

			// 예매 타이틀 라벨
			JLabel lbl_title = new JLabel("\uC608\uB9E4");
			lbl_title.setFont(new Font("맑은 고딕", Font.BOLD, 18));
			lbl_title.setBounds(20, 175, 170, 34);
			add(lbl_title);

			// 이름 레이블
			JLabel lblUserName = new JLabel(userName + "\uB2D8  /");
			lblUserName.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblUserName.setBounds(350, 83, 66, 15);
			add(lblUserName);

			// 로그아웃 레이블
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblLogout.setBounds(420, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 로그아웃 라벨 클릭 리스터
					// 1. 로그아웃 알림 메시지 발생
					//2. 로그아웃을 한다.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)을 생성한다.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame을 제거한다.
					dispose();
				}
			});
			
			//영화 스크롤
			JScrollPane moviePane = new JScrollPane();
			moviePane.setBounds(20, 268, 216, 246);
			getContentPane().add(moviePane);

			// 상영시간표 스크롤
			JScrollPane timePane = new JScrollPane();
			timePane.setBounds(255, 268, 216, 246);
			getContentPane().add(timePane);
			
			// 검색 텍스트필드
			JTextField tf_search = new JTextField();
			tf_search.setColumns(10);
			tf_search.setBounds(74, 230, 118, 25);
			add(tf_search);

			// 검색 라벨
			JLabel lbl_search = new JLabel("\uC601\uD654\uCC3E\uAE30");
			lbl_search.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lbl_search.setBounds(20, 230, 57, 25);
			add(lbl_search);
			
			// 검색 버튼
			JButton btn_search = new JButton("\uAC80\uC0C9");
			btn_search.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//검색 버튼 이벤트
					//1. 영화 리스트에 넣을 벡터를 초기화 하고 영화 리스트를 생성한다.
					movieVector.clear();
					movielist =  new JList<>(movieVector);
					
					movielist.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
					movielist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //단일 선택만 가능하게 설정
					moviePane.setViewportView(movielist);
					
					try {
						//2. 검색할 영화 제목을 가져온다.
						String searchTitle = tf_search.getText();
						
						//3. 검색 결과를 가져온다.
						ResultSet rs = MainFrame.theater.searchMovie(searchTitle);
						
						//4. 만약 검색 결과가 존재한다면
						while (rs.next()) {
							//5. 영화 제목을 가져온다.
							String searchResult = rs.getString("title");
							
							//6. 가져온 영화 제목을 벡터에 추가한다.
							movieVector.add(searchResult);
						}
						
						//7. 리스트에 벡터를 넣는다.
						movielist.setListData(movieVector);
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					//영화 리스트 클릭 이벤트(영화를 클릭하면)
					movielist.addListSelectionListener(new ListSelectionListener() {
						@Override
						public void valueChanged(ListSelectionEvent e) {
							//1. 시간 리스트에 넣을 벡터를 초기화하고 시간 리스트를 생성한다.
							timeVector.clear();
							timelist = new JList<>(timeVector);
							
							timelist.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
							timelist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	//단일 선택만 가능하게 설정
							timePane.setViewportView(timelist);
							
							//2. 선택한 영화 리스트의 값(영화 제목)을 object로 반환하고, String으로 형변환하여 저장한다.
							selectMovieTitle = (String)movielist.getSelectedValue();	
							
							//3. 해당 영화의 상영 영화들을 저장하는 배열을 만든다.
							//후에 영화를 예매할 때 이 곳에 저장된 상영 영화 객체를 사용한다. 
							movieOnScreen = new MovieNow[10];
							int i = 0;	//상영 영화 배열을 저장하는 인덱스
							
							try {							
								//4. 선택한 영화 제목으로 상영 시간을 검색하여 검색 결과를 가져온다.
								ResultSet rs = MainFrame.screen.searchMovieNow(selectMovieTitle);
								
								//5. 만약 검색 결과가 존재한다면
								while (rs.next()) {
									//6. 상영 영화 데이터를 가져온다.
									String title = rs.getString("title");
									String date = rs.getString("date");
									int rround = rs.getInt("round");
									String start = rs.getString("start");
									String finish = rs.getString("finish");
									
									//7. 해당 데이터를 상영 영화 배열에 추가한다.
									movieOnScreen[i] = new MovieNow(title, date, rround, start, finish);
									
									//8. 리스트에 출력할 정보만 골라 벡터에 추가한다.
									timeVector.add(new String("<html>" + rround + " 회차<br><span style=\\\\\\\"color:gray\\\\\\\">" + "시작 시간 </small></span>" + start + "  끝 시간 </small></span>" + finish));
									
									i++;
								}
								
								//9. 리스트에 벡터를 넣는다.
								timelist.setListData(timeVector);
								
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							
							
							//시간 리스트 클릭 이벤트(상영 회차를 클릭하면)
							timelist.addListSelectionListener(new ListSelectionListener() {
								@Override
								public void valueChanged(ListSelectionEvent e) {
									//선택한 list의 index 값을 저장한다.
									selectTimeIndex = timelist.getSelectedIndex();	
								}
							});
						}
						
					});
					
				}
			});
			btn_search.setBackground(Color.LIGHT_GRAY);
			btn_search.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
			btn_search.setBounds(198, 230, 57, 23);
			getContentPane().add(btn_search);


			// 예매 버튼
			JButton btn_buy = new JButton("\uC608\uB9E4");
			btn_buy.setBackground(Color.LIGHT_GRAY);
			btn_buy.setBounds(368, 592, 91, 23);
			add(btn_buy);
			btn_buy.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 예매 버튼 이벤트
					// 1. 예매하려는 영화의 상영영화 객체를 가져온다.
					MovieNow movieNow = movieOnScreen[selectTimeIndex];
					
					// 2. Ticketing panel로 변경한다.
					ChangeTicketingPanel(movieNow);
				}
			});

		}

		public ReservePanel(String title) {
			// 1. 매개변수로 받은 파라미터를 바로 검색하는 기능 추가하기
			setBounds(0, 0, 501, 677);
			setBackground(Color.WHITE);
			setLayout(null);

			// 티켓이미지(메인화면)라벨
			JLabel Ticket_main = new JLabel("티켓_메인화면");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 티켓 이미지 클릭 리스터
					// 1. ClientMain panel로 변경한다.
					PanelChange("ClientMainPanel");
				}
			});

//			// ImageIcon 객체 생성 => Image 추출 및 크기 조절해서 새로운 객체 생성
//			ImageIcon ticektIcon = new ImageIcon(MainFrame.class.getResource(TicketPath));
//			Image ticketImg = ticektIcon.getImage();
//			Image changedticektImg = ticketImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);
//			ImageIcon changedticektIcon = new ImageIcon(changedticektImg);
//			Ticket_main.setIcon(changedticektIcon);

			// 예매 카테고리
			JLabel lbl_ticketing = new JLabel("\uC608\uB9E4");
			lbl_ticketing.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_ticketing.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_ticketing.setBorder(bb);
			lbl_ticketing.setBounds(0, 109, 162, 42);
			add(lbl_ticketing);
			lbl_ticketing.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 예매 카테고리 클릭 리스너
					// 1. Reserve panel로 변경한다.
					PanelChange("ReservePanel");
				}
			});

			// 개봉 예정작 카테고리
			JLabel lbl_scheduleMovie = new JLabel("\uAC1C\uBD09\uC608\uC815\uC601\uD654");
			lbl_scheduleMovie.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_scheduleMovie.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_scheduleMovie.setBorder(bb);
			lbl_scheduleMovie.setBounds(161, 109, 164, 42);
			add(lbl_scheduleMovie);
			lbl_scheduleMovie.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 상영예정작 클릭 리스터
					// 1. DDayPlusMovie panel로 변경한다.
					PanelChange("DDayPlusMoviePanel");
				}
			});

			// 마이페이지 카테고리
			JLabel lbl_myPage = new JLabel("\uB9C8\uC774\uD398\uC774\uC9C0");
			lbl_myPage.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_myPage.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_myPage.setBorder(bb);
			lbl_myPage.setBounds(324, 109, 162, 42);
			add(lbl_myPage);
			lbl_myPage.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 마이페이지 클릭 리스너
					// 1. 마이페이지 panel로 변경한다.
					PanelChange("MyPagePanel");
				}
			});

			// 예매 타이틀 라벨
			JLabel lbl_title = new JLabel("\uC608\uB9E4");
			lbl_title.setFont(new Font("맑은 고딕", Font.BOLD, 18));
			lbl_title.setBounds(20, 175, 170, 34);
			add(lbl_title);

			// 이름 레이블
			JLabel lblUserName = new JLabel(userName + "\uB2D8  /");
			lblUserName.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblUserName.setBounds(357, 83, 57, 15);
			add(lblUserName);
			
			// 검색 텍스트필드
			JTextField tf_search = new JTextField();
			tf_search.setColumns(10);
			tf_search.setBounds(74, 230, 118, 25);
			add(tf_search);

			// 검색 라벨
			JLabel lbl_search = new JLabel("\uC601\uD654\uCC3E\uAE30");
			lbl_search.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lbl_search.setBounds(20, 230, 57, 25);
			add(lbl_search);

			// 로그아웃 레이블
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblLogout.setBounds(420, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 로그아웃 라벨 클릭 리스터
					// 1. 로그아웃 알림 메시지 발생
					//2. 로그아웃을 한다.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)을 생성한다.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame을 제거한다.
					dispose();
				}
			});

			// 예매 버튼
			JButton btn_buy = new JButton("\uC608\uB9E4");
			btn_buy.setBackground(Color.LIGHT_GRAY);
			btn_buy.setBounds(368, 592, 91, 23);
			add(btn_buy);
			btn_buy.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 예매 버튼 이벤트
					// 1. 예매하려는 영화의 상영영화 객체를 가져온다.
					MovieNow movieNow = movieOnScreen[selectTimeIndex];
					
					// 2. Ticketing panel로 변경한다.
					ChangeTicketingPanel(movieNow);
				}
			});

			//영화 스크롤
			JScrollPane moviePane = new JScrollPane();
			moviePane.setBounds(20, 268, 216, 246);
			getContentPane().add(moviePane);

			// 상영시간표 스크롤
			JScrollPane timePane = new JScrollPane();
			timePane.setBounds(255, 268, 216, 246);
			getContentPane().add(timePane);
			
			// 검색 버튼
			JButton btn_search = new JButton("\uAC80\uC0C9");
			btn_search.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//검색 버튼 이벤트
					//1. 영화 리스트에 넣을 벡터를 초기화 하고 영화 리스트를 생성한다.
					movieVector.clear();
					movielist =  new JList<>(movieVector);
					
					movielist.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
					movielist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //단일 선택만 가능하게 설정
					moviePane.setViewportView(movielist);
					
					try {
						//2. 검색할 영화 제목을 가져온다.
						String searchTitle = tf_search.getText();
						
						//3. 검색 결과를 가져온다.
						ResultSet rs = MainFrame.theater.searchMovie(searchTitle);
						
						//4. 만약 검색 결과가 존재한다면
						while (rs.next()) {
							//5. 영화 제목을 가져온다.
							String searchResult = rs.getString("title");
							
							//6. 가져온 영화 제목을 벡터에 추가한다.
							movieVector.add(searchResult);
						}
						
						//7. 리스트에 벡터를 넣는다.
						movielist.setListData(movieVector);
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					//영화 리스트 클릭 이벤트(영화를 클릭하면)
					movielist.addListSelectionListener(new ListSelectionListener() {
						@Override
						public void valueChanged(ListSelectionEvent e) {
							//1. 시간 리스트에 넣을 벡터를 초기화하고 시간 리스트를 생성한다.
							timeVector.clear();
							timelist = new JList<>(timeVector);
							
							timelist.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
							timelist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	//단일 선택만 가능하게 설정
							timePane.setViewportView(timelist);
							
							//2. 선택한 영화 리스트의 값(영화 제목)을 object로 반환하고, String으로 형변환하여 저장한다.
							selectMovieTitle = (String)movielist.getSelectedValue();	
							
							//3. 해당 영화의 상영 영화들을 저장하는 배열을 만든다.
							//후에 영화를 예매할 때 이 곳에 저장된 상영 영화 객체를 사용한다. 
							movieOnScreen = new MovieNow[10];
							int i = 0;	//상영 영화 배열을 저장하는 인덱스
							
							try {							
								//4. 선택한 영화 제목으로 상영 시간을 검색하여 검색 결과를 가져온다.
								ResultSet rs = MainFrame.screen.searchMovieNow(selectMovieTitle);
								
								//5. 만약 검색 결과가 존재한다면
								while (rs.next()) {
									//6. 상영 영화 데이터를 가져온다.
									String title = rs.getString("title");
									String date = rs.getString("date");
									int rround = rs.getInt("round");
									String start = rs.getString("start");
									String finish = rs.getString("finish");
									
									//7. 해당 데이터를 상영 영화 배열에 추가한다.
									movieOnScreen[i] = new MovieNow(title, date, rround, start, finish);
									
									//8. 리스트에 출력할 정보만 골라 벡터에 추가한다.
									timeVector.add(new String("<html>" + rround + " 회차<br><span style=\\\\\\\"color:gray\\\\\\\">" + "시작 시간 </small></span>" + start + "  끝 시간 </small></span>" + finish));
									
									i++;
								}
								
								//9. 리스트에 벡터를 넣는다.
								timelist.setListData(timeVector);
								
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							
							
							//시간 리스트 클릭 이벤트(상영 회차를 클릭하면)
							timelist.addListSelectionListener(new ListSelectionListener() {
								@Override
								public void valueChanged(ListSelectionEvent e) {
									//선택한 list의 값을 object 반환하고, String으로 형변환하여 저장
									selectTimeIndex = timelist.getSelectedIndex();	
								}
							});
						}
						
					});
					
				}
			});
			btn_search.setBackground(Color.LIGHT_GRAY);
			btn_search.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
			btn_search.setBounds(198, 230, 57, 23);
			getContentPane().add(btn_search);
		}
	}

	class OtherMoviePanel extends JPanel {

		String userName = Theater.personNow.getName();

		String TicketPath = "TICKET.png";

		String selectedMovie = null;
		
		private LineBorder bb = new LineBorder(Color.black, 1, true);
		ResultSet rs;
		
		public OtherMoviePanel() {
			setBounds(0, 0, 501, 677);
			setBackground(Color.WHITE);
			setLayout(null);

			// 티켓이미지(메인화면)라벨
			JLabel Ticket_main = new JLabel("티켓_메인화면");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 티켓 이미지 클릭 리스터
					// 1. ClientMain panel로 변경한다.
					PanelChange("ClientMainPanel");
				}
			});
//
//			// ImageIcon 객체 생성 => Image 추출 및 크기 조절해서 새로운 객체 생성
//			ImageIcon ticektIcon = new ImageIcon(MainFrame.class.getResource(TicketPath));
//			Image ticketImg = ticektIcon.getImage();
//			Image changedticektImg = ticketImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);
//			ImageIcon changedticektIcon = new ImageIcon(changedticektImg);
//			Ticket_main.setIcon(changedticektIcon);

			

//			// 스크롤
//			JScrollPane scrollPane = new JScrollPane();
//			JPanel firstpanel = new JPanel();
//			Dimension size = new Dimension();
//			size.setSize(447, 361);
//			firstpanel.setPreferredSize(size);
//			scrollPane.setViewportView(firstpanel);
//			scrollPane.setBounds(10, 170, 465, 361);
//			scrollPane.setVisible(true);
//			add(scrollPane);
//
//	         
//			// 첫번째 패널(영화들 목록 보여주는(?)패널)
//			firstpanel.setBackground(Color.WHITE);
//			size.setSize(200, 200);
//			firstpanel.setLayout(new FlowLayout(FlowLayout.LEFT, 18, 15));

			  // 첫번째 패널(영화들 목록 보여주는(?)패널)
	         JPanel firstpanel = new JPanel();
	         Dimension size = new Dimension(500,1000);
	         firstpanel.setPreferredSize(size);
	         firstpanel.setBackground(Color.WHITE);
	         firstpanel.setLayout(new FlowLayout(FlowLayout.LEFT, 18, 15));

	         
	         // 스크롤
	         JScrollPane scrollPane = new JScrollPane(firstpanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	         scrollPane.setBounds(10, 170, 465, 361);
	         add(scrollPane);
	         
	         scrollPane.setViewportView(firstpanel);
	         
			
			// 영화 하나 패널
			JPanel moviepanel1 = new JPanel();
			moviepanel1.setBackground(Color.WHITE);
			firstpanel.add(moviepanel1);

			// 캡슐화
			Dimension dim = new Dimension(130, 200);
			Dimension dim1 = new Dimension(-18, 0);
			
			moviepanel1.setPreferredSize(dim1);
			moviepanel1.setLayout(null);
			
			
			// 전체 영화 반환
			int movieCount = 0;
			try {
				rs = MainFrame.theater.movieAll();
				movieCount = MainFrame.theater.countMovie();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			// rs로 검색결과 보여주기
			JPanel[] moviepanels = new JPanel[movieCount];
			Image[] images = new Image[movieCount];
			ImageIcon[] imageIcons = new ImageIcon[movieCount];
			try {
				int i = 0;
				while (rs.next()) {
					// 패널 설정
					moviepanels[i] = new JPanel();
					moviepanels[i].setBackground(Color.WHITE);
					firstpanel.add(moviepanels[i]);
					moviepanels[i].setPreferredSize(dim);
					moviepanels[i].setLayout(null);
					// 영화정보 가져오기
					String title = rs.getString("title");
					String posterURL = rs.getString("posterURL");
					// 이미지 아이콘 설정
					URL url = new URL(posterURL);
					images[i] = ImageIO.read(url);
					images[i] = images[i].getScaledInstance(128, 160, Image.SCALE_SMOOTH);
					imageIcons[i] = new ImageIcon(images[i]);
					// 영화 포스터
					JLabel lblPoster = new JLabel("영화 포스터");
					lblPoster.setBounds(0, 0, 130, 167);
					moviepanels[i].add(lblPoster);
					lblPoster.setHorizontalAlignment(SwingConstants.CENTER);
					lblPoster.setIcon(imageIcons[i]);
					// 라벨: 영화 제목
					JLabel lblTitle = new JLabel(title);
					lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 12));
					lblTitle.setBounds(10, 172, 106, 22);
					moviepanels[i].add(lblTitle);
					// 클릭 이벤트
					moviepanels[i].addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							selectedMovie = title;
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 로그인 한 이름 보여줌
			JLabel lblUserName = new JLabel(userName + "\uB2D8  /");
			lblUserName.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblUserName.setBounds(350, 83, 66, 15);
			add(lblUserName);

			// 로그아웃
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblLogout.setBounds(420, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 로그아웃 라벨 클릭 리스터
					// 1. 로그아웃 알림 메시지 발생
					//2. 로그아웃을 한다.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)을 생성한다.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame을 제거한다.
					dispose();
				}
			});

			// 상단바 예매 라벨
			JLabel lbl_ticketing = new JLabel("\uC608\uB9E4");
			lbl_ticketing.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_ticketing.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_ticketing.setBorder(bb);
			lbl_ticketing.setBounds(0, 109, 162, 42);
			add(lbl_ticketing);
			lbl_ticketing.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 예매 카테고리 클릭 리스너
					// 1. Reserve panel로 변경한다.
					PanelChange("ReservePanel");
				}
			});

			// 상단바 개봉예정영화 라벨
			JLabel lbl_scheduleMovie = new JLabel("\uAC1C\uBD09\uC608\uC815\uC601\uD654");
			lbl_scheduleMovie.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_scheduleMovie.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_scheduleMovie.setBorder(bb);
			lbl_scheduleMovie.setBounds(161, 109, 164, 42);
			add(lbl_scheduleMovie);
			lbl_scheduleMovie.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 상영예정작 클릭 리스터
					// 1. DDayPlusMovie panel로 변경한다.
					PanelChange("DDayPlusMoviePanel");
				}
			});

			// 마이페이지 라벨
			JLabel lbl_myPage = new JLabel("\uB9C8\uC774\uD398\uC774\uC9C0");
			lbl_myPage.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_myPage.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_myPage.setBorder(bb);
			lbl_myPage.setBounds(324, 109, 162, 42);
			add(lbl_myPage);
			lbl_myPage.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 마이페이지 클릭 리스너
					// 1. 마이페이지 panel로 변경한다.
					PanelChange("MyPagePanel");
				}
			});

			// 검색 텍스트필드
			JTextField tfSearch = new JTextField();
			tfSearch.setBounds(100, 589, 296, 30);
			add(tfSearch);
			tfSearch.setColumns(10);

			// 검색 버튼
			JButton btnSearch = new JButton("\uAC80\uC0C9");
			btnSearch.setBackground(Color.LIGHT_GRAY);
			btnSearch.setBounds(408, 588, 70, 30);
			add(btnSearch);
			btnSearch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 영화 검색 버튼 이벤트
					// 1. 텍스트필드에 입력받은 데이터를 가져온다.
					String keyword = tfSearch.getText();
					// 2. 만약 입력받은 텍스트가 공백이라면
					if (keyword.equals("")) {
						// 3. 알림 메시지를 발생시킨다.
						JOptionPane.showMessageDialog(null, "검색할 영화 제을 입력해주세요..", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
					} else {
						// 4. 만약 입력받은 텍스트가 공백이 아니라면
						try {
							// 5. 입력받은 키워드로 영화를 검색한다.
							ResultSet rs = MainFrame.theater.searchMovie(keyword);
							
							// 6. 텍스트필드를 지우고
							tfSearch.setText("");
							
							// 7. SearchMovie panel로 변경한다.	
							// 이때, 입력받은 키워드의 검색 결과를 매개변수로 전달한다.
							ShowSearchMovie(rs);
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});

			// 검색라벨
			JLabel lbl_search = new JLabel("\uC601\uD654 \uAC80\uC0C9");
			lbl_search.setFont(new Font("맑은 고딕", Font.BOLD, 18));
			lbl_search.setBounds(14, 586, 96, 34);
			add(lbl_search);

			// 예매버튼
			JButton buy_button = new JButton("\uC608\uB9E4");
			buy_button.setForeground(Color.WHITE);
			buy_button.setBackground(Color.RED);
			buy_button.setBounds(408, 548, 70, 30);
			add(buy_button);
			buy_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//검색 버튼 이벤트
					//1. ReservePanel로 변경한다.
					ReserveSelectdMovie(selectedMovie);
					}
				});
		}
	}

	class ClientModifyPanel extends JPanel {

		String userName = Theater.personNow.getName();
		String client_id = Theater.personNow.getId();
		Person personNow = Theater.personNow;

		private LineBorder bb = new LineBorder(Color.black, 1, true);

		String TicketPath = "TICKET.png";

		public ClientModifyPanel() {
			setBounds(0, 0, 501, 677);
			setBackground(Color.WHITE);
			setLayout(null);

			// 티켓이미지(메인화면)라벨
			JLabel Ticket_main = new JLabel("티켓_메인화면");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 티켓 이미지 클릭 리스터
					// 1. ClientMain panel로 변경한다.
					PanelChange("ClientMainPanel");
				}
			});

//			// ImageIcon 객체 생성 => Image 추출 및 크기 조절해서 새로운 객체 생성
//			ImageIcon ticektIcon = new ImageIcon(MainFrame.class.getResource(TicketPath));
//			Image ticketImg = ticektIcon.getImage();
//			Image changedticektImg = ticketImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);
//			ImageIcon changedticektIcon = new ImageIcon(changedticektImg);
//			Ticket_main.setIcon(changedticektIcon);

			// 상단 예매 카테고리
			JLabel lbl_ticketing = new JLabel("\uC608\uB9E4");
			lbl_ticketing.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_ticketing.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_ticketing.setBounds(0, 109, 162, 42);
			lbl_ticketing.setBorder(bb);
			add(lbl_ticketing);
			lbl_ticketing.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 예매 카테고리 클릭 리스너
					// 1. Reserve panel로 변경한다.
					PanelChange("ReservePanel");
				}
			});

			// 상단 마이페이지 카테고리
			JLabel lbl_myPage = new JLabel("\uB9C8\uC774\uD398\uC774\uC9C0");
			lbl_myPage.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_myPage.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_myPage.setBorder(bb);
			lbl_myPage.setBounds(324, 109, 162, 42);
			add(lbl_myPage);
			lbl_myPage.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 마이페이지 클릭 리스너
					// 1. 마이페이지 panel로 변경한다.
					PanelChange("MyPagePanel");
				}
			});

			// 상영 예정 영화 카테고리
			JLabel lbl_scheduleMovie = new JLabel("\uAC1C\uBD09\uC608\uC815\uC601\uD654");
			lbl_scheduleMovie.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_scheduleMovie.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_scheduleMovie.setBorder(bb);
			lbl_scheduleMovie.setBounds(161, 109, 164, 42);
			add(lbl_scheduleMovie);
			lbl_scheduleMovie.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 상영예정작 클릭 리스터
					// 1. DDayPlusMovie panel로 변경한다.
					PanelChange("DDayPlusMoviePanel");
				}
			});

			// 회원정보 수정 타이틀 라벨
			JLabel lbl_title = new JLabel("\uD68C\uC6D0\uC815\uBCF4 \uC218\uC815\r\n");
			lbl_title.setFont(new Font("맑은 고딕", Font.BOLD, 18));
			lbl_title.setBounds(185, 175, 126, 34);
			add(lbl_title);

			// 아이디 라벨
			JLabel lbl_id = new JLabel("\uC544\uC774\uB514");
			lbl_id.setBounds(45, 421, 50, 15);
			add(lbl_id);

			// 비밀번호 라벨
			JLabel lbl_pw = new JLabel("\uBE44\uBC00\uBC88\uD638");
			lbl_pw.setBounds(39, 478, 56, 15);
			add(lbl_pw);

			// 비밀번호 확인 라벨
			JLabel lbl_checkPw = new JLabel(
					"<html><body><center>\uBE44\uBC00\uBC88\uD638<br>\uD655\uC778</center></body></html>");
			lbl_checkPw.setBounds(39, 525, 56, 29);
			add(lbl_checkPw);

			// 이름 라벨
			JLabel lbl_name = new JLabel("\uC774\uB984");
			lbl_name.setBounds(48, 260, 50, 15);
			add(lbl_name);

			// 전화번호 라벨
			JLabel label_phoneNum = new JLabel("\uC804\uD654\uBC88\uD638");
			label_phoneNum.setBounds(39, 311, 56, 15);
			add(label_phoneNum);

			// 생년월일 라벨
			JLabel lbl_birth = new JLabel("\uC0DD\uB144\uC6D4\uC77C");
			lbl_birth.setBounds(39, 367, 56, 15);
			add(lbl_birth);

			// 전화번호 받는 텍스트필드
			JTextField phoneNum_tf = new JTextField();
			phoneNum_tf.setColumns(10);
			phoneNum_tf.setBounds(107, 305, 158, 29);
			add(phoneNum_tf);

			// 비밀번호 텍스트필드
			JTextField tf_pw = new JTextField();
			tf_pw.setColumns(10);
			tf_pw.setBounds(107, 360, 158, 29);
			add(tf_pw);

			// 비밀번호 확인 텍스트 필드
			JTextField tf_checkPw = new JTextField();
			tf_checkPw.setColumns(10);
			tf_checkPw.setBounds(107, 470, 158, 29);
			add(tf_checkPw);

			// 이름 텍스트 필드
			JTextField tf_name = new JTextField();
			tf_name.setColumns(10);
			tf_name.setBounds(107, 250, 158, 29);
			add(tf_name);

			// 생년월일 텍스트 필드
			JTextField tf_birth = new JTextField();
			tf_birth.setColumns(10);
			tf_birth.setBounds(107, 525, 158, 29);
			add(tf_birth);

			// 탈퇴 버튼
			JButton btn_delete = new JButton("\uD0C8\uD1F4");
			btn_delete.setBackground(Color.LIGHT_GRAY);
			btn_delete.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btn_delete.setBounds(277, 581, 91, 23);
			add(btn_delete);
			btn_delete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 탈퇴 버튼 이벤트
					// 1. 탈퇴 확인 알림 메시지를 발생시킨다.
					int result = JOptionPane.showConfirmDialog(null, "탈퇴하시겠습니까?", "탈퇴 확인", JOptionPane.YES_NO_OPTION);
					
					if (result == JOptionPane.YES_OPTION) {
						//2. 사용자가 'Yes'를 선택했다면
						//3. 회원 탈퇴 함수를 실행한다.
						try {
							MainFrame.theater.withdrawal(client_id);
							
							//4. 탈퇴 알림 메시지를 발생시킨다.
							JOptionPane.showMessageDialog(null, "탈퇴되었습니다.", "회원 탈퇴", JOptionPane.INFORMATION_MESSAGE);
							
							// 3. MainFrame(Login frame)을 생성한다.
							MainFrame mainFrame = new MainFrame();
							mainFrame.setVisible(true);
							
							// 4. Client Frame을 없앤다.
							dispose();
							
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				}
			});

			// 수정완료 버튼
			JButton btn_modify = new JButton("\uC218\uC815\uC644\uB8CC");
			btn_modify.setBackground(Color.LIGHT_GRAY);
			btn_modify.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btn_modify.setBounds(380, 581, 91, 23);
			add(btn_modify);
			btn_modify.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 수정 완료 버튼 이벤트
					//1. 비밀번호와 비밀번호 확인 입력값을 가져온다.
					String pw = tf_pw.getText();
					String pwcheck = tf_checkPw.getText();
					
					//2. 만약 입력값이 다르면
					if (pw.equals(pwcheck)) {
						//3. 알림 메시지를 발생시킨다.
						JOptionPane.showMessageDialog(null, "비밀번호 확인이 잘못되었습니다..", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						//4. 만약 비밀번호가 일치하다면
						//5. 입력한 데이터들을 모아 Person 객체를 만든다.
						String name = tf_name.getText();
						String phone = phoneNum_tf.getText();
						String birth = tf_birth.getText();
						
						Person p = new Person(name, phone, birth, client_id, pw);
						
						//6. 수정 함수를 실행시킨다.
						try {
							MainFrame.theater.modifyPerson(p);	
						} catch (Exception e2) {
							e2.printStackTrace();
						}
						
						// 7. 수정이 완료되면 수정 완료 알림 메시지를 발생시킨다.
						JOptionPane.showMessageDialog(null, "수정완료 되었습니다.", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
						
						//8. 텍스트필드를 지운다.
						tf_name.setText("");
						phoneNum_tf.setText("");
						tf_birth.setText("");
						tf_pw.setText("");
						tf_checkPw.setText("");
						
						// 9. MyPage panel로 변경한다.
						PanelChange("MyPagePanel");
						
					}
						
				}

			});

			// 로그인된 이름 뜨는 라벨(상단)
			JLabel lblUserName = new JLabel(userName + "님 /");
			lblUserName.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblUserName.setBounds(350, 83, 50, 15);
			add(lblUserName);

			// 로그아웃 라벨
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblLogout.setBounds(420, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 로그아웃 라벨 클릭 리스터
					// 1. 로그아웃 알림 메시지 발생
					//2. 로그아웃을 한다.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)을 생성한다.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame을 제거한다.
					dispose();
				}
			});

			// 아이디 라벨 (답)
			JLabel lbl_a_id = new JLabel(client_id);
			lbl_a_id.setBounds(107, 256, 136, 23);
			add(lbl_a_id);

			// 생일 6자리로 입력하라고 알려주는 라벨
			JLabel birthlabel = new JLabel("\u203B6\uC790\uB9AC\uB85C \uC785\uB825");
			birthlabel.setForeground(Color.RED);
			birthlabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			birthlabel.setBounds(279, 507, 135, 15);
			add(birthlabel);

		}

		public void paint(Graphics g) {
			super.paint(g);
//			g.setColor(Color.gray);
//			g.fillRect(187, 233, 130, 3);

		}
	}

	class TicketingPanel extends JPanel {
		String userName = Theater.personNow.getName();
		
		String TicketPath = "TICKET.png";
		
		JButton[] btns;
		int seatNum;
		
		String today;

		private LineBorder bb = new LineBorder(Color.black, 1, true);

		public TicketingPanel(MovieNow movieNow) {			
			setBounds(0, 0, 501, 677);
			setBackground(Color.WHITE);
			setLayout(null);
			
			// 오늘 날짜
			LocalDateTime current = LocalDateTime.now();
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyMMdd");
			this.today = current.format(format);
			
			// 티켓이미지(메인화면)라벨
			JLabel Ticket_main = new JLabel("티켓_메인화면");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 티켓 이미지 클릭 리스터
					// 1. ClientMain panel로 변경한다.
					PanelChange("ClientMainPanel");
				}
			});

//			// ImageIcon 객체 생성 => Image 추출 및 크기 조절해서 새로운 객체 생성
//			ImageIcon ticektIcon = new ImageIcon(MainFrame.class.getResource(TicketPath));
//			Image ticketImg = ticektIcon.getImage();
//			Image changedticektImg = ticketImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);
//			ImageIcon changedticektIcon = new ImageIcon(changedticektImg);
//			Ticket_main.setIcon(changedticektIcon);

			// 예매 상단바 라벨
			JLabel lbl_ticketing = new JLabel("\uC608\uB9E4");
			lbl_ticketing.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_ticketing.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_ticketing.setBorder(bb);
			lbl_ticketing.setBounds(0, 109, 162, 42);
			add(lbl_ticketing);
			lbl_ticketing.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 예매 카테고리 클릭 리스너
					// 1. Reserve panel로 변경한다.
					PanelChange("ReservePanel");
				}
			});

			// 예매 예정 영화 상단바 라벨
			JLabel lbl_scheduleMovie = new JLabel("\uAC1C\uBD09\uC608\uC815\uC601\uD654");
			lbl_scheduleMovie.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_scheduleMovie.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_scheduleMovie.setBorder(bb);
			lbl_scheduleMovie.setBounds(161, 109, 164, 42);
			add(lbl_scheduleMovie);
			lbl_scheduleMovie.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 상영예정작 클릭 리스터
					// 1. DDayPlusMovie panel로 변경한다.
					PanelChange("DDayPlusMoviePanel");
				}
			});

			// 마이페이지 상단바 라벨
			JLabel lbl_myPage = new JLabel("\uB9C8\uC774\uD398\uC774\uC9C0");
			lbl_myPage.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_myPage.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_myPage.setBorder(bb);
			lbl_myPage.setBounds(324, 109, 162, 42);
			add(lbl_myPage);
			lbl_myPage.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 마이페이지 클릭 리스너
					// 1. 마이페이지 panel로 변경한다.
					PanelChange("MyPagePanel");
				}
			});

			// 메인 타이틀 라벨
			JLabel lbl_title = new JLabel("\uC601\uD654\uC81C\uBAA9, \uC0C1\uC601\uAD00, \uC0C1\uC601\uC2DC\uAC04");
			lbl_title.setFont(new Font("맑은 고딕", Font.BOLD, 16));
			lbl_title.setBounds(43, 175, 271, 34);
			add(lbl_title);

			// 유저 이름 라벨
			JLabel lblUserName = new JLabel(userName + "님  /");
			lblUserName.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblUserName.setBounds(350, 83, 66, 15);
			add(lblUserName);

			// 로그아웃 라벨
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblLogout.setBounds(420, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 로그아웃 라벨 클릭 리스터
					// 1. 로그아웃 알림 메시지 발생
					//2. 로그아웃을 한다.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)을 생성한다.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame을 제거한다.
					dispose();
				}
			});

			// 예매 버튼
			JButton btn_buy = new JButton("\uC608\uB9E4");
			btn_buy.setBackground(Color.LIGHT_GRAY);
			btn_buy.setForeground(Color.BLACK);
			btn_buy.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btn_buy.setBounds(368, 581, 91, 34);
			add(btn_buy);
			btn_buy.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 예매 버튼 이벤트
					// 1. 예매 확인 frame을 생성한다.
					ReserveCheckFrame reserveCheck = new ReserveCheckFrame(movieNow, Integer.toString(seatNum));
					reserveCheck.setVisible(true);
					// 2. client main frame으로 변경한다.
					PanelChange("ClientMainPanel");
				}
			});

			// 예매 좌석 고를 수 있게 하는 패널
			JPanel panel = new JPanel();
			panel.setBackground(Color.LIGHT_GRAY);
			panel.setBounds(30, 230, 429, 245);
			add(panel);
			panel.setLayout(null);

			// 스크린 패널
			JPanel screen_panel = new JPanel();
			screen_panel.setBackground(Color.GRAY);
			screen_panel.setBounds(35, 10, 361, 35);
			panel.add(screen_panel);
			screen_panel.setLayout(null);

			// 스크린 라벨
			JLabel lblNewLabel_1 = new JLabel("\uC2A4\uD06C\uB9B0");
			lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel_1.setFont(new Font("맑은 고딕", Font.BOLD, 14));
			lblNewLabel_1.setBounds(0, 0, 361, 36);
			screen_panel.add(lblNewLabel_1);
			lblNewLabel_1.setBackground(Color.DARK_GRAY);
			lblNewLabel_1.setForeground(Color.WHITE);

			// 버튼 1~21
			btns = new JButton[22];
			for(int i = 1; i < 22; i++) {
				btns[i] = new JButton(""+i+"");
				btns[i].setBackground(Color.white);
				btns[i].setFont(new Font("맑은 고딕", Font.PLAIN, 10));
				panel.add(btns[i]);
				int j = i;
				btns[i].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						seatNum = j;
					}
					
				});
			}
			
			//버튼 위치 지정
			btns[1].setBounds(10, 85, 46, 23);
			btns[8].setBounds(10, 145, 46, 23);
			btns[15].setBounds(10, 200, 46, 23);
			btns[2].setBounds(70, 85, 46, 23);
			btns[3].setBounds(130, 85, 46, 23);
			btns[4].setBounds(190, 85, 46, 23);
			btns[5].setBounds(250, 85, 46, 23);
			btns[6].setBounds(310, 85, 46, 23);
			btns[7].setBounds(370, 85, 46, 23);
			btns[9].setBounds(70, 145, 46, 23);
			btns[10].setBounds(130, 145, 46, 23);
			btns[11].setBounds(190, 145, 46, 23);
			btns[12].setBounds(250, 145, 46, 23);
			btns[13].setBounds(310, 145, 46, 23);
			btns[14].setBounds(370, 145, 46, 23);
			btns[16].setBounds(70, 201, 46, 23);
			btns[17].setBounds(130, 201, 46, 23);
			btns[18].setBounds(190, 201, 46, 23);
			btns[19].setBounds(250, 201, 46, 23);
			btns[20].setBounds(310, 201, 46, 23);
			btns[21].setBounds(370, 201, 46, 23);

			// 이미 선택된 좌석 체크
			ResultSet rs;
			try {
				rs = MainFrame.screen.returnSeat(today, movieNow.getRound());
				while (rs.next()) {
					int seat = rs.getInt("seat");
					btns[seat].setEnabled(false);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 한 좌석만 예매 가능하다는 것 알려주는 라벨
			JLabel lbl_alert = new JLabel("\u203B1\uC778 1\uC88C\uC11D\uB9CC \uC608\uB9E4 \uAC00\uB2A5\uD569\uB2C8\uB2E4.\u203B");
			lbl_alert.setForeground(Color.RED);
			lbl_alert.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_alert.setFont(new Font("맑은 고딕", Font.BOLD, 12));
			lbl_alert.setBounds(30, 485, 429, 22);
			add(lbl_alert);

		}
	}

	class SearchMoviePanel extends JPanel {

		String userName = Theater.personNow.getName();
		String TicketPath = "TICKET.png";

		private LineBorder bb = new LineBorder(Color.black, 1, true);
		
		ResultSet rs;
		private String selectedMovie = null;

		public SearchMoviePanel(ResultSet rs) {
			
			setBounds(0, 0, 501, 677);
			setBackground(Color.WHITE);
			setLayout(null);

			// 티켓이미지(메인화면)라벨
			JLabel Ticket_main = new JLabel("티켓_메인화면");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 티켓 이미지 클릭 리스터
					// 1. ClientMain panel로 변경한다.
					PanelChange("ClientMainPanel");
				}
			});
//
//			// ImageIcon 객체 생성 => Image 추출 및 크기 조절해서 새로운 객체 생성
//			ImageIcon ticektIcon = new ImageIcon(MainFrame.class.getResource(TicketPath));
//			Image ticketImg = ticektIcon.getImage();
//			Image changedticektImg = ticketImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);
//			ImageIcon changedticektIcon = new ImageIcon(changedticektImg);
//			Ticket_main.setIcon(changedticektIcon);

			// 스크롤
			JScrollPane scrollPane = new JScrollPane();
			JPanel firstpanel = new JPanel();
			Dimension size = new Dimension(447, 600);
			firstpanel.setPreferredSize(size);
			scrollPane.setViewportView(firstpanel);
			scrollPane.setBounds(10, 170, 465, 361);
			add(scrollPane);

			// 첫번째 패널(영화들 목록 보여주는(?)패널)
			firstpanel.setBackground(Color.WHITE);
			size.setSize(200, 200);
			firstpanel.setLayout(new FlowLayout(FlowLayout.LEFT, 18, 15));

			// 영화 하나 패널
			JPanel moviepanel1 = new JPanel();
			moviepanel1.setBackground(Color.WHITE);
			firstpanel.add(moviepanel1);

			// 캡슐화
			Dimension dim = new Dimension(130, 200);

			// rs로 검색결과 보여주기
			JPanel[] moviepanels = new JPanel[10];
			Image[] images = new Image[10];
			ImageIcon[] imageIcons = new ImageIcon[10];
			try {
				int i = 0;
				while (rs.next()) {
					// 패널 설정
					moviepanels[i] = new JPanel();
					moviepanels[i].setBackground(Color.WHITE);
					firstpanel.add(moviepanels[i]);
					moviepanels[i].setPreferredSize(dim);
					moviepanels[i].setLayout(null);
					// 영화정보 가져오기
					String title = rs.getString("title");
					String posterURL = rs.getString("posterURL");
					// 이미지 아이콘 설정
					URL url = new URL(posterURL);
					images[i] = ImageIO.read(url);
					images[i] = images[i].getScaledInstance(128, 160, Image.SCALE_SMOOTH);
					imageIcons[i] = new ImageIcon(images[i]);
					// 영화 포스터
					JLabel lblPoster = new JLabel("영화 포스터");
					lblPoster.setBounds(0, 0, 130, 167);
					moviepanels[i].add(lblPoster);
					lblPoster.setHorizontalAlignment(SwingConstants.CENTER);
					lblPoster.setIcon(imageIcons[i]);
					// 라벨: 영화 제목
					JLabel lblTitle = new JLabel(title);
					lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 12));
					lblTitle.setBounds(10, 172, 106, 22);
					moviepanels[i].add(lblTitle);
					// 클릭 이벤트
					moviepanels[i].addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							selectedMovie = title;
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 로그인 한 이름 보여줌
			JLabel lblUserName = new JLabel(userName + "\uB2D8  /");
			lblUserName.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblUserName.setBounds(350, 83, 66, 15);
			add(lblUserName);

			// 로그아웃
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblLogout.setBounds(420, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 로그아웃 라벨 클릭 리스터
					// 1. 로그아웃 알림 메시지 발생
					//2. 로그아웃을 한다.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)을 생성한다.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame을 제거한다.
					dispose();
				}
			});

			// 상단바 예매 라벨
			JLabel lbl_ticketing = new JLabel("\uC608\uB9E4");
			lbl_ticketing.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_ticketing.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_ticketing.setBorder(bb);
			lbl_ticketing.setBounds(0, 109, 162, 42);
			add(lbl_ticketing);
			lbl_ticketing.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 예매 카테고리 클릭 리스너
					// 1. Reserve panel로 변경한다.
					PanelChange("ReservePanel");
				}
			});

			// 상단바 개봉예정영화 라벨
			JLabel lbl_scheduleMovie = new JLabel("\uAC1C\uBD09\uC608\uC815\uC601\uD654");
			lbl_scheduleMovie.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_scheduleMovie.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_scheduleMovie.setBorder(bb);
			lbl_scheduleMovie.setBounds(161, 109, 164, 42);
			add(lbl_scheduleMovie);
			lbl_scheduleMovie.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 상영예정작 클릭 리스터
					// 1. DDayPlusMovie panel로 변경한다.
					PanelChange("DDayPlusMoviePanel");
				}
			});

			// 마이페이지 라벨
			JLabel lbl_myPage = new JLabel("\uB9C8\uC774\uD398\uC774\uC9C0");
			lbl_myPage.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_myPage.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			lbl_myPage.setBorder(bb);
			lbl_myPage.setBounds(324, 109, 162, 42);
			add(lbl_myPage);
			lbl_myPage.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 마이페이지 클릭 리스너
					// 1. 마이페이지 panel로 변경한다.
					PanelChange("MyPagePanel");
				}
			});

			// 검색 텍스트필드
			JTextField tfSearch = new JTextField();
			tfSearch.setBounds(100, 589, 296, 30);
			add(tfSearch);

			// 검색 버튼
			JButton btnSearch = new JButton("\uAC80\uC0C9");
			btnSearch.setBackground(Color.LIGHT_GRAY);
			btnSearch.setBounds(408, 588, 70, 30);
			add(btnSearch);
			btnSearch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 영화 검색 버튼 이벤트
					// 1. 텍스트필드에 입력받은 데이터를 가져온다.
					String keyword = tfSearch.getText();
					// 2. 만약 입력받은 텍스트가 공백이라면
					if (keyword.equals("")) {
						// 3. 알림 메시지를 발생시킨다.
						JOptionPane.showMessageDialog(null, "검색할 영화 제을 입력해주세요..", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
					} else {
						// 4. 만약 입력받은 텍스트가 공백이 아니라면
						try {
							// 5. 입력받은 키워드로 영화를 검색한다.
							ResultSet rs = MainFrame.theater.searchMovie(keyword);
							
							// 6. 텍스트필드를 지우고
							tfSearch.setText("");
							
							// 7. SearchMovie panel로 변경한다.	
							// 이때, 입력받은 키워드의 검색 결과를 매개변수로 전달한다.
							ShowSearchMovie(rs);
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});

			// 검색라벨
			JLabel lbl_search = new JLabel("\uC601\uD654 \uAC80\uC0C9");
			lbl_search.setFont(new Font("맑은 고딕", Font.BOLD, 18));
			lbl_search.setBounds(14, 586, 96, 34);
			add(lbl_search);

			// 예매버튼
			JButton buy_button = new JButton("\uC608\uB9E4");
			buy_button.setForeground(Color.WHITE);
			buy_button.setBackground(Color.RED);
			buy_button.setBounds(408, 548, 70, 30);
			add(buy_button);
			buy_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 예매 버튼 이벤트
					// 1. 예매 panel로 변경한다.
					ReserveSelectdMovie(selectedMovie);
				}
			});

		}
	}
}

//예매 확인 프레임: 예매 확인 크기의 프레임은 panel을 예매 확인 panel 하나만 사용하기 때문에 프레임 안에 panel을 생성하였음
class ReserveCheckFrame extends JFrame {

	//private LineBorder bb = new LineBorder(Color.black, 1, true);
	private JPanel ReserveCheckPanel; // 예매 확인 panel

	// 생성자
	public ReserveCheckFrame(MovieNow reserveMovie, String reserveSeat) {
		
		String movieName = reserveMovie.getTitle();
		String movieDate = reserveMovie.getDate();
		int movieRound = reserveMovie.getRound();
		String movieSeat = reserveSeat;
		
		
		// 예매확인 프레임 설정
		setTitle("Reserve Check"); // 프레임 title 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 501, 580);
		ReserveCheckPanel = new JPanel();
		ReserveCheckPanel.setBackground(Color.WHITE);
		ReserveCheckPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(ReserveCheckPanel);
		ReserveCheckPanel.setLayout(null);
		
		//예매확인 타이틀 라벨
		JLabel lbl_checkTicket = new JLabel("\uC608\uB9E4 \uD655\uC778");
		lbl_checkTicket.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_checkTicket.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		lbl_checkTicket.setBounds(0, 66, 487, 34);
		ReserveCheckPanel.add(lbl_checkTicket);

		//영화 제목 라벨
		JLabel lbl_movieTitle = new JLabel("\uC601\uD654 \uC81C\uBAA9:");
		lbl_movieTitle.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		lbl_movieTitle.setBounds(80, 140, 80, 23);
		ReserveCheckPanel.add(lbl_movieTitle);
		
		//영화 상영날짜 라벨
		JLabel lbl_movieDate = new JLabel("\uC0C1\uC601 \uB0A0\uC9DC:");
		lbl_movieDate.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		lbl_movieDate.setBounds(80, 180, 57, 23);
		ReserveCheckPanel.add(lbl_movieDate);
		
		//영화 회차정보 라벨
		JLabel lbl_movieRound = new JLabel("\uD68C\uCC28 \uC815\uBCF4:");
		lbl_movieRound.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		lbl_movieRound.setBounds(80, 259, 80, 23);
		ReserveCheckPanel.add(lbl_movieRound);
		
		//영화 좌석번호 라벨
		JLabel lbl_movieSeat = new JLabel("\uC88C\uC11D\uBC88\uD638:");
		lbl_movieSeat.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		lbl_movieSeat.setBounds(80, 300, 80, 23);
		ReserveCheckPanel.add(lbl_movieSeat);
		
		//예매 타이틀 정보 확인 라벨
		JLabel lbl_a_moveTitle = new JLabel(movieName);
		lbl_a_moveTitle.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		lbl_a_moveTitle.setBounds(149, 144, 185, 15);
		ReserveCheckPanel.add(lbl_a_moveTitle);
		
		//예매 날짜 정보 확인 라벨
		JLabel lbl_a_movieDate = new JLabel(movieDate);
		lbl_a_movieDate.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		lbl_a_movieDate.setBounds(149, 184, 185, 15);
		ReserveCheckPanel.add(lbl_a_movieDate);
		
		//예매 회차정보 확인 라벨
		JLabel lbl_a_movieRound = new JLabel(Integer.toString(movieRound));
		lbl_a_movieRound.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		lbl_a_movieRound.setBounds(149, 264, 185, 15);
		ReserveCheckPanel.add(lbl_a_movieRound);
		
		//예매 좌석 정보 확인 라벨
		JLabel lbl_a_movieSeat = new JLabel(movieSeat + " \uBC88");
		lbl_a_movieSeat.setFont(new Font("맑은고딕", Font.PLAIN, 12));
		lbl_a_movieSeat.setBounds(149, 304, 185, 15);
		ReserveCheckPanel.add(lbl_a_movieSeat);
		
		//예매 하시겠습니까 라벨
		JLabel lbl_Q = new JLabel("\uC608\uB9E4 \uD558\uC2DC\uACA0\uC2B5\uB2C8\uAE4C?");
		lbl_Q.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_Q.setFont(new Font("맑은 고딕", Font.BOLD, 18));
		lbl_Q.setBounds(0, 385, 487, 34);
		ReserveCheckPanel.add(lbl_Q);
		
		//버튼 no
		JButton btn_no = new JButton("\uC544\uB2C8\uC694");
		btn_no.setBackground(Color.LIGHT_GRAY);
		btn_no.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		btn_no.setBounds(245, 450, 108, 40);
		ReserveCheckPanel.add(btn_no);
		btn_no.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 예매 확인 No 버튼 이벤트
				// 1. 예매 확인 프레임 닫기
				dispose();
			}
		});
		
		//버튼 yes
		JButton btn_yes = new JButton("\uC608");
		btn_yes.setBackground(Color.LIGHT_GRAY);
		btn_yes.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		btn_yes.setBounds(122, 450, 108, 40);
		ReserveCheckPanel.add(btn_yes);
		btn_yes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 예매 확인 yes 버튼 이벤트
				//1. yes 버튼을 누르면 
				//2. 입력한 데이터로 reservInfo(예매 정보) 객체를 만든다.
				String userID = Theater.personNow.getId();
				ReservInfo reservInfo = new ReservInfo(userID, movieName, movieDate, movieRound, Integer.parseInt(movieSeat));
				
				//3. 만든 reservInfo 객체를 매개변수로 screen의 예매 함수를 사용한다.
				try {
					MainFrame.screen.booking(reservInfo);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				// 4. 예매 후 알림 메시지를 발생시킨다.
				JOptionPane.showMessageDialog(null, "예매되었습니다.", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
				
				// 5. 예매 확인 frame을 닫는다.
				dispose();
			}
		});

	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.gray);
		g.fillRect(200, 125, 100, 3);
	}
}

//관리자 프레임: 관리자 크기의 프레임은 panel을 여러개 사용하기 때문에 panel 교체 함수를 만들어 사용하였음
class ManagerFrame extends JFrame {

	public ManagerMainPanel managerMain = new ManagerMainPanel(); // manager 메인 panel
	public ClientManagementPanel clientManagement = new ClientManagementPanel(); // 회원 관리 panel
	public MovieManagementPanel movieManagement = new MovieManagementPanel(); // 영화 관리 panel
	public MovieTablePanel movieTable = new MovieTablePanel(); // 상영시간표 관리 panel

	public ManagerFrame() {
		// 1. ManagerMainpanel 생성
		managerMain = new ManagerMainPanel();
		// 2. ClientMainPanel 추가
		add(managerMain);

		// 사용자 프레임 설정
		setTitle("Theater Program"); // 프레임 title 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 521, 677); // 프레임 크기 설정
		setBackground(Color.WHITE); // 프레임 배경 설정
		setLayout(null); // 프레임 레이아웃 설정
		setVisible(true);
	}

	// panel 바꾸는 함수
	public void PanelChange(String panelName) {
		if (panelName.equals("ManagerMainPanel")) {
			// 만약 바꿀 panel이 ManagerMainPanel이라면
			getContentPane().removeAll();
			getContentPane().add(managerMain);
			revalidate();
			repaint();
		}
		if (panelName.equals("ClientManagementPanel")) {
			// 만약 바꿀 panel이 ClientManagementPanel이라면
			getContentPane().removeAll();
			getContentPane().add(clientManagement);
			revalidate();
			repaint();
		} else if (panelName.equals("MovieManagementPanel")) {
			// 만약 바꿀 panel이 MovieManagementPanel이라면
			getContentPane().removeAll();
			getContentPane().add(movieManagement);
			revalidate();
			repaint();
		} else if (panelName.equals("MovieTablePanel")) {
			// 만약 바꿀 panel이 MovieTablePanel이라면
			getContentPane().removeAll();
			getContentPane().add(movieTable);
			revalidate();
			repaint();
		}
	}

	class ManagerMainPanel extends JPanel {

		//접속 중인 회원 이름
		String userName = Theater.personNow.getName();

		int numberOfMember;
		int screeningMovie;

		String TicketPath = "TICKET.png";

		public ManagerMainPanel() {
			
			try {
				//회원 수, 영화 수 가져오기
				numberOfMember = MainFrame.theater.countPerson();
				screeningMovie = MainFrame.theater.countMovie();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			setBounds(0, 0, 521, 677);
			setBackground(Color.WHITE);
			setLayout(null);

			// 티켓이미지(메인화면)라벨
			JLabel Ticket_main = new JLabel("티켓_메인화면");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 티켓 이미지 클릭 리스터
					// 1. ManagerMain panel로 변경한다.
					PanelChange("ManagerMainPanel");
				}
			});

			// 로그인한 사람 라벨
			JLabel lblUserName = new JLabel(userName + "\uB2D8  /");
			lblUserName.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblUserName.setBounds(375, 83, 65, 15);
			add(lblUserName);

			// 로그아웃 라벨
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblLogout.setBounds(440, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 로그아웃 라벨 클릭 리스터
					// 1. 로그아웃 알림 메시지 발생
					//2. 로그아웃을 한다.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)을 생성한다.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame을 제거한다.
					dispose();
				}
			});

			// 회원관리 라벨
			JLabel lblMemberManage = new JLabel("\uD68C\uC6D0\uAD00\uB9AC");
			lblMemberManage.setHorizontalAlignment(SwingConstants.CENTER);
			lblMemberManage.setFont(new Font("맑은 고딕", Font.BOLD, 20));
			lblMemberManage.setBounds(180, 170, 150, 30);
			add(lblMemberManage);

			// 총 회원수 라벨
			JLabel lblNumberOfMember = new JLabel("\uCD1D \uD68C\uC6D0 \uC218: ");
			lblNumberOfMember.setHorizontalAlignment(SwingConstants.LEFT);
			lblNumberOfMember.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			lblNumberOfMember.setBounds(195, 230, 88, 30);
			add(lblNumberOfMember);

			// 회원정보 관리 버튼
			JButton btnMemberManager = new JButton("\uD68C\uC6D0 \uC815\uBCF4 \uAD00\uB9AC");
			btnMemberManager.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btnMemberManager.setBackground(Color.LIGHT_GRAY);
			btnMemberManager.setBounds(180, 280, 150, 30);
			add(btnMemberManager);
			btnMemberManager.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 회원정보 관리 버튼 이벤트
					PanelChange("ClientManagementPanel");
				}
			});

			// 영화관리 라벨
			JLabel lblMovieManage = new JLabel("\uC601\uD654\uAD00\uB9AC");
			lblMovieManage.setHorizontalAlignment(SwingConstants.CENTER);
			lblMovieManage.setFont(new Font("맑은 고딕", Font.BOLD, 20));
			lblMovieManage.setBounds(28, 390, 202, 30);
			add(lblMovieManage);

			// 상영중 영화 라벨
			JLabel lblScreeningMovie = new JLabel("\uCD1D \uC601\uD654 \uC218:");
			lblScreeningMovie.setHorizontalAlignment(SwingConstants.LEFT);
			lblScreeningMovie.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
			lblScreeningMovie.setBounds(81, 442, 97, 38);
			add(lblScreeningMovie);

			// 영화관리 버튼
			JButton btnMovieManager = new JButton("\uC601\uD654 \uAD00\uB9AC");
			btnMovieManager.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btnMovieManager.setBackground(Color.LIGHT_GRAY);
			btnMovieManager.setBounds(61, 500, 150, 30);
			add(btnMovieManager);
			btnMovieManager.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 영화 관리 버튼 이벤트
					PanelChange("MovieManagementPanel");
				}
			});

			// 총 회원수 대답 라벨
			JLabel Memberlabel = new JLabel(numberOfMember + " 명");
			Memberlabel.setBounds(281, 233, 77, 26);
			add(Memberlabel);

			// 상영중 영화 대답 라벨
			JLabel MovieLabel = new JLabel(screeningMovie + " 개");
			MovieLabel.setBounds(166, 450, 55, 26);
			add(MovieLabel);

			// 상영 시간표 라벨
			JLabel screenlabel = new JLabel("\uC0C1\uC601\uC2DC\uAC04\uD45C \uAD00\uB9AC");
			screenlabel.setHorizontalAlignment(SwingConstants.CENTER);
			screenlabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
			screenlabel.setBounds(277, 390, 218, 30);
			add(screenlabel);

			// 상영 시간표 버튼
			JButton screenbutton = new JButton("\uC0C1\uC601\uC2DC\uAC04\uD45C \uAD00\uB9AC");
			screenbutton.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			screenbutton.setBackground(Color.LIGHT_GRAY);
			screenbutton.setBounds(305, 500, 150, 30);
			add(screenbutton);
			screenbutton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 상영시간표 버튼 이벤트
					PanelChange("MovieTablePanel");
				}
			});
		}

		public void paint(Graphics g) {
			super.paint(g);
			g.drawLine(0, 115, 560, 115);
			g.drawRect(130, 140, 250, 210);
			g.drawRect(30, 370, 220, 210);
			g.drawRect(270, 370, 220, 210);
		}
	}

	class ClientManagementPanel extends JPanel {

		String userName = Theater.personNow.getName();
		String TicketPath = "TICKET.png";
		
		private int selectedRow;
		
		private JScrollPane scrollPane = new JScrollPane();

		public ClientManagementPanel() {
			setBounds(0, 0, 521, 677);
			setBackground(Color.WHITE);
			setLayout(null);
			

			// 티켓이미지(메인화면)라벨
			JLabel Ticket_main = new JLabel("티켓_메인화면");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 티켓 이미지 클릭 리스터
					// 1. ManagerMain panel로 변경한다.
					PanelChange("ManagerMainPanel");
				}
			});

			// 로그인한 사람 라벨
			JLabel lblUserName = new JLabel(userName + "\uB2D8  /");
			lblUserName.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblUserName.setBounds(375, 83, 65, 15);
			add(lblUserName);

			// 로그아웃 라벨
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblLogout.setBounds(440, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 로그아웃 라벨 클릭 리스터
					// 1. 로그아웃 알림 메시지 발생
					//2. 로그아웃을 한다.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)을 생성한다.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame을 제거한다.
					dispose();
				}
			});

			// 이전화면 라벨
			JLabel lbl_LastPanel = new JLabel("\u25C0 \uC774\uC804 \uD654\uBA74");
			lbl_LastPanel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lbl_LastPanel.setBounds(25, 120, 74, 15);
			add(lbl_LastPanel);
			lbl_LastPanel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 이전화면 라벨 클릭 리스터
					// 1. ManagerMain panel로 변경한다.
					PanelChange("ManagerMainPanel");
				}
			});

			// 고객정보관리(타이틀) 라벨
			JLabel lbl_title = new JLabel("\uACE0\uAC1D\uC815\uBCF4 \uAD00\uB9AC");
			lbl_title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
			lbl_title.setBounds(29, 168, 250, 30);
			add(lbl_title);

			// 고객 검색 라벨
			JLabel lbl_search = new JLabel("\uACE0\uAC1D \uAC80\uC0C9");
			lbl_search.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
			lbl_search.setBounds(30, 219, 69, 19);
			add(lbl_search);

			// 검색 텍스트필드
			JTextField tf_find = new JTextField();
			tf_find.setBounds(103, 220, 149, 21);
			add(tf_find);
			tf_find.setColumns(10);

			// 검색 버튼
			JButton btn_search = new JButton("\uAC80\uC0C9");
			btn_search.setBackground(Color.LIGHT_GRAY);
			btn_search.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btn_search.setBounds(264, 220, 69, 23);
			add(btn_search);
			// 버튼 리스너: 고객 정보를 검색한다
			btn_search.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// 테이블 생성
					String[] colName = { "\uC774\uB984", "\uC804\uD654\uBC88\uD638", "\uC0DD\uB144\uC6D4\uC77C",
							"\uC544\uC774\uB514", "\uBE44\uBC00\uBC88\uD638" };
					DefaultTableModel model = new DefaultTableModel(colName, 0);
					JTable table = new JTable(model);

					table.setBackground(Color.WHITE);
					table.setFillsViewportHeight(true);
					table.setForeground(Color.DARK_GRAY);

					// 텍스트 필드 값: 아이디
					String id = tf_find.getText();
					try {
						Person p = MainFrame.theater.searchPerson(id);
						if (p == null) {
							JOptionPane.showMessageDialog(null, "존재하지 않는 고객입니다");
						} else {
							Object[] data = new Object[] { p.getName(), p.getPhone(), p.getBirth(), p.getId(), p.getPw() };
							model.addRow(data);
							scrollPane.setViewportView(table);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
				}
				
			});


			// 새로고침 버튼
			JButton btn_reload = new JButton("\uC0C8\uB85C\uACE0\uCE68");
			btn_reload.setBackground(Color.LIGHT_GRAY);
			btn_reload.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btn_reload.setBounds(394, 550, 91, 23);
			add(btn_reload);
			// 버튼 리스너: 다시 전체 고객을 불러온다
			btn_reload.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// 테이블 생성
					String[] colName = { "\uC774\uB984", "\uC804\uD654\uBC88\uD638", "\uC0DD\uB144\uC6D4\uC77C",
							"\uC544\uC774\uB514", "\uBE44\uBC00\uBC88\uD638" };
					DefaultTableModel model = new DefaultTableModel(colName, 0);
					JTable table = new JTable(model);

					table.setBackground(Color.WHITE);
					table.setFillsViewportHeight(true);
					table.setForeground(Color.DARK_GRAY);

					// 테이블에 모델로 추가
					try {
						ResultSet rs = MainFrame.theater.personAll();
						Object[] data = null;
						while (rs.next()) {
							String name = rs.getString("name");
							String phone = rs.getString("phone");
							String birth = rs.getString("birth");
							String id = rs.getString("id");
							String pw = rs.getString("pw");

							data = new Object[] { name, phone, birth, id, pw };
							model.addRow(data);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					scrollPane.setViewportView(table);
					
				}
				
			});

			// scrollPane = new JScrollPane();
			scrollPane.setBounds(12, 272, 483, 248);
			add(scrollPane);

			// table
			String[] colName = { "\uC774\uB984", "\uC804\uD654\uBC88\uD638", "\uC0DD\uB144\uC6D4\uC77C",
					"\uC544\uC774\uB514", "\uBE44\uBC00\uBC88\uD638" };
			DefaultTableModel model = new DefaultTableModel(colName, 0);
			JTable table = new JTable(model);
			table.setBackground(Color.WHITE);
			table.setFillsViewportHeight(true);
			table.setForeground(Color.DARK_GRAY);

			table.getColumnModel().getColumn(0).setPreferredWidth(70);
			table.getColumnModel().getColumn(1).setPreferredWidth(70);
			table.getColumnModel().getColumn(2).setPreferredWidth(70);
			table.getColumnModel().getColumn(3).setPreferredWidth(70);
			table.getColumnModel().getColumn(4).setPreferredWidth(70);

			// 테이블 클릭 리스너
			table.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					selectedRow = table.getSelectedRow();
				}
			});

			// 테이블에 모델로 추가
			try {
				ResultSet rs = MainFrame.theater.personAll();
				Object[] data = null;
				while (rs.next()) {
					String name = rs.getString("name");
					String phone = rs.getString("phone");
					String birth = rs.getString("birth");
					String id = rs.getString("id");
					String pw = rs.getString("pw");

					data = new Object[] { name, phone, birth, id, pw };
					model.addRow(data);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			scrollPane.setViewportView(table);

			// 삭제버튼
			JButton btn_delete = new JButton("\uC0AD\uC81C");
			btn_delete.setBackground(Color.LIGHT_GRAY);
			btn_delete.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btn_delete.setBounds(292, 550, 91, 23);
			add(btn_delete);
			// 삭제 리스너
			btn_delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (selectedRow == -1) {
						JOptionPane.showMessageDialog(null, "선택된 회원이 없습니다");
					} else {
						int result = JOptionPane.showConfirmDialog(null, "선택한 회원 정보가 삭제됩니다." + '\n' + "계속하시겠습니까?", "Confirm",
								JOptionPane.YES_NO_OPTION);
						if (result == JOptionPane.YES_OPTION) {
							// 사용자가 예를 선택한 경우
							// 선택한 행 삭제
							String id = (String) table.getValueAt(selectedRow, 3);
							try {
								MainFrame.theater.withdrawal(id);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							((DefaultTableModel) table.getModel()).removeRow(selectedRow);
						}
					}
					
				}
				
			});

		}

		public void paint(Graphics g) {
			super.paint(g);
			g.drawLine(0, 140, 560, 140);
		}

	}

	class MovieManagementPanel extends JPanel {
		String TicketPath = "TICKET.png";
		String userName = Theater.personNow.getName();

		public MovieManagementPanel() {
			setBounds(0, 0, 521, 677);
			setBackground(Color.WHITE);
			setLayout(null);

			String arr[] = new String[4];
			
			// 티켓이미지(메인화면)라벨
			JLabel Ticket_main = new JLabel("티켓_메인화면");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 티켓 이미지 클릭 리스터
					// 1. ManagerMain panel로 변경한다.
					PanelChange("ManagerMainPanel");
				}
			});

			// 로그인한 사람 라벨
			JLabel lblUserName = new JLabel(userName + "\uB2D8  /");
			lblUserName.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblUserName.setBounds(375, 83, 65, 15);
			add(lblUserName);

			// 로그아웃 라벨
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblLogout.setBounds(440, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 로그아웃 라벨 클릭 리스터
					// 1. 로그아웃 알림 메시지 발생
					//2. 로그아웃을 한다.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)을 생성한다.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame을 제거한다.
					dispose();
				}
			});

			// 이전화면 라벨
			JLabel lbl_LastPanel = new JLabel("\u25C0 \uC774\uC804 \uD654\uBA74");
			lbl_LastPanel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lbl_LastPanel.setBounds(25, 120, 74, 15);
			add(lbl_LastPanel);
			lbl_LastPanel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 이전화면 라벨 클릭 리스터
					// 1. ManagerMain panel로 변경한다.
					PanelChange("ManagerMainPanel");
				}
			});
			
			// 스크롤 + 테이블
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(21, 228, 465, 203);
			add(scrollPane);
			JTable table = new JTable();
			table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "\uC81C\uBAA9",
					"\uC0C1\uC601\uC2DC\uAC04", "\uD3EC\uC2A4\uD130URL", "\uAC1C\uBD09\uB0A0\uC9DC" }));
			scrollPane.setViewportView(table);
			
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			
			try {
				ResultSet rs = MainFrame.theater.movieAll();

				model.setNumRows(0); // 초기화

				while (rs.next()) {
					String title = rs.getString("title");
					int runTime = rs.getInt("runTime");
					String posterURL = rs.getString("posterURL");
					String dday = rs.getString("dday");

					arr[0] = title;
					arr[1] = Integer.toString(runTime);
					arr[2] = posterURL;
					arr[3] = dday;

					model.addRow(arr);
				}

			} catch (SQLException e1) {
				return;
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			// 현재 등록된 영화 (타이틀 라벨)
			JLabel lblMovieList = new JLabel("\uC601\uD654 \uAD00\uB9AC");
			lblMovieList.setFont(new Font("맑은 고딕", Font.BOLD, 20));
			lblMovieList.setHorizontalAlignment(SwingConstants.LEFT);
			lblMovieList.setBounds(20, 150, 230, 30);
			add(lblMovieList);

			// 검색 텍스트필드
			JTextField tf_search = new JTextField();
			tf_search.setBounds(20, 195, 165, 25);
			add(tf_search);
			tf_search.setColumns(10);

			// 검색 버튼
			JButton btn_search = new JButton("\uAC80\uC0C9");
			btn_search.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btn_search.setBackground(Color.LIGHT_GRAY);
			btn_search.setBounds(192, 195, 60, 25);
			add(btn_search);
			btn_search.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String title = tf_search.getText();
					if (title.equals("")) {
						JOptionPane.showMessageDialog(null, "검색할 영화 제목을 작성해주세요.", "Message",
								JOptionPane.WARNING_MESSAGE);
					} else {
						try {
							//1. 테이블을 초기화한다.
							model.setNumRows(0);
							
							//2. 영화 제목을 검색한다.
							ResultSet rs = MainFrame.theater.searchMovie(title);
							
							//3. 영화 정보가 일치하는 정보를 출력한다.
							while (rs.next()) {
								title = rs.getString("title");
								int runTime = rs.getInt("runTime");
								String posterURL = rs.getString("posterURL");
								String dday = rs.getString("dday");

								arr[0] = title;
								arr[1] = Integer.toString(runTime);
								arr[2] = posterURL;
								arr[3] = dday;

								model.addRow(arr);
							}
							
							//4. 검색 텍스트필드를 지운다.
							tf_search.setText("");

						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}

			});			
			
			// 제목 라벨
			JLabel lblTitle = new JLabel("\uC81C\uBAA9");
			lblTitle.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
			lblTitle.setBounds(35, 460, 65, 20);
			add(lblTitle);

			// 상영시간 라벨
			JLabel lblRunningTime = new JLabel("\uC0C1\uC601\uC2DC\uAC04");
			lblRunningTime.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblRunningTime.setHorizontalAlignment(SwingConstants.LEFT);
			lblRunningTime.setBounds(25, 526, 65, 20);
			add(lblRunningTime);

			// 포스터 URL 라벨
			JLabel posterURL_label = new JLabel(
					"<html><body><center>\uD3EC\uC2A4\uD130<br> \uACBD\uB85C</center></br></body></html>");
			posterURL_label.setHorizontalAlignment(SwingConstants.LEFT);
			posterURL_label.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			posterURL_label.setBounds(264, 460, 65, 30);
			add(posterURL_label);

			// 개봉날짜 라벨
			JLabel dday_label = new JLabel("\uAC1C\uBD09 \uB0A0\uC9DC");
			dday_label.setHorizontalAlignment(SwingConstants.LEFT);
			dday_label.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			dday_label.setBounds(260, 524, 65, 20);
			add(dday_label);

			// 제목 텍스트필드
			JTextField tfTitle = new JTextField();
			tfTitle.setBounds(78, 461, 154, 23);
			add(tfTitle);
			tfTitle.setColumns(10);

			// 상영시간 텍스트필드
			JTextField screentime_tf = new JTextField();
			screentime_tf.setColumns(10);
			screentime_tf.setBounds(78, 525, 154, 23);
			add(screentime_tf);

			// 포스터URL 텍스트필드
			JTextField posterURL_tf = new JTextField();
			posterURL_tf.setColumns(10);
			posterURL_tf.setBounds(317, 462, 154, 23);
			add(posterURL_tf);

			// 개봉날짜 텍스트필드
			JTextField dday_tf = new JTextField();
			dday_tf.setColumns(10);
			dday_tf.setBounds(317, 521, 154, 23);
			add(dday_tf);

			// 삭제버튼
			JButton btnDelete = new JButton("\uC0AD\uC81C");
			btnDelete.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btnDelete.setBackground(Color.LIGHT_GRAY);
			btnDelete.setBounds(210, 595, 85, 23);
			add(btnDelete);
			btnDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//마우스로 선택한 줄 번호에서 title 가지고오기
					int rowIndex = table.getSelectedRow();
					String title = (String) table.getValueAt(rowIndex, 0);

					if (table.getSelectedRow() == -1) {
						JOptionPane.showMessageDialog(null, "삭제할 정보가 선택되지 않았습니다.", "Message", JOptionPane.WARNING_MESSAGE);
						return;
					} else {
						int result = JOptionPane.showConfirmDialog(null, "정보를 삭제하시겠습니까?", "Confirm",
								JOptionPane.YES_NO_OPTION);

						if (result == JOptionPane.CLOSED_OPTION)
							return;

						else if (result == JOptionPane.YES_OPTION) {
							model.removeRow(rowIndex); // 해당 row 삭제
							try {
								MainFrame.theater.deleteMovie(title);
							} catch (Exception e1) {
								e1.printStackTrace();
							} // 데이터 지우기
						}
					}
				}
			});
			
			// 추가버튼
			JButton btnAdd = new JButton("\uB4F1\uB85D");
			btnAdd.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btnAdd.setBackground(Color.LIGHT_GRAY);
			btnAdd.setBounds(70, 595, 85, 23);
			add(btnAdd);
			btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String title = tfTitle.getText();
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					int runTime = Integer.parseInt(screentime_tf.getText());
					String posterURL = posterURL_tf.getText();
					String dday = dday_tf.getText();

					if (tfTitle.getText().equals("") || screentime_tf.getText().equals("") || posterURL_tf.getText().equals("") || dday_tf.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "필수 정보를 입력해주세요.", "Message",
								JOptionPane.WARNING_MESSAGE);
						return;
					} else {

						Movie m = new Movie(title, runTime, posterURL, dday);
						arr[0] = title;
						arr[1] = Integer.toString(runTime);
						arr[2] = posterURL;
						arr[3] = dday;

						model.addRow(arr);
						
						
						try {
							MainFrame.theater.addMovie(m);
							
							// 입력후 텍스트 필드 값 제거
							tfTitle.setText("");
							screentime_tf.setText("");
							posterURL_tf.setText("");
							dday_tf.setText("");
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}

				}
			});


			// 새로고침 버튼
			JButton btn_reload = new JButton("\uC0C8\uB85C\uACE0\uCE68");
			btn_reload.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btn_reload.setBackground(Color.LIGHT_GRAY);
			btn_reload.setBounds(350, 595, 85, 23);
			add(btn_reload);
			btn_reload.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						ResultSet rs = MainFrame.theater.movieAll();

						DefaultTableModel model = (DefaultTableModel) table.getModel();
						model.setNumRows(0); // 초기화

						while (rs.next()) {
							String title = rs.getString("title");
							int runTime = rs.getInt("runTime");
							String posterURL = rs.getString("posterURL");
							String dday = rs.getString("dday");

							arr[0] = title;
							arr[1] = Integer.toString(runTime);
							arr[2] = posterURL;
							arr[3] = dday;

							model.addRow(arr);
						}

					} catch (SQLException e1) {
						return;
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
		}

		public void paint(Graphics g) {
			super.paint(g);
			g.drawLine(0, 140, 560, 140);
		}
	}

	class MovieTablePanel extends JPanel {

		String userName = Theater.personNow.getName();
		String TicketPath = "TICKET.png";
		
		String arr[] = new String[5];

		public MovieTablePanel() {
			setBounds(0, 0, 521, 677);
			setBackground(Color.WHITE);
			setLayout(null);

			// 티켓이미지(메인화면)라벨
			JLabel Ticket_main = new JLabel("티켓_메인화면");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 티켓 이미지 클릭 리스터
					// 1. ManagerMain panel로 변경한다.
					PanelChange("ManagerMainPanel");
				}
			});

			// 로그인한 사람 라벨
			JLabel lblUserName = new JLabel(userName + "\uB2D8  /");
			lblUserName.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblUserName.setBounds(375, 83, 65, 15);
			add(lblUserName);

			// 로그아웃 라벨
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblLogout.setBounds(440, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 로그아웃 라벨 클릭 리스터
					// 1. 로그아웃 알림 메시지 발생
					//2. 로그아웃을 한다.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)을 생성한다.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame을 제거한다.
					dispose();
				}
			});

			// 이전화면 라벨
			JLabel lbl_LastPanel = new JLabel("\u25C0 \uC774\uC804 \uD654\uBA74");
			lbl_LastPanel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lbl_LastPanel.setBounds(25, 120, 74, 15);
			add(lbl_LastPanel);
			lbl_LastPanel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 이전화면 라벨 클릭 리스터
					// 1. ManagerMain panel로 변경한다.
					PanelChange("ManagerMainPanel");
				}
			});

			// 상영 시간표 관리 라벨
			JLabel lblMovieList = new JLabel("\uC0C1\uC601 \uC2DC\uAC04\uD45C \uAD00\uB9AC");
			lblMovieList.setFont(new Font("맑은 고딕", Font.BOLD, 20));
			lblMovieList.setHorizontalAlignment(SwingConstants.LEFT);
			lblMovieList.setBounds(29, 168, 250, 30);
			add(lblMovieList);

			// 영화 라벨
			JLabel lblTitle = new JLabel("\uC601\uD654");
			lblTitle.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
			lblTitle.setBounds(28, 450, 65, 20);
			add(lblTitle);

			// 상영시간 라벨
			JLabel lblRunningTime = new JLabel("\uC2DC\uC791\uC2DC\uAC04");
			lblRunningTime.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lblRunningTime.setHorizontalAlignment(SwingConstants.LEFT);
			lblRunningTime.setBounds(265, 462, 65, 20);
			add(lblRunningTime);

			// 상영날짜 라벨
			JLabel date_label = new JLabel("\uC0C1\uC601\uB0A0\uC9DC");
			date_label.setHorizontalAlignment(SwingConstants.LEFT);
			date_label.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			date_label.setBounds(21, 494, 65, 20);
			add(date_label);

			// scrollPane
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(21, 225, 465, 203);
			add(scrollPane);

			// table
			JTable table = new JTable();
			table.setBackground(Color.WHITE);
			table.setFillsViewportHeight(true);
			table.setForeground(Color.DARK_GRAY);
			table.setModel(
					new DefaultTableModel(new Object[][] {}, new String[] { "\uC601\uD654", "\uC0C1\uC601\uB0A0\uC9DC",
							"\uD68C\uCC28\uC815\uBCF4", "\uC2DC\uC791\uC2DC\uAC04", "\uB05D\uC2DC\uAC04" }));
			table.getColumnModel().getColumn(0).setPreferredWidth(70);
			table.getColumnModel().getColumn(1).setPreferredWidth(70);
			table.getColumnModel().getColumn(2).setPreferredWidth(70);
			table.getColumnModel().getColumn(3).setPreferredWidth(70);
			table.getColumnModel().getColumn(4).setPreferredWidth(70);
			scrollPane.setViewportView(table);
			
			
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			
			//여기서부터 한번 추가
			try {
				ResultSet rs = MainFrame.screen.movieNowAll();
				model.setNumRows(0); // 초기화

				while (rs.next()) {
					String title = rs.getString("title");
					String date = rs.getString("date");
					int round = rs.getInt("round");
					String start = rs.getString("start");
					String finish = rs.getString("finish");

					arr[0] = title;
					arr[1] = date;
					arr[2] = Integer.toString(round);
					arr[3] = start;
					arr[4] = finish;

					model.addRow(arr);
				}

			} catch (SQLException e1) {
				return;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			

			// 영화 comboBox
			ArrayList<String> arrlist = new ArrayList<String>();
			ResultSet rs;
			try {
				rs = MainFrame.theater.movieAll();

				while (rs.next()) {
					String title = rs.getString("title");
					arrlist.add(title);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.out.println(arr);

			JComboBox<?> comboBox_movie = new JComboBox(arrlist.toArray(new String[arrlist.size()]));
			// comboBox_movie.setModel(new DefaultComboBoxModel(new String[] {}));
			comboBox_movie.setBounds(78, 450, 154, 23);
			add(comboBox_movie);

			// 시작시간(시) 라벨
			JLabel lbl_hour = new JLabel("\uC2DC");
			lbl_hour.setHorizontalAlignment(SwingConstants.LEFT);
			lbl_hour.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lbl_hour.setBounds(388, 463, 20, 20);
			add(lbl_hour);

			// 시작시간(분) 라벨
			JLabel lbl_minute = new JLabel("\uBD84");
			lbl_minute.setHorizontalAlignment(SwingConstants.LEFT);
			lbl_minute.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			lbl_minute.setBounds(473, 463, 20, 20);
			add(lbl_minute);

			// 시간콤보박스
			JComboBox comboBox_hour = new JComboBox();
			comboBox_hour.setModel(new DefaultComboBoxModel(new String[] { "08", "09", "10", "11", "12", "13", "14", "15",
					"16", "17", "18", "19", "20", "21", "22" }));
			comboBox_hour.setBounds(326, 463, 57, 23);
			add(comboBox_hour);

			// 날짜 텍스트필드
			JTextField date_textField = new JTextField();
			date_textField.setBounds(78, 493, 154, 23);
			add(date_textField);
			date_textField.setColumns(10);

			// 회차정보 라벨
			JLabel round_label = new JLabel("\uD68C\uCC28\uC815\uBCF4");
			round_label.setHorizontalAlignment(SwingConstants.LEFT);
			round_label.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			round_label.setBounds(25, 540, 65, 20);
			add(round_label);

			// 회차정보 콤보박스
			JComboBox round_comboBox = new JComboBox();
			round_comboBox.setModel(new DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
			round_comboBox.setBounds(78, 536, 154, 23);
			add(round_comboBox);

			// 시작하는 분 콤보박스
			JComboBox StartM_comboBox = new JComboBox();
			StartM_comboBox.setModel(new DefaultComboBoxModel(new String[] { "00", "15", "30", "45" }));
			StartM_comboBox.setBounds(410, 463, 57, 23);
			add(StartM_comboBox);

			// 끝나는 시간 라벨
			JLabel finsith_label = new JLabel("\uB05D\uC2DC\uAC04");
			finsith_label.setHorizontalAlignment(SwingConstants.LEFT);
			finsith_label.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			finsith_label.setBounds(272, 511, 65, 20);
			add(finsith_label);

			// 끝나는 시간 콤보박스
			JComboBox finishHour_comboBox = new JComboBox();
			finishHour_comboBox.setModel(new DefaultComboBoxModel(new String[] { "08", "09", "10", "11", "12", "13", "14",
					"15", "16", "17", "18", "19", "20", "21", "22" }));
			finishHour_comboBox.setBounds(326, 512, 57, 23);
			add(finishHour_comboBox);

			// 끝나는 시간 라벨
			JLabel finishHour_label = new JLabel("\uC2DC");
			finishHour_label.setHorizontalAlignment(SwingConstants.LEFT);
			finishHour_label.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			finishHour_label.setBounds(388, 512, 20, 20);
			add(finishHour_label);

			// 끝나는 분 콤보박스
			JComboBox finishM_comboBox = new JComboBox();
			finishM_comboBox.setModel(new DefaultComboBoxModel(new String[] { "00", "15", "30", "45" }));
			finishM_comboBox.setBounds(410, 512, 57, 23);
			add(finishM_comboBox);

			// 끝나는 분 라벨
			JLabel finishM_label = new JLabel("\uBD84");
			finishM_label.setHorizontalAlignment(SwingConstants.LEFT);
			finishM_label.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			finishM_label.setBounds(473, 512, 20, 20);
			add(finishM_label);

			// 추가버튼
			JButton btn_add = new JButton("\uCD94\uAC00");
			btn_add.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btn_add.setBackground(Color.LIGHT_GRAY);
			btn_add.setBounds(70, 595, 85, 23);
			add(btn_add);
			btn_add.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String title = comboBox_movie.getSelectedItem().toString();
					String date = date_textField.getText();
					int round = ((int) round_comboBox.getSelectedIndex() + 1);
					String start = (String) (comboBox_hour.getSelectedItem())
							+ (String) (StartM_comboBox.getSelectedItem());
					String finish = (String) (finishHour_comboBox.getSelectedItem())
							+ (String) (finishM_comboBox.getSelectedItem());

					if (date_textField.equals("") || start == finish) {
						JOptionPane.showMessageDialog(null, "오류발생.", "Message", JOptionPane.WARNING_MESSAGE);
						return;
					} else {
						MovieNow movieNow = new MovieNow(title, date, round, start, finish);
						arr[0] = title;
						arr[1] = date;
						arr[2] = Integer.toString(round);
						arr[3] = start;
						arr[4] = finish;

						model.addRow(arr);

						try {
							MainFrame.screen.addPlayingMovie(movieNow);
							date_textField.setText("");
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}

			});

			// 삭제 버튼
			JButton btn_delete = new JButton("\uC0AD\uC81C");
			btn_delete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// 마우스로 선택한 줄 번호에서 title 가지고오기
					int rowIndex = table.getSelectedRow();

					String title = (String) table.getValueAt(rowIndex, 0);
					String date = (String) table.getValueAt(rowIndex, 1);
					int round = Integer.parseInt(String.valueOf(table.getValueAt(rowIndex, 2)));
					// int round = (int) table.getValueAt(rowIndex, 2);
					String start = (String) table.getValueAt(rowIndex, 3);
					String finish = (String) table.getValueAt(rowIndex, 4);

					MovieNow movieNow = new MovieNow(title, date, round, null, null);

					if (table.getSelectedRow() == -1) {
						JOptionPane.showMessageDialog(null, "삭제할 정보가 선택되지 않았습니다.", "Message", JOptionPane.WARNING_MESSAGE);
						return;
					} else {
						int result = JOptionPane.showConfirmDialog(null, "정보를 삭제하시겠습니까?", "Confirm",
								JOptionPane.YES_NO_OPTION);

						if (result == JOptionPane.CLOSED_OPTION)
							return;

						else if (result == JOptionPane.YES_OPTION) {
							model.removeRow(rowIndex); // 해당 row 삭제
							try {
								MainFrame.screen.deletePlayingMovie(movieNow);
							} catch (Exception e1) {
								e1.printStackTrace();
							} // 데이터 지우기
						}
					}
				}
			});
			btn_delete.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btn_delete.setBackground(Color.LIGHT_GRAY);
			btn_delete.setBounds(210, 595, 85, 23);
			add(btn_delete);

			// 새로고침 버튼
			JButton btn_reload = new JButton("\uC0C8\uB85C\uACE0\uCE68");
			btn_reload.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
			btn_reload.setBackground(Color.LIGHT_GRAY);
			btn_reload.setBounds(350, 595, 85, 23);
			add(btn_reload);
			
			btn_reload.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						ResultSet rs = MainFrame.screen.movieNowAll();

						//DefaultTableModel model = (DefaultTableModel) table.getModel();
						model.setNumRows(0); // 초기화

						while (rs.next()) {
							String title = rs.getString("title");
							String date = rs.getString("date");
							int round = rs.getInt("round");
							String start = rs.getString("start");
							String finish = rs.getString("finish");

							arr[0] = title;
							arr[1] = date;
							arr[2] = Integer.toString(round);
							arr[3] = start;
							arr[4] = finish;

							model.addRow(arr);
						}

					} catch (SQLException e1) {
						return;
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
		}

		public void paint(Graphics g) {
			super.paint(g);
			g.drawLine(0, 140, 560, 140);

		}
	}
}