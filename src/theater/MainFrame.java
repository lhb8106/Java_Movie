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


//���� ������: �α��� â ũ���� ������: �α��� ũ���� �������� �α��� panel�� ����ϱ� ������ ������ �ȿ� panel�� �����Ͽ���
public class MainFrame extends JFrame {

	static Theater theater;			//��ȭ�� ��ü
	static Screen screen;			//�󿵰� ��ü
	
	private JPanel LoginPanel; // �α��� panel

	String TicketPath = "TICKET.png";

	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
		
		//DB ����
		/*
		//���� db
		String url = "jdbc:mariadb://localhost:3306/theater";			//������DB ����̹��� �޸𸮿� �ε���
		String id = "root";												//������DB id
		String pw = "wlswn0408";										//������DB password
		*/
		
		String url = "jdbc:mariadb://192.168.0.9:3306/theater";
		//String url = "jdbc:mariadb://172.30.1.25:3306/theater";
		
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");						//����̹� �ε�
			//Connection con = DriverManager.getConnection(url, id, pw);		//�޸𸮿� �ε��� ����̹��� �ڹٸ� ����
			Connection con = DriverManager.getConnection(url, "hello", "1013");
			//Connection con = DriverManager.getConnection(url, "root", "1013");
			
			theater = new Theater(con);
			screen = new Screen(con);
		} catch (Exception e) {
			e.printStackTrace();
		}										//theater ����
	}

	// ������
	public MainFrame() {

		// ���� ������ ����(�α��� ������)
		setTitle("Theater Program"); // ������ title ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 370); // ������ ũ�� ����
		LoginPanel = new JPanel();
		LoginPanel.setBackground(Color.WHITE); // ������ ��� ����
		LoginPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(LoginPanel);
		LoginPanel.setLayout(null);

		// Ƽ���̹���(����ȭ��)��
		JLabel Ticket_main = new JLabel("Ƽ��_����ȭ��");
		Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
		Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
		Ticket_main.setBounds(190, 10, 126, 80);
		LoginPanel.add(Ticket_main);

//		ImageIcon ticektIcon = new ImageIcon(MainFrame.class.getResource(TicketPath));				//ImageIcon ��ü ����
//		Image ticketImg = ticektIcon.getImage();																//ImageIcon���� Image ����
//		Image changedticektImg = ticketImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);					//����� Image�� ũ�⸦ �����Ͽ� ���ο� Image ��ü ����
//		ImageIcon changedticektIcon = new ImageIcon(changedticektImg);
//		Ticket_main.setIcon(changedticektIcon);

		// ���̵� ��
		JLabel lbl_id = new JLabel("\uC544\uC774\uB514");
		lbl_id.setFont(new Font("���� ���", Font.BOLD, 14));
		lbl_id.setBounds(69, 159, 50, 24);
		LoginPanel.add(lbl_id);

		// ��й�ȣ ��
		JLabel lbl_pw = new JLabel("\uBE44\uBC00\uBC88\uD638");
		lbl_pw.setFont(new Font("���� ���", Font.BOLD, 14));
		lbl_pw.setBounds(54, 212, 65, 24);
		LoginPanel.add(lbl_pw);

		// ���̵� �Է� �ؽ�Ʈ�ʵ�
		JTextField tf_id = new JTextField();
		tf_id.setBounds(131, 155, 185, 34);
		LoginPanel.add(tf_id);
		tf_id.setColumns(10);

		// ��й�ȣ �Է� �ؽ�Ʈ�ʵ�
		JTextField tf_pw = new JTextField();
		tf_pw.setColumns(10);
		tf_pw.setBounds(131, 208, 185, 34);
		LoginPanel.add(tf_pw);

		// �α��� ��ư
		JButton btn_Login = new JButton("\uB85C\uADF8\uC778");
		btn_Login.setBackground(Color.LIGHT_GRAY);
		btn_Login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//�α��� ��ư �̺�Ʈ
				//1. �Է��� id�� pw�� �����´�.
				String id = tf_id.getText();
				String pw = tf_pw.getText();
				
				//2. ���� �Է��� id�� ��й�ȣ�� �������� �����̶��
				//������ ���̵�: yk0309, ������ ��й�ȣ: 930309
				if (id.equals("yk0309")) {
					try {
						boolean tryLogin = MainFrame.theater.logIn(id, pw);
						
						if (tryLogin)
						{
							//3. ������ frame�� �����ϰ�
							ManagerFrame manager = new ManagerFrame();
							manager.setVisible(true);

							// 4. �α��� frame�� �ݴ´�.
							dispose();
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else if (id.equals("")) {
					//5. ���� �Է��� ���̵� �����̶��
					//6. �˸��޽��� �߻�
					JOptionPane.showMessageDialog(null, "���̵� �Է����ּ���.", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
				} else if (pw.equals("")) {
					// 5. ���� �Է��� ��й�ȣ�� �����̶��
					// 6. �˸��޽��� �߻�
					JOptionPane.showMessageDialog(null, "��й�ȣ�� �Է����ּ���.", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
				} else {
					
					// 5. ���� ������ ������ �ƴ϶��
					// 6. theater�� �α��� �Լ��� �����Ų��.
					try {
						boolean tryLogin = MainFrame.theater.logIn(id, pw);
						
						if (tryLogin)
						{
							// 7. ���� �α����� �����Ͽ��ٸ�(���̵� �����ϸ�, ��й�ȣ�� ��ġ�ϴٸ�)
							// 8. Ŭ���̾�Ʈ frame�� �����ϰ�
							ClientFrame client = new ClientFrame();
							client.setVisible(true);
							// 9. �α��� frame�� �ݴ´�.
							dispose();	
						}
						else {
							//10. �α��ο� �����Ͽ��ٸ�
							//11. �˸��޽��� �߻�
							JOptionPane.showMessageDialog(null, "���̵� ��й�ȣ�� Ȯ�����ּ���.", "�α��� ����", JOptionPane.ERROR_MESSAGE);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		btn_Login.setFont(new Font("���� ���", Font.PLAIN, 12));
		btn_Login.setBounds(346, 148, 79, 41);
		LoginPanel.add(btn_Login);

		// ȸ�����Թ�ư
		JButton btn_SignUp = new JButton("\uD68C\uC6D0\uAC00\uC785");
		btn_SignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ȸ������ ��ư �̺�Ʈ
				// 1. ȸ������ �������� �����Ѵ�.
				SignUpFrame SignUp = new SignUpFrame();
				SignUp.setVisible(true);
			}
		});
		btn_SignUp.setFont(new Font("���� ���", Font.PLAIN, 11));
		btn_SignUp.setBackground(Color.LIGHT_GRAY);
		btn_SignUp.setBounds(346, 202, 79, 41);
		LoginPanel.add(btn_SignUp);

	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.drawRect(40, 135, 430, 185);
		
	}

}

//ȸ������ ������: ȸ������ ũ���� �������� ȸ������ panel�� ����ϱ� ������ ������ �ȿ� panel�� �����Ͽ���
class SignUpFrame extends JFrame {

	private JPanel SignUpPanel; // ȸ������ panel

	// ������
	public SignUpFrame() {

		// ȸ������ ������ ����
		setTitle("SignUp"); // ������ title ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 519);
		SignUpPanel = new JPanel();
		SignUpPanel.setBackground(Color.WHITE);
		SignUpPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(SignUpPanel);
		SignUpPanel.setLayout(null);

		// �̸� �ؽ�Ʈ�ʵ�
		JTextField Name_field = new JTextField();
		Name_field.setBounds(103, 65, 158, 33);
		SignUpPanel.add(Name_field);
		Name_field.setColumns(10);

		// ���̵� �ؽ�Ʈ�ʵ�
		JTextField Id_field = new JTextField();
		Id_field.setColumns(10);
		Id_field.setBounds(103, 178, 158, 33);
		SignUpPanel.add(Id_field);

		// ��й�ȣ �ؽ�Ʈ�ʵ�
		JTextField pw_field = new JTextField();
		pw_field.setColumns(10);
		pw_field.setBounds(103, 238, 158, 33);
		SignUpPanel.add(pw_field);

		// ��й�ȣ Ȯ�� �ؽ�Ʈ�ʵ�
		JTextField pwCheck_field = new JTextField();
		pwCheck_field.setColumns(10);
		pwCheck_field.setBounds(103, 298, 158, 33);
		SignUpPanel.add(pwCheck_field);

		// ���� �ؽ�Ʈ �ʵ�
		JTextField birth_field = new JTextField();
		birth_field.setColumns(10);
		birth_field.setBounds(103, 358, 158, 33);
		SignUpPanel.add(birth_field);

		// ��ȭ��ȣ �ؽ�Ʈ�ʵ�
		JTextField Phone_field = new JTextField();
		Phone_field.setColumns(10);
		Phone_field.setBounds(103, 122, 158, 33);
		SignUpPanel.add(Phone_field);

		// ���̵� �ߺ�Ȯ�� ��ư
		JButton CheckSame_Button = new JButton("\uC911\uBCF5\uD655\uC778");
		CheckSame_Button.setFont(new Font("���� ���", Font.PLAIN, 12));
		CheckSame_Button.setBackground(Color.LIGHT_GRAY);
		CheckSame_Button.setBounds(283, 238, 99, 33);
		SignUpPanel.add(CheckSame_Button);
		CheckSame_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ���̵� �ߺ� �˻� ��ư �̺�Ʈ
				// 1. �Է��� id�� �����´�.
				String id = Id_field.getText();

				// 2. ���� �Է��� ���̵� �����̶��
				if (id.equals("")) {
					// 3. �˸� �޽��� �߻�
					JOptionPane.showMessageDialog(null, "���̵� �Է����ּ���.", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
				} else {
					// 4. ������ �ƴ϶��
					try {
						// 5. �Է��� id�� �����ϴ��� Ȯ���Ѵ�.
						boolean isExist = MainFrame.theater.isExistId(id);

						// 6. ���� id�� �����Ѵٸ�
						if (isExist) {
							// 7. ���̵� �ߺ� �˸� �޽��� �߻�
							JOptionPane.showMessageDialog(null, "���̵� �����մϴ�.", "NOTICE",
									JOptionPane.INFORMATION_MESSAGE);

							// 8. �Է��� id �ؽ�Ʈ �ʵ� �����
							Id_field.setText("");
						} else {
							// 9. ���� id�� �������� �ʴ´ٸ�
							// 10. ���̵� ��� ���� �˸� �޽��� �߻�
							JOptionPane.showMessageDialog(null, "��밡���� ���̵��Դϴ�.", "NOTICE",
									JOptionPane.INFORMATION_MESSAGE);
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		// ȸ������ ��ư
		JButton SignUp_button = new JButton("\uAC00\uC785");
		SignUp_button.setBackground(Color.LIGHT_GRAY);
		SignUp_button.setFont(new Font("���� ���", Font.PLAIN, 12));
		SignUp_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ȸ������ ��ư �̺�Ʈ
				// 1. �Է��� ��й�ȣ�������´�.
				String pw = pw_field.getText();
				String pw_check = pw_field.getText();
				// 2. ���� �Է��� ��й�ȣ�� ��й�ȣ Ȯ���� �����̶��
				if (pw.equals("") || pwCheck_field.getText().equals("")) {
					// 3. �˸� �޽��� �߻�
					JOptionPane.showMessageDialog(null, "��й�ȣ�� �Է����ּ���.", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
				} else {
					// 4. ������ �ƴ϶��
					// 5. ��й�ȣ�� ��й�ȣ Ȯ�� �����͸� ���Ѵ�.
					if (pw.equals(pw_check)) {
						// 6. ���� ��й�ȣ�� ��й�ȣ Ȯ�� �����Ͱ� ��ġ�Ѵٸ�
						// 7. �Է��� �ٸ� ȸ�� ������ �����´�. (�̸�, ��ȭ��ȣ, �������, ���̵�)
						String name = Name_field.getText();
						String phone = Phone_field.getText();
						String id = Id_field.getText();
						String birth = birth_field.getText();

						// 5. �Է��� �����ͷ� Person ��ü�� �����.
						Person person = new Person(name, phone, birth, id, pw);

						try {
							// 6. ȸ������ �Լ��� �����Ų��.
							MainFrame.theater.signUp(person);
						} catch (Exception e1) {
							e1.printStackTrace();
						}

						// 7. ȸ������ �Ϸ� �˸� �޽��� �߻�
						JOptionPane.showMessageDialog(null, "ȸ������ �Ǿ����ϴ�.", "NOTICE", JOptionPane.INFORMATION_MESSAGE);

						// 8. �Է��� �ؽ�Ʈ�ʵ带 ��� �����.
						Name_field.setText("");
						Phone_field.setText("");
						pw_field.setText("");
						pwCheck_field.setText("");
						birth_field.setText("");
						Id_field.setText("");

						// 9. ȸ������ frame�� �����Ѵ�.
						dispose();
					} else {
						// 9. ��й�ȣ�� ��й�ȣ Ȯ�� �����Ͱ� ��ġ���� �ʴ´ٸ�
						// 10. ��й�ȣ ���� �˸� �޽��� �߻�
						JOptionPane.showMessageDialog(null, "��й�ȣ Ȯ���� ��ġ���� �ʽ��ϴ�.", "NOTICE", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		SignUp_button.setBounds(212, 423, 99, 33);
		SignUpPanel.add(SignUp_button);

		// ��� ��ư
		JButton Cancel_button = new JButton("\uCDE8\uC18C");
		Cancel_button.setFont(new Font("���� ���", Font.PLAIN, 12));
		Cancel_button.setBackground(Color.LIGHT_GRAY);
		Cancel_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//ȸ������ ��� ��ư �̺�Ʈ
				//1. �ؽ�Ʈ�ʵ带 ��� �����.
				Name_field.setText("");
				Phone_field.setText("");
				pw_field.setText("");
				pwCheck_field.setText("");
				birth_field.setText("");
				Id_field.setText("");
				
				//2. ȸ������ frame�� �����Ѵ�.
				dispose();
			}
		});
		Cancel_button.setBounds(325, 423, 99, 33);
		SignUpPanel.add(Cancel_button);

		// ȸ������ Ÿ��Ʋ ����
		JLabel lbl_title = new JLabel("\uD68C\uC6D0\uAC00\uC785");
		lbl_title.setFont(new Font("���� ���", Font.BOLD, 19));
		lbl_title.setBounds(24, 10, 102, 33);
		SignUpPanel.add(lbl_title);

		// �̸� ��
		JLabel lbl_name = new JLabel("\uC774\uB984");
		lbl_name.setFont(new Font("���� ���", Font.PLAIN, 14));
		lbl_name.setBounds(48, 70, 48, 21);
		SignUpPanel.add(lbl_name);

		// ���̵� ��
		JLabel lbl_id = new JLabel("\uC544\uC774\uB514");
		lbl_id.setFont(new Font("���� ���", Font.PLAIN, 14));
		lbl_id.setBounds(36, 242, 55, 21);
		SignUpPanel.add(lbl_id);

		// ��й�ȣ ��
		JLabel lbl_pw = new JLabel("\uBE44\uBC00\uBC88\uD638");
		lbl_pw.setFont(new Font("���� ���", Font.PLAIN, 14));
		lbl_pw.setBounds(26, 302, 65, 21);
		SignUpPanel.add(lbl_pw);

		// ��й�ȣ Ȯ�� ��
		JLabel lbl_checkPw = new JLabel(
				"<html><body><center>\uBE44\uBC00\uBC88\uD638<br>\uD655\uC778</center></body></html>");
		lbl_checkPw.setFont(new Font("���� ���", Font.PLAIN, 14));
		lbl_checkPw.setBounds(25, 359, 71, 33);
		SignUpPanel.add(lbl_checkPw);

		// ���� ��
		JLabel lbl_birth = new JLabel("\uC0DD\uB144\uC6D4\uC77C");
		lbl_birth.setFont(new Font("���� ���", Font.PLAIN, 14));
		lbl_birth.setBounds(26, 180, 65, 21);
		SignUpPanel.add(lbl_birth);

		// ���� 6�ڸ��� �Է��ض�� ��
		JLabel sixbirth = new JLabel("\u203B6\uC790\uB9AC\uB85C \uC785\uB825");
		sixbirth.setForeground(Color.RED);
		sixbirth.setFont(new Font("���� ���", Font.PLAIN, 12));
		sixbirth.setBounds(280, 183, 135, 15);
		SignUpPanel.add(sixbirth);

		// ��ȭ ��ȣ ��
		JLabel lbl_phoneNum = new JLabel("\uC804\uD654\uBC88\uD638");
		lbl_phoneNum.setFont(new Font("���� ���", Font.PLAIN, 14));
		lbl_phoneNum.setBounds(24, 126, 65, 21);
		SignUpPanel.add(lbl_phoneNum);

	}
}

//����� ������: Ŭ���̾�Ʈ ũ���� �������� panel�� ������ ����ϱ� ������ panel ��ü �Լ��� ����� ����Ͽ���
class ClientFrame extends JFrame {
	public ClientMainPanel clientMain = new ClientMainPanel(); // Ŭ���̾�Ʈ ���� panel
	public MyPagePanel myPage = null; // ���������� panel
	public DDayPlusMoviePanel ddayplusMovie = new DDayPlusMoviePanel(); // �󿵿����� panel
	public ReservePanel reserve = null; // ����ȭ�� panel
	public OtherMoviePanel otherMovie = new OtherMoviePanel(); // ��ȭ ������ panel
	public ClientModifyPanel clientModify = new ClientModifyPanel();; // ȸ�� ���� ���� panel
	public TicketingPanel ticketing; // Ƽ�� ���� panel
	public SearchMoviePanel searchMovie = null; // ��ȭ �˻� panel

	public ClientFrame() {
		// 1. ClientMainPanel ����
		clientMain = new ClientMainPanel();
		// 2. ClientMainPanel �߰�
		add(clientMain);

		// ����� ������ ����
		setTitle("Theater Program"); // ������ title ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 501, 677); // ������ ũ�� ����
		setBackground(Color.WHITE); // ������ ��� ����
		setLayout(null); // ������ ���̾ƿ� ����
		setVisible(true);
	}

	// panel �ٲٴ� �Լ�
	public void PanelChange(String panelName) {
		if (panelName.equals("ClientMainPanel")) {
			// ���� �ٲ� panel�� ClientMainPanel�̶��
			getContentPane().removeAll();
			getContentPane().add(clientMain);
			revalidate();
			repaint();
		} else if (panelName.equals("MyPagePanel")) {
			// ���� �ٲ� panel�� MyPagePanel�̶��
			getContentPane().removeAll();
			myPage = new MyPagePanel();
			getContentPane().add(myPage);
			revalidate();
			repaint();
		} else if (panelName.equals("DDayPlusMoviePanel")) {
			// ���� �ٲ� panel�� DDayPlusMoviePanel�̶��
			getContentPane().removeAll();
			getContentPane().add(ddayplusMovie);
			revalidate();
			repaint();
		} else if (panelName.equals("ReservePanel")) {
			// ���� �ٲ� panel�� ReservePanel�̶��
			getContentPane().removeAll();
			reserve = new ReservePanel();
			getContentPane().add(reserve);
			revalidate();
			repaint();
		} else if (panelName.equals("OtherMoviePanel")) {
			// ���� �ٲ� panel�� OtherMoviePanel�̶��
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

	// ��ȭ �������� �ٷ� �����ϴ� â���� �Ѿ�� �Լ�(ClientMain panel���� ���� ��ư�� ������)
	public void ReserveSelectdMovie(String movieTitle) {
		getContentPane().removeAll();
		reserve = new ReservePanel(movieTitle);
		getContentPane().add(reserve);
		revalidate();
		repaint();
	}

	//��ȭ �˻� ����� �����ִ� â���� �Ѿ�� �Լ�
	public void ShowSearchMovie(ResultSet rs) {
		getContentPane().removeAll();
		searchMovie = new SearchMoviePanel(rs);
		getContentPane().add(searchMovie);
		revalidate();
		repaint();
	}

	//�¼� ���� â���� �Ѿ�� �Լ�
	public void ChangeTicketingPanel(MovieNow movieNow)
	{
		getContentPane().removeAll();
		ticketing = new TicketingPanel(movieNow);
		getContentPane().add(ticketing);
		revalidate();
		repaint();
	}

	class ClientMainPanel extends JPanel {

		//���� �������� ȸ�� �̸�
		String userName = Theater.personNow.getName();

		// ���� ������ ������ ���
		String FirstPosterPath;
		String SecondPosterPath;
		String ThirdPosterPath;
		String TicketPath;

		// ���� ������ ��ȭ ����
		String FirstTitle;
		String SecondTitle;
		String ThirdTitle;

		private LineBorder bb = new LineBorder(Color.black, 1, true);

		public ClientMainPanel() {
			setBounds(0, 0, 501, 677);
			setBackground(Color.WHITE);
			setLayout(null);
			
			//��ȭ 3�� �����ϱ�
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
			
			//������ ��ȭ 3�� ���
			FirstPosterPath = movies[0].getPosterURL();
			SecondPosterPath = movies[1].getPosterURL();
			ThirdPosterPath = movies[2].getPosterURL();
			FirstTitle = movies[0].getTitle();
			SecondTitle = movies[1].getTitle();
			ThirdTitle = movies[2].getTitle();

			
			// Ƽ���̹���(����ȭ��)��
			JLabel Ticket_main = new JLabel("Ƽ��_����ȭ��");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Ƽ�� �̹��� Ŭ�� ������
					// 1. ManagerMain panel�� �����Ѵ�.
					PanelChange("ManagerMainPanel");
				}
			});

			// ���� ���� ȸ�� �̸� ǥ�� ��
			JLabel lblUserName = new JLabel(userName + "��  /");
			//JLabel lblUserName = new JLabel(userName + "\uB2D8 /");
			lblUserName.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblUserName.setBounds(350, 83, 66, 15);
			add(lblUserName);

			// �α׾ƿ� ��
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblLogout.setBounds(420, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �α׾ƿ� �� Ŭ�� ������
					// 1. �α׾ƿ� �˸� �޽��� �߻�
					//2. �α׾ƿ��� �Ѵ�.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)�� �����Ѵ�.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame�� �����Ѵ�.
					dispose();
				}
			});

			// ���� ��ܹ� ��
			JLabel lbl_ticketing = new JLabel("\uC608\uB9E4");
			lbl_ticketing.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_ticketing.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_ticketing.setBorder(bb);
			lbl_ticketing.setBounds(0, 109, 162, 42);
			add(lbl_ticketing);
			lbl_ticketing.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ���� ī�װ� Ŭ�� ������
					// 1. Reserve panel�� �����Ѵ�.
					PanelChange("ReservePanel");
				}
			});

			// ���� ���� ��ȭ ��ܹ� ��
			JLabel lbl_scheduleMovie = new JLabel("\uAC1C\uBD09\uC608\uC815\uC601\uD654");
			lbl_scheduleMovie.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_scheduleMovie.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_scheduleMovie.setBorder(bb);
			lbl_scheduleMovie.setBounds(161, 109, 164, 42);
			add(lbl_scheduleMovie);
			lbl_scheduleMovie.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �󿵿����� Ŭ�� ������
					// 1. DDayPlusMovie panel�� �����Ѵ�.
					PanelChange("DDayPlusMoviePanel");
				}
			});

			// ���������� ��ܹ� ��
			JLabel lbl_myPage = new JLabel("\uB9C8\uC774\uD398\uC774\uC9C0");
			lbl_myPage.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_myPage.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_myPage.setBorder(bb);
			lbl_myPage.setBounds(324, 109, 162, 42);
			add(lbl_myPage);
			lbl_myPage.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ���������� Ŭ�� ������
					// 1. ���������� panel�� �����Ѵ�.
					PanelChange("MyPagePanel");
				}
			});

			// ������ ��ȭ ������ ��
			JLabel lblAddLanking = new JLabel("\u25B6\uB354\uBCF4\uAE30");
			lblAddLanking.setFont(new Font("���� ���", Font.PLAIN, 13));
			lblAddLanking.setBounds(414, 218, 62, 15);
			add(lblAddLanking);
			lblAddLanking.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ������ ��ȭ ������ Ŭ�� ������
					// 1. OtherMovie panel�� �����Ѵ�.
					PanelChange("OtherMoviePanel");
				}
			});

			// ������ ��ȭ ��
			JLabel lbl_title = new JLabel("\u2665 \uC624\uB298\uC758 \uC601\uD654 \u2665");
			lbl_title.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_title.setFont(new Font("���� ���", Font.BOLD, 18));
			lbl_title.setBounds(0, 175, 486, 34);
			add(lbl_title);

			// ��ȭ ������(�̹���X �̹����� �ؿ��� ����)
			JLabel lblFirstPoster = new JLabel("��ȭ ������1");
			lblFirstPoster.setIcon(new ImageIcon("C:\\Users\\82104\\OneDrive\\Desktop\\\uB77C\uB77C\uB79C\uB4DC.jpg"));
			lblFirstPoster.setHorizontalAlignment(SwingConstants.CENTER);
			lblFirstPoster.setBounds(32, 265, 118, 160);
			add(lblFirstPoster);

			JLabel lblSecondPoster = new JLabel("��ȭ������2");
			lblSecondPoster.setIcon(new ImageIcon("C:\\Users\\82104\\OneDrive\\Desktop\\\uC54C\uB77C\uB518.jpg"));
			lblSecondPoster.setHorizontalAlignment(SwingConstants.CENTER);
			lblSecondPoster.setBounds(190, 265, 118, 160);
			add(lblSecondPoster);

			JLabel lblThirdPoster = new JLabel("��ȭ������3");
			lblThirdPoster
					.setIcon(new ImageIcon("C:\\Users\\82104\\OneDrive\\Desktop\\\uC704\uB300\uD55C\uC1FC\uB9E8.jpg"));
			lblThirdPoster.setHorizontalAlignment(SwingConstants.CENTER);
			lblThirdPoster.setBounds(348, 265, 118, 160);
			add(lblThirdPoster);

			// ��ȭ ���� ��
			JLabel lblFirstTitle = new JLabel("����1");
			lblFirstTitle.setHorizontalAlignment(SwingConstants.CENTER);
			lblFirstTitle.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblFirstTitle.setBounds(42, 435, 108, 15);
			lblFirstTitle.setText(FirstTitle);
			add(lblFirstTitle);

			JLabel lblSecondTitle = new JLabel("����2");
			lblSecondTitle.setHorizontalAlignment(SwingConstants.CENTER);
			lblSecondTitle.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblSecondTitle.setBounds(200, 435, 108, 15);
			lblSecondTitle.setText(SecondTitle);
			add(lblSecondTitle);

			JLabel lblThirdTitle = new JLabel("����3");
			lblThirdTitle.setHorizontalAlignment(SwingConstants.CENTER);
			lblThirdTitle.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblThirdTitle.setBounds(358, 435, 108, 15);
			lblThirdTitle.setText(ThirdTitle);
			add(lblThirdTitle);

			// ���Ź�ư
			JButton btnFirstBooking = new JButton("����");
			btnFirstBooking.setFont(new Font("���� ���", Font.PLAIN, 12));
			btnFirstBooking.setForeground(new Color(255, 255, 255));
			btnFirstBooking.setBackground(new Color(255, 69, 0));
			btnFirstBooking.setBounds(67, 460, 60, 20);
			add(btnFirstBooking);
			btnFirstBooking.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ù��° ������ ��ȭ ���� ��ư �̺�Ʈ
					// 1. ù��° ������ ��ȭ�� ������ �����´�.
					String reserveTitle = lblFirstTitle.getText();
					//String title = movies[0].getTitle();
					// 2. ������ ������ �Ķ���ͷ� ���� ȭ�� panel�� �ٲ۴�.
					ReserveSelectdMovie(reserveTitle);
				}
			});

			JButton btnSecondBooking = new JButton("����");
			btnSecondBooking.setFont(new Font("���� ���", Font.PLAIN, 12));
			btnSecondBooking.setForeground(Color.WHITE);
			btnSecondBooking.setBackground(new Color(255, 69, 0));
			btnSecondBooking.setBounds(227, 460, 60, 20);
			add(btnSecondBooking);
			btnSecondBooking.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// �ι�° ������ ��ȭ ���� ��ư �̺�Ʈ
					// 1. �ι�° ������ ��ȭ�� ������ �����´�.
					String reserveTitle = lblSecondTitle.getText();
					//String title = movies[1].getTitle();
					// 2. ������ ������ �Ķ���ͷ� ���� ȭ�� panel�� �ٲ۴�.
					ReserveSelectdMovie(reserveTitle);
				}
			});

			JButton btnThirBooking = new JButton("����");
			btnThirBooking.setFont(new Font("���� ���", Font.PLAIN, 12));
			btnThirBooking.setForeground(Color.WHITE);
			btnThirBooking.setBackground(new Color(255, 69, 0));
			btnThirBooking.setBounds(385, 460, 60, 20);
			add(btnThirBooking);
			btnThirBooking.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ����° ������ ��ȭ ���� ��ư �̺�Ʈ
					// 1. ����° ������ ��ȭ�� ������ �����´�.
					String reserveTitle = lblThirdTitle.getText();
					//String title = movies[2].getTitle();
					// 2. ������ ������ �Ķ���ͷ� ���� ȭ�� panel�� �ٲ۴�.
					ReserveSelectdMovie(reserveTitle);
				}
			});

			// ��ȭ �˻� �ؽ�Ʈ�ʵ�
			JTextField tfSearch = new JTextField();
			tfSearch.setBounds(12, 589, 384, 30);
			add(tfSearch);
			tfSearch.setColumns(10);

			// ��ȭ �˻� ��ư
			JButton btnSearch = new JButton("�˻�");
			btnSearch.setBackground(Color.LIGHT_GRAY);
			btnSearch.setBounds(408, 588, 70, 30);
			add(btnSearch);
			btnSearch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ��ȭ �˻� ��ư �̺�Ʈ
					// 1. �ؽ�Ʈ�ʵ忡 �Է¹��� �����͸� �����´�.
					String keyword = tfSearch.getText();
					// 2. ���� �Է¹��� �ؽ�Ʈ�� �����̶��
					if (keyword.equals("")) {
						// 3. �˸� �޽����� �߻���Ų��.
						JOptionPane.showMessageDialog(null, "�˻��� ��ȭ ���� �Է����ּ���..", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
					} else {
						// 4. ���� �Է¹��� �ؽ�Ʈ�� ������ �ƴ϶��
						try {
							// 5. �Է¹��� Ű����� ��ȭ�� �˻��Ѵ�.
							ResultSet rs = MainFrame.theater.searchMovie(keyword);
							
							// 6. �ؽ�Ʈ�ʵ带 �����
							tfSearch.setText("");
							
							// 7. SearchMovie panel�� �����Ѵ�.	
							// �̶�, �Է¹��� Ű������ �˻� ����� �Ű������� �����Ѵ�.
							ShowSearchMovie(rs);
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});

			// ��ȭ ������ ǥ��
			// ��ȭ ������ ũ�� ����
			// ImageIcon�� ũ�⸦ ǰ���� ������ �ʰ� ��ȯ�ϱ� ���� Image�� ��ȯ�Ͽ� ũ�⸦ ������
			// Image �Լ��� getScaledInstance()�� ���� ǰ���� ������ ä ������ ����
			// ����� ���� �ٽ� ImageIcon���� ��ȯ
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
			Image changedFirstImg = FirstImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);					//����� Image�� ũ�⸦ �����Ͽ� ���ο� Image ��ü ����
			ImageIcon changedFirstIcon = new ImageIcon(changedFirstImg);
			lblFirstPoster.setIcon(changedFirstIcon);
			
			Image changedSecondImg = SecondImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);					//����� Image�� ũ�⸦ �����Ͽ� ���ο� Image ��ü ����
			ImageIcon changedSecondIcon = new ImageIcon(changedSecondImg);
			lblSecondPoster.setIcon(changedSecondIcon);
			
			Image changedThirdImg = ThirdImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);					//����� Image�� ũ�⸦ �����Ͽ� ���ο� Image ��ü ����
			ImageIcon changedThirdIcon = new ImageIcon(changedThirdImg);
			lblThirdPoster.setIcon(changedThirdIcon);

			// ��ȭ �˻� ��
			JLabel lbl_search = new JLabel("\uC601\uD654 \uAC80\uC0C9");
			lbl_search.setFont(new Font("���� ���", Font.BOLD, 18));
			lbl_search.setBounds(200, 535, 96, 34);
			add(lbl_search);
		}
	}

	class MyPagePanel extends JPanel {

		//���� �������� ����� ����
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

			// Ƽ���̹���(����ȭ��)��
			JLabel Ticket_main = new JLabel("Ƽ��_����ȭ��");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Ƽ�� �̹��� Ŭ�� ������
					// 1. ClientMain panel�� �����Ѵ�.
					PanelChange("ClientMainPanel");
				}
			});

