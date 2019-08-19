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
     * 获取连接     
     * getConnection     
     * @param name     
     * @return     
     */      
    public Connection getConnection() {       
        try{       
           // Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");//proxool驱动类       
            Connection conn = DriverManager.getConnection("proxool.mysql"); 
           //此处的DBPool是在proxool.xml中配置的连接池别名      
            showSnapshotInfo();       
                   
            return conn;       
        }catch(Exception ex){       
            ex.printStackTrace();       
        }       
        return null;       
    }       
    /**     
     * 此方法可以得到连接池的信息     
     * showSnapshotInfo     
     */      
    private void showSnapshotInfo(){       
        try{       
            SnapshotIF snapshot = ProxoolFacade.getSnapshot("mysql", true);       
            int curActiveCount=snapshot.getActiveConnectionCount();//获得活动连接数       
            int availableCount=snapshot.getAvailableConnectionCount();//获得可得到的连接数       
            int maxCount=snapshot.getMaximumConnectionCount() ;//获得总连接数       
            if(curActiveCount!=activeCount)//当活动连接数变化时输出的信息       
            {       
             System.out.println("活动连接数:"+curActiveCount+"(active)  可得到的连接数:"+availableCount+"(available)  总连接数:"+maxCount+"(max)");                    
             activeCount=curActiveCount;       
            }       
        }catch(ProxoolException e){       
            e.printStackTrace();       
        }       
    }       
    /**     
     * 获取连接     
     * getConnection     
     * @param name     
     * @return     
     */      
    public Connection getConnection(String name){       
        return getConnection();       
    }       
    /**     
     * 释放连接     
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
     * 释放连接     
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