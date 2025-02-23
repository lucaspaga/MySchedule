public class Tarefa{
    private String data;
    private String descricao;
    private String horario;
    private String diaDaSemana;
    private String tipo;
    private int id;

    public Tarefa(){
    }

    public Tarefa(String data, String horario, String descricao, String diaDaSemana, String tipo){
        this.data = data;
        this.descricao = descricao;
        this.horario = horario;
        this.diaDaSemana = diaDaSemana;
        this.tipo = tipo;
    }

    public void setData(String data){
        this.data = data;
    }

    public void setHorario(String horario){
        this.horario = horario;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public void setDiaDaSemana(String diaDaSemana){
        this.diaDaSemana = diaDaSemana;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    public String getData(){
        return data;
    }

    public String getHorario(){
        return horario;
    }

    public String getDescricao(){
        return descricao;
    }

    public String getDiaDaSemana(){
        return diaDaSemana;
    }

    public String getTipo(){
        return tipo;
    }

    public  int  getID()
    {   return  id;
    }

    public  void    setID(int id)
    {   this.id = id;
    }



    
}