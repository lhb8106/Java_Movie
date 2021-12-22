package theater;

public class MovieNow {//데이터3: 현재 상영중인 영화
	private String title; //영화 정보
	private String date; //상영 날짜
	private int round; //회차 정보
	private String start; //시작시간 
	private String finish; //끝시간
	
	MovieNow() {
		title = null;
		date = null;
		round = 0;
		start = null;
		finish = null;
	}
	
	MovieNow(String title, String date, int round, String start, String finish) {
		this.title = title;
		this.date = date;
		this.round = round;
		this.start = start;
		this.finish = finish;
	}
	
	//각각의 데이터에 대한 set 함수
	void setTitle(String title) {
		this.title = title;
	}
	
	void setDate(String date) {
		this.date = date;
	}
	
	void setRound(int round) {
		this.round = round;
	}
	
	void setStart(String start) {
		this.start = start;
	}
	
	void setFinish(String finish) {
		this.finish = finish;
	}

	//각각의 데이터에 대한 get함
	String getTitle() {
		return title;
	}
	
	String getDate() {
		return date;
	}
	
	int getRound() {
		return round;
	}
	
	String getStart() {
		return start;
	}
	
	String getFinish() {
		return finish;
	}

}
