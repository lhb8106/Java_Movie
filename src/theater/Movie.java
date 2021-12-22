package theater;

public class Movie {//데이터2: 영화 정보 클래스
	private String title; //영화 제목
	private int runTime; //러닝타임
	private String posterURL; //포스터 경로
	private String dday; //영화 개봉 날짜
	
	Movie() {
		title = null;
		runTime = 0;
		posterURL = null;
		dday = null;
	}
	
	Movie(String title, int runTime, String posterURL, String dday) {
		this.title = title;
		this.runTime = runTime;
		this.posterURL = posterURL;
		this.dday = dday;
	}
	
	//각각의 데이터에 대한 set함수
	void setTitle(String title) {
		this.title = title;
	}
	
	void setRunTime(int runTime) {
		this.runTime = runTime;
	}
	
	void setPosterURL(String posterURL) {
		this.posterURL = posterURL;
	}
	
	void setDDay(String dday) {
		this.dday = dday;
	}
	
	//각각의 데이터에 대한 get함수
	String getTitle() {
		return title;
	}
	
	int getRunTime() {
		return runTime;
	}
	
	String getPosterURL() {
		return posterURL;
	}
	
	String getDDay() {
		return dday.toString();
	}
}