//			// ImageIcon ��ü ���� => Image ���� �� ũ�� �����ؼ� ���ο� ��ü ����
//			ImageIcon ticektIcon = new ImageIcon(MainFrame.class.getResource(TicketPath));
//			Image ticketImg = ticektIcon.getImage();
//			Image changedticektImg = ticketImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);
//			ImageIcon changedticektIcon = new ImageIcon(changedticektImg);
//			Ticket_main.setIcon(changedticektIcon);
			
			// ���� ī�װ�
			JLabel lbl_ticketing = new JLabel("\uC608\uB9E4");
			lbl_ticketing.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_ticketing.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_ticketing.setBorder(bb);
			lbl_ticketing.setBounds(0, 109, 162, 42);
			add(lbl_ticketing);
			lbl_ticketing.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ���� ī�װ� Ŭ�� ������
					// 1. Reserve panel�� �����Ѵ�.
					PanelChange("ReservePanel");
				}
			});

			// ���ſ��� ī�װ�
			JLabel lbl_scheduleMovie = new JLabel("\uAC1C\uBD09\uC608\uC815\uC601\uD654");
			lbl_scheduleMovie.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_scheduleMovie.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_scheduleMovie.setBorder(bb);
			lbl_scheduleMovie.setBounds(161, 109, 164, 42);
			add(lbl_scheduleMovie);
			lbl_scheduleMovie.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �󿵿����� Ŭ�� ������
					// 1. DDayPlusMovie panel�� �����Ѵ�.
					PanelChange("DDayPlusMoviePanel");
				}
			});

			// ���������� ī�װ�
			JLabel lbl_myPage = new JLabel("\uB9C8\uC774\uD398\uC774\uC9C0");
			lbl_myPage.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_myPage.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_myPage.setBorder(bb);
			lbl_myPage.setBounds(324, 109, 162, 42);
			add(lbl_myPage);
			lbl_myPage.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ���������� Ŭ�� ������
					// 1. ���������� panel�� �����Ѵ�.
					PanelChange("MyPagePanel");
				}
			});

			// ���� ���� ȸ�� �̸� ǥ�� ��
			JLabel lblUserName = new JLabel(userName + "��  /");
			lblUserName.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblUserName.setBounds(350, 83, 66, 15);
			add(lblUserName);

			// �α׾ƿ� ��
			JLabel lblLogout = new JLabel("�α׾ƿ�");
			lblLogout.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblLogout.setBounds(420, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �α׾ƿ� �� Ŭ�� ������
					// 1. �α׾ƿ� �˸� �޽��� �߻�
					//2. �α׾ƿ��� �Ѵ�.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)�� �����Ѵ�.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame�� �����Ѵ�.
					dispose();
				}
			});

			// ȯ���մϴ� ��
			JLabel lbl_welcome = new JLabel("\uD658\uC601\uD569\uB2C8\uB2E4");
			lbl_welcome.setFont(new Font("���� ���", Font.BOLD, 18));
			lbl_welcome.setBounds(197, 175, 117, 34);
			add(lbl_welcome);

			// ��������Ȯ�� ��
			JLabel lbl_personCheck = new JLabel("\uAC1C\uC778\uC815\uBCF4\uD655\uC778");
			lbl_personCheck.setFont(new Font("���� ���", Font.BOLD, 14));
			lbl_personCheck.setBounds(43, 232, 86, 15);
			add(lbl_personCheck);

			// �̸� ��
			JLabel lbl_name = new JLabel("\uC774\uB984");
			lbl_name.setFont(new Font("���� ���", Font.PLAIN, 12));
			lbl_name.setBounds(43, 259, 50, 15);
			add(lbl_name);

			// ��ȭ��ȣ ��
			JLabel labelphoneNum = new JLabel("\uC804\uD654\uBC88\uD638");
			labelphoneNum.setFont(new Font("���� ���", Font.PLAIN, 12));
			labelphoneNum.setBounds(43, 282, 50, 15);
			add(labelphoneNum);

			// ���̵� ��
			JLabel lbl_id = new JLabel("\uC544\uC774\uB514");
			lbl_id.setFont(new Font("���� ���", Font.PLAIN, 12));
			lbl_id.setBounds(43, 305, 50, 15);
			add(lbl_id);

			// ���϶�
			JLabel lbl_birth = new JLabel("\uC0DD\uB144\uC6D4\uC77C");
			lbl_birth.setFont(new Font("���� ���", Font.PLAIN, 12));
			lbl_birth.setBounds(43, 328, 66, 15);
			add(lbl_birth);

			// ������ư
			JButton btn_modify = new JButton("\uC218\uC815\r\n");
			btn_modify.setBackground(Color.LIGHT_GRAY);
			btn_modify.setFont(new Font("���� ���", Font.PLAIN, 12));
			btn_modify.setBounds(348, 345, 91, 23);
			add(btn_modify);
			btn_modify.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ���� ��ư �̺�Ʈ
					// 1. ClientModify panel�� �����Ѵ�.
					PanelChange("ClientModifyPanel");
				}
			});

			// ����Ȯ�ζ�
			JLabel lbl_ticketCheck = new JLabel("\uC608\uB9E4\uD655\uC778");
			lbl_ticketCheck.setFont(new Font("���� ���", Font.BOLD, 14));
			lbl_ticketCheck.setBounds(43, 408, 86, 15);
			add(lbl_ticketCheck);

			// �̸� �� ��
			JLabel lbl_a_name = new JLabel(userName);
			lbl_a_name.setBounds(105, 259, 100, 15);
			add(lbl_a_name);

			// ��ȭ��ȣ �� ��
			JLabel lbl_a_phoneNum = new JLabel(userPhoneNum);
			lbl_a_phoneNum.setBounds(105, 284, 100, 15);
			add(lbl_a_phoneNum);

			// ���̵� �� ��
			JLabel lbl_a_id = new JLabel(userId);
			lbl_a_id.setBounds(105, 305, 100, 15);
			add(lbl_a_id);

			// ���� �� ��
			JLabel lbl_a_birth = new JLabel(userBirth);
			lbl_a_birth.setBounds(105, 330, 100, 15);
			add(lbl_a_birth);
			
			// ���� ��ư
			JButton btn_delete = new JButton("\uC0AD\uC81C");
			btn_delete.setFont(new Font("���� ���", Font.PLAIN, 12));
			btn_delete.setBackground(Color.LIGHT_GRAY);
			btn_delete.setBounds(368, 592, 91, 23);
			add(btn_delete);
			btn_delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					//���� ���� ���� ��ư �̺�Ʈ
					//1. ������ ���� ������ �����´�.
					ReservInfo selectedInfo = reservInfos[selectedInfoIndex];
					
					//2. ���� Ȯ�� ���̾�α�
					int result = JOptionPane.showConfirmDialog(null, "������ ��ȭ�� ����մϴ�." + '\n' + "����Ͻðڽ��ϱ�?", "Confirm",
							JOptionPane.YES_NO_OPTION);
					if(result == JOptionPane.YES_OPTION) {
						//3. yes��ư�� �����ٸ�
						try {
							//4. ���� ������ ���� �Լ��� ����Ѵ�.
							MainFrame.screen.cancelReserv(selectedInfo);
							
							//5. ���� ���� �迭���� �����Ѵ�.
							reservInfos[selectedInfoIndex] = null;
							
							//6. ���Ϳ��� ����� ����Ʈ�� �ʱ�ȭ�Ѵ�.
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
			
			//1. ������ ��ȭ ��Ͽ� ���� ���͸� �ʱ�ȭ �ϰ� ���� ����Ʈ�� �����Ѵ�.
			reserveVector.clear();
			reservelist = new JList<>(reserveVector);
			
			
			//2. ���� ������ ��� ��ȭ�� ������ �迭�� �����Ѵ�.
			reservInfos = new ReservInfo[10];

			try {
				//3. ����� ���̵� �����´�. 
				String userID = Theater.personNow.getId();
				
				//4. �������� �˻� ����� �����´�.
				ResultSet rs = MainFrame.screen.searchReservInfo(userID);
				
				//5. ���� �˻� ����� �����Ѵٸ�
				int i = 0;
				while(rs.next()) {
					//6. �������� �����͸� �����´�.
					String id = rs.getString("id");
					String title = rs.getString("title");
					String date = rs.getString("date");
					int round = rs.getInt("round");
					int seat = rs.getInt("seat");
					
					//7. ������ �����͸� ���� ���� �迭�� �ִ´�.
					reservInfos[i] = new ReservInfo(id, title, date, round, seat);
					
					//8. ������ ���� ������ ���Ϳ� �߰��Ѵ�.
					reserveVector.add(new String("<html>" + title + "<br><span style=\\\"color:gray\\\"><small>" + round + "ȸ��   "
							+ seat + "�� �¼�</small></span></html>"));

					i++;
				}
			} catch (Exception e) {
				e.getMessage();
			}
			
			//9. ����Ʈ�� ���͸� �ִ´�.
			reservelist.setListData(reserveVector);
			
			//10. ����Ʈ�� scrollPane�� �߰��Ѵ�.
			scrollPane.setViewportView(reservelist);
			
			//����Ʈ���� ���õ� ������ �˾Ƴ���
			reservelist.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					selectedInfoIndex = reservelist.getSelectedIndex();	
				}
				
			});
		}
	}

	class DDayPlusMoviePanel extends JPanel {

		// �������� ȸ�� 
		Person p = MainFrame.theater.personNow;
		String userName = MainFrame.theater.personNow.getName();

		String FirstPosterPath = "��󷣵�.jpg";
		String SecondPosterPath = "�˶��.jpg";
		String ThirdPosterPath = "�����Ѽ��.jpg";
		String TicketPath = "TICKET.png";

		String FirstTitle = "��󷣵�";
		String SecondTitle = "�˶��";
		String ThirdTitle = "�����Ѽ��";

		private LineBorder bb = new LineBorder(Color.black, 1, true);
		
		   ResultSet rs;
		   int movieCount;

		public DDayPlusMoviePanel() {
			setBounds(0, 0, 501, 677);
			setBackground(Color.WHITE);
			setLayout(null);

			// Ƽ���̹���(����ȭ��)��
			JLabel Ticket_main = new JLabel("Ƽ��_����ȭ��");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Ƽ�� �̹��� Ŭ�� ������
					// 1. ClientMain panel�� �����Ѵ�.
					PanelChange("ClientMainPanel");
				}
			});

