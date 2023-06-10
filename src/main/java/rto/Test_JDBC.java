package rto;


import java.sql.*;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dto.Bookmark;
import dto.BookmarkGroup_Object;
import dto.HistoryObject;
import dto.NearWifiObject;



public class Test_JDBC {
	
	

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
                
                //확인용 출력문
//              System.out.println(wifi.getDistance() + "," +  wifi.getX_SWIFI_MGR_NO() + "," + wifi.getX_SWIFI_WRDOFC() + "," + wifi.getX_SWIFI_MAIN_NM() + "," +
//              wifi.getX_SWIFI_ADRES1() + "," + wifi.getX_SWIFI_ADRES2() + "," + wifi.getX_SWIFI_INSTL_FLOOR() + "," + wifi.getX_SWIFI_INSTL_TY() + "," + wifi.getX_SWIFI_INSTL_MBY() + "," +
//              wifi.getX_SWIFI_SVC_SE() + "," + wifi.getX_SWIFI_CMCWR() + "," +wifi.getX_SWIFI_CNSTC_YEAR() + "," +wifi.getX_SWIFI_INOUT_DOOR() + "," +
//              wifi.getX_SWIFI_REMARS3() + "," +wifi.getLAT() + "," +wifi.getLNT() + "," +wifi.getWORK_DTTM()
//               );
                
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
                
                //확인용 출력문
//              System.out.println(detailWifi.getX_SWIFI_MGR_NO() + "," + detailWifi.getX_SWIFI_WRDOFC() + "," + detailWifi.getX_SWIFI_MAIN_NM() + "," +
//              detailWifi.getX_SWIFI_ADRES1() + "," + detailWifi.getX_SWIFI_ADRES2() + "," + detailWifi.getX_SWIFI_INSTL_FLOOR() + "," + detailWifi.getX_SWIFI_INSTL_TY() + "," + detailWifi.getX_SWIFI_INSTL_MBY() + "," +
//              detailWifi.getX_SWIFI_SVC_SE() + "," + detailWifi.getX_SWIFI_CMCWR() + "," +detailWifi.getX_SWIFI_CNSTC_YEAR() + "," +detailWifi.getX_SWIFI_INOUT_DOOR() + "," +
//              detailWifi.getX_SWIFI_REMARS3() + "," +detailWifi.getLAT() + "," +detailWifi.getLNT() + "," +detailWifi.getWORK_DTTM()
//              ) ;
                
                
                
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
    
    
    
    public void insert_BookmarkGroup(String name, String order) {
        
    	//예외처리
        if( name.trim().length()== 0){
        	//name컬럼의 중복된 이름을 피하기 위함 (unique컬럼이기때문에)
        	name = "북마크이름을 정해주세요."+ "("+ LocalDateTime.now()+ ")";
        }
        
        
        //order 공백처리
        if(order.trim().equals("")) {
        	order="1";
        }else {
        	//숫자가 아닌 문자가 들어왔을때 처리
        	for(char c :order.trim().toCharArray()) {
            	if(!Character.isDigit(c)) {
            		order = "1";
        
            		break;
            	}
            }
        }
        
        
        
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

            //본래 이걸 고정된 값으로 넣는 것이 아닌 바뀌는 값으로 넣어줘야함
            String sql = "insert into bookmark_group(BOOKMARK_NAME,ORDERS,REGISTER_DTTM) "
            		+ "values(?,?,now())";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,order);
            
            



