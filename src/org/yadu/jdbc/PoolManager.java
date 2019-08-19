package org.yadu.jdbc;
import java.sql.Connection;       
import java.sql.DriverManager;       
import java.sql.ResultSet;       
import java.sql.SQLException;       
import java.sql.Statement;       
      
import org.logicalcobwebs.proxool.ProxoolException;       
import org.logicalcobwebs.proxool.ProxoolFacade;       
import org.logicalcobwebs.proxool.admin.SnapshotIF;
      
      
public class PoolManager {       
           
    private static int activeCount = 0;       
           
           
    public PoolManager(){       
               
    }          
    /**     
     * ��ȡ����     
     * getConnection     
     * @param name     
     * @return     
     */      
    public Connection getConnection() {       
        try{       
           // Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");//proxool������       
            Connection conn = DriverManager.getConnection("proxool.mysql"); 
           //�˴���DBPool����proxool.xml�����õ����ӳر���      
            showSnapshotInfo();       
                   
            return conn;       
        }catch(Exception ex){       
            ex.printStackTrace();       
        }       
        return null;       
    }       
    /**     
     * �˷������Եõ����ӳص���Ϣ     
     * showSnapshotInfo     
     */      
    private void showSnapshotInfo(){       
        try{       
            SnapshotIF snapshot = ProxoolFacade.getSnapshot("mysql", true);       
            int curActiveCount=snapshot.getActiveConnectionCount();//��û������       
            int availableCount=snapshot.getAvailableConnectionCount();//��ÿɵõ���������       
            int maxCount=snapshot.getMaximumConnectionCount() ;//�����������       
            if(curActiveCount!=activeCount)//����������仯ʱ�������Ϣ       
            {       
             System.out.println("�������:"+curActiveCount+"(active)  �ɵõ���������:"+availableCount+"(available)  ��������:"+maxCount+"(max)");                    
             activeCount=curActiveCount;       
            }       
        }catch(ProxoolException e){       
            e.printStackTrace();       
        }       
    }       
    /**     
     * ��ȡ����     
     * getConnection     
     * @param name     
     * @return     
     */      
    public Connection getConnection(String name){       
        return getConnection();       
    }       
    /**     
     * �ͷ�����     
     * freeConnection     
     * @param conn     
     */      
    public void freeConnection(Connection conn){       
        if(conn!=null){       
            try {       
                conn.close();       
            } catch (SQLException e) {                     
                e.printStackTrace();       
            }       
        }       
    }       
    /**     
     * �ͷ�����     
     * freeConnection     
     * @param name     
     * @param con     
     */      
    public void freeConnection (String name,Connection con){       
        freeConnection(con);       
    }       
           
/*    public void getQuery() {               
        try {       
            Connection conn = getConnection();       
            if(conn != null){       
                Statement statement = conn.createStatement();       
                ResultSet rs = statement.executeQuery("select * from tblgxinterface");       
                int c = rs.getMetaData().getColumnCount();       
               while(rs.next()){                          
                   System.out.println();       
                   for(int i=1;i<=c;i++){       
                       System.out.print(rs.getObject(i));       
                   }       
               }       
               rs.close();       
           }       
           freeConnection(conn);       
       } catch (SQLException e) {                 
           e.printStackTrace();       
       }       
     
   }  */     
     
}   