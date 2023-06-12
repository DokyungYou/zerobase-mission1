package rto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.HistoryObject;

public class HistoryService {

	
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
