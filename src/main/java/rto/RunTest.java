package rto;



public class RunTest {
	public static void main(String[] args) {
	 Test_JDBC test= new Test_JDBC();
	 
	 
//	 long startTime = System.nanoTime();
//	 test.dbInsertAll_Batch1();
//	 long endTime = System.nanoTime();
//	 System.out.println(String.format("저장 소요시간: %d초", (endTime - startTime)/1000000000 ));
	 
	 
	 long startTime = System.nanoTime();
	 test.dbInsertAll();
	 long endTime = System.nanoTime();
	 System.out.println(String.format("저장 소요시간: %d초", (endTime - startTime)/1000000000 ));
	
}
}
