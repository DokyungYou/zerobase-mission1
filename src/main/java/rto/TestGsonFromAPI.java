package rto;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TestGsonFromAPI {
		
	
	
	// 하나의 "TbPublicWifiInfo"객체를 받아오는 메소드
		public String stringFromAPI(String start, String end) {
			//url
			String apiKey = "4f71504c6277696c38306c66637577";
			StringBuilder apiURL = new StringBuilder("http://openapi.seoul.go.kr:8088");
			
			BufferedReader br = null;
			StringBuilder content = null;
			
			try {
				
				// 이 5개는 필수적으로 순서바꾸지 않고 호출해야함
				apiURL.append("/" + URLEncoder.encode(apiKey,"UTF-8") ); //인증키
				apiURL.append("/" + URLEncoder.encode("json","UTF-8") ); //요청파일타입 json or xml
				apiURL.append("/" + URLEncoder.encode("TbPublicWifiInfo","UTF-8")); //서비스명
				
				
				apiURL.append("/" + URLEncoder.encode(start,"UTF-8")); //요청시작위치
				apiURL.append("/" + URLEncoder.encode(end,"UTF-8")); //요청마지막위치


					
				
				//정확한 url인지 아닌지 확인하기위해서 url객체이용
				//유효한 url이 들어갔다면 에러가 뜨지 않을 것
				URL url = new URL(apiURL.toString());
				
				HttpURLConnection connection = (HttpURLConnection)url.openConnection(); //해당 객체는 url를 통해서 생성(연결객체를 connection에 넘겨줘야하기때문에 캐스팅해줘야함)
				connection.setRequestMethod("GET");
				//웹에서의 요청방식(연결방식)은 get,post방식이 있는데 get을 선택
				
				connection.setRequestProperty("Content-type", "appplication/json");
				//요청하면 json으로 받겠다라는 의미
				
				//요청이 끝났다면 서버에서 데이터를 내려보내줄 때 정확하게 어떻게 응답을 했는지 받아온다. 정상적인 응답은 200리턴
				int responceCode = connection.getResponseCode();
//				System.out.println("Response code: "+ responceCode); //콘솔출력은 엥간하면 자제하자
				
				 //서버에서 정상적인 응답을 받았다면 서버로부터 데이터를 읽어와야하는데 stream(데이터읽어오는 빨대같은 것) 과의 연결이 필요
				//InputStreamReader: InputStream은 바이트단위 (바이트단위로 쪼갠뒤 다시 조합시에 한글은 깨질 것임) + Reader(문자단위로로 데이터를 읽어서 한글이 깨지지않음)
				//서버로부터 바이트단위로 내려오다가 Reader계열의 stream으로 인해 한글이 정상적으로 인코딩되어 클라이언트로 전달
				//BufferedReader: 데이터를 모은 뒤 한번에 처리
				//한번에 총 세개의 빨대를 서버와 클라이언트를 연결한 느낌
				//이것이 기본적인 stream연결방식
				
				if(responceCode >= 200 && responceCode <= 300) {
					br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					
						
				}else {//비정상적인 응답
					br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
				}
				
				
				String inputLine;
				content= new StringBuilder(); //읽어오는 데이터를 차곡차곡 쌓기 위함
				
				while((inputLine = br.readLine())!= null) {
					 //서버에서 날라온 데이터를  null이 아닐때까지 라인단위로 읽어오고 그걸 inputLine에 저장하고 그것을 Stringbuffer에 append (한 라인씩)
					content.append(inputLine);
				}
				
				br.close();
				connection.disconnect();
//				System.out.println("확인용: " + content.toString());
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			
	
			return content.toString();
		}
	
	
		
		
		public int get_Total_Cnt() {

			TestGsonFromAPI test = new TestGsonFromAPI();
			String jsonWifi = test.stringFromAPI("1","1");
			
			JsonObject jsonObject1 = (JsonObject)JsonParser.parseString(jsonWifi);
			JsonObject jsonObject2 = jsonObject1.get("TbPublicWifiInfo").getAsJsonObject(); 
		
			
			//TbPublicWifiInfo을 먼저 객체화해야만 그 안에 있는 key중 하나인 "list_total_count"에 접근가능
			return jsonObject2.get("list_total_count").getAsInt();
		}
		
		
		
		
	}
	

	


