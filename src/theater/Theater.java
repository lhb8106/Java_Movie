package theater;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

public class Theater {// 영화관 클래스

	static Person personNow = null; // 현재 로그인한 사용자
	ArrayList<Screen> screen = new ArrayList<Screen>(9);// 상영관 정보

	Connection con = null; // 드라이버와 자바를 연결하는 객체
	Statement st = null; // sql query 전송 객체(자바와 DB가 연결되어서 쿼리로 통신 가능)
	PreparedStatement ps = null;
	ResultSet rs = null;

	Theater(Connection con) throws Exception {
		this.con = con;
		st = con.createStatement();
	}

	// 회원 관리 메소드
	// 회원가입
	void signUp(Person person) throws Exception {
		// 1. GUI에서 추가할 person 객체를 받아온다.
		// 2. 받아온 Person 객체를 DB에 추가한다.
		String sql = "INSERT INTO person VALUES (?, ?, ?, ?, ?)";
		ps = con.prepareStatement(sql);

		ps.setString(1, person.getName());
		ps.setString(2, person.getPhone());
		ps.setString(3, person.getBirth());
		ps.setString(4, person.getId());
		ps.setString(5, person.getPw());

		ps.executeUpdate();
	}

	// 회원 탈퇴
	void withdrawal(String id) throws Exception {
		// 1. GUI에서 삭제할 회원 id를 받아온다.
		// 2. 받아온 id를 DB에서 삭제한다.
		String sql = "DELETE FROM person WHERE id = ?";
		ps = con.prepareStatement(sql);
		ps.setString(1, id);

		ps.executeUpdate();
	}

	// 고객 정보 수정
	void modifyPerson(Person person) throws Exception {
		// 1. GUI에서 수정할 person 객체를 받아온다.
		// 2. DB에서 받아온 person의 id의 위치에 새로운 Person 객체를 저장한다.
		String sql = "UPDATE person SET name=?, phone=?, birth=?, pw=? WHERE id = ?";
		ps = con.prepareStatement(sql);
		ps.setString(1, person.getName());
		ps.setString(2, person.getPhone());
		ps.setString(3, person.getBirth());
		ps.setString(4, person.getPw());
		ps.setString(5, person.getId());

		ps.executeUpdate();
	}

	// 아이디 중복 검사
	public boolean isExistId(String id) throws Exception {
		// 1. 검사할 id를 받아온다.
		// 2. 받아온 id를 DB에서 검색한다.
		String sql = "SELECT * FROM person WHERE id = ?";
		ps = con.prepareStatement(sql);
		ps.setString(1, id);

		rs = ps.executeQuery();

		// 3. 일치하는 아이디가 있다면 true를 반환한다.
		if (rs.next())
			return true;

		// 4. 일치하는 아이디가 없다면 false를 반환한다.
		return false;
	}

	// 로그인
	boolean logIn(String id, String pw) throws Exception {
		// 1. 사용자가 입력한 로그인 정보를 가져온다
		// 2. searchPerson 실행
		Person p = new Person();
		p = searchPerson(id);

		// 3. 검색한 데이터의 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 확인한다
		if (pw.equals(p.getPw())) {
			// 4. 일치하면 현재 로그인한 사용자에 객체 할당
			personNow = p;
			// 5. true 리턴(로그인 성공)
			return true;
		} else {
			// 6. 일치하지 않으면 false 리턴(로그인 실패)
			return false;
		}
	}

	// 로그아웃
	void logOut() {
		// 1. personNow를 null로 변경
		personNow = null;
	}

	// 회원 조회
	Person searchPerson(String id) throws Exception {
		Person p = null;

		// 1. GUI에서 검색할 id를 받아온다.
		// 2. 받아온 id를 DB의 Person 테이블에서 검색한다.
		String sql = "SELECT * FROM person WHERE id=?";
		ps = con.prepareStatement(sql);
		ps.setString(1, id);

		rs = ps.executeQuery();

		// 3. 일치하는 사용자를 찾으면
		if (rs.next()) {
			// 4. person 객체를 만들어 반환한다
			String name = rs.getString("name");
			String phone = rs.getString("phone");
			String birth = rs.getString("birth");
			String userId = rs.getString("id");
			String pw = rs.getString("pw");

			p = new Person(name, phone, birth, userId, pw);
		}

		// 5. 일치하는 사용자를 찾지 못하면 null을 반환한다.
		return p;
	}