//			// ImageIcon ��ü ���� => Image ���� �� ũ�� �����ؼ� ���ο� ��ü ����
//			ImageIcon ticektIcon = new ImageIcon(MainFrame.class.getResource(TicketPath));
//			Image ticketImg = ticektIcon.getImage();
//			Image changedticektImg = ticketImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);
//			ImageIcon changedticektIcon = new ImageIcon(changedticektImg);
//			Ticket_main.setIcon(changedticektIcon);

			// ��ũ��
			JScrollPane scrollPane = new JScrollPane();
			JPanel firstpanel = new JPanel();
			Dimension size = new Dimension();
			size.setSize(447, 375);
			firstpanel.setPreferredSize(size);
			scrollPane.setViewportView(firstpanel);
			scrollPane.setBounds(10, 170, 465, 361);
			add(scrollPane);

			// ù��° �г�(��ȭ�� ��� �����ִ�(?)�г�)
			firstpanel.setBackground(Color.WHITE);
			size.setSize(200, 200);
			firstpanel.setLayout(new FlowLayout(FlowLayout.LEFT, 18, 15));

			// ��ȭ �ϳ� �г�
			JPanel moviepanel1 = new JPanel();
			moviepanel1.setBackground(Color.WHITE);
			firstpanel.add(moviepanel1);

			// ĸ��ȭ
			Dimension dim = new Dimension(130, 200);
			
		      // ���� ��¥
		      LocalDateTime current = LocalDateTime.now();
		      DateTimeFormatter format = DateTimeFormatter.ofPattern("yyMMdd");
		      String today = current.format(format);
		      int today_int = Integer.parseInt(today);

		      // ��ü ��ȭ ��ȯ�ϱ�
		      try {
		         rs = MainFrame.theater.movieAll();
		         movieCount = MainFrame.theater.countMovie();
		      } catch (Exception e) {
		         e.getMessage();
		      }

		      // rs�� �˻���� �����ֱ�
		      JPanel[] moviepanels = new JPanel[movieCount];
		      Image[] images = new Image[movieCount];
		      ImageIcon[] imageIcons = new ImageIcon[movieCount];
		      try {
		         int i = 0;
		         while (rs.next()) {
		            // ���� ������ ��������
		            String dday = rs.getString("dday");
		            int dday_int = Integer.parseInt(dday);

		            if (dday_int > today_int) {// ���� ������ ��
		               // �г� ����
		               moviepanels[i] = new JPanel();
		               moviepanels[i].setBackground(Color.WHITE);
		               firstpanel.add(moviepanels[i]);
		               moviepanels[i].setPreferredSize(dim);
		               moviepanels[i].setLayout(null);
		               // ��ȭ���� ��������
		               String title = rs.getString("title");
		               String posterURL = rs.getString("posterURL");
		               // �̹��� ������ ����
		               URL url = new URL(posterURL);
		               images[i] = ImageIO.read(url);
		               images[i] = images[i].getScaledInstance(128, 160, Image.SCALE_SMOOTH);
		               imageIcons[i] = new ImageIcon(images[i]);
		               // ��ȭ ������
		               JLabel lblPoster = new JLabel("��ȭ ������");
		               lblPoster.setBounds(0, 0, 130, 167);
		               moviepanels[i].add(lblPoster);
		               lblPoster.setHorizontalAlignment(SwingConstants.CENTER);
		               lblPoster.setIcon(imageIcons[i]);
		               // ��: ��ȭ ����
		               JLabel lblTitle = new JLabel(title);
		               lblTitle.setFont(new Font("���� ���", Font.BOLD, 12));
		               lblTitle.setBounds(10, 172, 106, 22);
		               moviepanels[i].add(lblTitle);
		            }

		         }
		      } catch (Exception e) {
		         e.printStackTrace();
		      }


			// �α��� �� �̸� ������
			JLabel lblUserName = new JLabel(userName + "\uB2D8  /");
			lblUserName.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblUserName.setBounds(350, 83, 66, 15);
			add(lblUserName);

			// �α׾ƿ�
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblLogout.setBounds(420, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �α׾ƿ� �� Ŭ�� ������
					// 1. �α׾ƿ� �˸� �޽��� �߻�
					//2. �α׾ƿ��� �Ѵ�.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)�� �����Ѵ�.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame�� �����Ѵ�.
					dispose();
				}
			});

			// ��ܹ� ���� ��
			JLabel lbl_ticketing = new JLabel("\uC608\uB9E4");
			lbl_ticketing.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_ticketing.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_ticketing.setBorder(bb);
			lbl_ticketing.setBounds(0, 109, 162, 42);
			add(lbl_ticketing);
			lbl_ticketing.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ���� ī�װ� Ŭ�� ������
					// 1. Reserve panel�� �����Ѵ�.
					PanelChange("ReservePanel");
				}
			});

			// ��ܹ� ����������ȭ ��
			JLabel lbl_scheduleMovie = new JLabel("\uAC1C\uBD09\uC608\uC815\uC601\uD654");
			lbl_scheduleMovie.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_scheduleMovie.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_scheduleMovie.setBorder(bb);
			lbl_scheduleMovie.setBounds(161, 109, 164, 42);
			add(lbl_scheduleMovie);
			lbl_scheduleMovie.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �󿵿����� Ŭ�� ������
					// 1. DDayPlusMovie panel�� �����Ѵ�.
					PanelChange("DDayPlusMoviePanel");
				}
			});

			// ���������� ��
			JLabel lbl_myPage = new JLabel("\uB9C8\uC774\uD398\uC774\uC9C0");
			lbl_myPage.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_myPage.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_myPage.setBorder(bb);
			lbl_myPage.setBounds(324, 109, 162, 42);
			add(lbl_myPage);
			lbl_myPage.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ���������� Ŭ�� ������
					// 1. ���������� panel�� �����Ѵ�.
					PanelChange("MyPagePanel");
				}
			});

			// �˻� �ؽ�Ʈ�ʵ�
			JTextField tfSearch = new JTextField();
			tfSearch.setBounds(100, 589, 296, 30);
			add(tfSearch);
			tfSearch.setColumns(10);

			// �˻� ��ư
			JButton btnSearch = new JButton("\uAC80\uC0C9");
			btnSearch.setBackground(Color.LIGHT_GRAY);
			btnSearch.setBounds(408, 588, 70, 30);
			add(btnSearch);
			btnSearch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ��ȭ �˻� ��ư �̺�Ʈ
					// 1. �ؽ�Ʈ�ʵ忡 �Է¹��� �����͸� �����´�.
					String keyword = tfSearch.getText();
					// 2. ���� �Է¹��� �ؽ�Ʈ�� �����̶��
					if (keyword.equals("")) {
						// 3. �˸� �޽����� �߻���Ų��.
						JOptionPane.showMessageDialog(null, "�˻��� ��ȭ ���� �Է����ּ���..", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
					} else {
						// 4. ���� �Է¹��� �ؽ�Ʈ�� ������ �ƴ϶��
						try {
							// 5. �Է¹��� Ű����� ��ȭ�� �˻��Ѵ�.
							ResultSet rs = MainFrame.theater.searchMovie(keyword);
							
							// 6. �ؽ�Ʈ�ʵ带 �����
							tfSearch.setText("");
							
							// 7. SearchMovie panel�� �����Ѵ�.	
							// �̶�, �Է¹��� Ű������ �˻� ����� �Ű������� �����Ѵ�.
							ShowSearchMovie(rs);
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});

			// �˻���
			JLabel lbl_search = new JLabel("\uC601\uD654 \uAC80\uC0C9");
			lbl_search.setFont(new Font("���� ���", Font.BOLD, 18));
			lbl_search.setBounds(14, 586, 96, 34);
			add(lbl_search);
		}
	}

	class ReservePanel extends JPanel {

		String userName = Theater.personNow.getName();
		String TicketPath = "TICKET.png";
		
		// ���� ��¥
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
			
			// Ƽ���̹���(����ȭ��)��
			JLabel Ticket_main = new JLabel("Ƽ��_����ȭ��");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Ƽ�� �̹��� Ŭ�� ������
					// 1. ClientMain panel�� �����Ѵ�.
					PanelChange("ClientMainPanel");
				}
			});

