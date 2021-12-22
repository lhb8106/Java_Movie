package theater;

public class Person {//데이터1: 회원가입한 고객 클래스
	private String name; //사용자 이름
	private String phone; //전화번호
	private String birth; //생년월일
	private String id; //아이디
	private String pw; //비밀번호
	
	Person() {
		this.name = null;
		this.phone = null;
		this.birth = null;
		this.id = null;
		this.pw = null;
	}
	
	Person(String name, String phone, String birth, String id, String pw) {
		this.name = name;
		this.phone = phone;
		this.birth = birth;
		this.id = id;
		this.pw = pw;
	}
	
	//각각 데이터에 대한 set 함수
	void setName(String name) {
		this.name = name;
	}
	
	void setPhone(String phone) {
		this.phone = phone;
	}
	
	void setBirth(String birth) {
		this.birth = birth;
	}
	
	void setId(String id) {
		this.id = id;
	}
	
	void setPw(String pw) {
		this.pw = pw;
	}
	
	//각각 데이터에 대한 get 함수
	String getName() {
		return name;
	}
	
	String getPhone() {
		return phone;
	}
	
	String getBirth() {
		return birth;
	}
	
	String getId() {
		return id;
	}
	
	String getPw()
	{
		return pw;
	}

}
