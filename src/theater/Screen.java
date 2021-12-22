package theater;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Screen {// 상영관 클래스

	Connection con = null; // 드라이버와 자바를 연결하는 객체
	Statement st = null; // sql query 전송 객체(자바와 DB가 연결되어서 쿼리로 통신 가능)
	PreparedStatement ps = null;
	ResultSet rs = null;

	Screen(Connection con) throws Exception {
		this.con = con;
		st = con.createStatement();
	}

	// 상영중 영화 관리 메소드
	// 상영영화 등록
	void addPlayingMovie(MovieNow movieNow) throws Exception {
		// 1. GUI에서 movieNow 객체를 받아온다
		// 2. 받아온 movieNow 객체를 DB의 상영 영화 테이블에 저장한다
		String sql = "INSERT INTO movienow VALUES (?, ?, ?, ?, ?)";
		ps = con.prepareStatement(sql);

		ps.setString(1, movieNow.getTitle());
		ps.setString(2, movieNow.getDate());
		ps.setInt(3, movieNow.getRound());
		ps.setString(4, movieNow.getStart());
		ps.setString(5, movieNow.getFinish());

		ps.executeUpdate();
	}

	// 상영영화 삭제
	void deletePlayingMovie(MovieNow movieNow) throws Exception {
		// 1. GUI에서 movieNow 객체를 받아온다.
		// 1. 받아온 movieNow 객체와 일치하는 데이터를 DB 상영 영화 테이블에서 삭제한다.
		String sql = "DELETE FROM movienow WHERE title = ? AND date = ? AND round = ?";
		ps = con.prepareStatement(sql);
		ps.setString(1, movieNow.getTitle());
		ps.setString(2, movieNow.getDate());
		ps.setInt(3, movieNow.getRound());

		ps.executeUpdate();
	}

	// 상영 영화 정보 수정
	void modifyPlayingMovie(MovieNow movieNow) throws Exception {
		// 1. GUI에서 movieNow 객체를 받아온다.
		// 2. 기존 date와 round 위치에 새로운 movieNow 객체를 저장한다.
		// -> 상영관의 상영 날짜와 회차를 고정하고, 그 곳에서 상영하는 영화를 변경하는 형식
		String sql = "UPDATE movienow SET title = ?, start = ?, finish = ? WHERE date = ? AND round = ?";
		ps = con.prepareStatement(sql);

		ps.setString(1, movieNow.getTitle());
		ps.setString(2, movieNow.getStart());
		ps.setString(3, movieNow.getFinish());
		ps.setString(4, movieNow.getDate());
		ps.setInt(5, movieNow.getRound());

		ps.executeUpdate();
	}

	// 상영중 영화 검색
	// 영화 제목을 검색하면 그 영화의 상영 영화 데이터를 모두 반환함(어느 상영관에서 어느 회차에 상영중인지, 여러개면 여러 개 모두 반환)
	ResultSet searchMovieNow(String movieTitle) throws Exception {
		// 1. GUI에서 영화 title을 받아온다.
		// 2. 받아온 title을 DB에서 검색한다.
		// 3. 검색 결과인 resultSet을 반환한다.
		String sql = "SELECT * FROM movienow WHERE title = ?";
		ps = con.prepareStatement(sql);
		ps.setString(1, movieTitle);

		rs = ps.executeQuery();

		// 2. ResultSet을 반환한다. (한 영화의 모든 상영 정보 출력)
		return rs;
	}

	// 전체 상영 영화 조회
	ResultSet movieNowAll() throws Exception {
		// 1. movienow 테이블의 전체 데이터를 검색한다.
		String sql = "SELECT * FROM movienow;";

		rs = st.executeQuery(sql);

		// 2. ResultSet을 반환한다.
		return rs;
	}

	// 예매 관리 메소드
	// 예매하기(예매 정보 등록)
	// 예매 관리 메소드
	// 예매하기(예매 정보 등록)
	void booking(ReservInfo reservInfo) throws Exception {
		// 1. GUI에서 reservInfo를 받아온다.
		// 2. 받아온 reservInfo를 DB에 추가한다.
		String sql = "INSERT INTO reservinfo VALUES (?, ?, ?,?, ?)";
		ps = con.prepareStatement(sql);

		ps.setString(1, reservInfo.getId());
		ps.setString(2, reservInfo.getTitle());
		ps.setString(3, reservInfo.getDate());
		ps.setInt(4, reservInfo.getRound());
		ps.setInt(5, reservInfo.getSeat());

		ps.executeUpdate();
	}

	// 예매 취소(예매 정보 삭제)
	void cancelReserv(ReservInfo reservInfo) throws Exception {
		// 1. GUI에서 reservInfo를 받아온다.
		// 2. 받아온 reservInfo를 DB에서 검색하여 일치하는 데이터를 삭제한다.
		String sql = "DELETE FROM reservinfo WHERE id = ? AND title = ? AND date = ? AND round = ? AND seat = ?";
		ps = con.prepareStatement(sql);
		ps.setString(1, reservInfo.getId());
		ps.setString(2, reservInfo.getTitle());
		ps.setString(3, reservInfo.getDate());
		ps.setInt(4, reservInfo.getRound());
		ps.setInt(5, reservInfo.getSeat());

		ps.executeUpdate();
	}

	// 예매 정보 출력
	ResultSet searchReservInfo(String reservID) throws Exception {
		// 1. GUI에서 reservID를 받아온다.
		// 2. 받아온 reservID로 예매 정보를 검색한다.
		String sql = "SELECT * FROM ReservInfo WHERE id = ?";
		ps = con.prepareStatement(sql);
		ps.setString(1, reservID);

		rs = ps.executeQuery();

		// 3. ResultSet을 반환한다. (한 ID의 모든 예매 정보 출력)
		return rs;
	}

	// 예매 좌석 반환
	ResultSet returnSeat(String date, int round) throws Exception {
		String sql = "SELECT seat FROM reservinfo WHERE date=? AND round=?";
		ps = con.prepareStatement(sql);
		ps.setString(1, date);
		ps.setInt(2, round);

		rs = ps.executeQuery();
		return rs;
	}
}