//			// ImageIcon ��ü ���� => Image ���� �� ũ�� �����ؼ� ���ο� ��ü ����
//			ImageIcon ticektIcon = new ImageIcon(MainFrame.class.getResource(TicketPath));
//			Image ticketImg = ticektIcon.getImage();
//			Image changedticektImg = ticketImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);
//			ImageIcon changedticektIcon = new ImageIcon(changedticektImg);
//			Ticket_main.setIcon(changedticektIcon);

			// ���� ī�װ�
			JLabel lbl_ticketing = new JLabel("\uC608\uB9E4");
			lbl_ticketing.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_ticketing.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_ticketing.setBorder(bb);
			lbl_ticketing.setBounds(0, 109, 162, 42);
			add(lbl_ticketing);
			lbl_ticketing.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ���� ī�װ� Ŭ�� ������
					// 1. Reserve panel�� �����Ѵ�.
					PanelChange("ReservePanel");
				}
			});

			// ���� ������ ī�װ�
			JLabel lbl_scheduleMovie = new JLabel("\uAC1C\uBD09\uC608\uC815\uC601\uD654");
			lbl_scheduleMovie.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_scheduleMovie.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_scheduleMovie.setBorder(bb);
			lbl_scheduleMovie.setBounds(161, 109, 164, 42);
			add(lbl_scheduleMovie);
			lbl_scheduleMovie.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �󿵿����� Ŭ�� ������
					// 1. DDayPlusMovie panel�� �����Ѵ�.
					PanelChange("DDayPlusMoviePanel");
				}
			});

			// ���������� ī�װ�
			JLabel lbl_myPage = new JLabel("\uB9C8\uC774\uD398\uC774\uC9C0");
			lbl_myPage.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_myPage.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_myPage.setBorder(bb);
			lbl_myPage.setBounds(324, 109, 162, 42);
			add(lbl_myPage);
			lbl_myPage.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ���������� Ŭ�� ������
					// 1. ���������� panel�� �����Ѵ�.
					PanelChange("MyPagePanel");
				}
			});

			// ���� Ÿ��Ʋ ��
			JLabel lbl_title = new JLabel("\uC608\uB9E4");
			lbl_title.setFont(new Font("���� ���", Font.BOLD, 18));
			lbl_title.setBounds(20, 175, 170, 34);
			add(lbl_title);

			// �̸� ���̺�
			JLabel lblUserName = new JLabel(userName + "\uB2D8  /");
			lblUserName.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblUserName.setBounds(350, 83, 66, 15);
			add(lblUserName);

			// �α׾ƿ� ���̺�
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblLogout.setBounds(420, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �α׾ƿ� �� Ŭ�� ������
					// 1. �α׾ƿ� �˸� �޽��� �߻�
					//2. �α׾ƿ��� �Ѵ�.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)�� �����Ѵ�.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame�� �����Ѵ�.
					dispose();
				}
			});
			
			//��ȭ ��ũ��
			JScrollPane moviePane = new JScrollPane();
			moviePane.setBounds(20, 268, 216, 246);
			getContentPane().add(moviePane);

			// �󿵽ð�ǥ ��ũ��
			JScrollPane timePane = new JScrollPane();
			timePane.setBounds(255, 268, 216, 246);
			getContentPane().add(timePane);
			
			// �˻� �ؽ�Ʈ�ʵ�
			JTextField tf_search = new JTextField();
			tf_search.setColumns(10);
			tf_search.setBounds(74, 230, 118, 25);
			add(tf_search);

			// �˻� ��
			JLabel lbl_search = new JLabel("\uC601\uD654\uCC3E\uAE30");
			lbl_search.setFont(new Font("���� ���", Font.PLAIN, 12));
			lbl_search.setBounds(20, 230, 57, 25);
			add(lbl_search);
			
			// �˻� ��ư
			JButton btn_search = new JButton("\uAC80\uC0C9");
			btn_search.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//�˻� ��ư �̺�Ʈ
					//1. ��ȭ ����Ʈ�� ���� ���͸� �ʱ�ȭ �ϰ� ��ȭ ����Ʈ�� �����Ѵ�.
					movieVector.clear();
					movielist =  new JList<>(movieVector);
					
					movielist.setFont(new Font("���� ���", Font.PLAIN, 12));
					movielist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //���� ���ø� �����ϰ� ����
					moviePane.setViewportView(movielist);
					
					try {
						//2. �˻��� ��ȭ ������ �����´�.
						String searchTitle = tf_search.getText();
						
						//3. �˻� ����� �����´�.
						ResultSet rs = MainFrame.theater.searchMovie(searchTitle);
						
						//4. ���� �˻� ����� �����Ѵٸ�
						while (rs.next()) {
							//5. ��ȭ ������ �����´�.
							String searchResult = rs.getString("title");
							
							//6. ������ ��ȭ ������ ���Ϳ� �߰��Ѵ�.
							movieVector.add(searchResult);
						}
						
						//7. ����Ʈ�� ���͸� �ִ´�.
						movielist.setListData(movieVector);
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					//��ȭ ����Ʈ Ŭ�� �̺�Ʈ(��ȭ�� Ŭ���ϸ�)
					movielist.addListSelectionListener(new ListSelectionListener() {
						@Override
						public void valueChanged(ListSelectionEvent e) {
							//1. �ð� ����Ʈ�� ���� ���͸� �ʱ�ȭ�ϰ� �ð� ����Ʈ�� �����Ѵ�.
							timeVector.clear();
							timelist = new JList<>(timeVector);
							
							timelist.setFont(new Font("���� ���", Font.PLAIN, 12));
							timelist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	//���� ���ø� �����ϰ� ����
							timePane.setViewportView(timelist);
							
							//2. ������ ��ȭ ����Ʈ�� ��(��ȭ ����)�� object�� ��ȯ�ϰ�, String���� ����ȯ�Ͽ� �����Ѵ�.
							selectMovieTitle = (String)movielist.getSelectedValue();	
							
							//3. �ش� ��ȭ�� �� ��ȭ���� �����ϴ� �迭�� �����.
							//�Ŀ� ��ȭ�� ������ �� �� ���� ����� �� ��ȭ ��ü�� ����Ѵ�. 
							movieOnScreen = new MovieNow[10];
							int i = 0;	//�� ��ȭ �迭�� �����ϴ� �ε���
							
							try {							
								//4. ������ ��ȭ �������� �� �ð��� �˻��Ͽ� �˻� ����� �����´�.
								ResultSet rs = MainFrame.screen.searchMovieNow(selectMovieTitle);
								
								//5. ���� �˻� ����� �����Ѵٸ�
								while (rs.next()) {
									//6. �� ��ȭ �����͸� �����´�.
									String title = rs.getString("title");
									String date = rs.getString("date");
									int rround = rs.getInt("round");
									String start = rs.getString("start");
									String finish = rs.getString("finish");
									
									//7. �ش� �����͸� �� ��ȭ �迭�� �߰��Ѵ�.
									movieOnScreen[i] = new MovieNow(title, date, rround, start, finish);
									
									//8. ����Ʈ�� ����� ������ ��� ���Ϳ� �߰��Ѵ�.
									timeVector.add(new String("<html>" + rround + " ȸ��<br><span style=\\\\\\\"color:gray\\\\\\\">" + "���� �ð� </small></span>" + start + "  �� �ð� </small></span>" + finish));
									
									i++;
								}
								
								//9. ����Ʈ�� ���͸� �ִ´�.
								timelist.setListData(timeVector);
								
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							
							
							//�ð� ����Ʈ Ŭ�� �̺�Ʈ(�� ȸ���� Ŭ���ϸ�)
							timelist.addListSelectionListener(new ListSelectionListener() {
								@Override
								public void valueChanged(ListSelectionEvent e) {
									//������ list�� index ���� �����Ѵ�.
									selectTimeIndex = timelist.getSelectedIndex();	
								}
							});
						}
						
					});
					
				}
			});
			btn_search.setBackground(Color.LIGHT_GRAY);
			btn_search.setFont(new Font("���� ���", Font.PLAIN, 10));
			btn_search.setBounds(198, 230, 57, 23);
			getContentPane().add(btn_search);


			// ���� ��ư
			JButton btn_buy = new JButton("\uC608\uB9E4");
			btn_buy.setBackground(Color.LIGHT_GRAY);
			btn_buy.setBounds(368, 592, 91, 23);
			add(btn_buy);
			btn_buy.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ���� ��ư �̺�Ʈ
					// 1. �����Ϸ��� ��ȭ�� �󿵿�ȭ ��ü�� �����´�.
					MovieNow movieNow = movieOnScreen[selectTimeIndex];
					
					// 2. Ticketing panel�� �����Ѵ�.
					ChangeTicketingPanel(movieNow);
				}
			});

		}

		public ReservePanel(String title) {
			// 1. �Ű������� ���� �Ķ���͸� �ٷ� �˻��ϴ� ��� �߰��ϱ�
			setBounds(0, 0, 501, 677);
			setBackground(Color.WHITE);
			setLayout(null);

			// Ƽ���̹���(����ȭ��)��
			JLabel Ticket_main = new JLabel("Ƽ��_����ȭ��");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Ƽ�� �̹��� Ŭ�� ������
					// 1. ClientMain panel�� �����Ѵ�.
					PanelChange("ClientMainPanel");
				}
			});

//			// ImageIcon ��ü ���� => Image ���� �� ũ�� �����ؼ� ���ο� ��ü ����
//			ImageIcon ticektIcon = new ImageIcon(MainFrame.class.getResource(TicketPath));
//			Image ticketImg = ticektIcon.getImage();
//			Image changedticektImg = ticketImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);
//			ImageIcon changedticektIcon = new ImageIcon(changedticektImg);
//			Ticket_main.setIcon(changedticektIcon);

			// ���� ī�װ�
			JLabel lbl_ticketing = new JLabel("\uC608\uB9E4");
			lbl_ticketing.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_ticketing.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_ticketing.setBorder(bb);
			lbl_ticketing.setBounds(0, 109, 162, 42);
			add(lbl_ticketing);
			lbl_ticketing.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ���� ī�װ� Ŭ�� ������
					// 1. Reserve panel�� �����Ѵ�.
					PanelChange("ReservePanel");
				}
			});

			// ���� ������ ī�װ�
			JLabel lbl_scheduleMovie = new JLabel("\uAC1C\uBD09\uC608\uC815\uC601\uD654");
			lbl_scheduleMovie.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_scheduleMovie.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_scheduleMovie.setBorder(bb);
			lbl_scheduleMovie.setBounds(161, 109, 164, 42);
			add(lbl_scheduleMovie);
			lbl_scheduleMovie.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �󿵿����� Ŭ�� ������
					// 1. DDayPlusMovie panel�� �����Ѵ�.
					PanelChange("DDayPlusMoviePanel");
				}
			});

			// ���������� ī�װ�
			JLabel lbl_myPage = new JLabel("\uB9C8\uC774\uD398\uC774\uC9C0");
			lbl_myPage.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_myPage.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_myPage.setBorder(bb);
			lbl_myPage.setBounds(324, 109, 162, 42);
			add(lbl_myPage);
			lbl_myPage.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ���������� Ŭ�� ������
					// 1. ���������� panel�� �����Ѵ�.
					PanelChange("MyPagePanel");
				}
			});

			// ���� Ÿ��Ʋ ��
			JLabel lbl_title = new JLabel("\uC608\uB9E4");
			lbl_title.setFont(new Font("���� ���", Font.BOLD, 18));
			lbl_title.setBounds(20, 175, 170, 34);
			add(lbl_title);

			// �̸� ���̺�
			JLabel lblUserName = new JLabel(userName + "\uB2D8  /");
			lblUserName.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblUserName.setBounds(357, 83, 57, 15);
			add(lblUserName);
			
			// �˻� �ؽ�Ʈ�ʵ�
			JTextField tf_search = new JTextField();
			tf_search.setColumns(10);
			tf_search.setBounds(74, 230, 118, 25);
			add(tf_search);

			// �˻� ��
			JLabel lbl_search = new JLabel("\uC601\uD654\uCC3E\uAE30");
			lbl_search.setFont(new Font("���� ���", Font.PLAIN, 12));
			lbl_search.setBounds(20, 230, 57, 25);
			add(lbl_search);

			// �α׾ƿ� ���̺�
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblLogout.setBounds(420, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �α׾ƿ� �� Ŭ�� ������
					// 1. �α׾ƿ� �˸� �޽��� �߻�
					//2. �α׾ƿ��� �Ѵ�.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)�� �����Ѵ�.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame�� �����Ѵ�.
					dispose();
				}
			});

			// ���� ��ư
			JButton btn_buy = new JButton("\uC608\uB9E4");
			btn_buy.setBackground(Color.LIGHT_GRAY);
			btn_buy.setBounds(368, 592, 91, 23);
			add(btn_buy);
			btn_buy.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ���� ��ư �̺�Ʈ
					// 1. �����Ϸ��� ��ȭ�� �󿵿�ȭ ��ü�� �����´�.
					MovieNow movieNow = movieOnScreen[selectTimeIndex];
					
					// 2. Ticketing panel�� �����Ѵ�.
					ChangeTicketingPanel(movieNow);
				}
			});

			//��ȭ ��ũ��
			JScrollPane moviePane = new JScrollPane();
			moviePane.setBounds(20, 268, 216, 246);
			getContentPane().add(moviePane);

			// �󿵽ð�ǥ ��ũ��
			JScrollPane timePane = new JScrollPane();
			timePane.setBounds(255, 268, 216, 246);
			getContentPane().add(timePane);
			
			// �˻� ��ư
			JButton btn_search = new JButton("\uAC80\uC0C9");
			btn_search.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//�˻� ��ư �̺�Ʈ
					//1. ��ȭ ����Ʈ�� ���� ���͸� �ʱ�ȭ �ϰ� ��ȭ ����Ʈ�� �����Ѵ�.
					movieVector.clear();
					movielist =  new JList<>(movieVector);
					
					movielist.setFont(new Font("���� ���", Font.PLAIN, 12));
					movielist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //���� ���ø� �����ϰ� ����
					moviePane.setViewportView(movielist);
					
					try {
						//2. �˻��� ��ȭ ������ �����´�.
						String searchTitle = tf_search.getText();
						
						//3. �˻� ����� �����´�.
						ResultSet rs = MainFrame.theater.searchMovie(searchTitle);
						
						//4. ���� �˻� ����� �����Ѵٸ�
						while (rs.next()) {
							//5. ��ȭ ������ �����´�.
							String searchResult = rs.getString("title");
							
							//6. ������ ��ȭ ������ ���Ϳ� �߰��Ѵ�.
							movieVector.add(searchResult);
						}
						
						//7. ����Ʈ�� ���͸� �ִ´�.
						movielist.setListData(movieVector);
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
					//��ȭ ����Ʈ Ŭ�� �̺�Ʈ(��ȭ�� Ŭ���ϸ�)
					movielist.addListSelectionListener(new ListSelectionListener() {
						@Override
						public void valueChanged(ListSelectionEvent e) {
							//1. �ð� ����Ʈ�� ���� ���͸� �ʱ�ȭ�ϰ� �ð� ����Ʈ�� �����Ѵ�.
							timeVector.clear();
							timelist = new JList<>(timeVector);
							
							timelist.setFont(new Font("���� ���", Font.PLAIN, 12));
							timelist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	//���� ���ø� �����ϰ� ����
							timePane.setViewportView(timelist);
							
							//2. ������ ��ȭ ����Ʈ�� ��(��ȭ ����)�� object�� ��ȯ�ϰ�, String���� ����ȯ�Ͽ� �����Ѵ�.
							selectMovieTitle = (String)movielist.getSelectedValue();	
							
							//3. �ش� ��ȭ�� �� ��ȭ���� �����ϴ� �迭�� �����.
							//�Ŀ� ��ȭ�� ������ �� �� ���� ����� �� ��ȭ ��ü�� ����Ѵ�. 
							movieOnScreen = new MovieNow[10];
							int i = 0;	//�� ��ȭ �迭�� �����ϴ� �ε���
							
							try {							
								//4. ������ ��ȭ �������� �� �ð��� �˻��Ͽ� �˻� ����� �����´�.
								ResultSet rs = MainFrame.screen.searchMovieNow(selectMovieTitle);
								
								//5. ���� �˻� ����� �����Ѵٸ�
								while (rs.next()) {
									//6. �� ��ȭ �����͸� �����´�.
									String title = rs.getString("title");
									String date = rs.getString("date");
									int rround = rs.getInt("round");
									String start = rs.getString("start");
									String finish = rs.getString("finish");
									
									//7. �ش� �����͸� �� ��ȭ �迭�� �߰��Ѵ�.
									movieOnScreen[i] = new MovieNow(title, date, rround, start, finish);
									
									//8. ����Ʈ�� ����� ������ ��� ���Ϳ� �߰��Ѵ�.
									timeVector.add(new String("<html>" + rround + " ȸ��<br><span style=\\\\\\\"color:gray\\\\\\\">" + "���� �ð� </small></span>" + start + "  �� �ð� </small></span>" + finish));
									
									i++;
								}
								
								//9. ����Ʈ�� ���͸� �ִ´�.
								timelist.setListData(timeVector);
								
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							
							
							//�ð� ����Ʈ Ŭ�� �̺�Ʈ(�� ȸ���� Ŭ���ϸ�)
							timelist.addListSelectionListener(new ListSelectionListener() {
								@Override
								public void valueChanged(ListSelectionEvent e) {
									//������ list�� ���� object ��ȯ�ϰ�, String���� ����ȯ�Ͽ� ����
									selectTimeIndex = timelist.getSelectedIndex();	
								}
							});
						}
						
					});
					
				}
			});
			btn_search.setBackground(Color.LIGHT_GRAY);
			btn_search.setFont(new Font("���� ���", Font.PLAIN, 10));
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

			// Ƽ���̹���(����ȭ��)��
			JLabel Ticket_main = new JLabel("Ƽ��_����ȭ��");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Ƽ�� �̹��� Ŭ�� ������
					// 1. ClientMain panel�� �����Ѵ�.
					PanelChange("ClientMainPanel");
				}
			});
