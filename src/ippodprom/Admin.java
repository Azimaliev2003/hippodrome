package ippodprom;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Admin extends db {

    static db worker = new db();
    public static void run() throws IOException, SQLException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("""
                    Выберите пункт :
                    1.Добавить лошадь
                    2.Посмотреть список лошадей
                    3.Выход
                    """);
            int com = sc.nextInt();
            if (com <= 0 || com > 4) {
                System.out.println("try again, no existing command(((");
            } else if (com == 1) {
                addCondidate();
            } else if (com == 2) {
                ratingAndList();
            } else if (com == 3)  {
                System.out.println("bye bye");
                break;
            }
        }
    }

    public static void addCondidate() throws SQLException {
        String insert = "insert into horses (HorsName) values (?);";
        PreparedStatement ps = worker.getConnection().prepareStatement(insert);
        Scanner regisUser = new Scanner(System.in);
        System.out.println("Что бы добавить лошадя");
        System.out.println("Введите имя лошадя : ");
        String name = regisUser.nextLine();
        ps.setString(1,name);
        ps.execute();
        ps.close();
        System.out.println("Вы успешно добавили!");
    }
    public static void ratingAndList() throws IOException, SQLException {
        Statement st = worker.getConnection().createStatement();

        ResultSet rs = st.executeQuery("SELECT * FROM horses ORDER BY AllMatches DESC");
        int rating = 0;
        while (rs.next()){
            rating++;
            System.out.println(rating+" | Имя лошадя : "+rs.getString("HorsName")+"  |  Общее количество матчей : " + rs.getString("AllMatches"));

        }
        st.close();
    }
}
