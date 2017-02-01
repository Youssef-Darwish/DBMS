package eg.edu.alexu.csd.oop.dbms.Database;

import java.io.File;
import java.sql.Date;
import java.util.Scanner;

public class DBMSmain {
    
    public static void main(String args[]) {
    
        Scanner ino = new Scanner(System.in);
        String input = "", line;

        DBSystem dbms = new DBSystem(new File(System.getProperty("user.home"), "DBMS"));
        dbms.setWriterClass(Json.class);
       System.out.println( dbms.pushQuery("create database bs19"));

       System.out.println( dbms.pushQuery("use bs19"));
        System.out.println(dbms.pushQuery("create table testo (name varchar,score float,birth date)"));
        while (true) {
          line = ino.nextLine();
          if (line.equals("exit;"))
            break;
          input += line;
          if (input.endsWith(";")) {
            input = input.replace(";", "");
            System.out.println(dbms.pushQuery(input.toLowerCase().trim()));

            input = "";
          }
        }
        ino.close();
        return;
      }
    }

//create table testo (name varchar,score float,birth date);
//alter table testo add column column_name3 int
//insert into testo (name,score,birth) values ('abdo',3.6,1996-04-12); 


