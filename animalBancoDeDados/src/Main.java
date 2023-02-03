import org.apache.log4j.Logger;

import java.sql.*;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);

    private static final String SQLCREATETABLE = "DROP TABLE IF EXISTS Animal;" +
            "CREATE TABLE Animal " +
            "(id INT PRIMARY KEY, " +
            "nome VARCHAR(100) NOT NULL, " +
            "tipo VARCHAR(50) NOT NULL);"
    ;

    private static final String SQLINSERT1 = "INSERT INTO Animal (id, nome, tipo) VALUES" +
            "(1,'Meg','Cachorro')";
    private static final String SQLINSERT2 = "INSERT INTO Animal (id, nome, tipo) VALUES" +
            "(2,'Mingau','Gato')";
    private static final String SQLINSERT3 = "INSERT INTO Animal (id, nome, tipo) VALUES" +
            "(3,'Dumbo','Elefante')";

    private static final String SQLINSERT4 = "INSERT INTO Animal (id, nome, tipo) VALUES" +
            "(4,'Pe de Pano','Cavalo')";



    private  static final String SQLDELETE = "DELETE FROM Animal WHERE id = 1;";


    private static final String SQLSELECT = "SELECT * FROM Animal";


    public static void main(String[] args) throws SQLException {
        Connection connection = null;

        try {
            logger.info("Abrindo conexao com o banco de dados");
            connection = getConnection();

            Statement statement = connection.createStatement();

            logger.info("Criando tabela no banco de dados");
            statement.execute(SQLCREATETABLE);

            logger.info("Inserindo animais 1");
            statement.execute(SQLINSERT1);

            logger.info("Inserindo animais 2");
            statement.execute(SQLINSERT2);

            logger.info("Inserindo animais 3");
            statement.execute(SQLINSERT3);

            logger.info("Inserindo animais 4");
            statement.execute(SQLINSERT4);

            logger.warn("Deletando um Animal");
            statement.execute(SQLDELETE);

            logger.info("Consultando Animal");
            imprimirAnimal(connection);


        } catch (Exception e){
            e.printStackTrace();
            logger.error("Erro ao executar SQL");
        } finally {
            if(connection == null)
                return;
            logger.info("Fechando conexao com o banco de dados");
            connection.close();
        }

    }

    private static void imprimirAnimal(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQLSELECT);
        while (resultSet.next()){
            System.out.println(resultSet.getInt("id")+
                    " - " + resultSet.getString("nome")+
                    " - " + resultSet.getString("tipo"));
        }
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        return DriverManager.getConnection("jdbc:h2:~/animal","sa",""); //jdbc:h2:~/animal;AUTO_SERVER=TRUE
    }
}