//
//			// ImageIcon ��ü ���� => Image ���� �� ũ�� �����ؼ� ���ο� ��ü ����
//			ImageIcon ticektIcon = new ImageIcon(MainFrame.class.getResource(TicketPath));
//			Image ticketImg = ticektIcon.getImage();
//			Image changedticektImg = ticketImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);
//			ImageIcon changedticektIcon = new ImageIcon(changedticektImg);
//			Ticket_main.setIcon(changedticektIcon);

			

//			// ��ũ��
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
//			// ù��° �г�(��ȭ�� ��� �����ִ�(?)�г�)
//			firstpanel.setBackground(Color.WHITE);
//			size.setSize(200, 200);
//			firstpanel.setLayout(new FlowLayout(FlowLayout.LEFT, 18, 15));

			  // ù��° �г�(��ȭ�� ��� �����ִ�(?)�г�)
	         JPanel firstpanel = new JPanel();
	         Dimension size = new Dimension(500,1000);
	         firstpanel.setPreferredSize(size);
	         firstpanel.setBackground(Color.WHITE);
	         firstpanel.setLayout(new FlowLayout(FlowLayout.LEFT, 18, 15));

	         
	         // ��ũ��
	         JScrollPane scrollPane = new JScrollPane(firstpanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	         scrollPane.setBounds(10, 170, 465, 361);
	         add(scrollPane);
	         
	         scrollPane.setViewportView(firstpanel);
	         
			
			// ��ȭ �ϳ� �г�
			JPanel moviepanel1 = new JPanel();
			moviepanel1.setBackground(Color.WHITE);
			firstpanel.add(moviepanel1);

			// ĸ��ȭ
			Dimension dim = new Dimension(130, 200);
			Dimension dim1 = new Dimension(-18, 0);
			
			moviepanel1.setPreferredSize(dim1);
			moviepanel1.setLayout(null);
			
			
			// ��ü ��ȭ ��ȯ
			int movieCount = 0;
			try {
				rs = MainFrame.theater.movieAll();
				movieCount = MainFrame.theater.countMovie();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			// rs�� �˻���� �����ֱ�
			JPanel[] moviepanels = new JPanel[movieCount];
			Image[] images = new Image[movieCount];
			ImageIcon[] imageIcons = new ImageIcon[movieCount];
			try {
				int i = 0;
				while (rs.next()) {
					// �г� ����
					moviepanels[i] = new JPanel();
					moviepanels[i].setBackground(Color.WHITE);
					firstpanel.add(moviepanels[i]);
					moviepanels[i].setPreferredSize(dim);
					moviepanels[i].setLayout(null);
					// ��ȭ���� ��������
					String title = rs.getString("title");
					String posterURL = rs.getString("posterURL");
					// �̹��� ������ ����
					URL url = new URL(posterURL);
					images[i] = ImageIO.read(url);
					images[i] = images[i].getScaledInstance(128, 160, Image.SCALE_SMOOTH);
					imageIcons[i] = new ImageIcon(images[i]);
					// ��ȭ ������
					JLabel lblPoster = new JLabel("��ȭ ������");
					lblPoster.setBounds(0, 0, 130, 167);
					moviepanels[i].add(lblPoster);
					lblPoster.setHorizontalAlignment(SwingConstants.CENTER);
					lblPoster.setIcon(imageIcons[i]);
					// ��: ��ȭ ����
					JLabel lblTitle = new JLabel(title);
					lblTitle.setFont(new Font("���� ���", Font.BOLD, 12));
					lblTitle.setBounds(10, 172, 106, 22);
					moviepanels[i].add(lblTitle);
					// Ŭ�� �̺�Ʈ
					moviepanels[i].addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							selectedMovie = title;
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// �α��� �� �̸� ������
			JLabel lblUserName = new JLabel(userName + "\uB2D8  /");
			lblUserName.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblUserName.setBounds(350, 83, 66, 15);
			add(lblUserName);

			// �α׾ƿ�
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblLogout.setBounds(420, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �α׾ƿ� �� Ŭ�� ������
					// 1. �α׾ƿ� �˸� �޽��� �߻�
					//2. �α׾ƿ��� �Ѵ�.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)�� �����Ѵ�.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame�� �����Ѵ�.
					dispose();
				}
			});

			// ��ܹ� ���� ��
			JLabel lbl_ticketing = new JLabel("\uC608\uB9E4");
			lbl_ticketing.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_ticketing.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_ticketing.setBorder(bb);
			lbl_ticketing.setBounds(0, 109, 162, 42);
			add(lbl_ticketing);
			lbl_ticketing.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ���� ī�װ� Ŭ�� ������
					// 1. Reserve panel�� �����Ѵ�.
					PanelChange("ReservePanel");
				}
			});

			// ��ܹ� ����������ȭ ��
			JLabel lbl_scheduleMovie = new JLabel("\uAC1C\uBD09\uC608\uC815\uC601\uD654");
			lbl_scheduleMovie.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_scheduleMovie.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_scheduleMovie.setBorder(bb);
			lbl_scheduleMovie.setBounds(161, 109, 164, 42);
			add(lbl_scheduleMovie);
			lbl_scheduleMovie.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �󿵿����� Ŭ�� ������
					// 1. DDayPlusMovie panel�� �����Ѵ�.
					PanelChange("DDayPlusMoviePanel");
				}
			});

			// ���������� ��
			JLabel lbl_myPage = new JLabel("\uB9C8\uC774\uD398\uC774\uC9C0");
			lbl_myPage.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_myPage.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_myPage.setBorder(bb);
			lbl_myPage.setBounds(324, 109, 162, 42);
			add(lbl_myPage);
			lbl_myPage.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ���������� Ŭ�� ������
					// 1. ���������� panel�� �����Ѵ�.
					PanelChange("MyPagePanel");
				}
			});

			// �˻� �ؽ�Ʈ�ʵ�
			JTextField tfSearch = new JTextField();
			tfSearch.setBounds(100, 589, 296, 30);
			add(tfSearch);
			tfSearch.setColumns(10);

			// �˻� ��ư
			JButton btnSearch = new JButton("\uAC80\uC0C9");
			btnSearch.setBackground(Color.LIGHT_GRAY);
			btnSearch.setBounds(408, 588, 70, 30);
			add(btnSearch);
			btnSearch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ��ȭ �˻� ��ư �̺�Ʈ
					// 1. �ؽ�Ʈ�ʵ忡 �Է¹��� �����͸� �����´�.
					String keyword = tfSearch.getText();
					// 2. ���� �Է¹��� �ؽ�Ʈ�� �����̶��
					if (keyword.equals("")) {
						// 3. �˸� �޽����� �߻���Ų��.
						JOptionPane.showMessageDialog(null, "�˻��� ��ȭ ���� �Է����ּ���..", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
					} else {
						// 4. ���� �Է¹��� �ؽ�Ʈ�� ������ �ƴ϶��
						try {
							// 5. �Է¹��� Ű����� ��ȭ�� �˻��Ѵ�.
							ResultSet rs = MainFrame.theater.searchMovie(keyword);
							
							// 6. �ؽ�Ʈ�ʵ带 �����
							tfSearch.setText("");
							
							// 7. SearchMovie panel�� �����Ѵ�.	
							// �̶�, �Է¹��� Ű������ �˻� ����� �Ű������� �����Ѵ�.
							ShowSearchMovie(rs);
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});

			// �˻���
			JLabel lbl_search = new JLabel("\uC601\uD654 \uAC80\uC0C9");
			lbl_search.setFont(new Font("���� ���", Font.BOLD, 18));
			lbl_search.setBounds(14, 586, 96, 34);
			add(lbl_search);

			// ���Ź�ư
			JButton buy_button = new JButton("\uC608\uB9E4");
			buy_button.setForeground(Color.WHITE);
			buy_button.setBackground(Color.RED);
			buy_button.setBounds(408, 548, 70, 30);
			add(buy_button);
			buy_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//�˻� ��ư �̺�Ʈ
					//1. ReservePanel�� �����Ѵ�.
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

			// Ƽ���̹���(����ȭ��)��
			JLabel Ticket_main = new JLabel("Ƽ��_����ȭ��");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Ƽ�� �̹��� Ŭ�� ������
					// 1. ClientMain panel�� �����Ѵ�.
					PanelChange("ClientMainPanel");
				}
			});

//			// ImageIcon ��ü ���� => Image ���� �� ũ�� �����ؼ� ���ο� ��ü ����
//			ImageIcon ticektIcon = new ImageIcon(MainFrame.class.getResource(TicketPath));
//			Image ticketImg = ticektIcon.getImage();
//			Image changedticektImg = ticketImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);
//			ImageIcon changedticektIcon = new ImageIcon(changedticektImg);
//			Ticket_main.setIcon(changedticektIcon);

			// ��� ���� ī�װ�
			JLabel lbl_ticketing = new JLabel("\uC608\uB9E4");
			lbl_ticketing.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_ticketing.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_ticketing.setBounds(0, 109, 162, 42);
			lbl_ticketing.setBorder(bb);
			add(lbl_ticketing);
			lbl_ticketing.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ���� ī�װ� Ŭ�� ������
					// 1. Reserve panel�� �����Ѵ�.
					PanelChange("ReservePanel");
				}
			});

			// ��� ���������� ī�װ�
			JLabel lbl_myPage = new JLabel("\uB9C8\uC774\uD398\uC774\uC9C0");
			lbl_myPage.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_myPage.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_myPage.setBorder(bb);
			lbl_myPage.setBounds(324, 109, 162, 42);
			add(lbl_myPage);
			lbl_myPage.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ���������� Ŭ�� ������
					// 1. ���������� panel�� �����Ѵ�.
					PanelChange("MyPagePanel");
				}
			});

			// �� ���� ��ȭ ī�װ�
			JLabel lbl_scheduleMovie = new JLabel("\uAC1C\uBD09\uC608\uC815\uC601\uD654");
			lbl_scheduleMovie.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_scheduleMovie.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_scheduleMovie.setBorder(bb);
			lbl_scheduleMovie.setBounds(161, 109, 164, 42);
			add(lbl_scheduleMovie);
			lbl_scheduleMovie.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �󿵿����� Ŭ�� ������
					// 1. DDayPlusMovie panel�� �����Ѵ�.
					PanelChange("DDayPlusMoviePanel");
				}
			});

			// ȸ������ ���� Ÿ��Ʋ ��
			JLabel lbl_title = new JLabel("\uD68C\uC6D0\uC815\uBCF4 \uC218\uC815\r\n");
			lbl_title.setFont(new Font("���� ���", Font.BOLD, 18));
			lbl_title.setBounds(185, 175, 126, 34);
			add(lbl_title);

			// ���̵� ��
			JLabel lbl_id = new JLabel("\uC544\uC774\uB514");
			lbl_id.setBounds(45, 421, 50, 15);
			add(lbl_id);

			// ��й�ȣ ��
			JLabel lbl_pw = new JLabel("\uBE44\uBC00\uBC88\uD638");
			lbl_pw.setBounds(39, 478, 56, 15);
			add(lbl_pw);

			// ��й�ȣ Ȯ�� ��
			JLabel lbl_checkPw = new JLabel(
					"<html><body><center>\uBE44\uBC00\uBC88\uD638<br>\uD655\uC778</center></body></html>");
			lbl_checkPw.setBounds(39, 525, 56, 29);
			add(lbl_checkPw);

			// �̸� ��
			JLabel lbl_name = new JLabel("\uC774\uB984");
			lbl_name.setBounds(48, 260, 50, 15);
			add(lbl_name);

			// ��ȭ��ȣ ��
			JLabel label_phoneNum = new JLabel("\uC804\uD654\uBC88\uD638");
			label_phoneNum.setBounds(39, 311, 56, 15);
			add(label_phoneNum);

			// ������� ��
			JLabel lbl_birth = new JLabel("\uC0DD\uB144\uC6D4\uC77C");
			lbl_birth.setBounds(39, 367, 56, 15);
			add(lbl_birth);

			// ��ȭ��ȣ �޴� �ؽ�Ʈ�ʵ�
			JTextField phoneNum_tf = new JTextField();
			phoneNum_tf.setColumns(10);
			phoneNum_tf.setBounds(107, 305, 158, 29);
			add(phoneNum_tf);

			// ��й�ȣ �ؽ�Ʈ�ʵ�
			JTextField tf_pw = new JTextField();
			tf_pw.setColumns(10);
			tf_pw.setBounds(107, 360, 158, 29);
			add(tf_pw);

			// ��й�ȣ Ȯ�� �ؽ�Ʈ �ʵ�
			JTextField tf_checkPw = new JTextField();
			tf_checkPw.setColumns(10);
			tf_checkPw.setBounds(107, 470, 158, 29);
			add(tf_checkPw);

			// �̸� �ؽ�Ʈ �ʵ�
			JTextField tf_name = new JTextField();
			tf_name.setColumns(10);
			tf_name.setBounds(107, 250, 158, 29);
			add(tf_name);

			// ������� �ؽ�Ʈ �ʵ�
			JTextField tf_birth = new JTextField();
			tf_birth.setColumns(10);
			tf_birth.setBounds(107, 525, 158, 29);
			add(tf_birth);

			// Ż�� ��ư
			JButton btn_delete = new JButton("\uD0C8\uD1F4");
			btn_delete.setBackground(Color.LIGHT_GRAY);
			btn_delete.setFont(new Font("���� ���", Font.PLAIN, 12));
			btn_delete.setBounds(277, 581, 91, 23);
			add(btn_delete);
			btn_delete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Ż�� ��ư �̺�Ʈ
					// 1. Ż�� Ȯ�� �˸� �޽����� �߻���Ų��.
					int result = JOptionPane.showConfirmDialog(null, "Ż���Ͻðڽ��ϱ�?", "Ż�� Ȯ��", JOptionPane.YES_NO_OPTION);
					
					if (result == JOptionPane.YES_OPTION) {
						//2. ����ڰ� 'Yes'�� �����ߴٸ�
						//3. ȸ�� Ż�� �Լ��� �����Ѵ�.
						try {
							MainFrame.theater.withdrawal(client_id);
							
							//4. Ż�� �˸� �޽����� �߻���Ų��.
							JOptionPane.showMessageDialog(null, "Ż��Ǿ����ϴ�.", "ȸ�� Ż��", JOptionPane.INFORMATION_MESSAGE);
							
							// 3. MainFrame(Login frame)�� �����Ѵ�.
							MainFrame mainFrame = new MainFrame();
							mainFrame.setVisible(true);
							
							// 4. Client Frame�� ���ش�.
							dispose();
							
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				}
			});

			// �����Ϸ� ��ư
			JButton btn_modify = new JButton("\uC218\uC815\uC644\uB8CC");
			btn_modify.setBackground(Color.LIGHT_GRAY);
			btn_modify.setFont(new Font("���� ���", Font.PLAIN, 12));
			btn_modify.setBounds(380, 581, 91, 23);
			add(btn_modify);
			btn_modify.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ���� �Ϸ� ��ư �̺�Ʈ
					//1. ��й�ȣ�� ��й�ȣ Ȯ�� �Է°��� �����´�.
					String pw = tf_pw.getText();
					String pwcheck = tf_checkPw.getText();
					
					//2. ���� �Է°��� �ٸ���
					if (pw.equals(pwcheck)) {
						//3. �˸� �޽����� �߻���Ų��.
						JOptionPane.showMessageDialog(null, "��й�ȣ Ȯ���� �߸��Ǿ����ϴ�..", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						//4. ���� ��й�ȣ�� ��ġ�ϴٸ�
						//5. �Է��� �����͵��� ��� Person ��ü�� �����.
						String name = tf_name.getText();
						String phone = phoneNum_tf.getText();
						String birth = tf_birth.getText();
						
						Person p = new Person(name, phone, birth, client_id, pw);
						
						//6. ���� �Լ��� �����Ų��.
						try {
							MainFrame.theater.modifyPerson(p);	
						} catch (Exception e2) {
							e2.printStackTrace();
						}
						
						// 7. ������ �Ϸ�Ǹ� ���� �Ϸ� �˸� �޽����� �߻���Ų��.
						JOptionPane.showMessageDialog(null, "�����Ϸ� �Ǿ����ϴ�.", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
						
						//8. �ؽ�Ʈ�ʵ带 �����.
						tf_name.setText("");
						phoneNum_tf.setText("");
						tf_birth.setText("");
						tf_pw.setText("");
						tf_checkPw.setText("");
						
						// 9. MyPage panel�� �����Ѵ�.
						PanelChange("MyPagePanel");
						
					}
						
				}

			});

			// �α��ε� �̸� �ߴ� ��(���)
			JLabel lblUserName = new JLabel(userName + "�� /");
			lblUserName.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblUserName.setBounds(350, 83, 50, 15);
			add(lblUserName);

			// �α׾ƿ� ��
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblLogout.setBounds(420, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �α׾ƿ� �� Ŭ�� ������
					// 1. �α׾ƿ� �˸� �޽��� �߻�
					//2. �α׾ƿ��� �Ѵ�.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)�� �����Ѵ�.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame�� �����Ѵ�.
					dispose();
				}
			});

			// ���̵� �� (��)
			JLabel lbl_a_id = new JLabel(client_id);
			lbl_a_id.setBounds(107, 256, 136, 23);
			add(lbl_a_id);

			// ���� 6�ڸ��� �Է��϶�� �˷��ִ� ��
			JLabel birthlabel = new JLabel("\u203B6\uC790\uB9AC\uB85C \uC785\uB825");
			birthlabel.setForeground(Color.RED);
			birthlabel.setFont(new Font("���� ���", Font.PLAIN, 12));
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
			
			// ���� ��¥
			LocalDateTime current = LocalDateTime.now();
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyMMdd");
			this.today = current.format(format);
			
			// Ƽ���̹���(����ȭ��)��
			JLabel Ticket_main = new JLabel("Ƽ��_����ȭ��");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Ƽ�� �̹��� Ŭ�� ������
					// 1. ClientMain panel�� �����Ѵ�.
					PanelChange("ClientMainPanel");
				}
			});

