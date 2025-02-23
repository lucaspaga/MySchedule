import  java.sql.Connection;
import  java.sql.PreparedStatement;
import  java.sql.ResultSet;
import  java.sql.SQLException;

public class CrudBD
{   public void incluirReg(User uD)
    {   String   sqlInsert = "INSERT INTO USUARIO(USER, PASS) VALUES(?, ?)";
        Connection  conn = ConnFactory.getConn();
        PreparedStatement stmt = null;
        try
        {   stmt = conn.prepareStatement(sqlInsert);
            stmt.setString(1, uD.getUser());
            stmt.setString(2, uD.getPass());
            stmt.executeUpdate();
        }
        catch(SQLException e)
        {   try
            {   conn.rollback();
            }
            catch(SQLException ex)
            {   System.out.println("Erro ao incluir os dados" + ex.toString());
            }
        }
        finally
        {   ConnFactory.closeConn(conn, stmt);
        }
    }   
    
    public void consultarReg(User uD)
    {   String   sqlSelect = "SELECT PASS FROM USUARIO WHERE USER = ?";
        Connection  conn = ConnFactory.getConn();
        PreparedStatement stmt = null;
        ResultSet rs;
        try
        {   stmt = conn.prepareStatement(sqlSelect);
            stmt.setString(1, uD.getUser());
            rs = stmt.executeQuery();
            if(rs.next())
            {   uD.setPass(rs.getString(1));
            }
        }
        catch(SQLException ex)
        {   System.out.println("Erro ao consultar os dados" + ex.toString());
        }
        finally
        {   ConnFactory.closeConn(conn, stmt);
        }       
    } 

    public void consultarId(User uD)
    {   String   sqlSelect = "SELECT IDUSUARIO FROM USUARIO WHERE USER = ?";
        Connection  conn = ConnFactory.getConn();
        PreparedStatement stmt = null;
        ResultSet rs;
        try
        {   stmt = conn.prepareStatement(sqlSelect);
            stmt.setString(1, uD.getUser());
            rs = stmt.executeQuery();
            if(rs.next())
            {   uD.setID(rs.getInt(1));
            }
        }
        catch(SQLException ex)
        {   System.out.println("Erro ao consultar os dados" + ex.toString());
        }
        finally
        {   ConnFactory.closeConn(conn, stmt);
        }     }

    
    public void alterarReg(User uD)
    {   String   sqlUpdate = "UPDATE USUARIO SET PASS = ? WHERE USER = ?";
        Connection  conn = ConnFactory.getConn();
        PreparedStatement stmt = null;
        try
        {   stmt = conn.prepareStatement(sqlUpdate);
            stmt.setString(1, uD.getPass());
            stmt.setString(2, uD.getUser());
            stmt.executeUpdate();
        }
        catch(SQLException e)
        {   try
            {   conn.rollback();
            }
            catch(SQLException ex)
            {   System.out.println("Erro ao alterar os dados" + ex.toString());
            }
        }
        finally
        {   ConnFactory.closeConn(conn, stmt);
        }       
    }   
    
    public void excluirReg(User uD)
    {   String   sqlDelete = "DELETE FROM USUARIO WHERE USER = ?";
        Connection  conn = ConnFactory.getConn();
        PreparedStatement stmt = null;
        try
        {   stmt = conn.prepareStatement(sqlDelete);
            stmt.setString(1, uD.getUser());
            stmt.executeUpdate();
        }
        catch(SQLException e)
        {   try
            {   conn.rollback();
            }
            catch(SQLException ex)
            {   System.out.println("Erro ao excluir os dados" + ex.toString());
            }
        }
        finally
        {   ConnFactory.closeConn(conn, stmt);
        }       
    }
    
}
