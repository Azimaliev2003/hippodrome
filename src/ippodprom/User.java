package ippodprom;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;



public class User extends db{
    static db worker = new db();
    static Scanner num = new Scanner(System.in);
    static Scanner summ = new Scanner(System.in);
    static Scanner nameCond = new Scanner(System.in);
    static String info;
    public static void run(String login, String password) throws IOException, SQLException {
        Scanner sc = new Scanner(System.in);
        info=login;
        while (true) {
            System.out.println("""
                    Выберите пункт :
                    1.Посмотреть список лошадей
                    2.Сделать ставку
                    3.Выход
                    """);
            int com = sc.nextInt();
            if (com <= 0 || com > 4) {
                System.out.println("try again, no existing command(((");
            } else if (com == 1) {
                ratingAndList();
            } else if (com == 2) {
                poll();
            } else if (com == 3)  {
                System.out.println("bye bye");
                break;
            }
        }


    }

    public static void ratingAndList() throws IOException, SQLException {

        // Этот метод показывает список всех кондидатов
        // по их голосов будет изменятся расположение в списке
        Statement st = worker.getConnection().createStatement();

        ResultSet rs = st.executeQuery("SELECT * FROM horses  ORDER BY AllMatches DESC");
        int rating = 0;
        while (rs.next()){
            rating++;
            System.out.println(rating+" | Имя лошадя : "+rs.getString("HorsName")+"  |  Общее количество матчей : " + rs.getString("AllMatches"));

        }
        st.close();
    }

    public static void poll() throws IOException, SQLException {
        // Голосование

        PreparedStatement sm = worker.getConnection().prepareStatement("update users set winners = ? where name = '"+info+"';");
        // SQL запрос для добавление голоса
        PreparedStatement pollWinn = worker.getConnection().prepareStatement("update users set winners = winners * 1.96 where name = '"+info+"';");
        // SQL запрос для проверки уже голосовал ли избератель
        Statement st = worker.getConnection().createStatement();
        Statement kk = worker.getConnection().createStatement();
        PreparedStatement PSCOND = worker.getConnection().prepareStatement("SELECT * FROM horses  where HorsName = ?");
        PreparedStatement PSUSER = worker.getConnection().prepareStatement("SELECT * FROM users  where login = ? and TypeAccount='user'");
        ResultSet rs = st.executeQuery("SELECT * FROM horses");
        ratingAndList();

        System.out.println("Введите имя лошадя для ставки : ");
        String name = nameCond.nextLine();
        PSCOND.setString(1,name);
        PSUSER.setString(1,info);
        ResultSet condRS = PSCOND.executeQuery();
        ResultSet userRS = PSUSER.executeQuery();
        boolean bool = false;
        if (userRS.next() && condRS.next()){
            bool = true;
        }

        if (bool){
            if (condRS.getString(2).trim().equals(name)){

                System.out.println("Введите сумму ставки : ");
                int stav = num.nextInt();
                sm.setString(1, String.valueOf(stav));
                sm.executeUpdate();
                int count = 0;
                while (rs.next()){
                    count++;
                }
                System.out.println("На каком месте финиширует лошадь : "+condRS.getString(2));
                System.out.println("Общее количество лошадей : " + count);
                int place = summ.nextInt();
                int a = 1;
                int mest = a + (int) (Math.random() * count);
                System.out.println("Выбранный вами лошадь : "+mest);
                PreparedStatement poll = worker.getConnection().prepareStatement("update horses set PlaceInCurrent = ? where HorsName ='"+name+"';");
                poll.setInt(1, mest);
                poll.executeUpdate();

                ResultSet rr = kk.executeQuery("SELECT * FROM horses where HorsName = '"+name+"' and PlaceInCurrent = '"+place+"';");
                while (rr.next()){
                    System.out.println("Есть выигрыш!!!");
                    pollWinn.executeUpdate();
                }
            }
            else {
                System.out.println("Error1");
            }
        } else {
            System.out.println("Error2");
        }
        st.close();
    }
}