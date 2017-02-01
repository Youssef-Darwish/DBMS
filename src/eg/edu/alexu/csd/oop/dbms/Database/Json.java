package eg.edu.alexu.csd.oop.dbms.Database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import org.w3c.dom.Element;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Json implements IFileBehaviour {

    private Table operand;
    private String tableName;

    @Override
    public void setTable(Table table) {
        operand = table;
        tableName = operand.getTableName();
    }

    @Override
    public Table getTable() {
        return operand;
    }

    @Override
    public void saveTable(File file) {

      file = new File(file, tableName + ".json");
        try {
            FileWriter writer = new FileWriter(file);
            Gson gson = new GsonBuilder().disableHtmlEscaping()
                    .setPrettyPrinting().create();
            gson.toJson(operand, writer);
            writer.close();
        } catch (Exception e) {
            System.out.println("error!");
        }

    }

    @Override
    public void loadTable(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            Gson gson = new Gson();
            operand = gson.fromJson(br, Table.class);
        } catch (Exception e) {
            System.out.println("error!");
        }
    }

    @Override
    public void saveTableSchema(File file) {
        return;

    }

    @Override
    public String getExtension() {
      return "json";
    }

}
