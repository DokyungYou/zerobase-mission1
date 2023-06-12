package rto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dto.NearWifiObject;

public class WifiService {

	
    public void dbInsertAll(){
        
    	//이미 데이터 있는 상태에선 다시 못 넣으니까 모든 데이터를 지우고 시작
    	this.delete_All();
    	
    	
    	//0.준비 작업 (connection 객체에 인자로 넣을 값들)
        String url = "jdbc:mariadb://localhost:3306/wifi_info" ;
        String dbUserId = "mission1";
        String dbPassWord ="zerobase";



        //1.드라이버 로드
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        //2. DataBase Connection 객체 생성 (DB와의 연결을 위한 객체)
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;


        try{
            //커넥션 객체 생성
            connection = DriverManager.getConnection(url, dbUserId,dbPassWord);

            
           
            String sql = "insert into  wifi_info values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            preparedStatement = connection.prepareStatement(sql);
            
            
            TestGsonFromAPI openApi = new TestGsonFromAPI();
            
            int totalCnt = openApi.get_Total_Cnt();
            TestGsonFromAPI pagingTest = new TestGsonFromAPI();
            
            
            System.out.println("데이터를 넣기 시작합니다.");
    		
    		//가져오는 횟수
    		for(int i = 1; i <= totalCnt; i += 1000) {
    			String start = String.valueOf(i);
    			String end = String.valueOf(Math.min(totalCnt, i + 1000 - 1));
    			
    			
    			 String result = pagingTest.stringFromAPI(String.valueOf(start), String.valueOf(end));
    			 JsonObject jsonObject1 = (JsonObject)JsonParser.parseString(result);
    			 JsonObject jsonObject2 = jsonObject1.get("TbPublicWifiInfo").getAsJsonObject();
    			 //여기서 java.lang.NullPointerException
    			
    			 JsonArray jsonArray = jsonObject2.getAsJsonArray("row");
    			 //row배열안의 여러{}덩어리객체들을 JsonObject화 시키고 각 객체에서 key값을 접근해야함
    			 
    			 
    			 
    			 for(int j = 0; j < jsonArray.size(); j++) {
    			 //row배열 안의 {}들을 하나씩 객체화
   				 JsonObject wifiJsonObject = (JsonObject)jsonArray.get(j); 
   				 NearWifiObject transWifiObject = new Gson().fromJson(wifiJsonObject,NearWifiObject.class);
    		 
                                                                                         
                 preparedStatement.setString(1 ,transWifiObject.getX_SWIFI_MGR_NO());                 
                 preparedStatement.setString(2 ,transWifiObject.getX_SWIFI_WRDOFC());                 
                 preparedStatement.setString(3 ,transWifiObject.getX_SWIFI_MAIN_NM());                
                 preparedStatement.setString(4 ,transWifiObject.getX_SWIFI_ADRES1());                 
                 preparedStatement.setString(5 ,transWifiObject.getX_SWIFI_ADRES2());                 
                 preparedStatement.setString(6 ,transWifiObject.getX_SWIFI_INSTL_FLOOR());            
                 preparedStatement.setString(7 ,transWifiObject.getX_SWIFI_INSTL_TY());               
                 preparedStatement.setString(8 ,transWifiObject.getX_SWIFI_INSTL_MBY());              
                 preparedStatement.setString(9 ,transWifiObject.getX_SWIFI_SVC_SE());                 
                 preparedStatement.setString(10 ,transWifiObject.getX_SWIFI_CMCWR());                 
                 preparedStatement.setString(11 ,transWifiObject.getX_SWIFI_CNSTC_YEAR());            
                 preparedStatement.setString(12 ,transWifiObject.getX_SWIFI_INOUT_DOOR());            
                 preparedStatement.setString(13 ,transWifiObject.getX_SWIFI_REMARS3());               
                 preparedStatement.setDouble(14, transWifiObject.getLAT());;                          
                 preparedStatement.setDouble(15 ,transWifiObject.getLNT());                           
                 preparedStatement.setString(16 ,transWifiObject.getWORK_DTTM());                     
                                                                                         
   
            	int affectedRows = preparedStatement.executeUpdate();
    		            
	            if(affectedRows <= 0 ) System.out.println("저장 실패!");
    			 
				 }
	    		 
			}
    		
       
        }catch (SQLException | NullPointerException e ){
        	e.printStackTrace();
        
        }finally {
            try {
                if(connection != null && !connection.isClosed()){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(preparedStatement != null && !preparedStatement.isClosed()){
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(rs != null && !rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("모든 데이터를 넣었습니다!");
    }
    
    
    
    //데이터베이스에 넣기 전 기존 데이터 삭제용
    public void delete_All(){
        //0.준비 작업 (connection 객체에 인자로 넣을 값들)
        String url = "jdbc:mariadb://localhost/wifi_info" ;
        String dbUserId = "mission1";
        String dbPassWord ="zerobase";


        //1.드라이버 로드
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        //2. DataBase Connection 객체 생성 (DB와의 연결을 위한 객체)
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;




        try{
            //커넥션 객체 생성
            connection = DriverManager.getConnection(url, dbUserId,dbPassWord);

            String sql = "delete from wifi_info";

            preparedStatement = connection.prepareStatement(sql);
            
            

            int affectedRows = preparedStatement.executeUpdate();
   
            if(affectedRows > 0 ){
                System.out.println("해당 테이블의 전체 데이터를 지웠습니다!");
            }else{
                System.out.println("삭제 실패!");
            }


        }catch (SQLException e){
        	e.printStackTrace();
        }finally {
            try {
                if(connection != null && !connection.isClosed()){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(preparedStatement != null && !preparedStatement.isClosed()){
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(rs != null && !rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    //인자로 x,y좌표 값 넣어야함(박스에 들어간 값으로)
    public List<NearWifiObject> selectNearWifi20(double lat, double lnt) {
        //0.준비 작업 (connection 객체에 인자로 넣을 값들)
       
    	List<NearWifiObject> wifiList = new ArrayList<>();
    	String url = "jdbc:mariadb://localhost/wifi_info" ;
        String dbUserId = "mission1";
        String dbPassWord ="zerobase";
        


        //1.드라이버 로드
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        //2. DataBase Connection 객체 생성 (DB와의 연결을 위한 객체)
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        
        try{
            //커넥션 객체 생성
            connection = DriverManager.getConnection(url, dbUserId,dbPassWord);

            String sql = "SELECT "
            		+ "ROUND( "
            		+ "(6371 * ACOS( "
            		+ "COS(RADIANS(?)) * COS(RADIANS(wi.LAT)) * COS(RADIANS(wi.LNT) - RADIANS(?)) + "
            		+ "SIN(RADIANS(?)) * SIN(RADIANS(wi.LAT)) "
            		+ ")), "
            		+ "4 "
            		+ ") AS distance, "
            		+ "wi.* "
            		+ "FROM wifi_info wi "
            		+ "ORDER BY distance "
            		+ "LIMIT 0, 20 ";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setDouble(1,lat);
            preparedStatement.setDouble(2,lnt);
            preparedStatement.setDouble(3,lat);
          

            rs = preparedStatement.executeQuery();

      
            
          while(rs.next()){
            	NearWifiObject wifi =  new NearWifiObject();
            	wifi.setDistance(rs.getDouble("distance"));
                wifi.setX_SWIFI_MGR_NO(rs.getString("wi.X_SWIFI_MGR_NO"));
                wifi.setX_SWIFI_WRDOFC( rs.getString("wi.X_SWIFI_WRDOFC"));
                wifi.setX_SWIFI_MAIN_NM( rs.getString("wi.X_SWIFI_MAIN_NM"));
                wifi.setX_SWIFI_ADRES1(rs.getString("wi.X_SWIFI_ADRES1"));
                wifi.setX_SWIFI_ADRES2(rs.getString("wi.X_SWIFI_ADRES2"));
                wifi.setX_SWIFI_INSTL_FLOOR(rs.getString("wi.X_SWIFI_INSTL_FLOOR"));
                wifi.setX_SWIFI_INSTL_TY(rs.getString("wi.X_SWIFI_INSTL_TY"));
                wifi.setX_SWIFI_INSTL_MBY(rs.getString("wi.X_SWIFI_INSTL_MBY"));
                wifi.setX_SWIFI_SVC_SE(rs.getString("wi.X_SWIFI_SVC_SE"));
                wifi.setX_SWIFI_CMCWR(rs.getString("wi.X_SWIFI_CMCWR"));
                wifi.setX_SWIFI_CNSTC_YEAR(rs.getString("wi.X_SWIFI_CNSTC_YEAR"));
                wifi.setX_SWIFI_INOUT_DOOR( rs.getString("wi.X_SWIFI_INOUT_DOOR"));
                wifi.setX_SWIFI_REMARS3(rs.getString("wi.X_SWIFI_REMARS3"));
                wifi.setLAT(rs.getDouble("wi.LAT"));
                wifi.setLNT(rs.getDouble("wi.LNT"));
                wifi.setWORK_DTTM(rs.getString("wi.WORK_DTTM"));
                

                
                wifiList.add(wifi);
             
            }

        }catch (SQLException e){
        	e.printStackTrace();
        }finally {
            
        	try {
                if(!connection.isClosed()){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(!preparedStatement.isClosed()){
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(!rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
        }
        return wifiList; 
    }
    
    
    //와이파이 이름 링크 타고 갔을 때 나올 것
    public NearWifiObject select_Wifi_Detail(String number) {
        //0.준비 작업 (connection 객체에 인자로 넣을 값들)
    	NearWifiObject detailWifi = new NearWifiObject();
    	
    	
    	String url = "jdbc:mariadb://localhost/wifi_info" ;
        String dbUserId = "mission1";
        String dbPassWord ="zerobase";
        


        //1.드라이버 로드
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        //2. DataBase Connection 객체 생성 (DB와의 연결을 위한 객체)
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        
        try{
            //커넥션 객체 생성
            connection = DriverManager.getConnection(url, dbUserId,dbPassWord);

   
            String sql = "select * from wifi_info wi "
            		+ "where wi.X_SWIFI_MGR_NO = ?";
            		
            		

            preparedStatement = connection.prepareStatement(sql);
          
            preparedStatement.setString(1,number);
          

            rs = preparedStatement.executeQuery();

            
            
            
            while(rs.next()){
            	
            	
            	detailWifi.setX_SWIFI_MGR_NO(rs.getString("wi.X_SWIFI_MGR_NO"));
            	detailWifi.setX_SWIFI_WRDOFC( rs.getString("wi.X_SWIFI_WRDOFC"));
            	detailWifi.setX_SWIFI_MAIN_NM( rs.getString("wi.X_SWIFI_MAIN_NM"));
            	detailWifi.setX_SWIFI_ADRES1(rs.getString("wi.X_SWIFI_ADRES1"));
            	detailWifi.setX_SWIFI_ADRES2(rs.getString("wi.X_SWIFI_ADRES2"));
            	detailWifi.setX_SWIFI_INSTL_FLOOR(rs.getString("wi.X_SWIFI_INSTL_FLOOR"));
            	detailWifi.setX_SWIFI_INSTL_TY(rs.getString("wi.X_SWIFI_INSTL_TY"));
            	detailWifi.setX_SWIFI_INSTL_MBY(rs.getString("wi.X_SWIFI_INSTL_MBY"));
            	detailWifi.setX_SWIFI_SVC_SE(rs.getString("wi.X_SWIFI_SVC_SE"));
            	detailWifi.setX_SWIFI_CMCWR(rs.getString("wi.X_SWIFI_CMCWR"));
            	detailWifi.setX_SWIFI_CNSTC_YEAR(rs.getString("wi.X_SWIFI_CNSTC_YEAR"));
            	detailWifi.setX_SWIFI_INOUT_DOOR( rs.getString("wi.X_SWIFI_INOUT_DOOR"));
            	detailWifi.setX_SWIFI_REMARS3(rs.getString("wi.X_SWIFI_REMARS3"));
            	detailWifi.setLAT(rs.getDouble("wi.LAT"));
            	detailWifi.setLNT(rs.getDouble("wi.LNT"));
            	detailWifi.setWORK_DTTM(rs.getString("wi.WORK_DTTM"));
                
           
            }

          

        }catch (SQLException e){
        	e.printStackTrace();
        }finally {
            
        	
        	
        	try {
                if(!connection.isClosed()){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(!preparedStatement.isClosed()){
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(!rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
        }
        return detailWifi; 
    }
 
    
    
    //각 테이블출력 조건문 위함 (데이터가 0개면 기본화면이 뜨도록)
    public int select_tableDataCount(String tableName) {
        //0.준비 작업 (connection 객체에 인자로 넣을 값들)
    	
    	int result = 0;
    
    	
    	String url = "jdbc:mariadb://localhost/wifi_info" ;
        String dbUserId = "mission1";
        String dbPassWord ="zerobase";
        


        //1.드라이버 로드
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        //2. DataBase Connection 객체 생성 (DB와의 연결을 위한 객체)
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        
        try{
            //커넥션 객체 생성
            connection = DriverManager.getConnection(url, dbUserId,dbPassWord);
            statement = connection.createStatement();
            
            String sql = "select count(*) as totalCnt from " + tableName;
            //테이블 이름은 ?로 바인딩 처리가 안된다고한다.( 테이블 이름은 파라미터로 처리 불가하다고 함)
            //동적으로 테이블 이름을 쿼리에 포함시키려면 일반적인 Statement를 사용해야한다고함
            		

            rs = statement.executeQuery(sql);

           
            
            while(rs.next()){
            	
            	
            	result = rs.getInt("totalCnt"); //테이블 데이터 노출 조건
                
                //확인용 출력문
            	System.out.println(result +"개의 데이터가 있습니다.");             
            }

        }catch (SQLException e){
        	e.printStackTrace();
        }finally {
            
        	
        	
        	try {
                if(!connection.isClosed()){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(!statement.isClosed()){
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if(!rs.isClosed()){
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
        }
        return result; 
    }
}
