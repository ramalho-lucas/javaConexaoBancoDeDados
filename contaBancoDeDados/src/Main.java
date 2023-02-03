import com.digitalhouse.jdbc.ConnectionJDBC;
import com.digitalhouse.model.Conta;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    final static Logger logger = Logger.getLogger(Main.class);
    private static final String SQLCREATE = "DROP TABLE IF EXISTS Conta; " +
            "CREATE TABLE Conta " +
            "( id INT PRIMARY KEY, " +
            "nome VARCHAR(100) NOT NULL, " +
            "numConta VARCHAR(20) NOT NULL, " +
            "saldo NUMERIC(15,2) );";

    private static final String SQLINSERT = "INSERT INTO Conta (id,nome,numConta,saldo) VALUES (?,?,?,?)";

    private static final String SQLUPDATE = "UPDATE Conta SET saldo = ? WHERE id = ?;";
    public static void main(String[] args) throws SQLException {

        //System.out.println(SQLCREATE);
        Conta conta = new Conta(1,"Lucas","5555-5",100);
        Connection connection = null;

        try{
            logger.info("Abrindo conexao com banco de dados");
            connection = ConnectionJDBC.getConnection();
            Statement statement = connection.createStatement();


            logger.info("Criando a tabela conta");
            statement.execute(SQLCREATE);

            logger.info("Preparando para popular tabela criada");
            PreparedStatement prStatementInsert = connection.prepareStatement(SQLINSERT);
            prStatementInsert.setInt(1,conta.getId());
            prStatementInsert.setString(2,conta.getNome());
            prStatementInsert.setString(3, conta.getNumConta());
            prStatementInsert.setDouble(4,conta.getSaldo());

            logger.info("Inserindo conta do "+conta.getNome());
            prStatementInsert.execute();

            connection.setAutoCommit(false);

            logger.info("Preparando para atualizar tabela");
            PreparedStatement prStatementUpdate = connection.prepareStatement(SQLUPDATE);
            prStatementUpdate.setDouble(1, conta.aumentarSaldo(50));
            prStatementUpdate.setInt(2,conta.getId());

            logger.info("Saldo atual da Conta:  " + conta.getSaldo());
            logger.warn("alterando saldo da conta");
            prStatementUpdate.execute();
            connection.commit();
            logger.info("Novo Saldo da Conta: " + conta.getSaldo());


            logger.info("Preparando para atualizar tabela pela 2x. Agora forcando um erro no script");
            PreparedStatement prStatementUpdate2 = connection.prepareStatement(SQLUPDATE);
            prStatementUpdate2.setDouble(1, conta.aumentarSaldo(40));
            prStatementUpdate2.setInt(2,conta.getId());

            logger.info("Saldo atual da Conta:  " + conta.getSaldo());
            logger.warn("alterando saldo da conta");
            prStatementUpdate2.execute();

            logger.info("Forcando um erro no script");
            int erro = 20/0;
            connection.commit();


        } catch (Exception e){

            logger.error("Erro ao executar transação");
            e.printStackTrace();

            logger.warn("Foi executado um rollback das transações");
            connection.rollback();


        } finally {

            logger.info("Fechando conexao com banco de dados");
            connection.setAutoCommit(true);
            connection.close();
        }
    }
}