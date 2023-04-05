package ippodprom;


import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;




/**
 Ипподромный тотализатор

 Сначала система нас спрашивает есть ли у нас аккаунт
 "У вас есть акккаунт?   Да-1 / Нет-2"

 если есть то нажимаем на 1 если нет то 2
 после этого выберем тип аккаунта :
 "Выберите тип аккаунта : "
 "1 - Админ"
 "2 - Пользователь"

 У нас в базе есть только один аккаунт "Админа"
 и никто не может создать аккаунт админа

 Если выберем 1 или 2 то система вызывает метот login();

 if (type == 1){
 accType = "admin";
 Login();
 } else if(type == 2){
 accType = "user";
 Login();
 }

 Если у нас нет аккаунта мы можем создать новый
 Но только аккаунт пользователя то есть
 тип аккаунта по умолчанию будет как пользователь


 ------Аккаунт админа-------


 Приветствую !!! 'Нурик' (Имя админа мы получаем из базы данных по его логину)
 Выберите пункт :
 1.Добавить лошадя

 2.Посмотреть список и рейтинг лошадей
 1 | Имя лошадя : akkula  |  Общее количество матчей : null
 2 | Имя лошадя : dur  |  Общее количество матчей : null
 3 | Имя лошадя : Gigant  |  Общее количество матчей : null
 4 | Имя лошадя : Fast  |  Общее количество матчей : null
 3.Выход

 ------Аккаунт пользователя------

 Приветствую !!! 'Нурик' (Имя пользователя мы получаем из базы данных по его логину)
 Выберите пункт :
 1.Посмотреть список и рейтинг лошадей
 1 | Имя лошадя : akkula  |  Общее количество матчей : null
 2 | Имя лошадя : dur  |  Общее количество матчей : null
 3 | Имя лошадя : Gigant  |  Общее количество матчей : null
 4 | Имя лошадя : Fast  |  Общее количество матчей : null
 2.Сделать ставку
 (Выводится список лошадей)
 и система просит нас вести имя лошадя
 после мы вводим сумму ставки и на каком месте финиширует
 выбранный нами лошадь


 если мы угадали то сообшается что мы выиграли
 и наш счет увеличивается на 1.96
 3.Выход


 **/




public class Main extends db {
    static db worker = new db(); //подключение к базе данных идет через класс "db.java"
    static Scanner log = new Scanner(System.in);
    static String accType; // это для того чтобы определить тип аккаунта "Избератель" или "Админ"
    public static void main(String[] args) throws SQLException, IOException {
        String insert = "insert into users ( " +
                "name, login, password ) values (?, ?, ?);";  //  SQL запрос для добавление данных
        PreparedStatement ps = worker.getConnection().prepareStatement(insert); // PreparedStatement используется для выполнения SQL-запросов
        Scanner input = new Scanner(System.in);
        Scanner regisUser = new Scanner(System.in);
        int choice;
        do {
            System.out.println("У вас есть акккаунт?   Да-1 / Нет-2");
            choice = input.nextInt();

            if (choice == 1){
                System.out.println("Выберите тип аккаунта : ");
                System.out.println("1 - Пользователь \n" +
                        "2 - Админ");
                int type = input.nextInt();

                if (type == 1){
                    accType = "user";
                    Login(); // Вызов метода логин
                } else if(type == 2){
                    accType = "admin";
                    Login(); // Вызов метода логин
                }
            }
            else if (choice == 2){
                System.out.println("Что бы зарегистрироватся заполните данные");
                System.out.println("Введите свое имя : ");
                String name = regisUser.nextLine();
                System.out.println("Введите логин : ");
                String login = regisUser.nextLine();
                System.out.println("Введите пароль : ");
                String pass = regisUser.nextLine();
                ps.setString(1,name);
                ps.setString(3,login);
                ps.setString(2,pass);
                ps.execute();
                System.out.println("Вы успешно зарегистировались!!!");
            }
        }while (choice !=3);

    }


    public static void Login() throws SQLException, IOException {
        PreparedStatement ps = worker.getConnection().prepareStatement("select * from users where login = ? and password = ? and TypeAccount =  '" + accType + "'");
        boolean isUserExists = false;
        System.out.println("Введите логин : ");
        String login = log.nextLine();
        System.out.println("Введите пароль : ");
        String password = log.nextLine();
        ps.setString(1, login);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {   // проверка на наличие записи в БД
            isUserExists = true;
        }
        if (isUserExists && rs.getString(5).trim().equals("user")) {
            // Здесь проверяем какой тип аккаунта пользователя
            System.out.println("Приветствую !!! " + rs.getString(2));
            // если избератель то вызываем класс "User" c методом "run"
            User.run(login,password);
        }
        else if (isUserExists && rs.getString(5).trim().equals("admin")) {
            // Аналогичго
            System.out.println("Приветствую !!! " + rs.getString(2));
            Admin.run();
        }
    }

}