            //executeUpdate()는 int를 반환(updated Rows를 반환)
            //resultSet이 아닌 int변수로 결과를 받는다.
            int affectedRows = preparedStatement.executeUpdate();
            //잘 실행됐는지 확인
            if(affectedRows > 0 ){
                System.out.println("저장 성공!");
            }else{
                System.out.println("저장 실패!");
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
    
    
    
    public List<BookmarkGroup_Object> selectAll_BookmarkGroup() {
    	List<BookmarkGroup_Object> listObject = new ArrayList<>();
    
    	
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

            String sql = "select * from bookmark_group "
            		+ "order by ORDERS asc;";
            		
            		

            preparedStatement = connection.prepareStatement(sql);
          
            rs = preparedStatement.executeQuery();

            //처음에 객체는 재활용가능하지않을까해서 객체생성코드를 밖에 뺏었는데 가장 마지막에 만든 것만 뜨게 된다
            while(rs.next()){
            	BookmarkGroup_Object bookmark = new BookmarkGroup_Object();
            	bookmark.setGroup_id(rs.getInt("ID(Group-ID)"));
            	bookmark.setBookmarkName(rs.getString("BOOKMARK_NAME"));      
            	bookmark.setOrder(rs.getInt("ORDERS"));
            	bookmark.setRegister_dttm(rs.getString("REGISTER_DTTM"));
            	
            	if(rs.getString("UPDATE_DTTM")!= null) {
            		bookmark.setUpdate_dttm(rs.getString("UPDATE_DTTM"));
            	}else {
            		bookmark.setUpdate_dttm("");
            	}
            	
        
            	listObject.add(bookmark);
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
        return listObject; 
    }
    
    
    
    
    public void update_bookmarkGroup(String name, String order, int id){
        
        //예외처리
    	 if( name.trim().length()== 0){
         	//name컬럼의 중복된 이름을 피하기 위함 (unique컬럼이기때문에)
         	name = "북마크이름을 정해주세요."+ "("+ LocalDateTime.now()+ ")";
         }
    	
    	//order 공백처리
        if(order.trim().equals("")) {
        	order="1";
        }else {
        	//숫자가 아닌 문자가 들어왔을때 처리
        	for(char c :order.trim().toCharArray()) {
            	if(!Character.isDigit(c)) {
            		order = "1";
        
            		break;
            	}
            }
        }
        
    
    	
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

          
            String sql = "update  bookmark_group "
            		+ "set bookmark_name = ?, ORDERS =?, UPDATE_DTTM = now() "
            		+ "where `ID(Group-ID)`= ? ";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,order);
            preparedStatement.setInt(3,id);
            

            int affectedRows = preparedStatement.executeUpdate();
            //잘 실행됐는지 확인
            if(affectedRows > 0 ){
                System.out.println("업데이트 성공!");
            }else{
                System.out.println("업데이트 실패!");
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
    
    
    
    
    public void delete_bookmarkGroup(int id){
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

            //본래 이걸 고정된 값으로 넣는 것이 아닌 바뀌는 값으로 넣어줘야함
            String sql = "delete from bookmark_group "
            		+ "where bookmark_group.`ID(Group-ID)`= ?";


            preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setInt(1,id);
            

            int affectedRows = preparedStatement.executeUpdate();
   
            if(affectedRows > 0 ){
                System.out.println("삭제 성공!");
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
    
    
    
    
    
   
    public void insertBookmarkList(int g_id,String w_num, String distance) {
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

            //본래 이걸 고정된 값으로 넣는 것이 아닌 바뀌는 값으로 넣어줘야함
            String sql = "insert into bookmark_list(`ID(Group-ID)`,X_SWIFI_MGR_NO,REGISTER_DTTM,DISTANCE) "
            		+ "values(?,?,now(),?)";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,g_id);
            preparedStatement.setString(2,w_num);
            preparedStatement.setString(3,distance);
            



            //executeUpdate()는 int를 반환(updated Rows를 반환)
            //resultSet이 아닌 int변수로 결과를 받는다.
            int affectedRows = preparedStatement.executeUpdate();
            //잘 실행됐는지 확인
            if(affectedRows > 0 ){
                System.out.println("저장 성공!");
            }else{
                System.out.println("저장 실패!");
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
    
    
    
    
    

    public List<BookmarkGroup_Object> select_bookmark_NameID() {
       
    	
    	List<BookmarkGroup_Object> list = new ArrayList<>();
    	
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

          
            String sql = "select bg.`ID(Group-ID)` ,bg.BOOKMARK_NAME "
            		+ "from bookmark_group bg";
            		
            		

            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();

      
            while(rs.next()){
            	BookmarkGroup_Object bookmarkO = new BookmarkGroup_Object();
            	
            	bookmarkO.setGroup_id(rs.getInt("ID(Group-ID)"));
            	bookmarkO.setBookmarkName(rs.getString("bg.BOOKMARK_NAME"));
            	               
                list.add(bookmarkO);
                
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
        return list; 
    }
    
    
    
    
    
    
    
    public void insert_history(double x, double y) {
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

            String sql = "insert into history(LAT, LNT,INQUIRY_DTTM) "
            		+ "values(?,?,now())";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1,x);
            preparedStatement.setDouble(2,y);
            
           
            int affectedRows = preparedStatement.executeUpdate();
            //잘 실행됐는지 확인
            if(affectedRows > 0 ){
                System.out.println("저장 성공!");
            }else{
                System.out.println("저장 실패!");
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
    
    
    
    
    
    
    public List<HistoryObject> selectAll_history() {
    	List<HistoryObject> listObject = new ArrayList<>();
    
    	
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

            String sql = "select * from history "
            		+ "order by `ID(history)` desc";
            		
            		

            preparedStatement = connection.prepareStatement(sql);
          
            rs = preparedStatement.executeQuery();

          
            while(rs.next()){
            	HistoryObject history = new HistoryObject();
            	history.setID(rs.getInt("ID(history)"));
            	history.setX(rs.getDouble("LAT"));      
            	history.setY(rs.getDouble("LNT"));  
            	history.setInquiryDate(rs.getString("INQUIRY_DTTM"));
            	
           
            	listObject.add(history);
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
        return listObject; 
    }
    
    
    
    
    
    public void delete_history(int id){
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

            //본래 이걸 고정된 값으로 넣는 것이 아닌 바뀌는 값으로 넣어줘야함
            String sql = "delete from history where `ID(history)` = ?";


            preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setInt(1,id);
            

            
            int affectedRows = preparedStatement.executeUpdate();
   
            if(affectedRows > 0 ){
                System.out.println("삭제 성공!");
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
    
    
    
    
    
    


   
    public List<Bookmark> selectAll_BookmarkList() {
    	List<Bookmark> bookmarkList = new ArrayList<>();
    
    	
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

            String sql = 
            		"select "
            		+ "bl.`ID(List-ID)`, "  //화면노출
            		+ "bg.BOOKMARK_NAME, "  //화면노출
            		+ "wi.X_SWIFI_MAIN_NM, " //화면노출
            		+ "wi.X_SWIFI_MGR_NO, " //detail 전송
            		+ "bl.REGISTER_DTTM, "  //화면노출
            		+ "bl.DISTANCE "  //detail 전송
            		+ "from "
            		+ "bookmark_list bl "
            		+ "inner join bookmark_group bg on bl.`ID(Group-ID)` = bg.`ID(Group-ID)` "
            		+ "inner join wifi_info wi on bl.X_SWIFI_MGR_NO = wi.X_SWIFI_MGR_NO ";
           	
            		

            preparedStatement = connection.prepareStatement(sql);
          
            rs = preparedStatement.executeQuery();

            while(rs.next()){
            	Bookmark bookmark = new Bookmark();
            	
            	bookmark.setBookmarkID(rs.getInt("ID(List-ID)"));      
            	bookmark.setBookmarkName(rs.getString("BOOKMARK_NAME"));
            	bookmark.setWifiName(rs.getString("X_SWIFI_MAIN_NM"));
            	bookmark.setWifiNum(rs.getString("X_SWIFI_MGR_NO"));
            	bookmark.setRegisterDTTM(rs.getString("REGISTER_DTTM"));
            	bookmark.setDistance(rs.getString("DISTANCE"));
            	
        
            	bookmarkList.add(bookmark);
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
        return bookmarkList; 
    }
    
    
    
    
    //북마크 삭제 창 노출 위함
    public Bookmark select_bookmarkDelete(int id) {
    	Bookmark bookmark= new Bookmark();
    
    	
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

            String sql = 
            		"select "
            		+ "bg.BOOKMARK_NAME, " 	//화면노출
            		+ "wi.X_SWIFI_MAIN_NM, "  //화면노출
            		+ "wi.X_SWIFI_MGR_NO, " //detail 전송
            		+ "bl.REGISTER_DTTM, "  //화면노출
            		+ "bl.`ID(List-ID)`, "  //delete-run 전송
            		+ "bl.DISTANCE "  //detail 전송
            		+ "from "
            		+ "bookmark_list bl "
            		+ "inner join bookmark_group bg on bl.`ID(Group-ID)` = bg.`ID(Group-ID)` "
            		+ "inner join wifi_info wi on bl.X_SWIFI_MGR_NO = wi.X_SWIFI_MGR_NO "
            		+ "where bl.`ID(List-ID)` = ? ";
            		
           	
            		

            preparedStatement = connection.prepareStatement(sql);
          
            preparedStatement.setInt(1,id);
            
            rs = preparedStatement.executeQuery();

            while(rs.next()){
            
            	bookmark.setBookmarkName(rs.getString("BOOKMARK_NAME"));
            	bookmark.setWifiName(rs.getString("X_SWIFI_MAIN_NM"));
            	bookmark.setWifiNum(rs.getString("X_SWIFI_MGR_NO"));
            	bookmark.setRegisterDTTM(rs.getString("REGISTER_DTTM"));
            	bookmark.setBookmarkID(rs.getInt("ID(List-ID)")); 
            	bookmark.setDistance(rs.getString("DISTANCE")); 
            
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
        return bookmark; 
    }
    
    
    
    public void delete_bookmark(int id){
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

            //본래 이걸 고정된 값으로 넣는 것이 아닌 바뀌는 값으로 넣어줘야함
            String sql = "delete from bookmark_list "
            		+ "where `ID(List-ID)` = ?";


            preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setInt(1,id);
            
     
            int affectedRows = preparedStatement.executeUpdate();
   
            if(affectedRows > 0 ){
                System.out.println("삭제 성공!");
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
    

}
