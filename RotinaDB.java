import  java.sql.Connection;
import  java.sql.PreparedStatement;
import  java.sql.ResultSet;
import  java.sql.SQLException;
import  java.util.ArrayList;

public class RotinaDB {
    public void incluirReg(Tarefa tarefa, int id)
    {   
        String   sqlInsert = "INSERT INTO TAREFAS(USUARIO, TAREFA, DATA, HORARIO, TIPO) VALUES(?, ?, ?, ?, ?)";
        Connection  conn = ConnFactory.getConn();
        PreparedStatement stmt = null;
        try
        {   stmt = conn.prepareStatement(sqlInsert);
            stmt.setInt(1, id);
            stmt.setString(2, tarefa.getDescricao());
            stmt.setString(3, tarefa.getData());
            stmt.setString(4, tarefa.getHorario());
            stmt.setString(5, tarefa.getTipo());
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
    
    public  ArrayList<String>  consultarTarefa(Tarefa tarefa)
    {  
         ArrayList<String> valores = new ArrayList<>();
        String   sqlSelect = "SELECT tarefa FROM tarefas WHERE usuario = ?";
        Connection  conn = ConnFactory.getConn();
        PreparedStatement stmt = null;
        ResultSet rs;
        try
        {   stmt = conn.prepareStatement(sqlSelect);
            stmt.setString(1, Integer.toString(tarefa.getID()));
            rs = stmt.executeQuery();
            while(rs.next())
            {    String valor = rs.getString(1);
                valores.add(valor); 
            }
        }
        catch(SQLException ex)
        {   System.out.println("Erro ao consultar os dados" + ex.toString());
        }
        finally
        {   ConnFactory.closeConn(conn, stmt);
        }   
        return valores;      
    } 

    public ArrayList<String>  consultarData(Tarefa tarefa)
    {   
        ArrayList<String> valores = new ArrayList<>();
        String   sqlSelect = "SELECT DATA FROM TAREFAS WHERE USUARIO = ?";
        Connection  conn = ConnFactory.getConn();
        PreparedStatement stmt = null;
        ResultSet rs;
        try
        {   stmt = conn.prepareStatement(sqlSelect);
            stmt.setString(1, Integer.toString(tarefa.getID()));
            rs = stmt.executeQuery();
            while(rs.next())
            {    String valor = rs.getString(1);
                valores.add(valor); 
            }
        }
        catch(SQLException ex)
        {   System.out.println("Erro ao consultar os dados" + ex.toString());
        }
        finally
        {   ConnFactory.closeConn(conn, stmt);
        }     
        return valores;    
    }  

    public ArrayList<String> consultarHorario(Tarefa tarefa)
    {   
        ArrayList<String> valores = new ArrayList<>();
        String   sqlSelect = "SELECT HORARIO FROM TAREFAS WHERE USUARIO = ?";
        Connection  conn = ConnFactory.getConn();
        PreparedStatement stmt = null;
        ResultSet rs;
        try
        {   stmt = conn.prepareStatement(sqlSelect);
            stmt.setString(1, Integer.toString(tarefa.getID()));
            rs = stmt.executeQuery();
            while(rs.next())
            {    String valor = rs.getString(1);
                valores.add(valor); 
            }
        }
        catch(SQLException ex)
        {   System.out.println("Erro ao consultar os dados" + ex.toString());
        }
        finally
        {   ConnFactory.closeConn(conn, stmt);
        }     
        return valores;  
    } 

    public  ArrayList<String>  consultarTipo(Tarefa tarefa)
    {   
        ArrayList<String> valores = new ArrayList<>();
        String   sqlSelect = "SELECT TIPO FROM TAREFAS WHERE USUARIO = ?";
        Connection  conn = ConnFactory.getConn();
        PreparedStatement stmt = null;
        ResultSet rs;
        try
        {   stmt = conn.prepareStatement(sqlSelect);
            stmt.setString(1, Integer.toString(tarefa.getID()));
            rs = stmt.executeQuery();
            while(rs.next())
            {    String valor = rs.getString(1);
                valores.add(valor); 
            }
        }
        catch(SQLException ex)
        {   System.out.println("Erro ao consultar os dados" + ex.toString());
        }
        finally
        {   ConnFactory.closeConn(conn, stmt);
        }     
        return valores;    
    } 

    public void consultarId(Tarefa tarefa)
    {   String   sqlSelect = "SELECT IDTAREFA FROM TAREFAS WHERE DATA = ? AND TAREFA = ? AND HORARIO = ? AND TIPO = ?";
        Connection  conn = ConnFactory.getConn();
        PreparedStatement stmt = null;
        ResultSet rs;
        try
        {   stmt = conn.prepareStatement(sqlSelect);
            stmt.setString(1, tarefa.getData());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setString(3, tarefa.getHorario());
            stmt.setString(4, tarefa.getTipo());
            rs = stmt.executeQuery();
            if(rs.next())
            {   tarefa.setID(rs.getInt(1));
            }
        }
        catch(SQLException ex)
        {   System.out.println("Erro ao consultar os dados" + ex.toString());
        }
        finally
        {   ConnFactory.closeConn(conn, stmt);
        }     }

    

    public void alterarReg(Tarefa tarefa, int id)
    {   String   sqlUpdate = "UPDATE TAREFAS SET DATA = ?, TAREFA = ?, HORARIO = ?, TIPO = ? WHERE IDTAREFA = ?";
        Connection  conn = ConnFactory.getConn();
        PreparedStatement stmt = null;
        try
        {   stmt = conn.prepareStatement(sqlUpdate);
            stmt.setString(1, tarefa.getData());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setString(3, tarefa.getHorario());
            stmt.setString(4, tarefa.getTipo());
            stmt.setInt(5, id);
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
    
    public void excluirReg(Tarefa tarefa, int id)
    {   String   sqlDelete = "DELETE FROM TAREFAS WHERE DATA = ? AND TAREFA = ? AND HORARIO = ? AND TIPO = ? AND IDTAREFA = ?";
        Connection  conn = ConnFactory.getConn();
        PreparedStatement stmt = null;
        try
        {   stmt = conn.prepareStatement(sqlDelete);
            stmt.setString(1, tarefa.getData());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setString(3, tarefa.getHorario());
            stmt.setString(4, tarefa.getTipo());
            stmt.setInt(5, id);
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

    public void excluirRegData(Tarefa tarefa)
    {   String   sqlDelete = "DELETE FROM TAREFAS WHERE DATA = ?";
        Connection  conn = ConnFactory.getConn();
        PreparedStatement stmt = null;
        try
        {   stmt = conn.prepareStatement(sqlDelete);
            stmt.setString(1, tarefa.getData());
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