//			// ImageIcon ��ü ���� => Image ���� �� ũ�� �����ؼ� ���ο� ��ü ����
//			ImageIcon ticektIcon = new ImageIcon(MainFrame.class.getResource(TicketPath));
//			Image ticketImg = ticektIcon.getImage();
//			Image changedticektImg = ticketImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);
//			ImageIcon changedticektIcon = new ImageIcon(changedticektImg);
//			Ticket_main.setIcon(changedticektIcon);

			// ���� ��ܹ� ��
			JLabel lbl_ticketing = new JLabel("\uC608\uB9E4");
			lbl_ticketing.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_ticketing.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_ticketing.setBorder(bb);
			lbl_ticketing.setBounds(0, 109, 162, 42);
			add(lbl_ticketing);
			lbl_ticketing.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ���� ī�װ� Ŭ�� ������
					// 1. Reserve panel�� �����Ѵ�.
					PanelChange("ReservePanel");
				}
			});

			// ���� ���� ��ȭ ��ܹ� ��
			JLabel lbl_scheduleMovie = new JLabel("\uAC1C\uBD09\uC608\uC815\uC601\uD654");
			lbl_scheduleMovie.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_scheduleMovie.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_scheduleMovie.setBorder(bb);
			lbl_scheduleMovie.setBounds(161, 109, 164, 42);
			add(lbl_scheduleMovie);
			lbl_scheduleMovie.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �󿵿����� Ŭ�� ������
					// 1. DDayPlusMovie panel�� �����Ѵ�.
					PanelChange("DDayPlusMoviePanel");
				}
			});

			// ���������� ��ܹ� ��
			JLabel lbl_myPage = new JLabel("\uB9C8\uC774\uD398\uC774\uC9C0");
			lbl_myPage.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_myPage.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_myPage.setBorder(bb);
			lbl_myPage.setBounds(324, 109, 162, 42);
			add(lbl_myPage);
			lbl_myPage.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ���������� Ŭ�� ������
					// 1. ���������� panel�� �����Ѵ�.
					PanelChange("MyPagePanel");
				}
			});

			// ���� Ÿ��Ʋ ��
			JLabel lbl_title = new JLabel("\uC601\uD654\uC81C\uBAA9, \uC0C1\uC601\uAD00, \uC0C1\uC601\uC2DC\uAC04");
			lbl_title.setFont(new Font("���� ���", Font.BOLD, 16));
			lbl_title.setBounds(43, 175, 271, 34);
			add(lbl_title);

			// ���� �̸� ��
			JLabel lblUserName = new JLabel(userName + "��  /");
			lblUserName.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblUserName.setBounds(350, 83, 66, 15);
			add(lblUserName);

			// �α׾ƿ� ��
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblLogout.setBounds(420, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �α׾ƿ� �� Ŭ�� ������
					// 1. �α׾ƿ� �˸� �޽��� �߻�
					//2. �α׾ƿ��� �Ѵ�.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)�� �����Ѵ�.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame�� �����Ѵ�.
					dispose();
				}
			});

			// ���� ��ư
			JButton btn_buy = new JButton("\uC608\uB9E4");
			btn_buy.setBackground(Color.LIGHT_GRAY);
			btn_buy.setForeground(Color.BLACK);
			btn_buy.setFont(new Font("���� ���", Font.PLAIN, 12));
			btn_buy.setBounds(368, 581, 91, 34);
			add(btn_buy);
			btn_buy.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ���� ��ư �̺�Ʈ
					// 1. ���� Ȯ�� frame�� �����Ѵ�.
					ReserveCheckFrame reserveCheck = new ReserveCheckFrame(movieNow, Integer.toString(seatNum));
					reserveCheck.setVisible(true);
					// 2. client main frame���� �����Ѵ�.
					PanelChange("ClientMainPanel");
				}
			});

			// ���� �¼� �� �� �ְ� �ϴ� �г�
			JPanel panel = new JPanel();
			panel.setBackground(Color.LIGHT_GRAY);
			panel.setBounds(30, 230, 429, 245);
			add(panel);
			panel.setLayout(null);

			// ��ũ�� �г�
			JPanel screen_panel = new JPanel();
			screen_panel.setBackground(Color.GRAY);
			screen_panel.setBounds(35, 10, 361, 35);
			panel.add(screen_panel);
			screen_panel.setLayout(null);

			// ��ũ�� ��
			JLabel lblNewLabel_1 = new JLabel("\uC2A4\uD06C\uB9B0");
			lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel_1.setFont(new Font("���� ���", Font.BOLD, 14));
			lblNewLabel_1.setBounds(0, 0, 361, 36);
			screen_panel.add(lblNewLabel_1);
			lblNewLabel_1.setBackground(Color.DARK_GRAY);
			lblNewLabel_1.setForeground(Color.WHITE);

			// ��ư 1~21
			btns = new JButton[22];
			for(int i = 1; i < 22; i++) {
				btns[i] = new JButton(""+i+"");
				btns[i].setBackground(Color.white);
				btns[i].setFont(new Font("���� ���", Font.PLAIN, 10));
				panel.add(btns[i]);
				int j = i;
				btns[i].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						seatNum = j;
					}
					
				});
			}
			
			//��ư ��ġ ����
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

			// �̹� ���õ� �¼� üũ
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

			// �� �¼��� ���� �����ϴٴ� �� �˷��ִ� ��
			JLabel lbl_alert = new JLabel("\u203B1\uC778 1\uC88C\uC11D\uB9CC \uC608\uB9E4 \uAC00\uB2A5\uD569\uB2C8\uB2E4.\u203B");
			lbl_alert.setForeground(Color.RED);
			lbl_alert.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_alert.setFont(new Font("���� ���", Font.BOLD, 12));
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

			// Ƽ���̹���(����ȭ��)��
			JLabel Ticket_main = new JLabel("Ƽ��_����ȭ��");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Ƽ�� �̹��� Ŭ�� ������
					// 1. ClientMain panel�� �����Ѵ�.
					PanelChange("ClientMainPanel");
				}
			});
//
//			// ImageIcon ��ü ���� => Image ���� �� ũ�� �����ؼ� ���ο� ��ü ����
//			ImageIcon ticektIcon = new ImageIcon(MainFrame.class.getResource(TicketPath));
//			Image ticketImg = ticektIcon.getImage();
//			Image changedticektImg = ticketImg.getScaledInstance(128, 160, Image.SCALE_SMOOTH);
//			ImageIcon changedticektIcon = new ImageIcon(changedticektImg);
//			Ticket_main.setIcon(changedticektIcon);

			// ��ũ��
			JScrollPane scrollPane = new JScrollPane();
			JPanel firstpanel = new JPanel();
			Dimension size = new Dimension(447, 600);
			firstpanel.setPreferredSize(size);
			scrollPane.setViewportView(firstpanel);
			scrollPane.setBounds(10, 170, 465, 361);
			add(scrollPane);

			// ù��° �г�(��ȭ�� ��� �����ִ�(?)�г�)
			firstpanel.setBackground(Color.WHITE);
			size.setSize(200, 200);
			firstpanel.setLayout(new FlowLayout(FlowLayout.LEFT, 18, 15));

			// ��ȭ �ϳ� �г�
			JPanel moviepanel1 = new JPanel();
			moviepanel1.setBackground(Color.WHITE);
			firstpanel.add(moviepanel1);

			// ĸ��ȭ
			Dimension dim = new Dimension(130, 200);

			// rs�� �˻���� �����ֱ�
			JPanel[] moviepanels = new JPanel[10];
			Image[] images = new Image[10];
			ImageIcon[] imageIcons = new ImageIcon[10];
			try {
				int i = 0;
				while (rs.next()) {
					// �г� ����
					moviepanels[i] = new JPanel();
					moviepanels[i].setBackground(Color.WHITE);
					firstpanel.add(moviepanels[i]);
					moviepanels[i].setPreferredSize(dim);
					moviepanels[i].setLayout(null);
					// ��ȭ���� ��������
					String title = rs.getString("title");
					String posterURL = rs.getString("posterURL");
					// �̹��� ������ ����
					URL url = new URL(posterURL);
					images[i] = ImageIO.read(url);
					images[i] = images[i].getScaledInstance(128, 160, Image.SCALE_SMOOTH);
					imageIcons[i] = new ImageIcon(images[i]);
					// ��ȭ ������
					JLabel lblPoster = new JLabel("��ȭ ������");
					lblPoster.setBounds(0, 0, 130, 167);
					moviepanels[i].add(lblPoster);
					lblPoster.setHorizontalAlignment(SwingConstants.CENTER);
					lblPoster.setIcon(imageIcons[i]);
					// ��: ��ȭ ����
					JLabel lblTitle = new JLabel(title);
					lblTitle.setFont(new Font("���� ���", Font.BOLD, 12));
					lblTitle.setBounds(10, 172, 106, 22);
					moviepanels[i].add(lblTitle);
					// Ŭ�� �̺�Ʈ
					moviepanels[i].addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							selectedMovie = title;
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// �α��� �� �̸� ������
			JLabel lblUserName = new JLabel(userName + "\uB2D8  /");
			lblUserName.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblUserName.setBounds(350, 83, 66, 15);
			add(lblUserName);

			// �α׾ƿ�
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblLogout.setBounds(420, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �α׾ƿ� �� Ŭ�� ������
					// 1. �α׾ƿ� �˸� �޽��� �߻�
					//2. �α׾ƿ��� �Ѵ�.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)�� �����Ѵ�.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame�� �����Ѵ�.
					dispose();
				}
			});

			// ��ܹ� ���� ��
			JLabel lbl_ticketing = new JLabel("\uC608\uB9E4");
			lbl_ticketing.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_ticketing.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_ticketing.setBorder(bb);
			lbl_ticketing.setBounds(0, 109, 162, 42);
			add(lbl_ticketing);
			lbl_ticketing.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ���� ī�װ� Ŭ�� ������
					// 1. Reserve panel�� �����Ѵ�.
					PanelChange("ReservePanel");
				}
			});

			// ��ܹ� ����������ȭ ��
			JLabel lbl_scheduleMovie = new JLabel("\uAC1C\uBD09\uC608\uC815\uC601\uD654");
			lbl_scheduleMovie.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_scheduleMovie.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_scheduleMovie.setBorder(bb);
			lbl_scheduleMovie.setBounds(161, 109, 164, 42);
			add(lbl_scheduleMovie);
			lbl_scheduleMovie.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �󿵿����� Ŭ�� ������
					// 1. DDayPlusMovie panel�� �����Ѵ�.
					PanelChange("DDayPlusMoviePanel");
				}
			});

			// ���������� ��
			JLabel lbl_myPage = new JLabel("\uB9C8\uC774\uD398\uC774\uC9C0");
			lbl_myPage.setHorizontalAlignment(SwingConstants.CENTER);
			lbl_myPage.setFont(new Font("���� ���", Font.BOLD, 15));
			lbl_myPage.setBorder(bb);
			lbl_myPage.setBounds(324, 109, 162, 42);
			add(lbl_myPage);
			lbl_myPage.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ���������� Ŭ�� ������
					// 1. ���������� panel�� �����Ѵ�.
					PanelChange("MyPagePanel");
				}
			});

			// �˻� �ؽ�Ʈ�ʵ�
			JTextField tfSearch = new JTextField();
			tfSearch.setBounds(100, 589, 296, 30);
			add(tfSearch);

			// �˻� ��ư
			JButton btnSearch = new JButton("\uAC80\uC0C9");
			btnSearch.setBackground(Color.LIGHT_GRAY);
			btnSearch.setBounds(408, 588, 70, 30);
			add(btnSearch);
			btnSearch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ��ȭ �˻� ��ư �̺�Ʈ
					// 1. �ؽ�Ʈ�ʵ忡 �Է¹��� �����͸� �����´�.
					String keyword = tfSearch.getText();
					// 2. ���� �Է¹��� �ؽ�Ʈ�� �����̶��
					if (keyword.equals("")) {
						// 3. �˸� �޽����� �߻���Ų��.
						JOptionPane.showMessageDialog(null, "�˻��� ��ȭ ���� �Է����ּ���..", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
					} else {
						// 4. ���� �Է¹��� �ؽ�Ʈ�� ������ �ƴ϶��
						try {
							// 5. �Է¹��� Ű����� ��ȭ�� �˻��Ѵ�.
							ResultSet rs = MainFrame.theater.searchMovie(keyword);
							
							// 6. �ؽ�Ʈ�ʵ带 �����
							tfSearch.setText("");
							
							// 7. SearchMovie panel�� �����Ѵ�.	
							// �̶�, �Է¹��� Ű������ �˻� ����� �Ű������� �����Ѵ�.
							ShowSearchMovie(rs);
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});

			// �˻���
			JLabel lbl_search = new JLabel("\uC601\uD654 \uAC80\uC0C9");
			lbl_search.setFont(new Font("���� ���", Font.BOLD, 18));
			lbl_search.setBounds(14, 586, 96, 34);
			add(lbl_search);

			// ���Ź�ư
			JButton buy_button = new JButton("\uC608\uB9E4");
			buy_button.setForeground(Color.WHITE);
			buy_button.setBackground(Color.RED);
			buy_button.setBounds(408, 548, 70, 30);
			add(buy_button);
			buy_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ���� ��ư �̺�Ʈ
					// 1. ���� panel�� �����Ѵ�.
					ReserveSelectdMovie(selectedMovie);
				}
			});

		}
	}
}

//���� Ȯ�� ������: ���� Ȯ�� ũ���� �������� panel�� ���� Ȯ�� panel �ϳ��� ����ϱ� ������ ������ �ȿ� panel�� �����Ͽ���
class ReserveCheckFrame extends JFrame {

	//private LineBorder bb = new LineBorder(Color.black, 1, true);
	private JPanel ReserveCheckPanel; // ���� Ȯ�� panel

