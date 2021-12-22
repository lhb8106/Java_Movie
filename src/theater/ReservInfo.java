package theater;

public class ReservInfo {//데이터4: 예매정보 객체
	private String id; //예매한 고객
	private String title;//제목
	private String date; //예매한 날짜
	private int round; //영화의 회차 정보
	private int seat; //예매한 좌석
	
	ReservInfo()
	{
		this.id = null;
		this.title = null;
		this.date = null;
		this.round = 0;
		this.seat = 0;
	}
	
	ReservInfo(String id, String title, String date, int round, int seat) {
		this.id = id;
		this.title = title;
		this.date = date;
		this.round = round;
		this.seat = seat;
	}
	
	//각각의 데이터에 대한 set 함수
	void setId(String id) {
		this.id = id;
	}
	
	void setTitle(String title) {
		this.title = title;
	}
	
	void setDate(String date) {
		this.date = date;
	}
	
	void setRound(int round) {
		this.round = round;
	}
	
	void setSeat(int seat) {
		this.seat = seat;
	}
	
	//각각의 데이터에 대한 get함수
	String getId() {
		return id;
	}
	
	String getTitle() {
		return title;
	}
	
	String getDate() {
		return date;
	}
	
	int getRound() {
		return round;
	}
	
	int getSeat() {
		return seat;
	}

}