	// 전체 회원 조회
	ResultSet personAll() throws Exception {
		// 1. Person 테이블의 전체 데이터를 검색한다.
		String sql = "SELECT * FROM person;";
		st = con.createStatement();
		rs = st.executeQuery(sql);

		// 2. ResultSet을 반환한다.
		return rs;
	}

	// 총 고객수 count
	int countPerson() throws Exception {
		// 1. 전체 고객 수를 저장하는 변수를 생성한다.
		int result = 0;

		// 2. DB의 Person 테이블의 데이터 개수를 검색한다.
		String sql = "SELECT COUNT(*) FROM person";
		rs = st.executeQuery(sql);

		// 3. 데이터가 존재하면 카운트를 저장한다.
		while (rs.next())
			result = rs.getInt(1);

		// 4. 전체 고객 수를 반환한다.
		return result;
	}

	// 영화 관리 메소드
	// 영화 추가
	void addMovie(Movie movie) throws Exception {
		// 1. GUI에서 movie 객체를 받아온다.
		// 2. 받아온 Movie 객체를 DB에 추가한다.
		String sql = "INSERT INTO movie VALUES (?, ?, ?, ?)";
		ps = con.prepareStatement(sql);
		ps.setString(1, movie.getTitle());
		ps.setInt(2, movie.getRunTime());
		ps.setString(3, movie.getPosterURL());
		ps.setString(4, movie.getDDay());

		ps.executeUpdate();
	}

	// 영화 삭제
	void deleteMovie(String title) throws Exception {
		// 1. GUI에서 삭제할 영화 title을 받아온다.
		// 1. 받아온 title을 DB에 검색하여 삭제한다.
		String sql = "DELETE FROM movie WHERE title = ?";
		ps = con.prepareStatement(sql);
		ps.setString(1, title);

		ps.executeUpdate();
	}

	// 영화 정보 수정
	void modifyMovie(Movie movie, String title_before) throws Exception {
		// 1. GUI에서 movie 객체와 저장할 title을 받아온다.
		// 2. DB에서 받아온 title_before의 위치에 새로운 Movie 객체를 저장한다.
		String sql = "UPDATE movie SET title = ?, runTime = ?, posterURL = ?, dday = ? WHERE title = ?";
		ps = con.prepareStatement(sql);
		ps.setString(1, movie.getTitle());
		ps.setInt(2, movie.getRunTime());
		ps.setString(3, movie.getPosterURL());
		ps.setString(4, movie.getDDay());
		ps.setString(5, title_before);

		ps.executeUpdate();
	}
	
	// 영화 검색
	ResultSet searchMovie(String title) throws Exception {
		// 1. GUI에서 검색할 영화 title을 받아온다.
		// 2. 받아온 title로 DB에 영화가 존재하는지 검색한다.
		String sql = "SELECT * FROM movie WHERE title LIKE '%" + title + "%'";

		rs = st.executeQuery(sql);

		// 3. ResultSet을 반환한다.
		return rs;
	}
	
	//영화 3개 랜덤 추출
	ResultSet selectRandomMovie() throws Exception {
		String sql = "SELECT * FROM movie ORDER BY rand() LIMIT 3";
		
		rs = st.executeQuery(sql);
		return rs;
	}

	// 전체 영화 조회
	ResultSet movieAll() throws Exception {
		// 1. Movie 테이블의 전체 데이터를 검색한다.
		String sql = "SELECT * FROM movie;";

		rs = st.executeQuery(sql);

		// 2. ResultSet을 반환한다.
		return rs;
	}

	// 총 영화수 count
	int countMovie() throws Exception {
		// 1. 전체 영화 수를 저장하는 변수를 생성한다.
		int result = 0;

		// 2. DB의 Movie 테이블의 데이터 개수를 검색한다.
		String sql = "SELECT COUNT(*) FROM Movie";
		rs = st.executeQuery(sql);

		// 3. 데이터가 존재하면 카운트를 저장한다.
		while (rs.next())
			result = rs.getInt(1);

		// 4. 전체 영화 수를 반환한다.
		return result;
	}
}