	// ������
	public ReserveCheckFrame(MovieNow reserveMovie, String reserveSeat) {
		
		String movieName = reserveMovie.getTitle();
		String movieDate = reserveMovie.getDate();
		int movieRound = reserveMovie.getRound();
		String movieSeat = reserveSeat;
		
		
		// ����Ȯ�� ������ ����
		setTitle("Reserve Check"); // ������ title ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 501, 580);
		ReserveCheckPanel = new JPanel();
		ReserveCheckPanel.setBackground(Color.WHITE);
		ReserveCheckPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(ReserveCheckPanel);
		ReserveCheckPanel.setLayout(null);
		
		//����Ȯ�� Ÿ��Ʋ ��
		JLabel lbl_checkTicket = new JLabel("\uC608\uB9E4 \uD655\uC778");
		lbl_checkTicket.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_checkTicket.setFont(new Font("���� ���", Font.BOLD, 18));
		lbl_checkTicket.setBounds(0, 66, 487, 34);
		ReserveCheckPanel.add(lbl_checkTicket);

		//��ȭ ���� ��
		JLabel lbl_movieTitle = new JLabel("\uC601\uD654 \uC81C\uBAA9:");
		lbl_movieTitle.setFont(new Font("���� ���", Font.BOLD, 12));
		lbl_movieTitle.setBounds(80, 140, 80, 23);
		ReserveCheckPanel.add(lbl_movieTitle);
		
		//��ȭ �󿵳�¥ ��
		JLabel lbl_movieDate = new JLabel("\uC0C1\uC601 \uB0A0\uC9DC:");
		lbl_movieDate.setFont(new Font("���� ���", Font.BOLD, 12));
		lbl_movieDate.setBounds(80, 180, 57, 23);
		ReserveCheckPanel.add(lbl_movieDate);
		
		//��ȭ ȸ������ ��
		JLabel lbl_movieRound = new JLabel("\uD68C\uCC28 \uC815\uBCF4:");
		lbl_movieRound.setFont(new Font("���� ���", Font.BOLD, 12));
		lbl_movieRound.setBounds(80, 259, 80, 23);
		ReserveCheckPanel.add(lbl_movieRound);
		
		//��ȭ �¼���ȣ ��
		JLabel lbl_movieSeat = new JLabel("\uC88C\uC11D\uBC88\uD638:");
		lbl_movieSeat.setFont(new Font("���� ���", Font.BOLD, 12));
		lbl_movieSeat.setBounds(80, 300, 80, 23);
		ReserveCheckPanel.add(lbl_movieSeat);
		
		//���� Ÿ��Ʋ ���� Ȯ�� ��
		JLabel lbl_a_moveTitle = new JLabel(movieName);
		lbl_a_moveTitle.setFont(new Font("���� ���", Font.PLAIN, 12));
		lbl_a_moveTitle.setBounds(149, 144, 185, 15);
		ReserveCheckPanel.add(lbl_a_moveTitle);
		
		//���� ��¥ ���� Ȯ�� ��
		JLabel lbl_a_movieDate = new JLabel(movieDate);
		lbl_a_movieDate.setFont(new Font("���� ���", Font.PLAIN, 12));
		lbl_a_movieDate.setBounds(149, 184, 185, 15);
		ReserveCheckPanel.add(lbl_a_movieDate);
		
		//���� ȸ������ Ȯ�� ��
		JLabel lbl_a_movieRound = new JLabel(Integer.toString(movieRound));
		lbl_a_movieRound.setFont(new Font("���� ���", Font.PLAIN, 12));
		lbl_a_movieRound.setBounds(149, 264, 185, 15);
		ReserveCheckPanel.add(lbl_a_movieRound);
		
		//���� �¼� ���� Ȯ�� ��
		JLabel lbl_a_movieSeat = new JLabel(movieSeat + " \uBC88");
		lbl_a_movieSeat.setFont(new Font("�������", Font.PLAIN, 12));
		lbl_a_movieSeat.setBounds(149, 304, 185, 15);
		ReserveCheckPanel.add(lbl_a_movieSeat);
		
		//���� �Ͻðڽ��ϱ� ��
		JLabel lbl_Q = new JLabel("\uC608\uB9E4 \uD558\uC2DC\uACA0\uC2B5\uB2C8\uAE4C?");
		lbl_Q.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_Q.setFont(new Font("���� ���", Font.BOLD, 18));
		lbl_Q.setBounds(0, 385, 487, 34);
		ReserveCheckPanel.add(lbl_Q);
		
		//��ư no
		JButton btn_no = new JButton("\uC544\uB2C8\uC694");
		btn_no.setBackground(Color.LIGHT_GRAY);
		btn_no.setFont(new Font("���� ���", Font.PLAIN, 12));
		btn_no.setBounds(245, 450, 108, 40);
		ReserveCheckPanel.add(btn_no);
		btn_no.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ���� Ȯ�� No ��ư �̺�Ʈ
				// 1. ���� Ȯ�� ������ �ݱ�
				dispose();
			}
		});
		
		//��ư yes
		JButton btn_yes = new JButton("\uC608");
		btn_yes.setBackground(Color.LIGHT_GRAY);
		btn_yes.setFont(new Font("���� ���", Font.PLAIN, 12));
		btn_yes.setBounds(122, 450, 108, 40);
		ReserveCheckPanel.add(btn_yes);
		btn_yes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ���� Ȯ�� yes ��ư �̺�Ʈ
				//1. yes ��ư�� ������ 
				//2. �Է��� �����ͷ� reservInfo(���� ����) ��ü�� �����.
				String userID = Theater.personNow.getId();
				ReservInfo reservInfo = new ReservInfo(userID, movieName, movieDate, movieRound, Integer.parseInt(movieSeat));
				
				//3. ���� reservInfo ��ü�� �Ű������� screen�� ���� �Լ��� ����Ѵ�.
				try {
					MainFrame.screen.booking(reservInfo);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				// 4. ���� �� �˸� �޽����� �߻���Ų��.
				JOptionPane.showMessageDialog(null, "���ŵǾ����ϴ�.", "NOTICE", JOptionPane.INFORMATION_MESSAGE);
				
				// 5. ���� Ȯ�� frame�� �ݴ´�.
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

//������ ������: ������ ũ���� �������� panel�� ������ ����ϱ� ������ panel ��ü �Լ��� ����� ����Ͽ���
class ManagerFrame extends JFrame {

	public ManagerMainPanel managerMain = new ManagerMainPanel(); // manager ���� panel
	public ClientManagementPanel clientManagement = new ClientManagementPanel(); // ȸ�� ���� panel
	public MovieManagementPanel movieManagement = new MovieManagementPanel(); // ��ȭ ���� panel
	public MovieTablePanel movieTable = new MovieTablePanel(); // �󿵽ð�ǥ ���� panel

	public ManagerFrame() {
		// 1. ManagerMainpanel ����
		managerMain = new ManagerMainPanel();
		// 2. ClientMainPanel �߰�
		add(managerMain);

		// ����� ������ ����
		setTitle("Theater Program"); // ������ title ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 521, 677); // ������ ũ�� ����
		setBackground(Color.WHITE); // ������ ��� ����
		setLayout(null); // ������ ���̾ƿ� ����
		setVisible(true);
	}

	// panel �ٲٴ� �Լ�
	public void PanelChange(String panelName) {
		if (panelName.equals("ManagerMainPanel")) {
			// ���� �ٲ� panel�� ManagerMainPanel�̶��
			getContentPane().removeAll();
			getContentPane().add(managerMain);
			revalidate();
			repaint();
		}
		if (panelName.equals("ClientManagementPanel")) {
			// ���� �ٲ� panel�� ClientManagementPanel�̶��
			getContentPane().removeAll();
			getContentPane().add(clientManagement);
			revalidate();
			repaint();
		} else if (panelName.equals("MovieManagementPanel")) {
			// ���� �ٲ� panel�� MovieManagementPanel�̶��
			getContentPane().removeAll();
			getContentPane().add(movieManagement);
			revalidate();
			repaint();
		} else if (panelName.equals("MovieTablePanel")) {
			// ���� �ٲ� panel�� MovieTablePanel�̶��
			getContentPane().removeAll();
			getContentPane().add(movieTable);
			revalidate();
			repaint();
		}
	}

	class ManagerMainPanel extends JPanel {

		//���� ���� ȸ�� �̸�
		String userName = Theater.personNow.getName();

		int numberOfMember;
		int screeningMovie;

		String TicketPath = "TICKET.png";

		public ManagerMainPanel() {
			
			try {
				//ȸ�� ��, ��ȭ �� ��������
				numberOfMember = MainFrame.theater.countPerson();
				screeningMovie = MainFrame.theater.countMovie();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			setBounds(0, 0, 521, 677);
			setBackground(Color.WHITE);
			setLayout(null);

			// Ƽ���̹���(����ȭ��)��
			JLabel Ticket_main = new JLabel("Ƽ��_����ȭ��");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Ƽ�� �̹��� Ŭ�� ������
					// 1. ManagerMain panel�� �����Ѵ�.
					PanelChange("ManagerMainPanel");
				}
			});

			// �α����� ��� ��
			JLabel lblUserName = new JLabel(userName + "\uB2D8  /");
			lblUserName.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblUserName.setBounds(375, 83, 65, 15);
			add(lblUserName);

			// �α׾ƿ� ��
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblLogout.setBounds(440, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �α׾ƿ� �� Ŭ�� ������
					// 1. �α׾ƿ� �˸� �޽��� �߻�
					//2. �α׾ƿ��� �Ѵ�.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)�� �����Ѵ�.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame�� �����Ѵ�.
					dispose();
				}
			});

			// ȸ������ ��
			JLabel lblMemberManage = new JLabel("\uD68C\uC6D0\uAD00\uB9AC");
			lblMemberManage.setHorizontalAlignment(SwingConstants.CENTER);
			lblMemberManage.setFont(new Font("���� ���", Font.BOLD, 20));
			lblMemberManage.setBounds(180, 170, 150, 30);
			add(lblMemberManage);

			// �� ȸ���� ��
			JLabel lblNumberOfMember = new JLabel("\uCD1D \uD68C\uC6D0 \uC218: ");
			lblNumberOfMember.setHorizontalAlignment(SwingConstants.LEFT);
			lblNumberOfMember.setFont(new Font("���� ���", Font.PLAIN, 15));
			lblNumberOfMember.setBounds(195, 230, 88, 30);
			add(lblNumberOfMember);

			// ȸ������ ���� ��ư
			JButton btnMemberManager = new JButton("\uD68C\uC6D0 \uC815\uBCF4 \uAD00\uB9AC");
			btnMemberManager.setFont(new Font("���� ���", Font.PLAIN, 12));
			btnMemberManager.setBackground(Color.LIGHT_GRAY);
			btnMemberManager.setBounds(180, 280, 150, 30);
			add(btnMemberManager);
			btnMemberManager.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ȸ������ ���� ��ư �̺�Ʈ
					PanelChange("ClientManagementPanel");
				}
			});

			// ��ȭ���� ��
			JLabel lblMovieManage = new JLabel("\uC601\uD654\uAD00\uB9AC");
			lblMovieManage.setHorizontalAlignment(SwingConstants.CENTER);
			lblMovieManage.setFont(new Font("���� ���", Font.BOLD, 20));
			lblMovieManage.setBounds(28, 390, 202, 30);
			add(lblMovieManage);

			// ���� ��ȭ ��
			JLabel lblScreeningMovie = new JLabel("\uCD1D \uC601\uD654 \uC218:");
			lblScreeningMovie.setHorizontalAlignment(SwingConstants.LEFT);
			lblScreeningMovie.setFont(new Font("���� ���", Font.PLAIN, 15));
			lblScreeningMovie.setBounds(81, 442, 97, 38);
			add(lblScreeningMovie);

			// ��ȭ���� ��ư
			JButton btnMovieManager = new JButton("\uC601\uD654 \uAD00\uB9AC");
			btnMovieManager.setFont(new Font("���� ���", Font.PLAIN, 12));
			btnMovieManager.setBackground(Color.LIGHT_GRAY);
			btnMovieManager.setBounds(61, 500, 150, 30);
			add(btnMovieManager);
			btnMovieManager.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ��ȭ ���� ��ư �̺�Ʈ
					PanelChange("MovieManagementPanel");
				}
			});

			// �� ȸ���� ��� ��
			JLabel Memberlabel = new JLabel(numberOfMember + " ��");
			Memberlabel.setBounds(281, 233, 77, 26);
			add(Memberlabel);

			// ���� ��ȭ ��� ��
			JLabel MovieLabel = new JLabel(screeningMovie + " ��");
			MovieLabel.setBounds(166, 450, 55, 26);
			add(MovieLabel);

			// �� �ð�ǥ ��
			JLabel screenlabel = new JLabel("\uC0C1\uC601\uC2DC\uAC04\uD45C \uAD00\uB9AC");
			screenlabel.setHorizontalAlignment(SwingConstants.CENTER);
			screenlabel.setFont(new Font("���� ���", Font.BOLD, 20));
			screenlabel.setBounds(277, 390, 218, 30);
			add(screenlabel);

			// �� �ð�ǥ ��ư
			JButton screenbutton = new JButton("\uC0C1\uC601\uC2DC\uAC04\uD45C \uAD00\uB9AC");
			screenbutton.setFont(new Font("���� ���", Font.PLAIN, 12));
			screenbutton.setBackground(Color.LIGHT_GRAY);
			screenbutton.setBounds(305, 500, 150, 30);
			add(screenbutton);
			screenbutton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// �󿵽ð�ǥ ��ư �̺�Ʈ
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
			

			// Ƽ���̹���(����ȭ��)��
			JLabel Ticket_main = new JLabel("Ƽ��_����ȭ��");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Ƽ�� �̹��� Ŭ�� ������
					// 1. ManagerMain panel�� �����Ѵ�.
					PanelChange("ManagerMainPanel");
				}
			});

			// �α����� ��� ��
			JLabel lblUserName = new JLabel(userName + "\uB2D8  /");
			lblUserName.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblUserName.setBounds(375, 83, 65, 15);
			add(lblUserName);

			// �α׾ƿ� ��
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblLogout.setBounds(440, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �α׾ƿ� �� Ŭ�� ������
					// 1. �α׾ƿ� �˸� �޽��� �߻�
					//2. �α׾ƿ��� �Ѵ�.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)�� �����Ѵ�.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame�� �����Ѵ�.
					dispose();
				}
			});

			// ����ȭ�� ��
			JLabel lbl_LastPanel = new JLabel("\u25C0 \uC774\uC804 \uD654\uBA74");
			lbl_LastPanel.setFont(new Font("���� ���", Font.PLAIN, 12));
			lbl_LastPanel.setBounds(25, 120, 74, 15);
			add(lbl_LastPanel);
			lbl_LastPanel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ����ȭ�� �� Ŭ�� ������
					// 1. ManagerMain panel�� �����Ѵ�.
					PanelChange("ManagerMainPanel");
				}
			});

			// ����������(Ÿ��Ʋ) ��
			JLabel lbl_title = new JLabel("\uACE0\uAC1D\uC815\uBCF4 \uAD00\uB9AC");
			lbl_title.setFont(new Font("���� ���", Font.BOLD, 20));
			lbl_title.setBounds(29, 168, 250, 30);
			add(lbl_title);

			// �� �˻� ��
			JLabel lbl_search = new JLabel("\uACE0\uAC1D \uAC80\uC0C9");
			lbl_search.setFont(new Font("���� ���", Font.PLAIN, 13));
			lbl_search.setBounds(30, 219, 69, 19);
			add(lbl_search);

			// �˻� �ؽ�Ʈ�ʵ�
			JTextField tf_find = new JTextField();
			tf_find.setBounds(103, 220, 149, 21);
			add(tf_find);
			tf_find.setColumns(10);

			// �˻� ��ư
			JButton btn_search = new JButton("\uAC80\uC0C9");
			btn_search.setBackground(Color.LIGHT_GRAY);
			btn_search.setFont(new Font("���� ���", Font.PLAIN, 12));
			btn_search.setBounds(264, 220, 69, 23);
			add(btn_search);
			// ��ư ������: �� ������ �˻��Ѵ�
			btn_search.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// ���̺� ����
					String[] colName = { "\uC774\uB984", "\uC804\uD654\uBC88\uD638", "\uC0DD\uB144\uC6D4\uC77C",
							"\uC544\uC774\uB514", "\uBE44\uBC00\uBC88\uD638" };
					DefaultTableModel model = new DefaultTableModel(colName, 0);
					JTable table = new JTable(model);

					table.setBackground(Color.WHITE);
					table.setFillsViewportHeight(true);
					table.setForeground(Color.DARK_GRAY);

					// �ؽ�Ʈ �ʵ� ��: ���̵�
					String id = tf_find.getText();
					try {
						Person p = MainFrame.theater.searchPerson(id);
						if (p == null) {
							JOptionPane.showMessageDialog(null, "�������� �ʴ� ���Դϴ�");
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


			// ���ΰ�ħ ��ư
			JButton btn_reload = new JButton("\uC0C8\uB85C\uACE0\uCE68");
			btn_reload.setBackground(Color.LIGHT_GRAY);
			btn_reload.setFont(new Font("���� ���", Font.PLAIN, 12));
			btn_reload.setBounds(394, 550, 91, 23);
			add(btn_reload);
			// ��ư ������: �ٽ� ��ü ���� �ҷ��´�
			btn_reload.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// ���̺� ����
					String[] colName = { "\uC774\uB984", "\uC804\uD654\uBC88\uD638", "\uC0DD\uB144\uC6D4\uC77C",
							"\uC544\uC774\uB514", "\uBE44\uBC00\uBC88\uD638" };
					DefaultTableModel model = new DefaultTableModel(colName, 0);
					JTable table = new JTable(model);

					table.setBackground(Color.WHITE);
					table.setFillsViewportHeight(true);
					table.setForeground(Color.DARK_GRAY);

					// ���̺� �𵨷� �߰�
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

			// ���̺� Ŭ�� ������
			table.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					selectedRow = table.getSelectedRow();
				}
			});

			// ���̺� �𵨷� �߰�
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

			// ������ư
			JButton btn_delete = new JButton("\uC0AD\uC81C");
			btn_delete.setBackground(Color.LIGHT_GRAY);
			btn_delete.setFont(new Font("���� ���", Font.PLAIN, 12));
			btn_delete.setBounds(292, 550, 91, 23);
			add(btn_delete);
			// ���� ������
			btn_delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (selectedRow == -1) {
						JOptionPane.showMessageDialog(null, "���õ� ȸ���� �����ϴ�");
					} else {
						int result = JOptionPane.showConfirmDialog(null, "������ ȸ�� ������ �����˴ϴ�." + '\n' + "����Ͻðڽ��ϱ�?", "Confirm",
								JOptionPane.YES_NO_OPTION);
						if (result == JOptionPane.YES_OPTION) {
							// ����ڰ� ���� ������ ���
							// ������ �� ����
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
			
			// Ƽ���̹���(����ȭ��)��
			JLabel Ticket_main = new JLabel("Ƽ��_����ȭ��");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Ƽ�� �̹��� Ŭ�� ������
					// 1. ManagerMain panel�� �����Ѵ�.
					PanelChange("ManagerMainPanel");
				}
			});

			// �α����� ��� ��
			JLabel lblUserName = new JLabel(userName + "\uB2D8  /");
			lblUserName.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblUserName.setBounds(375, 83, 65, 15);
			add(lblUserName);

			// �α׾ƿ� ��
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblLogout.setBounds(440, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �α׾ƿ� �� Ŭ�� ������
					// 1. �α׾ƿ� �˸� �޽��� �߻�
					//2. �α׾ƿ��� �Ѵ�.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)�� �����Ѵ�.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame�� �����Ѵ�.
					dispose();
				}
			});

			// ����ȭ�� ��
			JLabel lbl_LastPanel = new JLabel("\u25C0 \uC774\uC804 \uD654\uBA74");
			lbl_LastPanel.setFont(new Font("���� ���", Font.PLAIN, 12));
			lbl_LastPanel.setBounds(25, 120, 74, 15);
			add(lbl_LastPanel);
			lbl_LastPanel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ����ȭ�� �� Ŭ�� ������
					// 1. ManagerMain panel�� �����Ѵ�.
					PanelChange("ManagerMainPanel");
				}
			});
			
			// ��ũ�� + ���̺�
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

				model.setNumRows(0); // �ʱ�ȭ

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

			// ���� ��ϵ� ��ȭ (Ÿ��Ʋ ��)
			JLabel lblMovieList = new JLabel("\uC601\uD654 \uAD00\uB9AC");
			lblMovieList.setFont(new Font("���� ���", Font.BOLD, 20));
			lblMovieList.setHorizontalAlignment(SwingConstants.LEFT);
			lblMovieList.setBounds(20, 150, 230, 30);
			add(lblMovieList);

			// �˻� �ؽ�Ʈ�ʵ�
			JTextField tf_search = new JTextField();
			tf_search.setBounds(20, 195, 165, 25);
			add(tf_search);
			tf_search.setColumns(10);

			// �˻� ��ư
			JButton btn_search = new JButton("\uAC80\uC0C9");
			btn_search.setFont(new Font("���� ���", Font.PLAIN, 12));
			btn_search.setBackground(Color.LIGHT_GRAY);
			btn_search.setBounds(192, 195, 60, 25);
			add(btn_search);
			btn_search.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String title = tf_search.getText();
					if (title.equals("")) {
						JOptionPane.showMessageDialog(null, "�˻��� ��ȭ ������ �ۼ����ּ���.", "Message",
								JOptionPane.WARNING_MESSAGE);
					} else {
						try {
							//1. ���̺��� �ʱ�ȭ�Ѵ�.
							model.setNumRows(0);
							
							//2. ��ȭ ������ �˻��Ѵ�.
							ResultSet rs = MainFrame.theater.searchMovie(title);
							
							//3. ��ȭ ������ ��ġ�ϴ� ������ ����Ѵ�.
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
							
							//4. �˻� �ؽ�Ʈ�ʵ带 �����.
							tf_search.setText("");

						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}

			});			
			
			// ���� ��
			JLabel lblTitle = new JLabel("\uC81C\uBAA9");
			lblTitle.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
			lblTitle.setBounds(35, 460, 65, 20);
			add(lblTitle);

			// �󿵽ð� ��
			JLabel lblRunningTime = new JLabel("\uC0C1\uC601\uC2DC\uAC04");
			lblRunningTime.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblRunningTime.setHorizontalAlignment(SwingConstants.LEFT);
			lblRunningTime.setBounds(25, 526, 65, 20);
			add(lblRunningTime);

			// ������ URL ��
			JLabel posterURL_label = new JLabel(
					"<html><body><center>\uD3EC\uC2A4\uD130<br> \uACBD\uB85C</center></br></body></html>");
			posterURL_label.setHorizontalAlignment(SwingConstants.LEFT);
			posterURL_label.setFont(new Font("���� ���", Font.PLAIN, 12));
			posterURL_label.setBounds(264, 460, 65, 30);
			add(posterURL_label);

			// ������¥ ��
			JLabel dday_label = new JLabel("\uAC1C\uBD09 \uB0A0\uC9DC");
			dday_label.setHorizontalAlignment(SwingConstants.LEFT);
			dday_label.setFont(new Font("���� ���", Font.PLAIN, 12));
			dday_label.setBounds(260, 524, 65, 20);
			add(dday_label);

			// ���� �ؽ�Ʈ�ʵ�
			JTextField tfTitle = new JTextField();
			tfTitle.setBounds(78, 461, 154, 23);
			add(tfTitle);
			tfTitle.setColumns(10);

			// �󿵽ð� �ؽ�Ʈ�ʵ�
			JTextField screentime_tf = new JTextField();
			screentime_tf.setColumns(10);
			screentime_tf.setBounds(78, 525, 154, 23);
			add(screentime_tf);

			// ������URL �ؽ�Ʈ�ʵ�
			JTextField posterURL_tf = new JTextField();
			posterURL_tf.setColumns(10);
			posterURL_tf.setBounds(317, 462, 154, 23);
			add(posterURL_tf);

			// ������¥ �ؽ�Ʈ�ʵ�
			JTextField dday_tf = new JTextField();
			dday_tf.setColumns(10);
			dday_tf.setBounds(317, 521, 154, 23);
			add(dday_tf);

			// ������ư
			JButton btnDelete = new JButton("\uC0AD\uC81C");
			btnDelete.setFont(new Font("���� ���", Font.PLAIN, 12));
			btnDelete.setBackground(Color.LIGHT_GRAY);
			btnDelete.setBounds(210, 595, 85, 23);
			add(btnDelete);
			btnDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//���콺�� ������ �� ��ȣ���� title ���������
					int rowIndex = table.getSelectedRow();
					String title = (String) table.getValueAt(rowIndex, 0);

					if (table.getSelectedRow() == -1) {
						JOptionPane.showMessageDialog(null, "������ ������ ���õ��� �ʾҽ��ϴ�.", "Message", JOptionPane.WARNING_MESSAGE);
						return;
					} else {
						int result = JOptionPane.showConfirmDialog(null, "������ �����Ͻðڽ��ϱ�?", "Confirm",
								JOptionPane.YES_NO_OPTION);

						if (result == JOptionPane.CLOSED_OPTION)
							return;

						else if (result == JOptionPane.YES_OPTION) {
							model.removeRow(rowIndex); // �ش� row ����
							try {
								MainFrame.theater.deleteMovie(title);
							} catch (Exception e1) {
								e1.printStackTrace();
							} // ������ �����
						}
					}
				}
			});
			
			// �߰���ư
			JButton btnAdd = new JButton("\uB4F1\uB85D");
			btnAdd.setFont(new Font("���� ���", Font.PLAIN, 12));
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
						JOptionPane.showMessageDialog(null, "�ʼ� ������ �Է����ּ���.", "Message",
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
							
							// �Է��� �ؽ�Ʈ �ʵ� �� ����
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


			// ���ΰ�ħ ��ư
			JButton btn_reload = new JButton("\uC0C8\uB85C\uACE0\uCE68");
			btn_reload.setFont(new Font("���� ���", Font.PLAIN, 12));
			btn_reload.setBackground(Color.LIGHT_GRAY);
			btn_reload.setBounds(350, 595, 85, 23);
			add(btn_reload);
			btn_reload.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						ResultSet rs = MainFrame.theater.movieAll();

						DefaultTableModel model = (DefaultTableModel) table.getModel();
						model.setNumRows(0); // �ʱ�ȭ

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

			// Ƽ���̹���(����ȭ��)��
			JLabel Ticket_main = new JLabel("Ƽ��_����ȭ��");
			Ticket_main.setIcon(new ImageIcon(MainFrame.class.getResource("/theater/TICKET.png")));
			Ticket_main.setHorizontalAlignment(SwingConstants.CENTER);
			Ticket_main.setBounds(190, 10, 126, 80);
			add(Ticket_main);
			Ticket_main.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// Ƽ�� �̹��� Ŭ�� ������
					// 1. ManagerMain panel�� �����Ѵ�.
					PanelChange("ManagerMainPanel");
				}
			});

			// �α����� ��� ��
			JLabel lblUserName = new JLabel(userName + "\uB2D8  /");
			lblUserName.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblUserName.setBounds(375, 83, 65, 15);
			add(lblUserName);

			// �α׾ƿ� ��
			JLabel lblLogout = new JLabel("\uB85C\uADF8\uC544\uC6C3");
			lblLogout.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblLogout.setBounds(440, 83, 50, 15);
			add(lblLogout);
			lblLogout.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// �α׾ƿ� �� Ŭ�� ������
					// 1. �α׾ƿ� �˸� �޽��� �߻�
					//2. �α׾ƿ��� �Ѵ�.
					MainFrame.theater.logOut();
					// 3. MainFrame(Login frame)�� �����Ѵ�.
					MainFrame mainFrame = new MainFrame();
					mainFrame.setVisible(true);
					// 4. Client frame�� �����Ѵ�.
					dispose();
				}
			});

			// ����ȭ�� ��
			JLabel lbl_LastPanel = new JLabel("\u25C0 \uC774\uC804 \uD654\uBA74");
			lbl_LastPanel.setFont(new Font("���� ���", Font.PLAIN, 12));
			lbl_LastPanel.setBounds(25, 120, 74, 15);
			add(lbl_LastPanel);
			lbl_LastPanel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// ����ȭ�� �� Ŭ�� ������
					// 1. ManagerMain panel�� �����Ѵ�.
					PanelChange("ManagerMainPanel");
				}
			});

			// �� �ð�ǥ ���� ��
			JLabel lblMovieList = new JLabel("\uC0C1\uC601 \uC2DC\uAC04\uD45C \uAD00\uB9AC");
			lblMovieList.setFont(new Font("���� ���", Font.BOLD, 20));
			lblMovieList.setHorizontalAlignment(SwingConstants.LEFT);
			lblMovieList.setBounds(29, 168, 250, 30);
			add(lblMovieList);

			// ��ȭ ��
			JLabel lblTitle = new JLabel("\uC601\uD654");
			lblTitle.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
			lblTitle.setBounds(28, 450, 65, 20);
			add(lblTitle);

			// �󿵽ð� ��
			JLabel lblRunningTime = new JLabel("\uC2DC\uC791\uC2DC\uAC04");
			lblRunningTime.setFont(new Font("���� ���", Font.PLAIN, 12));
			lblRunningTime.setHorizontalAlignment(SwingConstants.LEFT);
			lblRunningTime.setBounds(265, 462, 65, 20);
			add(lblRunningTime);

			// �󿵳�¥ ��
			JLabel date_label = new JLabel("\uC0C1\uC601\uB0A0\uC9DC");
			date_label.setHorizontalAlignment(SwingConstants.LEFT);
			date_label.setFont(new Font("���� ���", Font.PLAIN, 12));
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
			
			//���⼭���� �ѹ� �߰�
			try {
				ResultSet rs = MainFrame.screen.movieNowAll();
				model.setNumRows(0); // �ʱ�ȭ

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
			

			// ��ȭ comboBox
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

			// ���۽ð�(��) ��
			JLabel lbl_hour = new JLabel("\uC2DC");
			lbl_hour.setHorizontalAlignment(SwingConstants.LEFT);
			lbl_hour.setFont(new Font("���� ���", Font.PLAIN, 12));
			lbl_hour.setBounds(388, 463, 20, 20);
			add(lbl_hour);

			// ���۽ð�(��) ��
			JLabel lbl_minute = new JLabel("\uBD84");
			lbl_minute.setHorizontalAlignment(SwingConstants.LEFT);
			lbl_minute.setFont(new Font("���� ���", Font.PLAIN, 12));
			lbl_minute.setBounds(473, 463, 20, 20);
			add(lbl_minute);

			// �ð��޺��ڽ�
			JComboBox comboBox_hour = new JComboBox();
			comboBox_hour.setModel(new DefaultComboBoxModel(new String[] { "08", "09", "10", "11", "12", "13", "14", "15",
					"16", "17", "18", "19", "20", "21", "22" }));
			comboBox_hour.setBounds(326, 463, 57, 23);
			add(comboBox_hour);

			// ��¥ �ؽ�Ʈ�ʵ�
			JTextField date_textField = new JTextField();
			date_textField.setBounds(78, 493, 154, 23);
			add(date_textField);
			date_textField.setColumns(10);

			// ȸ������ ��
			JLabel round_label = new JLabel("\uD68C\uCC28\uC815\uBCF4");
			round_label.setHorizontalAlignment(SwingConstants.LEFT);
			round_label.setFont(new Font("���� ���", Font.PLAIN, 12));
			round_label.setBounds(25, 540, 65, 20);
			add(round_label);

			// ȸ������ �޺��ڽ�
			JComboBox round_comboBox = new JComboBox();
			round_comboBox.setModel(new DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
			round_comboBox.setBounds(78, 536, 154, 23);
			add(round_comboBox);

			// �����ϴ� �� �޺��ڽ�
			JComboBox StartM_comboBox = new JComboBox();
			StartM_comboBox.setModel(new DefaultComboBoxModel(new String[] { "00", "15", "30", "45" }));
			StartM_comboBox.setBounds(410, 463, 57, 23);
			add(StartM_comboBox);

			// ������ �ð� ��
			JLabel finsith_label = new JLabel("\uB05D\uC2DC\uAC04");
			finsith_label.setHorizontalAlignment(SwingConstants.LEFT);
			finsith_label.setFont(new Font("���� ���", Font.PLAIN, 12));
			finsith_label.setBounds(272, 511, 65, 20);
			add(finsith_label);

			// ������ �ð� �޺��ڽ�
			JComboBox finishHour_comboBox = new JComboBox();
			finishHour_comboBox.setModel(new DefaultComboBoxModel(new String[] { "08", "09", "10", "11", "12", "13", "14",
					"15", "16", "17", "18", "19", "20", "21", "22" }));
			finishHour_comboBox.setBounds(326, 512, 57, 23);
			add(finishHour_comboBox);

			// ������ �ð� ��
			JLabel finishHour_label = new JLabel("\uC2DC");
			finishHour_label.setHorizontalAlignment(SwingConstants.LEFT);
			finishHour_label.setFont(new Font("���� ���", Font.PLAIN, 12));
			finishHour_label.setBounds(388, 512, 20, 20);
			add(finishHour_label);

			// ������ �� �޺��ڽ�
			JComboBox finishM_comboBox = new JComboBox();
			finishM_comboBox.setModel(new DefaultComboBoxModel(new String[] { "00", "15", "30", "45" }));
			finishM_comboBox.setBounds(410, 512, 57, 23);
			add(finishM_comboBox);

			// ������ �� ��
			JLabel finishM_label = new JLabel("\uBD84");
			finishM_label.setHorizontalAlignment(SwingConstants.LEFT);
			finishM_label.setFont(new Font("���� ���", Font.PLAIN, 12));
			finishM_label.setBounds(473, 512, 20, 20);
			add(finishM_label);

			// �߰���ư
			JButton btn_add = new JButton("\uCD94\uAC00");
			btn_add.setFont(new Font("���� ���", Font.PLAIN, 12));
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
						JOptionPane.showMessageDialog(null, "�����߻�.", "Message", JOptionPane.WARNING_MESSAGE);
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

			// ���� ��ư
			JButton btn_delete = new JButton("\uC0AD\uC81C");
			btn_delete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// ���콺�� ������ �� ��ȣ���� title ���������
					int rowIndex = table.getSelectedRow();

					String title = (String) table.getValueAt(rowIndex, 0);
					String date = (String) table.getValueAt(rowIndex, 1);
					int round = Integer.parseInt(String.valueOf(table.getValueAt(rowIndex, 2)));
					// int round = (int) table.getValueAt(rowIndex, 2);
					String start = (String) table.getValueAt(rowIndex, 3);
					String finish = (String) table.getValueAt(rowIndex, 4);

					MovieNow movieNow = new MovieNow(title, date, round, null, null);

					if (table.getSelectedRow() == -1) {
						JOptionPane.showMessageDialog(null, "������ ������ ���õ��� �ʾҽ��ϴ�.", "Message", JOptionPane.WARNING_MESSAGE);
						return;
					} else {
						int result = JOptionPane.showConfirmDialog(null, "������ �����Ͻðڽ��ϱ�?", "Confirm",
								JOptionPane.YES_NO_OPTION);

						if (result == JOptionPane.CLOSED_OPTION)
							return;

						else if (result == JOptionPane.YES_OPTION) {
							model.removeRow(rowIndex); // �ش� row ����
							try {
								MainFrame.screen.deletePlayingMovie(movieNow);
							} catch (Exception e1) {
								e1.printStackTrace();
							} // ������ �����
						}
					}
				}
			});
			btn_delete.setFont(new Font("���� ���", Font.PLAIN, 12));
			btn_delete.setBackground(Color.LIGHT_GRAY);
			btn_delete.setBounds(210, 595, 85, 23);
			add(btn_delete);

			// ���ΰ�ħ ��ư
			JButton btn_reload = new JButton("\uC0C8\uB85C\uACE0\uCE68");
			btn_reload.setFont(new Font("���� ���", Font.PLAIN, 12));
			btn_reload.setBackground(Color.LIGHT_GRAY);
			btn_reload.setBounds(350, 595, 85, 23);
			add(btn_reload);
			
			btn_reload.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						ResultSet rs = MainFrame.screen.movieNowAll();

						//DefaultTableModel model = (DefaultTableModel) table.getModel();
						model.setNumRows(0); // �ʱ�ȭ

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