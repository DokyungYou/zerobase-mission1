package rto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dto.Bookmark;
import dto.BookmarkGroup_Object;

//북마크그룹, 북마크리스트
public class BookmarkService {

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
